package org.icmss.icmssuserservice.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.icmss.icmssuserservice.domain.enums.CertificationStatus;
import org.icmss.icmssuserservice.domain.enums.DbStatus;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "dealer_certification")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DealerCertification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    @Enumerated(EnumType.STRING)
    private CertificationStatus status;

    private LocalDateTime submittedAt;

    private LocalDateTime reviewedAt;

    private String reviewerUserId;

    @Enumerated(EnumType.STRING)
    private DbStatus dbStatus = DbStatus.ACTIVE;
}
