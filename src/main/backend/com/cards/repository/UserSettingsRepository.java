package com.cards.repository;

import com.cards.entity.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface UserSettingsRepository extends JpaRepository<UserSettings, UUID>, JpaSpecificationExecutor<UserSettings> {
}