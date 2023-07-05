package com.example.demo.account.controller;

import com.example.demo.account.controller.form.AccountRegisterForm;
import com.example.demo.account.service.AccountService;
import com.example.demo.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    final private AccountService accountService;

    @PostMapping("/create-account")
    public Boolean accountRegister (@RequestBody AccountRegisterForm registerForm) {
        return accountService.register(registerForm.toAccountRegisterRequest());
    }

    @GetMapping("/check-email-duplicate/{email}")
    public Boolean checkEmail(@PathVariable("email") String email) {
        return accountService.checkDuplicateEmail(email);
    }
}
