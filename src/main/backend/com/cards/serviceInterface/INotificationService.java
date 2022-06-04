package com.cards.serviceInterface;

import com.cards.dto.response.NotificationModelDTO;
import com.cards.entity.User;

import java.util.List;

public interface INotificationService {
    List<NotificationModelDTO> getUserNotifications(User user);
}
