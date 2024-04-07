package org.icmss.icmssuserservice.util;

import org.keycloak.representations.idm.CredentialRepresentation;

public class Util {

//    public static int optimizePageLimit(int limit){
//        if(limit == 0) return 1;
//        return Math.min(limit, AppConstants.MAX_PAGE_SIZE);
//    }

    public static CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }
}
