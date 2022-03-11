package ru.netology.api.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String login;
    private String password;
    private String code;


    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }
}


