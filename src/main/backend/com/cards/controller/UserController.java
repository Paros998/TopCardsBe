package com.cards.controller;

import com.cards.dto.request.*;
import com.cards.dto.response.BasicCardModelDTO;
import com.cards.dto.response.BasicUserDTO;
import com.cards.dto.response.NotificationModelDTO;
import com.cards.dto.response.PageResponse;
import com.cards.entity.User;
import com.cards.entity.UserSettings;
import com.cards.serviceInterface.IFileService;
import com.cards.serviceInterface.INotificationService;
import com.cards.serviceInterface.IUserService;
import com.cards.serviceInterface.IUserSettingsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class UserController {
    private final IUserService userService;
    private final IUserSettingsService settingsService;
    private final IFileService fileService;
    private final INotificationService notificationService;

    @GetMapping
    public PageResponse<BasicUserDTO> getUsers(@RequestParam Integer page, @RequestParam Integer pageLimit,
                                               @RequestParam(required = false, defaultValue = "asc") String sortDir,
                                               @RequestParam(required = false, defaultValue = "title") String sortBy) {
        return userService.getUsers(new PageRequestDTO(page, pageLimit, sortDir, sortBy));
    }

    @GetMapping("{userId}")
    public BasicUserDTO getUser(@PathVariable UUID userId) {
        User user = userService.getUser(userId);
        return BasicUserDTO.convertFromEntity(user, userService.getUserAvatar(userId));
    }

    @GetMapping("{userId}/avatar")
    public String getUserAvatar(@PathVariable UUID userId) {
        return userService.getUserAvatar(userId);
    }

    @GetMapping("{userId}/settings")
    public UserSettings getUserSettings(@PathVariable UUID userId) {
        return settingsService.getSettingsByUser(userService.getUser(userId));
    }

    @GetMapping("{userId}/followed-cards")
    public List<BasicCardModelDTO> getUserFollowedCards(@PathVariable UUID userId) {
        return userService.getUserFollowedCards(userId);
    }

    @GetMapping("{userId}/notifications")
    public List<NotificationModelDTO> getUserNotifications(@PathVariable UUID userId) {
        return notificationService.getUserNotifications(userService.getUser(userId));
    }

    @GetMapping("{userId}/follows/{cardId}")
    public Boolean isUserFollowingCard(@PathVariable UUID userId, @PathVariable UUID cardId) {
        return userService.isUserFollowingCard(userId, cardId);
    }

    @PostMapping
    public BasicUserDTO createAppUser(@RequestBody UserCredentials userCredentials) {
        User user = userService.createUser(userCredentials);
        return BasicUserDTO.convertFromEntity(user, fileService.getFileUrl(user.getAvatar().getFileId()));
    }

    @PostMapping("/register")
    public BasicUserDTO registerClient(@RequestBody RegisterClientDTO clientDTO) {
        User user = userService.createClient(clientDTO);
        return BasicUserDTO.convertFromEntity(user, fileService.getFileUrl(user.getAvatar().getFileId()));
    }


    @PostMapping(value = "{userId}/avatar", consumes = {"multipart/form-data"})
    public void uploadUserAvatar(@PathVariable UUID userId, @RequestParam("file") MultipartFile file) {
        userService.uploadUserAvatar(userId, file);
    }

    @PutMapping("{userId}")
    public void updateAppUser(@PathVariable UUID userId, @RequestBody UserUpdateDTO userUpdateDTO) {
        userService.updateUser(userId, userUpdateDTO);
    }

    @PutMapping("{userId}/change-password")
    public void changeUserPassword(@PathVariable UUID userId, @RequestBody ChangePasswordDTO passwordDTO) {
        userService.changeUserPassword(userId, passwordDTO);
    }

    @PutMapping(value = "{userId}/avatar", consumes = {"multipart/form-data"})
    public void updateUserAvatar(@PathVariable UUID userId, @RequestParam("file") MultipartFile file) {
        userService.updateUserAvatar(userId, file);
    }

    @PutMapping("{userId}/settings")
    public void updateUserSettings(@PathVariable UUID userId, @RequestBody UserSettings newSettings) {
        settingsService.updateUserSettings(
                settingsService.getSettingsByUser(userService.getUser(userId)),
                newSettings
        );
    }

    @PutMapping("{userId}/follow-card/{cardId}")
    public void addCardToUserFollowed(@PathVariable UUID userId, @PathVariable UUID cardId) {
        userService.addCardToUserFollowed(userId, cardId);
    }

    @PutMapping("{userId}/unfollow-card/{cardId}")
    public void removeCardFromUserFollowed(@PathVariable UUID userId, @PathVariable UUID cardId) {
        userService.removeCardFromUserFollowed(userId, cardId);
    }

    @PatchMapping("{userId}")
    public void changeStateOfUser(@PathVariable UUID userId) {
        userService.changeStateOfUser(userId);
    }

    @DeleteMapping("{userId}")
    public void deleteUserById(@PathVariable UUID userId) {
        userService.deleteUserById(userId);
    }

    @DeleteMapping("{userId}/avatar")
    public void deleteUserAvatar(@PathVariable UUID userId) {
        userService.deleteUserAvatar(userId);
    }
}
