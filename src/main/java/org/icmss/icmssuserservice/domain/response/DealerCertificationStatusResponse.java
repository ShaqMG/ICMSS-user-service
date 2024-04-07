package org.icmss.icmssuserservice.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.icmss.icmssuserservice.domain.enums.CertificationStatus;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DealerCertificationStatusResponse {
    private CertificationStatus status;
}
