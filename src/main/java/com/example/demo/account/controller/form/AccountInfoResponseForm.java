package com.example.demo.account.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccountInfoResponseForm {
    final private String email;
    final private String nickname;
    final private int postCount;
}
