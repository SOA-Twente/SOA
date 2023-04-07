package com.QuackAttack.DirectMessageConsumer.objects;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageRowMapper implements RowMapper<Message> {
    @Override
    public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
        Message message = new Message();
        message.setID(rs.getInt("ID"));
        message.setConvoID(rs.getInt("convoID"));
        message.setSender(rs.getString("sender"));
        message.setReceiver(rs.getString("receiver"));
        message.setMessage(rs.getString("message"));
        return message;
    }
}
