package org.icmss.icmssuserservice.controller;

import lombok.RequiredArgsConstructor;
import org.icmss.icmssuserservice.domain.request.NotificationRequest;
import org.icmss.icmssuserservice.domain.response.NotificationResponse;
import org.icmss.icmssuserservice.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> createNotification(@Valid @RequestBody NotificationRequest request) {
        notificationService.createNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('DEALER') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<NotificationResponse>> getUserNotifications() {
        List<NotificationResponse> notifications = notificationService.getUserNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{notificationId}")
    @PreAuthorize("hasRole('USER') or hasRole('DEALER') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<NotificationResponse> getNotificationById(@PathVariable Long notificationId) {
        NotificationResponse notification = notificationService.getNotificationById(notificationId);
        return ResponseEntity.ok(notification);
    }

    @PutMapping("/{notificationId}/mark-as-read")
    @PreAuthorize("hasRole('USER') or hasRole('DEALER') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable Long notificationId) {
        notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{notificationId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.noContent().build();
    }
}
