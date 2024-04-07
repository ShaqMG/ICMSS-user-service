package org.icmss.icmssuserservice.controller;

import lombok.RequiredArgsConstructor;
import org.icmss.icmssuserservice.domain.response.DealerCertificationResponse;
import org.icmss.icmssuserservice.service.DealerCertificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/dealer-certifications")
@RequiredArgsConstructor
public class DealerCertificationController {

    private final DealerCertificationService dealerCertificationService;

    @PostMapping("/apply")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> applyForDealerCertification() {
        dealerCertificationService.applyForDealerCertification();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/status")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<DealerCertificationResponse> getDealerCertificationInfo() {
        DealerCertificationResponse response = dealerCertificationService.getDealerCertificationInfo();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<DealerCertificationResponse>> getAllDealerCertifications() {
        List<DealerCertificationResponse> response = dealerCertificationService.getAllDealerCertifications();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{certificationId}/approve")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> approveDealerCertification(@PathVariable @NotBlank Long certificationId) {
        dealerCertificationService.approveDealerCertification(certificationId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{certificationId}/reject")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> rejectDealerCertification(@PathVariable @NotBlank Long certificationId) {
        dealerCertificationService.rejectDealerCertification(certificationId);
        return ResponseEntity.ok().build();
    }
}
