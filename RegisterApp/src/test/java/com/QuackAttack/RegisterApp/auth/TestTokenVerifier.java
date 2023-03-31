package com.QuackAttack.RegisterApp.auth;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class TestTokenVerifier implements TokenVerifier {
    @Override
    public String checkToken(String idTokenString) throws GeneralSecurityException, IOException {
        if (idTokenString.equals("test")) {
            return "myTest@testy.com";
        } else {
            throw new GeneralSecurityException("Invalid ID token.");
        }
    }
}
