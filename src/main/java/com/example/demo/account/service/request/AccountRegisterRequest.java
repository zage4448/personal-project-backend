package com.example.demo.account.service.request;

import com.example.demo.account.entity.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccountRegisterRequest {
    final private String email;
    final private String password;
    final private String nickname;

    public Account toAccount () {
        return new Account(email,password,nickname);
    }
}
