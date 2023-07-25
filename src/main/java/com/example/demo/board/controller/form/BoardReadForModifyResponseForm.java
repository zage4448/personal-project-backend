package com.example.demo.board.controller.form;

import com.example.demo.board.entity.Board;
import com.example.demo.board.entity.BoardCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
public class BoardReadForModifyResponseForm {

    private String title;
    private String writer;
    private String content;
    private Date createDate;
    private Date updateDate;
    private BoardCategory boardCategory;
    private int views;
    private int likeCount;
    private int commentCount;
    private String thumbNailName;
    private List<String> imageNameList;

    public BoardReadForModifyResponseForm(Board board) {
        this.title = board.getTitle();
        this.writer = board.getWriter().getNickname();
        this.content = board.getContent();
        this.boardCategory = board.getBoardCategory();
        this.createDate = Date.from(board.getCreateDate().atZone(ZoneId.systemDefault()).toInstant());
        this.updateDate = Date.from(board.getUpdateDate().atZone(ZoneId.systemDefault()).toInstant());
        this.views = board.getViews();
        this.likeCount = board.getLikes().size();
        this.commentCount = board.getComments().size();
        this.thumbNailName = board.getThumbNailName();
        this.imageNameList = board.getImageNameList();
    }
}
