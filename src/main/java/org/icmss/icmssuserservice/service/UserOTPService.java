package org.icmss.icmssuserservice.service;

import lombok.RequiredArgsConstructor;
import org.icmss.icmssuserservice.domain.entity.UserOTP;
import org.icmss.icmssuserservice.domain.enums.DbStatus;
import org.icmss.icmssuserservice.domain.request.SendOTPRequest;
import org.icmss.icmssuserservice.exceptions.BusinessException;
import org.icmss.icmssuserservice.repository.UserOTPRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserOTPService{

    private final UserOTPRepository userOTPRepository;
    private final UserService userService;

    private final EmailService emailService;


    public void sendOtp(String email, UserOTP.Flow flow) {
        if (flow == UserOTP.Flow.REGISTRATION) {
            userService.existsByEmail(email);
        } else {
            userService.getUserByEmail(email, DbStatus.ACTIVE);
        }
        Thread thread = new Thread(() -> sendOTP(new SendOTPRequest(email, flow, false, null)));
        thread.start();
    }

    public String verifyOTPforSignIn(String email, String otp) {
        UserOTP userOTP = userOTPRepository.findById(email).orElseThrow(
                () -> new BusinessException(HttpStatus.NOT_FOUND, "OTP not found")
        );

        checkOtpTryLimit(userOTP);

        if (otp.equals(userOTP.getOtp())) {
            userOTP.setVerified(true);
            userOTPRepository.save(userOTP);
            System.out.println("TOKEN " + userOTP.getToken());
            return userOTP.getToken();
        }
        throw new BusinessException(HttpStatus.CONFLICT, "OTP is not valid");

    }

    public void checkOtpTryLimit(UserOTP userOTP) {
        if (userOTP.getOtpTryLimit() >= 10){
            throw new BusinessException(HttpStatus.TOO_MANY_REQUESTS, "OTP try limit is exceeded, try later !");
        } else {
            userOTP.setOtpTryLimit(userOTP.getOtpTryLimit() + 1);
            userOTPRepository.save(userOTP);
        }
    }

    public void verifyOTPForSignUpAndPasswordRestore(String email, String otp) {
        UserOTP userOTP = userOTPRepository.findById(email).orElseThrow(
                () -> new BusinessException(HttpStatus.NOT_FOUND, "OTP not found")
        );

        checkOtpTryLimit(userOTP);

        if (otp.equals(userOTP.getOtp())) {
            userOTP.setVerified(true);
            userOTPRepository.save(userOTP);

        } else throw new BusinessException(HttpStatus.CONFLICT, "OTP is not valid");

    }

    public void sendOTP(SendOTPRequest param) {
        UserOTP userOTP = new UserOTP(param.getEmail(), generateOTP(), param.getFlow(), param.isVerified(), param.getToken(), 0);
        userOTPRepository.save(userOTP);
        try {
            emailService.sendOtp(param.getEmail(), userOTP.getOtp());
        } catch (MessagingException | UnsupportedEncodingException e) {
             throw new BusinessException(HttpStatus.CONFLICT, "Sending OTP on email failed!");
        }

    }

    private String generateOTP() {
        Random random = new Random();
        return String.format("%04d", random.nextInt(10000));
    }
}