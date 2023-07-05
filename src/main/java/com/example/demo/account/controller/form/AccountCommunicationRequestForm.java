package com.example.demo.account.controller.form;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountCommunicationRequestForm {
    private String userToken;

    public AccountCommunicationRequestForm(String userToken) {
        this.userToken = userToken;
    }
}
