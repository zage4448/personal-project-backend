package com.example.demo.account.service;

import com.example.demo.account.controller.form.AccountLoginRequestForm;
import com.example.demo.account.entity.Account;
import com.example.demo.account.service.request.AccountRegisterRequest;

public interface AccountService {
    Boolean register(AccountRegisterRequest registerRequest);

    String findAccountNicknameByEmail(String email);

    Boolean checkDuplicateEmail(String email);

    Long login(AccountLoginRequestForm loginForm);

    Account findAccountById(Long accountId);
}
