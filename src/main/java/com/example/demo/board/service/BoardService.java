package com.example.demo.board.service;

import com.example.demo.board.controller.form.CategoryBoardListResponseForm;
import com.example.demo.board.controller.form.CategoryListForm;
import com.example.demo.board.entity.Board;
import com.example.demo.board.entity.BoardCategory;
import com.example.demo.board.service.request.BoardRegisterRequest;

import java.util.List;

public interface BoardService {
    Board register(BoardRegisterRequest registerRequest);

    Board read(Long boardId);

    List<CategoryBoardListResponseForm> getListByCategory(BoardCategory category);

    List<CategoryListForm> getCategoryList();
}
