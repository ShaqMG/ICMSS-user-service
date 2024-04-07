package org.icmss.icmssuserservice.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    private String email;
    private String firstName;
    private String lastName;
}
