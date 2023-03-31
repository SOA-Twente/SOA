package com.QuackAttack.RegisterApp;

import com.QuackAttack.RegisterApp.auth.TokenVerifier;
import com.QuackAttack.RegisterApp.database.RegisterAppDb;
import com.QuackAttack.RegisterApp.objects.UserData;
import com.QuackAttack.RegisterApp.web.IndexController;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
class RegisterAppApplicationTests {

	@Autowired
	IndexController index;

	@MockBean
	private TokenVerifier verifier;

	@MockBean
	private RegisterAppDb appDb;

	@Test
	void contextLoads() throws GeneralSecurityException, IOException {
		when(verifier.checkToken(Fixtures.TEST_CREDENTIAL)).thenReturn(Fixtures.TEST_USERNAME);
		when(appDb.getUsers(Fixtures.TEST_USERNAME)).thenReturn(ImmutableList.of(Fixtures.TEST_USER));

		assertThat(index.doesUserExist(Fixtures.TEST_CREDENTIAL).getBody())
				.asString()
				.contains("User already exists");
	}

}
