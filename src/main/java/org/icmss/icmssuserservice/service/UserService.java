package org.icmss.icmssuserservice.service;

import com.nimbusds.jose.util.Base64;
import lombok.RequiredArgsConstructor;
import org.icmss.icmssuserservice.domain.entity.UserOTP;
import org.icmss.icmssuserservice.domain.entity.Users;
import org.icmss.icmssuserservice.domain.enums.DbStatus;
import org.icmss.icmssuserservice.domain.enums.UserRole;
import org.icmss.icmssuserservice.domain.request.*;
import org.icmss.icmssuserservice.domain.response.BalanceResponse;
import org.icmss.icmssuserservice.domain.response.UserResponse;
import org.icmss.icmssuserservice.exceptions.BusinessException;
import org.icmss.icmssuserservice.mapper.UserMapper;
import org.icmss.icmssuserservice.repository.UserOTPRepository;
import org.icmss.icmssuserservice.repository.UserRepository;
import org.icmss.icmssuserservice.util.Util;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final KeycloakService keycloakService;
    private final UserOTPRepository userOTPRepository;
    private final Resilience4JCircuitBreakerFactory circuitBreakerFactory;

    @Value("${CLIENT_ID}")
    private String clientId;
    @Value("${CLIENT_SECRET}")
    private String clientSecret;
    @Value("${TOKEN_URI}")
    private String tokenUri;

    @Transactional
    public String registerUser(UserRegistrationRequest params) {
        trimAndLowerCaseUserRegisterParams(params);
        existsByEmail(params.getEmail());

        UserOTP userOTP = userOTPRepository.findById(params.getEmail()).orElseThrow(
                () -> new BusinessException(HttpStatus.NOT_FOUND, "OTP not found")
        );

        if (userOTP.getFlow().equals(UserOTP.Flow.LOGIN) || !userOTP.isVerified())
            throw new BusinessException(HttpStatus.FORBIDDEN, "Registration otp is not verified.");

        UserRepresentation userRepresentation = createUserRepresentation(params);
        userRepresentation = keycloakService.addUser(userRepresentation);

        Users user = convertParamsToUser(params);
        user.setId(userRepresentation.getId());
        userRepository.save(user);
        userOTP.setVerified(false);
        userOTPRepository.save(userOTP);

        return getJwtToken(params.getEmail(), params.getPassword());
    }

    @Transactional
    public String login(String email, String password) {
        email = email.trim().toLowerCase();
        password = password.trim();
        getUserByEmail(email, DbStatus.ACTIVE);

        String token;
        token = getJwtToken(email, password);

        return token;
    }

    public UserResponse getUserProfile(String userId) {
        Users currentUser = getCurrentUser();
        if (!currentUser.getId().equals(userId) && currentUser.getRole() != UserRole.ADMIN && currentUser.getRole() != UserRole.SUPER_ADMIN) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "Permission denied.");
        }

        Users user = userRepository.getUserById(userId, DbStatus.ACTIVE)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Not found user with id: " + userId));

        return UserMapper.toUserResponse(user);
    }

    @Transactional
    public UserResponse updateUser(UserUpdateRequest param) {
        Users user = getCurrentUser();

        int counter = 0;

        if (!user.getEmail().equals(param.getEmail())) {
            existsByEmail(param.getEmail());
            user.setEmail(param.getEmail());
            counter++;
        }

        if (!user.getFirstName().equals(param.getFirstName())) {
            user.setFirstName(param.getFirstName());
            counter++;
        }

        if (!user.getLastName().equals(param.getLastName())) {
            user.setLastName(param.getLastName());
            counter++;
        }

        if (counter > 0) keycloakService.updateUser(user);
        return UserMapper.toUserResponse(userRepository.save(user));
    }

    @Transactional
    public void changePassword(ChangePasswordRequest changePasswordParam) {
        Users user = getCurrentUser();
        getJwtToken(user.getEmail(), changePasswordParam.getCurrentPassword());

        if (!changePasswordParam.getNewPassword().equals(changePasswordParam.getConfirmNewPassword())) {
            throw new BusinessException(HttpStatus.CONFLICT, "New Password and confirmation must match!");
        }

        if (changePasswordParam.getCurrentPassword().equals(changePasswordParam.getNewPassword()))
            throw new BusinessException(HttpStatus.CONFLICT, "New password can not be same as the current password");

        keycloakService.changePassword(changePasswordParam, user.getId());
    }

    public BigDecimal getUserBalance() {
        Users user = getCurrentUser();
        return user.getBalance();
    }

    public Users getCurrentUser() {
        JwtAuthenticationToken token = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Jwt jwtToken = token.getToken();

        return userRepository.getUserById(jwtToken.getSubject(), DbStatus.ACTIVE)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Not found user with id: " + jwtToken.getSubject()));
    }

    public void getUserByEmail(String email, DbStatus status) {
        userRepository.getUserByEmailAndStatus(email, status)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Not found user with email: " + email));
    }

    private void trimAndLowerCaseUserRegisterParams(UserRegistrationRequest params) {
        params.setEmail(params.getEmail().trim().toLowerCase());
        params.setLastName(StringUtils.capitalize(params.getLastName().trim().toLowerCase()));
        params.setFirstName(StringUtils.capitalize(params.getFirstName().trim().toLowerCase()));
        params.setPassword(params.getPassword().trim());
    }

    public void existsByEmail(String email) {
        Optional<Users> optional = userRepository.getUserByEmailAndStatus(email, DbStatus.ACTIVE);

        if (optional.isPresent()) {
            throw new BusinessException(HttpStatus.CONFLICT, "User with email: " + email + " already exists!");
        }
    }

    private UserRepresentation createUserRepresentation(UserRegistrationRequest params) {
        CredentialRepresentation credential = Util
                .createPasswordCredentials(params.getPassword());
        UserRepresentation user = new UserRepresentation();
        user.setUsername(params.getEmail());
        user.setFirstName(params.getFirstName());
        user.setLastName(params.getLastName());
        user.setEmail(params.getEmail());
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);

        return user;
    }

    private Users convertParamsToUser(UserRegistrationRequest params) {
        Users user = new Users();
        user.setEmail(params.getEmail());
        user.setFirstName(params.getFirstName());
        user.setLastName(params.getLastName());
        user.setStatus(DbStatus.ACTIVE);
        user.setRole(UserRole.USER);
        user.setBalance(BigDecimal.valueOf(0));
        return user;
    }

    private String getJwtToken(String email, String password) {
        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("grant_type", "password");
        request.add("username", email);
        request.add("password", password);

        return getResponse(request);
    }

    private String getResponse(MultiValueMap<String, String> request) {

        WebClient client = WebClient.builder()
                .baseUrl(tokenUri)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " + Base64.encode(clientId + ":" + clientSecret))
                .build();

        return circuitBreakerFactory.create("keycloak").run(() ->
                        client.post()
                                .body(BodyInserters.fromFormData(request))
                                .retrieve()
                                .bodyToMono(String.class)
                                .block(),
                throwable -> {
                    if (throwable.getMessage().contains("Connection refused:"))
                        throw new BusinessException(HttpStatus.SERVICE_UNAVAILABLE, throwable.getMessage());
                    if (throwable instanceof WebClientResponseException webClientException) {
                        throw new BusinessException(HttpStatus.BAD_REQUEST, "Current password is incorrect");
                    } else {
                        return fallBack();
                    }
                });

    }

    private String fallBack() {
        throw new BusinessException(HttpStatus.SERVICE_UNAVAILABLE, "Keycloak service is down.");
    }

}
