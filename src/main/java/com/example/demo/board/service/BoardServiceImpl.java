package com.example.demo.board.service;

import com.example.demo.board.entity.Board;
import com.example.demo.board.repostiry.BoardRepository;
import com.example.demo.board.service.request.BoardRegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    final private BoardRepository boardRepository;

    @Override
    public Board register(BoardRegisterRequest registerRequest) {
        return  boardRepository.save(registerRequest.toBoard());
    }
}
