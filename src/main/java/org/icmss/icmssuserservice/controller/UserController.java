package org.icmss.icmssuserservice.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.RequiredArgsConstructor;
import org.icmss.icmssuserservice.domain.request.ChangePasswordRequest;
import org.icmss.icmssuserservice.domain.request.UserRegistrationRequest;
import org.icmss.icmssuserservice.domain.request.UserUpdateRequest;
import org.icmss.icmssuserservice.domain.response.UserResponse;
import org.icmss.icmssuserservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    @SecurityRequirements
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        String response = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    @SecurityRequirements
    public ResponseEntity<String> login(@RequestParam @NotBlank String email,
                                        @RequestParam @NotBlank String password) {
        String token = userService.login(email, password);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER') or hasRole('DEALER') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<UserResponse> getUserProfile(@RequestParam @NotBlank String userId) {
        UserResponse response = userService.getUserProfile(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('USER') or hasRole('DEALER') or hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UserUpdateRequest request) {
        UserResponse response = userService.updateUser(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/change-password")
    @PreAuthorize("hasRole('USER') or hasRole('DEALER')")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/balance")
    @PreAuthorize("hasRole('USER') or hasRole('DEALER')")
    public ResponseEntity<BigDecimal> getUserBalance() {
        BigDecimal response = userService.getUserBalance();
        return ResponseEntity.ok(response);
    }
}
