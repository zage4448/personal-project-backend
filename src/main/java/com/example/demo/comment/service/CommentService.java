package com.example.demo.comment.service;

import com.example.demo.comment.controller.form.CommentListResponseForm;

import java.util.List;

public interface CommentService {
    void addComment(Long boardId, String comment, Long accountId);

    List<CommentListResponseForm> getAllCommentsWithNicknameByBoardId(Long boardId);
}
