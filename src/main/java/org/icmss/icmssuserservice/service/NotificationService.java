package org.icmss.icmssuserservice.service;

import lombok.RequiredArgsConstructor;
import org.icmss.icmssuserservice.domain.entity.Notification;
import org.icmss.icmssuserservice.domain.enums.DbStatus;
import org.icmss.icmssuserservice.domain.request.NotificationRequest;
import org.icmss.icmssuserservice.domain.response.NotificationResponse;
import org.icmss.icmssuserservice.exceptions.BusinessException;
import org.icmss.icmssuserservice.mapper.NotificationMapper;
import org.icmss.icmssuserservice.repository.NotificationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserService userService;

    @Transactional
    public void createNotification(NotificationRequest request) {
        Notification notification = new Notification();
        notification.setUserId(userService.getCurrentUser().getId());
        notification.setMessage(request.getMessage());
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setStatus(DbStatus.ACTIVE);

        notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getUserNotifications() {
        String currentUserId = userService.getCurrentUser().getId();
        List<Notification> notifications = notificationRepository.findByUserIdAndStatus(currentUserId, DbStatus.ACTIVE);
        return notifications.stream()
                .map(NotificationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NotificationResponse getNotificationById(Long notificationId) {
        Notification notification = notificationRepository.findByIdAndStatus(notificationId, DbStatus.ACTIVE)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Notification not found"));
        return NotificationMapper.toResponse(notification);
    }

    @Transactional
    public void markNotificationAsRead(Long notificationId) {
        Notification notification = notificationRepository.findByIdAndStatus(notificationId, DbStatus.ACTIVE)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Notification not found"));

        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    @Transactional
    public void deleteNotification(Long notificationId) {
        Notification notification = notificationRepository.findByIdAndStatus(notificationId, DbStatus.ACTIVE)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Notification not found"));

        notification.setStatus(DbStatus.DELETED);
        notificationRepository.save(notification);
    }
}
