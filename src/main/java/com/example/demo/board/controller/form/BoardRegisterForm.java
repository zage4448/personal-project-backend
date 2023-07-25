package com.example.demo.board.controller.form;

import com.example.demo.board.entity.BoardCategory;
import com.example.demo.board.service.request.BoardRegisterRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Getter
@Slf4j
@RequiredArgsConstructor
public class BoardRegisterForm {

    final private String userToken;
    final private String title;
    final private String content;
    final private BoardCategory category;
    final private String thumbNailName;
    final private List<String> imageNameList;

}
