package org.icmss.icmssuserservice.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private Long id;
    private String userId;
    private String message;
    private Boolean isRead;
    private LocalDateTime createdAt;
}
