package com.QuackAttack.RegisterApp.database;

import com.QuackAttack.RegisterApp.auth.GoogleTokenVerifier;
import com.QuackAttack.RegisterApp.auth.TokenVerifier;
import com.QuackAttack.RegisterApp.objects.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RegisterAppJdbcDb implements RegisterAppDb {
    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final RegisterAppJdbcDb INSTANCE = new RegisterAppJdbcDb();

    private RegisterAppJdbcDb() { }

    @Primary
    public static RegisterAppDb get() {
        return INSTANCE;
    }

    @Override
    public List<UserData> getUsers(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        return jdbcTemplate.query(sql,
                BeanPropertyRowMapper.newInstance(UserData.class), username);
    }
}
