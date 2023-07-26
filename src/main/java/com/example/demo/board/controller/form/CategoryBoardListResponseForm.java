package com.example.demo.board.controller.form;

import com.example.demo.board.entity.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

@Getter
@Slf4j
@NoArgsConstructor
public class CategoryBoardListResponseForm {

    private Long boardId;
    private String title;
    private String writer;
    private Date createDate;
    private int likeCount;
    private int viewCount;
    private int commentCount;
    private String thumbNailName;
    private String content;

    public CategoryBoardListResponseForm(Board board) {
        int maxLength = 10;
        String content;

        this.boardId = board.getBoardId();
        this.title = board.getTitle();
        this.writer = board.getWriter().getNickname();
        this.createDate = Date.from(board.getCreateDate().atZone(ZoneId.systemDefault()).toInstant());
        this.likeCount = board.getLikes().size();
        this.viewCount = board.getViews();
        this.commentCount = board.getComments().stream().distinct().collect(Collectors.toList()).size();
        this.thumbNailName = board.getThumbNailName();

        if (board.getContent().length() > maxLength) {
            content = board.getContent().substring(0, maxLength) + "...";
        } else {
            content = board.getContent() + "...";
        }
        this.content = content;
    }
}
