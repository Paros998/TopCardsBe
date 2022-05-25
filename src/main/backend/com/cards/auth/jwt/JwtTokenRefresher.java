package com.cards.auth.jwt;

import com.cards.entity.User;
import com.cards.serviceInterface.IUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import static com.cards.enums.JwtExpire.ACCESS_TOKEN;

@Service
public class JwtTokenRefresher {
    private final IUserService userService;
    private final String secretKey;

    @Autowired
    public JwtTokenRefresher(IUserService userService, @Value("${jwt.secret}") String secretKey) {
        this.userService = userService;
        this.secretKey = secretKey;
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {

        String authorizationHeader = request.getHeader("Authorization-Refresh");

        if (!Strings.isNullOrEmpty(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.replace("Bearer ", "");
            try {

                Jws<Claims> claimsJws = Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                        .build()
                        .parseClaimsJws(token);

                Claims body = claimsJws.getBody();

                String UID = body.get("userId", String.class);

                UUID userId = UUID.fromString(UID);

                User user = userService.getUser(userId);

                String accessToken = Jwts.builder()
                        .setSubject(user.getUsername())
                        .claim("authorities", user.getAuthorities())
                        .claim("userId", userId)
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN.getAmount()))
                        .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                        .compact();

                response.addHeader("Authorization", "Bearer " + accessToken);
                response.setStatus(HttpStatus.OK.value());

            } catch (Exception e) {

                if (e.getClass().equals(ExpiredJwtException.class))
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, String.format("Token %s has expired, please login again", token));

                if (e.getClass().equals(SignatureException.class))
                    throw new IllegalStateException(String.format("Token %s cannot be trusted", token));

                else throw e;
            }

        } else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Authorization-Refresh token hasn't been provided with request");
    }

}
