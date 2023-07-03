package com.example.demo.account.controller.form;

import com.example.demo.account.entity.Account;
import com.example.demo.account.service.request.AccountRegisterRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountRegisterForm {

    private String email;
    private String password;
    private String nickname;

    public AccountRegisterForm(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public AccountRegisterRequest toAccountRegisterRequest() {
        return new AccountRegisterRequest(email,password,nickname);
    }
}
