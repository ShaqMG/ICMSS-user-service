package org.icmss.icmssuserservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.RequiredArgsConstructor;
import org.icmss.icmssuserservice.annotations.ValidEmail;
import org.icmss.icmssuserservice.domain.entity.UserOTP;
import org.icmss.icmssuserservice.service.UserOTPService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;


@RestController
@RequiredArgsConstructor
@RequestMapping("/otp")
@Validated
public class UserOTPController {

    private final UserOTPService userOTPService;
    @PostMapping
    @Operation(summary = "Send OTP to user")
    @SecurityRequirements
    public ResponseEntity<Void> sendOTP(@RequestParam @NotBlank @ValidEmail String email,
                                        @RequestParam UserOTP.Flow flow) {
        userOTPService.sendOtp(email, flow);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/sign_up")
    @Operation(summary = "Verify OTP for sign up")
    @SecurityRequirements
    public ResponseEntity<Void> verifyOTPForSignUp(@RequestParam @NotBlank @ValidEmail String email,
                                             @RequestParam @NotBlank String OTP){
        userOTPService.verifyOTPForSignUpAndPasswordRestore(email,OTP);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/sign_in")
    @Operation(summary = "Verify OTP for sign in")
    @SecurityRequirements
    public ResponseEntity<String> verifyOTPForSignIn(@RequestParam @NotBlank @ValidEmail String email,
                                            @RequestParam @NotBlank String OTP){

        return ResponseEntity.status(HttpStatus.OK).body(userOTPService.verifyOTPforSignIn(email,OTP));
    }

    @GetMapping("/restore_password")
    @Operation(summary = "Verify OTP for restoring the password")
    @SecurityRequirements
    public ResponseEntity<Void> verifyOTPFroPasswordRestore(@RequestParam @NotBlank @ValidEmail String email,
                                                            @RequestParam @NotBlank String OTP) {
        userOTPService.verifyOTPForSignUpAndPasswordRestore(email, OTP);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}







