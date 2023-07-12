package com.example.demo.board.service;

import com.example.demo.board.controller.form.CategoryBoardListResponseForm;
import com.example.demo.board.controller.form.CategoryListForm;
import com.example.demo.board.controller.form.ReadBoardResponseForm;
import com.example.demo.board.controller.form.SearchBoardListResponseForm;
import com.example.demo.board.entity.Board;
import com.example.demo.board.entity.BoardCategory;
import com.example.demo.board.repository.BoardRepository;
import com.example.demo.board.service.request.BoardRegisterRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public Long register(BoardRegisterRequest registerRequest) {
        Board board = boardRepository.save(registerRequest.toBoard());
        return board.getBoardId();
    }

    @Override
    public ReadBoardResponseForm read(Long boardId) {
        Optional<Board> maybeBoard = boardRepository.findById(boardId);

        if (maybeBoard.isPresent()) {
            Board board = maybeBoard.get();
            return new ReadBoardResponseForm(board.getTitle(),
                                        board.getWriter().getNickname(),
                                        board.getContent(),
                                        Date.from(board.getCreateDate().atZone(ZoneId.systemDefault()).toInstant()),
                                        Date.from(board.getUpdateDate().atZone(ZoneId.systemDefault()).toInstant()),
                                        board.getBoardCategory(),
                                        board.getViews());
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
                            board.getBoardId(), board.getTitle(), board.getWriter().getNickname(),
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

    @Override
    public List<SearchBoardListResponseForm> searchBoards(String searchKeyword) {
        List<Board> searchedBoardList = boardRepository.findByKeywordSorted(searchKeyword);
        List<SearchBoardListResponseForm> responseFormList = new ArrayList<>();
        for (Board board: searchedBoardList) {
            responseFormList.add(
                    new SearchBoardListResponseForm(
                            board.getBoardId(), board.getTitle(), board.getWriter().getNickname(), board.getContent(),
                            Date.from(board.getCreateDate().atZone(ZoneId.systemDefault()).toInstant())
                    )
            );
        }
        return responseFormList;
    }

    @Override
    @Transactional
    public Integer updateViews(Long boardId) {
        return boardRepository.updateViews(boardId);
    }
}
