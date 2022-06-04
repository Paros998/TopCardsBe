package com.cards.serviceImplementation;

import com.cards.dto.response.NotificationModelDTO;
import com.cards.entity.User;
import com.cards.repository.NotificationRepository;
import com.cards.serviceInterface.INotificationService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NotificationService implements INotificationService {
    private final NotificationRepository notificationRepository;


    public List<NotificationModelDTO> getUserNotifications(User user) {
        return notificationRepository.findAllByUser(user, Sort.by(Sort.Direction.DESC, "notificationTime"))
                .stream()
                .map(notification -> new NotificationModelDTO(
                        notification.getCardId(),
                        notification.getMessage(),
                        notification.getNotificationTime(),
                        notification.getType().name()
                ))
                .collect(Collectors.toList());
    }
}
