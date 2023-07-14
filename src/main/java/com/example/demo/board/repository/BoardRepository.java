package com.example.demo.board.repository;

import com.example.demo.board.entity.Board;
import com.example.demo.board.entity.BoardCategory;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("SELECT b FROM Board b WHERE b.boardCategory = :category ORDER BY b.createDate DESC")
    List<Board> findAllByCategory(@Param("category") BoardCategory category);

    @Query("SELECT COUNT(b) FROM Board b WHERE b.boardCategory = :category")
    long findPostNumberByCategory(@Param("category") BoardCategory category);

    @Modifying
    @Query("UPDATE Board b set b.views = b.views + 1 where b.boardId = :boardId")
    int updateViews(@Param("boardId") Long boardId);

    @Query("SELECT DISTINCT b FROM Board b " +
            "LEFT JOIN FETCH b.writer " +
            "WHERE b.title LIKE CONCAT('%', :keyword, '%') " +
            "OR b.content LIKE CONCAT('%', :keyword, '%') " +
            "OR b.writer.nickname LIKE CONCAT('%', :keyword, '%') " +
            "ORDER BY b.views DESC")
    List<Board> findByKeywordSorted(@Param("keyword") String keyword);

    @Query("SELECT b FROM Board b WHERE b.boardCategory = :boardCategory AND b.boardId <> :boardId ORDER BY b.createDate DESC")
    List<Board> findRelatedBoardsByCategoryAndBoardIdNot(BoardCategory boardCategory, Long boardId, Pageable pageable);
}
