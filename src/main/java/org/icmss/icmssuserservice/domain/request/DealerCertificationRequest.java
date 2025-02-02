package org.icmss.icmssuserservice.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DealerCertificationRequest {
    @NotNull
    private Long userId;
}
