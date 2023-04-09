package com.QuackAttack.RegisterApp;

import java.util.List;

public record SearchResultsQuack(List<quackData> quack) {
    public static record quackData(String user_id, String quack, java.sql.Timestamp created_at) {
    }
}
