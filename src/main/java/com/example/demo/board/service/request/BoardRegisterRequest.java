package com.example.demo.board.service.request;

import com.example.demo.board.entity.Board;
import com.example.demo.board.entity.BoardCategory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BoardRegisterRequest {
    final private String title;
    final private String writer;
    final private String content;
    final private BoardCategory category;

    public Board toBoard () {
        return new Board(title, writer, content, category);
    }
}
