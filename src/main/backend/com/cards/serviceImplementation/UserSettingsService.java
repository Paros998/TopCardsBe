package com.cards.serviceImplementation;

import com.cards.entity.User;
import com.cards.entity.UserSettings;
import com.cards.repository.UserSettingsRepository;
import com.cards.serviceInterface.IUserSettingsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserSettingsService implements IUserSettingsService {
    private final UserSettingsRepository settingsRepository;


    public void getSettingsById(UUID settingsId) {
        settingsRepository.findById(settingsId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Settings with id %s doesn't exists in database", settingsId))
        );
    }

    public UserSettings getSettingsByUser(User user) {
        return settingsRepository.getUserSettingsByUser(user);
    }

    public UserSettings createUserSettings(User user) {
        return settingsRepository.save(new UserSettings(user));
    }

    public void updateUserSettings(UserSettings oldSettings, UserSettings newSettings) {

        oldSettings.setIsNewCardAdded(newSettings.getIsNewCardAdded());
        oldSettings.setHasFollowedCardBecomeAvailableLocally(newSettings.getHasFollowedCardBecomeAvailableLocally());
        oldSettings.setHasFollowedCardBecomeAvailableOnline(newSettings.getHasFollowedCardBecomeAvailableOnline());
        oldSettings.setHasFollowedCardLowerOnlinePrice(newSettings.getHasFollowedCardLowerOnlinePrice());
        oldSettings.setHasFollowedCardNewReview(newSettings.getHasFollowedCardNewReview());

        settingsRepository.save(oldSettings);

    }


    public void deleteSettingsById(UUID settingsId) {

        getSettingsById(settingsId);

        settingsRepository.deleteById(settingsId);

    }
}
