package org.icmss.icmssuserservice.service;

import lombok.RequiredArgsConstructor;
import org.icmss.icmssuserservice.domain.entity.DealerCertification;
import org.icmss.icmssuserservice.domain.entity.Users;
import org.icmss.icmssuserservice.domain.enums.CertificationStatus;
import org.icmss.icmssuserservice.domain.enums.DbStatus;
import org.icmss.icmssuserservice.domain.response.DealerCertificationResponse;
import org.icmss.icmssuserservice.exceptions.BusinessException;
import org.icmss.icmssuserservice.mapper.DealerCertificationMapper;
import org.icmss.icmssuserservice.repository.DealerCertificationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DealerCertificationService {

    private final DealerCertificationRepository dealerCertificationRepository;
    private final UserService userService;

    @Transactional
    public void applyForDealerCertification() {
        Users currentUser = userService.getCurrentUser();

        Optional<DealerCertification> existingCertification = dealerCertificationRepository.findByUserIdAndDbStatus(currentUser.getId(), DbStatus.ACTIVE);
        if (existingCertification.isPresent()) {
            CertificationStatus status = existingCertification.get().getStatus();
            if (status == CertificationStatus.PENDING || status == CertificationStatus.APPROVED) {
                throw new BusinessException(HttpStatus.CONFLICT, "User already has a pending or approved certification");
            }
        }

        DealerCertification certification = new DealerCertification();
        certification.setUserId(currentUser.getId());
        certification.setStatus(CertificationStatus.PENDING);
        certification.setSubmittedAt(LocalDateTime.now());
        certification.setDbStatus(DbStatus.ACTIVE);

        dealerCertificationRepository.save(certification);
    }

    @Transactional(readOnly = true)
    public DealerCertificationResponse getDealerCertificationInfo() {
        Users currentUser = userService.getCurrentUser();

        DealerCertification certification = dealerCertificationRepository.findByUserIdAndDbStatus(currentUser.getId(), DbStatus.ACTIVE)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Dealer certification not found"));

        return DealerCertificationMapper.toResponse(certification);
    }

    @Transactional(readOnly = true)
    public List<DealerCertificationResponse> getAllDealerCertifications() {
        List<DealerCertification> certifications = dealerCertificationRepository.findAllByDbStatus(DbStatus.ACTIVE);
        return certifications.stream()
                .map(DealerCertificationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void approveDealerCertification(Long certificationId) {
        DealerCertification certification = dealerCertificationRepository.findByIdAndDbStatus(certificationId, DbStatus.ACTIVE)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Dealer certification not found"));

        if (certification.getStatus() != CertificationStatus.PENDING) {
            throw new BusinessException(HttpStatus.CONFLICT, "Dealer certification is not in pending status");
        }

        certification.setStatus(CertificationStatus.APPROVED);
        certification.setReviewedAt(LocalDateTime.now());
        certification.setReviewerUserId(userService.getCurrentUser().getId());

        dealerCertificationRepository.save(certification);
    }

    @Transactional
    public void rejectDealerCertification(Long certificationId) {
        DealerCertification certification = dealerCertificationRepository.findByIdAndDbStatus(certificationId, DbStatus.ACTIVE)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Dealer certification not found"));

        if (certification.getStatus() != CertificationStatus.PENDING) {
            throw new BusinessException(HttpStatus.CONFLICT, "Dealer certification is not in pending status");
        }

        certification.setStatus(CertificationStatus.REJECTED);
        certification.setReviewedAt(LocalDateTime.now());
        certification.setReviewerUserId(userService.getCurrentUser().getId());

        dealerCertificationRepository.save(certification);
    }
}
