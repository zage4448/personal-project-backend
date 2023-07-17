package com.example.demo.comment.controller.form;

import com.example.demo.comment.entity.Comment;
import lombok.Getter;

import java.time.ZoneId;
import java.util.Date;

@Getter
public class CommentListResponseForm {

    private Long commentId;
    private String content;
    private String nickname;
    private Date createDate;

    public CommentListResponseForm(Comment comment, String nickname) {
        this.commentId = comment.getCommentId();
        this.content = comment.getContent().replace("\n", "<br>");
        this.nickname = nickname;
        this.createDate = Date.from(comment.getCreateDate().atZone(ZoneId.systemDefault()).toInstant());
    }
}
