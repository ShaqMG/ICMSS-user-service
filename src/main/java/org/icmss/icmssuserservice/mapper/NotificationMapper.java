package org.icmss.icmssuserservice.mapper;

import org.icmss.icmssuserservice.domain.entity.Notification;
import org.icmss.icmssuserservice.domain.response.NotificationResponse;

public class NotificationMapper {

    public static NotificationResponse toResponse(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId());
        response.setUserId(notification.getUserId());
        response.setMessage(notification.getMessage());
        response.setIsRead(notification.getIsRead());
        response.setCreatedAt(notification.getCreatedAt());
        return response;
    }
}
