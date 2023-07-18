package com.example.demo.board.controller.form;

import com.example.demo.board.entity.Board;
import com.example.demo.board.entity.BoardCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.util.Date;

@Getter
@NoArgsConstructor
public class MyBoardsResponseForm {

    private Long boardId;
    private String title;
    private BoardCategory category;
    private Date createDate;
    private int likeCount;
    private int viewCount;

    public MyBoardsResponseForm(Board board) {
        this.boardId = board.getBoardId();
        this.title = board.getTitle();
        this.category = board.getBoardCategory();
        this.createDate = Date.from(board.getCreateDate().atZone(ZoneId.systemDefault()).toInstant());
        this.likeCount = board.getLikes().size();
        this.viewCount = board.getViews();
    }
}
