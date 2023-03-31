package com.QuackAttack.RegisterApp;

import java.util.List;
import java.util.Map;

public record SearchResults(List<UserData> user) {
    public static record UserData(int id, String username, String email) { }
}
