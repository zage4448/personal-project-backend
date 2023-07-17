package com.example.demo.comment.service;

import com.example.demo.account.entity.Account;
import com.example.demo.account.repository.AccountRepository;
import com.example.demo.board.entity.Board;
import com.example.demo.comment.controller.form.CommentListResponseForm;
import com.example.demo.comment.entity.Comment;
import com.example.demo.board.repository.BoardRepository;
import com.example.demo.comment.repository.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    final private BoardRepository boardRepository;
    final private AccountRepository accountRepository;
    final private CommentRepository commentRepository;

    @Override
    public void addComment(Long boardId, String commentContent, Long accountId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        Comment comment = new Comment(commentContent, board, account);
        board.getComments().add(comment);
        commentRepository.save(comment);
        boardRepository.save(board);
    }
    
}
