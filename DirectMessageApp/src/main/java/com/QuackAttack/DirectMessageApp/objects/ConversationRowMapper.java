package com.QuackAttack.DirectMessageApp.objects;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConversationRowMapper implements RowMapper<Conversation> {
    @Override
    public Conversation mapRow(ResultSet rs, int rowNum) throws SQLException {
        Conversation conversation = new Conversation();
        conversation.setConvoID(rs.getInt("convoID"));
        conversation.setUserInitiator(rs.getString("userinitiator"));
        conversation.setUserReceiver(rs.getString("userreceiver"));
        return conversation;
    }
}
