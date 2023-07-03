package com.example.demo.account.service;

import com.example.demo.account.controller.form.AccountRegisterForm;
import com.example.demo.account.service.request.AccountRegisterRequest;

public interface AccountService {
    Boolean register(AccountRegisterRequest registerRequest);

    String findAccountNicknameByEmail(String email);
}
