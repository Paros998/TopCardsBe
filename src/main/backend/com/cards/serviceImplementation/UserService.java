package com.cards.serviceImplementation;

import com.cards.dto.request.UserCredentials;
import com.cards.dto.request.UserUpdateDTO;
import com.cards.entity.File;
import com.cards.entity.User;
import com.cards.repository.UserRepository;
import com.cards.serviceInterface.IFileService;
import com.cards.serviceInterface.IUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class UserService implements IUserService {
    private final static String USER_NOT_FOUND = "User with username %s not found";
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final IFileService fileService;

    private final static UUID BasicUserPhoto = UUID.fromString("ffffffff-ffff-eeee-ffff-ffffffffffff");

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

    public String getUserAvatar(UUID userId) {
        File avatar = getUser(userId).getAvatar();

        if(avatar == null)
            return fileService.getFileUrl(BasicUserPhoto);

        return fileService.getFileUrl(avatar.getFileId());
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

    public void uploadUserAvatar(UUID userId, MultipartFile file) {

        User user = getUser(userId);

        UUID avatarId = fileService.uploadFile(file);

        user.setAvatar(fileService.getFileById(avatarId));

        userRepository.save(user);

    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public void updateUser(UUID userId, UserUpdateDTO userUpdateDTO) {

        User user = getUser(userId);

        //TODO fix update
        if (!user.getEmail().equals(userUpdateDTO.getEmail()))
            if (userRepository.existsByEmail(userUpdateDTO.getEmail()))
                throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("This email %s is already taken!", userUpdateDTO.getEmail()));

        if (!user.getUsername().equals(userUpdateDTO.getUsername()))
            if (userRepository.existsByUsername(userUpdateDTO.getUsername()))
                throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("This username %s is already taken!", userUpdateDTO.getUsername()));

        if (!userUpdateDTO.getUsername().isEmpty())
            user.setUsername(userUpdateDTO.getUsername());

        if (!userUpdateDTO.getPassword().isEmpty())
            user.setPassword(bCryptPasswordEncoder.encode(userUpdateDTO.getPassword()));

        if (!userUpdateDTO.getEmail().isEmpty())
            user.setEmail(userUpdateDTO.getEmail());

        userRepository.save(user);
    }

    public void updateUserAvatar(UUID userId, MultipartFile file) {

        User user = getUser(userId);

        UUID newAvatarId = fileService.uploadFile(file);

        File oldAvatar = user.getAvatar();

        user.setAvatar(null);

        fileService.deleteFile(oldAvatar.getFileId());

        user.setAvatar(fileService.getFileById(newAvatarId));

        userRepository.save(user);
    }

    public void changeStateOfUser(UUID userId) {

        User user = getUser(userId);

        user.setLocked(!user.getLocked());

        user.setEnabled(!user.getEnabled());

        userRepository.save(user);
    }

    public void deleteUserById(UUID userId) {

        getUser(userId);

        userRepository.deleteById(userId);
    }

    public void deleteUserAvatar(UUID userId) {

        User user = getUser(userId);

        File avatar = user.getAvatar();

        user.setAvatar(null);

        userRepository.save(user);

        fileService.deleteFile(avatar.getFileId());

    }

}
