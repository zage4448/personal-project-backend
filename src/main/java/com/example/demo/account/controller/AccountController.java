package com.example.demo.account.controller;

import com.example.demo.account.controller.form.AccountLoginRequestForm;
import com.example.demo.account.controller.form.AccountCommunicationRequestForm;
import com.example.demo.account.controller.form.AccountRegisterForm;
import com.example.demo.account.service.AccountService;
import com.example.demo.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    final private AccountService accountService;
    final private RedisService redisService;

    @PostMapping("/create-account")
    public Boolean accountRegister (@RequestBody AccountRegisterForm registerForm) {
        return accountService.register(registerForm.toAccountRegisterRequest());
    }

    @GetMapping("/check-email-duplicate/{email}")
    public Boolean checkEmail(@PathVariable("email") String email) {
        return accountService.checkDuplicateEmail(email);
    }

    @PostMapping("/login")
    public String accountLogin (@RequestBody AccountLoginRequestForm requestForm) {
        Long accountId = accountService.login(requestForm);
        if (accountId != null) {
            UUID userToken = UUID.randomUUID();

            redisService.setKeyAndValue(userToken.toString(), accountId);

            return userToken.toString();
        }
        return null;
    }

    @DeleteMapping("/logout")
    public void accountLogout (@RequestBody AccountCommunicationRequestForm requestForm) {
        redisService.deleteByKey(requestForm.getUserToken());
    }

    @GetMapping("/check-nickname-duplicate/{nickname}")
    public Boolean checkNickname(@PathVariable("nickname") String nickname) {
        return accountService.checkDuplicateNickname(nickname);
    }
}
