package com.cards.serviceInterface;

import com.cards.dto.request.UserCredentials;
import com.cards.dto.request.UserUpdateDTO;
import com.cards.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface IUserService extends UserDetailsService {

    User getUser(UUID userId);

    User getUser(String username);

    String getUserAvatar(UUID userId);

    User getUserByEmail(String email);

    List<User> getUsers();

    User createUser(UserCredentials userCredentials);

    void uploadUserAvatar(UUID userId, MultipartFile file);

    void updateUser(User user);

    void updateUser(UUID userId, UserUpdateDTO userUpdateDTO);

    void updateUserAvatar(UUID userId, MultipartFile file);

    void changeStateOfUser(UUID userId);

    void deleteUserById(UUID userId);

    void deleteUserAvatar(UUID userId);

}
