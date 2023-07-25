package com.example.demo.board.controller.form;

import com.example.demo.board.entity.BoardCategory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ReadBoardResponseForm {

    final private String title;
    final private String writer;
    final private String content;
    final private Date createDate;
    final private Date updateDate;
    final private BoardCategory boardCategory;
    final private int views;
    final private int likeCount;
    final private int commentCount;
    final private List<String> imageNameList;
}
