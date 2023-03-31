package com.QuackAttack.RegisterApp;

import com.QuackAttack.RegisterApp.auth.GoogleTokenVerifier;
import com.QuackAttack.RegisterApp.auth.TokenVerifier;
import com.QuackAttack.RegisterApp.database.RegisterAppDb;
import com.QuackAttack.RegisterApp.database.RegisterAppJdbcDb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class RegisterAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(RegisterAppApplication.class, args);
	}

}
