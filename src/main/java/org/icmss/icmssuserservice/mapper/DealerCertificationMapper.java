package org.icmss.icmssuserservice.mapper;

import org.icmss.icmssuserservice.domain.entity.DealerCertification;
import org.icmss.icmssuserservice.domain.response.DealerCertificationResponse;

public class DealerCertificationMapper {

    public static DealerCertificationResponse toResponse(DealerCertification certification) {
        DealerCertificationResponse response = new DealerCertificationResponse();
        response.setCertificationId(certification.getId().toString());
        response.setUserId(certification.getUserId());
        response.setStatus(certification.getStatus());
        response.setSubmittedAt(certification.getSubmittedAt());
        response.setReviewedAt(certification.getReviewedAt());
        return response;
    }
}