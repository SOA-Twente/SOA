package com.QuackAttack.DirectMessageApp.auth;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface TokenVerifier {
    String checkToken(String idTokenString) throws GeneralSecurityException, IOException;
}
