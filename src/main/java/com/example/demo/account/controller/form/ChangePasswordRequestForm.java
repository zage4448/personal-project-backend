package com.example.demo.account.controller.form;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChangePasswordRequestForm {
    private String password;
    private String newPassword;

    public ChangePasswordRequestForm(String password, String newPassword) {
        this.password = password;
        this.newPassword = newPassword;
    }
}
