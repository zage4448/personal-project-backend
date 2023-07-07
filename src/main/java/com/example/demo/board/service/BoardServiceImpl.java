package com.example.demo.board.service;

import com.example.demo.board.controller.form.CategoryBoardListResponseForm;
import com.example.demo.board.controller.form.CategoryListForm;
import com.example.demo.board.entity.Board;
import com.example.demo.board.entity.BoardCategory;
import com.example.demo.board.repostiry.BoardRepository;
import com.example.demo.board.service.request.BoardRegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    final private BoardRepository boardRepository;

    @Override
    public Board register(BoardRegisterRequest registerRequest) {
        return  boardRepository.save(registerRequest.toBoard());
    }

    @Override
    public Board read(Long boardId) {
        Optional<Board> maybeBoard = boardRepository.findById(boardId);

        if (maybeBoard.isPresent()) {
            return maybeBoard.get();
        }
        return null;
    }

    @Override
    public List<CategoryBoardListResponseForm> getListByCategory(BoardCategory category) {
        List<Board> boardList = boardRepository.findAllByCategory(category);

        List<CategoryBoardListResponseForm> categoryBoardList = new ArrayList<>();
        for (Board board: boardList) {
            categoryBoardList.add(
                    new CategoryBoardListResponseForm(
                            board.getBoardId(), board.getTitle(), board.getWriter(),
                            Date.from(board.getCreateDate().atZone(ZoneId.systemDefault()).toInstant())
                    )
            );
        }
        return categoryBoardList;
    }

    @Override
    public List<CategoryListForm> getCategoryList() {
        List<CategoryListForm> categoryList = new ArrayList<>();
        for (BoardCategory category: BoardCategory.values()) {
            Long posts =  boardRepository.findPostNumberByCategory(category);
            log.info("posts: " + posts);
            categoryList.add(
                    new CategoryListForm( category, posts));
        }
        return categoryList;
    }
}
