package com.cards.serviceInterface;

import com.cards.dto.request.*;
import com.cards.dto.response.BasicCardModelDTO;
import com.cards.dto.response.BasicUserDTO;
import com.cards.dto.response.PageResponse;
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

    PageResponse<BasicUserDTO> getUsers(PageRequestDTO pageRequestDTO);

    List<BasicCardModelDTO> getUserFollowedCards(UUID userId);

    Boolean isUserFollowingCard(UUID userId, UUID cardId);

    User createUser(UserCredentials userCredentials);

    User createClient(RegisterClientDTO clientDTO);

    void uploadUserAvatar(UUID userId, MultipartFile file);

    void updateUser(User user);

    void changeUserPassword(UUID userId, ChangePasswordDTO passwordDTO);

    void updateUser(UUID userId, UserUpdateDTO userUpdateDTO);

    void updateUserAvatar(UUID userId, MultipartFile file);

    void addCardToUserFollowed(UUID userId, UUID cardId);

    void removeCardFromUserFollowed(UUID userId, UUID cardId);

    void changeStateOfUser(UUID userId);

    void deleteUserById(UUID userId);

    void deleteUserAvatar(UUID userId);

}
