package com.example.demo.board.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BoardModifyRequestForm {

    final private String title;
    final private String content;
}
