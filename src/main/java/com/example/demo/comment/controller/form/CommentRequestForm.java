package com.example.demo.comment.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@RequiredArgsConstructor
public class CommentRequestForm {

    private String userToken;
    private String comment;

}
