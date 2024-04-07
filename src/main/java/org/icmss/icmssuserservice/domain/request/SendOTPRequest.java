package org.icmss.icmssuserservice.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.icmss.icmssuserservice.domain.entity.UserOTP;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SendOTPRequest {

    private String email;
    private UserOTP.Flow flow;
    private boolean verified;
    private String token;


}
