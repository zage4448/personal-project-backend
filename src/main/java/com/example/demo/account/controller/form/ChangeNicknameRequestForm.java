package com.example.demo.account.controller.form;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChangeNicknameRequestForm {
    private String newNickname;

    public ChangeNicknameRequestForm(String newNickname) {
        this.newNickname = newNickname;
    }
}
