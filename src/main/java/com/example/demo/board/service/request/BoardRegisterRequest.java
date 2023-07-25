package com.example.demo.board.service.request;

import com.example.demo.account.entity.Account;
import com.example.demo.board.entity.Board;
import com.example.demo.board.entity.BoardCategory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class BoardRegisterRequest {
    final private String title;
    final private Account writer;
    final private String content;
    final private BoardCategory category;
    final private String thumbNailName;
    final private List<String> imageNameList;

    public Board toBoard () {
        return new Board(title, writer, content, category, thumbNailName, imageNameList);
    }
}
