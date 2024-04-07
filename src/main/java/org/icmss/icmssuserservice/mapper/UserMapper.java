package org.icmss.icmssuserservice.mapper;

import org.icmss.icmssuserservice.domain.entity.Users;
import org.icmss.icmssuserservice.domain.response.UserResponse;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public static UserResponse toUserResponse(Users user) {
        return UserResponse
                .builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .balance(user.getBalance())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
