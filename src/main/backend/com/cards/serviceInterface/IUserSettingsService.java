package com.cards.serviceInterface;

import com.cards.entity.User;
import com.cards.entity.UserSettings;

import java.util.UUID;

public interface IUserSettingsService {

    void getSettingsById(UUID settingsId);

    UserSettings getSettingsByUser(User user);

    UserSettings createUserSettings(User user);

    void updateUserSettings(UserSettings oldSettings, UserSettings newSettings);

    void deleteSettingsById(UUID settingsId);

}
