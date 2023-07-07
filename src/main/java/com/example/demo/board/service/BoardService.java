package com.example.demo.board.service;

import com.example.demo.board.entity.Board;
import com.example.demo.board.service.request.BoardRegisterRequest;

public interface BoardService {
    Board register(BoardRegisterRequest registerRequest);

    Board read(Long boardId);
}
