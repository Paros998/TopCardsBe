package com.cards.controller;

import com.cards.dto.request.UserCredentials;
import com.cards.dto.request.UserUpdateDTO;
import com.cards.dto.response.BasicUserDTO;
import com.cards.serviceInterface.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class UserController {
    private final IUserService userService;

    @GetMapping("{userId}")
    public BasicUserDTO getUser(@PathVariable UUID userId) {
        return BasicUserDTO.convertFromEntity(userService.getUser(userId));
    }

    @GetMapping("{userId}/avatar")
    public String getUserAvatar(@PathVariable UUID userId) {
        return userService.getUserAvatar(userId);
    }


    @PostMapping
    public BasicUserDTO createAppUser(@RequestBody UserCredentials userCredentials) {
        return BasicUserDTO.convertFromEntity(userService.createUser(userCredentials));
    }


    @PostMapping(value = "{userId}/avatar", consumes = {"multipart/form-data"})
    public void uploadUserAvatar(@PathVariable UUID userId, @RequestParam("file") MultipartFile file) {
        userService.uploadUserAvatar(userId, file);
    }

    @PutMapping("{userId}")
    public void updateAppUser(@PathVariable UUID userId, @RequestBody UserUpdateDTO userCredentials) {
        userService.updateUser(userId, userCredentials);
    }

    @PutMapping(value = "{userId}/avatar", consumes = {"multipart/form-data"})
    public void updateUserAvatar(@PathVariable UUID userId, @RequestParam("file") MultipartFile file) {
        userService.updateUserAvatar(userId, file);
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
