package com.example.demo.board.service;

import com.example.demo.board.controller.form.*;
import com.example.demo.board.entity.Board;
import com.example.demo.board.entity.BoardCategory;
import com.example.demo.board.service.request.BoardRegisterRequest;

import java.util.List;

public interface BoardService {
    Long register(BoardRegisterRequest registerRequest);

    ReadBoardResponseForm read(Long boardId);

    List<CategoryBoardListResponseForm> getListByCategory(BoardCategory category);

    List<CategoryListForm> getCategoryList();

    Integer updateViews(Long boardId);

    List<SearchBoardListResponseForm> searchBoards(String searchKeyword);

    List<RelatedBoardResponseForm> getRelatedBoardList(Long boardId);

    List<RecentBoardListResponseForm> getRecentBoardList();

    Boolean likeBoard(Long boardId, Long accountId);

    Boolean unlikeBoard(Long boardId, Long accountId);

    Boolean isBoardLiked(Long boardId, Long accountId);
}
