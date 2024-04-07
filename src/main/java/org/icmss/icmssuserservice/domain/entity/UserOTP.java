package org.icmss.icmssuserservice.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.io.Serializable;

@RedisHash(value = "user_otp",timeToLive = 600L)
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class UserOTP implements Serializable {
  
    public enum Flow {
        REGISTRATION,LOGIN,FORGET_PASSWORD
    }

    @Id
    private String id;
    private String otp;
    private Flow flow;
    private boolean verified;
    private String token;
    private Integer otpTryLimit = 0;


}