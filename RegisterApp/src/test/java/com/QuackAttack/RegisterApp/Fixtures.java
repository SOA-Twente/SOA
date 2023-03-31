package com.QuackAttack.RegisterApp;

import com.QuackAttack.RegisterApp.objects.UserData;

public class Fixtures {
    public static final String TEST_CREDENTIAL = "my.test.jwt";
    public static final String TEST_USERNAME = "test@example.com";
    public static final UserData TEST_USER = new UserData(
            Fixtures.TEST_USERNAME,
            "test",
            Fixtures.TEST_USERNAME
    );
}
