package com.example.demo.comment.controller;

import com.example.demo.comment.controller.form.CommentListResponseForm;
import com.example.demo.comment.service.CommentService;
import com.example.demo.comment.controller.form.CommentRequestForm;
import com.example.demo.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/{boardId}/comments")
public class CommentController {

    final private CommentService commentService;
    final private RedisService redisService;

    @PostMapping("/add-comment")
    public void addComment(@PathVariable Long boardId, @RequestBody CommentRequestForm requestForm) {
        Long accountId = redisService.getValueByKey(requestForm.getUserToken());
        commentService.addComment(boardId, requestForm.getComment(), accountId);
    }

    @GetMapping("/list")
    public List<CommentListResponseForm> getAllCommentsWithNicknameByBoardId(@PathVariable Long boardId) {
        return commentService.getAllCommentsWithNicknameByBoardId(boardId);
    }
}
