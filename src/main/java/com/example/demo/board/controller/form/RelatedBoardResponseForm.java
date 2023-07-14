package com.example.demo.board.controller.form;

import com.example.demo.board.entity.BoardCategory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@RequiredArgsConstructor
public class RelatedBoardResponseForm {

    final private Long boardId;
    final private String title;

}
