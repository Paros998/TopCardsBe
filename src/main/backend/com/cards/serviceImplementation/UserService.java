package com.cards.serviceImplementation;

import com.cards.dto.request.*;
import com.cards.dto.response.BasicCardModelDTO;
import com.cards.dto.response.BasicUserDTO;
import com.cards.dto.response.PageResponse;
import com.cards.entity.File;
import com.cards.entity.User;
import com.cards.enums.Action;
import com.cards.enums.Roles;
import com.cards.repository.UserRepository;
import com.cards.serviceInterface.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UserService implements IUserService {
    private final static String USER_NOT_FOUND = "User with username %s not found";
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final IFileService fileService;
    private final IUserSettingsService settingsService;
    private final IHistoryService historyService;
    private final ICardsService cardsService;

    @Autowired
    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder,
                       UserRepository userRepository,
                       IFileService fileService,
                       IUserSettingsService settingsService,
                       @Lazy IHistoryService historyService,
                       @Lazy ICardsService cardsService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.fileService = fileService;
        this.settingsService = settingsService;
        this.historyService = historyService;
        this.cardsService = cardsService;
    }

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

        if (avatar == null)
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

    public PageResponse<BasicUserDTO> getUsers(PageRequestDTO pageRequestDTO) {
        return new PageResponse<>(
                userRepository.findAll(pageRequestDTO.getRequest(User.class))
                        .map(user -> BasicUserDTO.convertFromEntity(
                                        user,
                                        getUserAvatar(user.getUserId())
                                )
                        )
        );
    }

    public List<BasicCardModelDTO> getUserFollowedCards(UUID userId) {

        User user = getUser(userId);

        List<UUID> cardIds = user.getFollowedCards();

        return cardsService.getCards(cardIds);
    }

    public Boolean isUserFollowingCard(UUID userId, UUID cardId) {

        User user = getUser(userId);

        List<UUID> cardIds = user.getFollowedCards();

        return cardIds.stream().anyMatch(uuid -> uuid.equals(cardId));
    }

    public User createUser(UserCredentials userCredentials) {

        checkForEmailDuplicates(userCredentials.getEmail());


        checkForUsernameDuplicates(userCredentials.getUsername());


        userCredentials.setPassword(bCryptPasswordEncoder.encode(userCredentials.getPassword()));

        User user = new User(userCredentials, false, true);

        userRepository.save(user);

        user.setUserSettings(settingsService.createUserSettings(user));

        userRepository.save(user);

        return user;
    }

    public User createClient(RegisterClientDTO clientDTO) {

        checkForEmailDuplicates(clientDTO.getEmail());


        checkForUsernameDuplicates(clientDTO.getUsername());

        clientDTO.setPassword(bCryptPasswordEncoder.encode(clientDTO.getPassword()));

        User user = new User(
                new UserCredentials(clientDTO.getUsername(), clientDTO.getPassword(), clientDTO.getEmail(), Roles.CLIENT),
                false,
                true);

        userRepository.save(user);

        user.setAvatar(fileService.getFileById(clientDTO.getFileId()));

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

    public void changeUserPassword(UUID userId, ChangePasswordDTO passwordDTO) {

        User user = getUser(userId);

        if (!bCryptPasswordEncoder.matches(passwordDTO.getOldPassword(), user.getPassword()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Old password doesn't match existing one");

        if (bCryptPasswordEncoder.matches(passwordDTO.getNewPassword(), user.getPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password and current password are the same");

        user.setPassword(bCryptPasswordEncoder.encode(passwordDTO.getNewPassword()));

        userRepository.save(user);

    }

    public void updateUser(UUID userId, UserUpdateDTO userUpdateDTO) {

        User user = getUser(userId);

        //TODO fix update
        if (!user.getEmail().equals(userUpdateDTO.getEmail().trim())) {
            checkForEmailDuplicates(userUpdateDTO.getEmail());
        }

        if (!user.getUsername().equals(userUpdateDTO.getUsername().trim())) {
            checkForUsernameDuplicates(userUpdateDTO.getUsername());
        }

        if (!userUpdateDTO.getUsername().isEmpty())
            user.setUsername(userUpdateDTO.getUsername().trim());

        if (!userUpdateDTO.getEmail().isEmpty())
            user.setEmail(userUpdateDTO.getEmail().trim());

        userRepository.save(user);
    }

    private void checkForUsernameDuplicates(String username) {
        if (userRepository.existsByUsername(username.trim()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("This username %s is already taken!", username));
    }

    private void checkForEmailDuplicates(String email) {
        if (userRepository.existsByEmail(email.trim()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("This email %s is already taken!", email));
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

    @Transactional(rollbackOn = ResponseStatusException.class)
    public void addCardToUserFollowed(UUID userId, UUID cardId) {

        User user = getUser(userId);

        List<UUID> followedCards = user.getFollowedCards();

        followedCards.add(cardId);

        user.setFollowedCards(followedCards);

        historyService.addHistory(new AddHistoryDTO(
                Action.follow.name(),
                cardId.toString(),
                user.getUserId().toString()
        ));

        userRepository.save(user);

    }

    @Transactional(rollbackOn = ResponseStatusException.class)
    public void removeCardFromUserFollowed(UUID userId, UUID cardId) {

        User user = getUser(userId);

        List<UUID> followedCards = user.getFollowedCards();

        followedCards.remove(cardId);

        user.setFollowedCards(followedCards);

        historyService.addHistory(new AddHistoryDTO(
                Action.unfollow.name(),
                cardId.toString(),
                user.getUserId().toString()
        ));

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
