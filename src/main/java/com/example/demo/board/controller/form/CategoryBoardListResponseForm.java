package com.example.demo.board.controller.form;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Getter
@Slf4j
@RequiredArgsConstructor
public class CategoryBoardListResponseForm {

    final private Long boardId;
    final private String title;
    final private String writer;
    final private Date createDate;
    final private int likeCount;
    final private int viewCount;
    final private int commentCount;
    final private String thumbNailName;
    final private String content;

}
