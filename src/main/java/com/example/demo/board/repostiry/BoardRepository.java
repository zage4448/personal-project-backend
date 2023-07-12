package com.example.demo.board.repostiry;

import com.example.demo.board.entity.Board;
import com.example.demo.board.entity.BoardCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("SELECT b FROM Board b WHERE b.boardCategory = :category")
    List<Board> findAllByCategory(@Param("category") BoardCategory category);

    @Query("SELECT COUNT(b) FROM Board b WHERE b.boardCategory = :category")
    long findPostNumberByCategory(@Param("category") BoardCategory category);

    @Modifying
    @Query("UPDATE Board b set b.views = b.views + 1 where b.boardId = :boardId")
    int updateViews(@Param("boardId") Long boardId);
}
