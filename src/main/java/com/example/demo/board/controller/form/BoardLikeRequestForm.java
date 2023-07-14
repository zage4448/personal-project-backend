package com.example.demo.board.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@RequiredArgsConstructor
public class BoardLikeRequestForm {

    private String userToken;
    private Long boardId;

}
