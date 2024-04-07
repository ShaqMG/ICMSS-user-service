package org.icmss.icmssuserservice.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.icmss.icmssuserservice.domain.enums.CertificationStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DealerCertificationResponse {
    private String certificationId;
    private String userId;
    private CertificationStatus status;
    private LocalDateTime submittedAt;
    private LocalDateTime reviewedAt;
}
