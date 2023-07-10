package com.example.demo.accountTest;

import com.example.demo.account.controller.form.AccountLoginRequestForm;
import com.example.demo.account.controller.form.AccountRegisterForm;
import com.example.demo.account.entity.Account;
import com.example.demo.account.service.AccountService;
import com.example.demo.redis.service.RedisService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class AccountTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private RedisService redisService;

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

        assertTrue(isDuplicate);
    }

    @Test
    @DisplayName("로그인 테스트")
    void loginTest () {
        final String email = "test@test.com";
        final String password = "test";

        AccountLoginRequestForm loginForm = new AccountLoginRequestForm(email, password);
        Long accountId = accountService.login(loginForm);

        if (accountId != null) {
            UUID userToken = UUID.randomUUID();

            redisService.setKeyAndValue(userToken.toString(), accountId);

            Long actualAccountId = redisService.getValueByKey(userToken.toString());
            Account actualAccount = accountService.findAccountById(actualAccountId);
            assertEquals(email, actualAccount.getEmail());
        }
    }

    @Test
    @DisplayName("로그아웃 테스트")
    void LogoutTest () {
        final String email = "test@test.com";
        final String password = "test";
        UUID userToken = UUID.randomUUID();

        AccountLoginRequestForm loginForm = new AccountLoginRequestForm(email, password);
        Long accountId = accountService.login(loginForm);

        if (accountId != null) {
            redisService.setKeyAndValue(userToken.toString(), accountId);
        }

        redisService.deleteByKey(userToken.toString());
        Long testAccountId = redisService.getValueByKey(userToken.toString());

        assertEquals(null, testAccountId);
    }

    @Test
    @DisplayName("닉네임 중복확인 테스트")
    void checkDuplicateNicknameTest () {
        final String nickname = "test";

        Boolean isDuplicate = accountService.checkDuplicateNickname(nickname);

        assertFalse(isDuplicate);
    }
}
