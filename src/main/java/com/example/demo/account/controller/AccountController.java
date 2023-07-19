package com.example.demo.account.controller;

import com.example.demo.account.controller.form.*;
import com.example.demo.account.entity.Account;
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
    public AccountLoginResponseForm accountLogin (@RequestBody AccountLoginRequestForm requestForm) {
        Account account = accountService.login(requestForm);
        if (account != null) {
            UUID userToken = UUID.randomUUID();

            redisService.setKeyAndValue(userToken.toString(), account.getAccountId());

            return new AccountLoginResponseForm(userToken.toString(), account.getNickname());
        }
        return null;
    }

    @PostMapping("/logout")
    public void accountLogout (@RequestBody AccountCommunicationRequestForm requestForm) {
        log.info("logout");
        redisService.deleteByKey(requestForm.getUserToken());
    }

    @GetMapping("/check-nickname-duplicate/{nickname}")
    public Boolean checkNickname(@PathVariable("nickname") String nickname) {
        return accountService.checkDuplicateNickname(nickname);
    }

    @GetMapping("/{userToken}/account-info")
    public AccountInfoResponseForm getAccountInfo(@PathVariable("userToken") String userToken) {
        Long accountId = redisService.getValueByKey(userToken);
        return accountService.getAccountInfo(accountId);
    }

    @GetMapping("/{userToken}/check-password")
    public Boolean checkPassword(@PathVariable("userToken") String userToken, @RequestParam("password") String password) {
        Long accountId = redisService.getValueByKey(userToken);
        return accountService.checkPassword(accountId, password);
    }

    @PutMapping("/{userToken}/change-nickname")
    public void changeNickname(@PathVariable("userToken") String userToken, @RequestBody ChangeNicknameRequestForm requestForm) {
        Long accountId = redisService.getValueByKey(userToken);
        String newNickname = requestForm.getNewNickname();
        accountService.changeNickname(accountId, newNickname);
    }

    @PutMapping("/{userToken}/change-password")
    public Boolean changePassword(@PathVariable("userToken") String userToken, @RequestBody ChangePasswordRequestForm requestForm) {
        Long accountId = redisService.getValueByKey(userToken);
        return accountService.changePassword(accountId, requestForm);
    }

    @DeleteMapping("/{userToken}/delete-account")
    public void deleteAccount(@PathVariable("userToken") String userToken) {
        Long accountId = redisService.getValueByKey(userToken);
        accountService.deleteAccount(accountId);
        redisService.deleteByKey(userToken);
    }
}
