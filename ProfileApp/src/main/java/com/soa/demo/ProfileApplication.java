package com.soa.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ProfileApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProfileApplication.class, args);
	}


//	@Bean
//	public WebMvcConfigurer corsConfigurer() {
//		return new WebMvcConfigurer() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
////				registry.addMapping("/login/oauth2/code/google").allowedOrigins("http://localhost:5173");
//				registry.addMapping("/**").allowedOrigins("http://localhost:5173");
//				registry.addMapping("/**").allowedHeaders("*");
//				registry.addMapping("/**").allowedMethods("*");
//				registry.addMapping("/**").allowCredentials(true);
//				registry.addMapping("/**").maxAge(3600);
//				registry.addMapping("/**").exposedHeaders("Authorization");
//				registry.addMapping("/**").exposedHeaders("Content-Type");
////				registry.addMapping("/").allowedOrigins("http://localhost:8080");
//
//
//			}
//		};
//	}

}
