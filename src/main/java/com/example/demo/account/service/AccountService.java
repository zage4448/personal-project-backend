package com.example.demo.account.service;

import com.example.demo.account.controller.form.AccountInfoResponseForm;
import com.example.demo.account.controller.form.AccountLoginRequestForm;
import com.example.demo.account.controller.form.ChangePasswordRequestForm;
import com.example.demo.account.entity.Account;
import com.example.demo.account.service.request.AccountRegisterRequest;

public interface AccountService {
    Boolean register(AccountRegisterRequest registerRequest);

    String findAccountNicknameByEmail(String email);

    Boolean checkDuplicateEmail(String email);

    Account login(AccountLoginRequestForm loginForm);

    Account findAccountById(Long accountId);

    Boolean checkDuplicateNickname(String nickname);

    AccountInfoResponseForm getAccountInfo(Long accountId);

    Boolean checkPassword(Long accountId, String password);

    void changeNickname(Long accountId, String newNickname);

    Boolean changePassword(Long accountId, ChangePasswordRequestForm requestForm);

    void deleteAccount(Long accountId);
}
