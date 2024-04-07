package org.icmss.icmssuserservice.service;

import lombok.RequiredArgsConstructor;
import org.icmss.icmssuserservice.constants.AppConstants;
import org.icmss.icmssuserservice.domain.entity.Users;
import org.icmss.icmssuserservice.domain.request.ChangePasswordRequest;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    private final Keycloak keycloak;

    public UserRepresentation addUser(UserRepresentation userRepresentation) {
        UsersResource instance = getInstance();
        instance.create(userRepresentation);

        List<RoleRepresentation> rolesToAdd =
                List.of(keycloak.realm(AppConstants.REALM_NAME)
                        .roles().get("USER").toRepresentation());
        instance.get(instance.search(userRepresentation.getUsername()).get(0).getId()).roles().realmLevel().add(rolesToAdd);
        return instance.search(userRepresentation.getUsername()).get(0);
    }

    public UsersResource getInstance() {
        return keycloak.realm(AppConstants.REALM_NAME).users();
    }

    public void updateUser(Users user) {
        UsersResource usersResource = getInstance();
        UserRepresentation userRepresentation = usersResource.get(user.getId()).toRepresentation();
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());

        usersResource.get(user.getId()).update(userRepresentation);
    }

    public void changePassword(ChangePasswordRequest changePasswordParam, String userId) {
        UserResource userResource = getInstance().get(userId);
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(changePasswordParam.getNewPassword());
        userResource.resetPassword(credentialRepresentation);
        logout(userId);
    }

    public void logout(String userId) {
        UserResource userResource = getInstance().get(userId);
        userResource.logout();
    }
}
