package com.example.demo.board.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class BoardModifyRequestForm {

    final private String title;
    final private String content;
    final private String newThumbNailName;
    final private List<String> newImageNameList;
}
