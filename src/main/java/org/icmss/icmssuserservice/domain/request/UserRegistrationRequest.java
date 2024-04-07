package org.icmss.icmssuserservice.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.icmss.icmssuserservice.annotations.ValidEmail;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationRequest {
    @NotBlank
    private String firstName;

    @NotBlank
    private String LastName;

    @NotBlank
    @ValidEmail
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d!@#%&()–\\[\\]{}:;',?/*~$^+=<>]{8,}$", message = "password-Invalid password format;")
    private String password;
}
