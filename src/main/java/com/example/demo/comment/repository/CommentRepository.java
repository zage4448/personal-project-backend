package com.example.demo.comment.repository;

import com.example.demo.account.entity.Account;
import com.example.demo.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.board.boardId = :boardId ")
    List<Comment> findByBoardId(Long boardId);

    @Query("SELECT c FROM Comment c WHERE c.account = :account")
    List<Comment> findAllByAccount(Account account);
}
