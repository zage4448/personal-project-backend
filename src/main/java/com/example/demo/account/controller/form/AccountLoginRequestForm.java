package com.example.demo.account.controller.form;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountLoginRequestForm {

    private String email;
    private String password;

    public AccountLoginRequestForm(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
