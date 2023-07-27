package com.example.demo.board.service;

import com.example.demo.board.controller.form.*;
import com.example.demo.board.entity.Board;
import com.example.demo.board.entity.BoardCategory;
import com.example.demo.board.service.request.BoardRegisterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardService {
    Long register(BoardRegisterRequest registerRequest);

    ReadBoardResponseForm read(Long boardId);

    Page<CategoryBoardListResponseForm> getListByCategory(BoardCategory category, Pageable pageable);

    List<CategoryListForm> getCategoryList();

    Integer updateViews(Long boardId);

    Page<BoardListWithCategoryResponseForm> searchBoards(String searchKeyword, Pageable pageable);

    List<RelatedBoardResponseForm> getRelatedBoardList(Long boardId);

    Page<BoardListWithCategoryResponseForm> getRecentBoardList(Pageable pageable);

    Boolean likeBoard(Long boardId, Long accountId);

    Boolean unlikeBoard(Long boardId, Long accountId);

    Boolean isBoardLiked(Long boardId, Long accountId);

    Page<MyBoardsResponseForm> getMyBoardList(Long accountId, Pageable pageable);

    void modify(Long boardId, BoardModifyRequestForm requestForm);

    void delete(Long boardId);

    List<MyLikedBoardsResponseForm> getMyLikedBoardList(Long accountId);

    BoardReadForModifyResponseForm readForModify(Long boardId);
}
