package com.example.demo.accountTest;

import com.example.demo.account.controller.form.AccountRegisterForm;
import com.example.demo.account.entity.Account;
import com.example.demo.account.service.AccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class AccountTest {

    @Autowired
    private AccountService accountService;

    @Test
    @DisplayName("사용자 회원가입 테스트")
    void registerAccountTest () {
        final String email = "test@test.com";
        final String password = "test";
        final String nickname = "test";

        AccountRegisterForm registerForm = new AccountRegisterForm(email, password, nickname);

        Boolean isRegistered = accountService.register(registerForm.toAccountRegisterRequest());
        if (isRegistered) {
            String actualNickname = accountService.findAccountNicknameByEmail(email);
            assertEquals(nickname, actualNickname);
        }
    }

    @Test
    @DisplayName("이메일 중복확인 테스트")
    void checkDuplicateTest () {
        final String email = "test@test.com";

        Boolean isDuplicate = accountService.checkDuplicateEmail(email);

        assertFalse(isDuplicate);
    }
}
