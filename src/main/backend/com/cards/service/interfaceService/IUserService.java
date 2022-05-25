package com.cards.service.interfaceService;

import com.cards.dto.UserCredentials;
import com.cards.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.UUID;

public interface IUserService extends UserDetailsService {

    User getUser(UUID userId);

    User getUser(String username);

    User getUserByEmail(String email);

    List<User> getUsers();

    User createUser(UserCredentials userCredentials);

    void updateUser(User user);

    void updateUser(UUID userId, UserCredentials userCredentials);

    void changeStateOfUser(UUID userId);

    void deleteUserById(UUID userId);

}
