package org.icmss.icmssuserservice.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {

    @NotBlank(message = "currentPassword-must not be blank;")
    private String currentPassword;

    @NotBlank(message = "newPassword-must not be blank;")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "newPassword-Invalid password format;")
    private String newPassword;

    @NotBlank(message = "confirmNewPassword-must not be blank;")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "confirmNewPassword-Invalid password format;")
    private String confirmNewPassword;
}
