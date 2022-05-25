package com.cards.serviceImplementation;

import com.cards.dto.UserCredentials;
import com.cards.entity.User;
import com.cards.repository.UserRepository;
import com.cards.serviceInterface.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements IUserService {
    private final static String USER_NOT_FOUND = "User with username %s not found";

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, username))
        );

    }

    public User getUser(UUID userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with given userId %s doesn't exist!", userId))
        );
    }

    public User getUser(String username) {
        return userRepository.getByUsername(username);
    }

    public User getUserByEmail(String email) {

        return userRepository.findByEmail(email).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with given email %s doesn't exist in database!", email))
        );

    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User createUser(UserCredentials userCredentials) {

        if (userRepository.existsByEmail(userCredentials.getEmail()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This email is already taken!");


        if (userRepository.existsByUsername(userCredentials.getUsername()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This username is already taken!");


        userCredentials.setPassword(bCryptPasswordEncoder.encode(userCredentials.getPassword()));

        User user = new User(userCredentials, false, true);

        userRepository.save(user);

        return user;
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public void updateUser(UUID userId, UserCredentials userCredentials) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException(String.format(USER_NOT_FOUND, userCredentials.getUsername()))
        );

        //TODO fix update
        if (!user.getEmail().equals(userCredentials.getEmail()))
            if (userRepository.existsByEmail(userCredentials.getEmail()))
                throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("This email %s is already taken!", userCredentials.getEmail()));


        if (!user.getUsername().equals(userCredentials.getUsername()))
            if (userRepository.existsByUsername(userCredentials.getUsername()))
                throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("This username %s is already taken!", userCredentials.getUsername()));


        if (!userCredentials.getUsername().isEmpty())
            user.setUsername(userCredentials.getUsername());

        if (!userCredentials.getPassword().isEmpty()) {

            userCredentials.setPassword(bCryptPasswordEncoder.encode(userCredentials.getPassword()));

            user.setPassword(userCredentials.getPassword());
        }

        if (!userCredentials.getEmail().isEmpty())
            user.setEmail(userCredentials.getEmail());

        userRepository.save(user);
    }

    public void changeStateOfUser(UUID userId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with given id %s doesn't exist in database!", userId))
        );

        user.setLocked(!user.getLocked());

        user.setEnabled(!user.getEnabled());

        userRepository.save(user);
    }

    public void deleteUserById(UUID userId) {

        userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User with given id %s doesn't exist in database!", userId))
        );

        userRepository.deleteById(userId);
    }
}
