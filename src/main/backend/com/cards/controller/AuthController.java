package com.cards.controller;

import com.cards.auth.jwt.JwtTokenRefresher;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final JwtTokenRefresher tokenRefresher;

    @GetMapping("refresh-access")
    public void refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        tokenRefresher.refreshToken(request, response);
    }
}
