package com.example.demo.board.service;

import com.example.demo.account.entity.Account;
import com.example.demo.account.repository.AccountRepository;
import com.example.demo.board.controller.form.*;
import com.example.demo.board.entity.Board;
import com.example.demo.board.entity.BoardCategory;
import com.example.demo.board.repository.BoardRepository;
import com.example.demo.board.service.request.BoardRegisterRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{

    final private BoardRepository boardRepository;
    final private AccountRepository accountRepository;

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
                                        board.getViews(),
                                        board.getLikes().size(),
                                        board.getComments().size());
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
                            board.getBoardId(), board.getTitle(), board.getWriter().getNickname(), board.getContent().replace("<br>", "\n"),
                            Date.from(board.getCreateDate().atZone(ZoneId.systemDefault()).toInstant()), board.getBoardCategory()
                    )
            );
        }
        return responseFormList;
    }

    @Override
    public List<RelatedBoardResponseForm> getRelatedBoardList(Long boardId) {
        Optional<Board> maybeBoard = boardRepository.findById(boardId);
        if (maybeBoard.isEmpty()) {
            return null;
        }
        Board currentBoard = maybeBoard.get();
        BoardCategory category = currentBoard.getBoardCategory();
        List<Board> boardList = boardRepository
                                        .findRelatedBoardsByCategoryAndBoardIdNot(
                                                category, boardId,
                                                PageRequest.of(0, 2,
                                                        Sort.by(Sort.Direction.DESC, "createDate")));
        List<RelatedBoardResponseForm> responseFormList = new ArrayList<>();
        for (Board board: boardList) {
            responseFormList.add(new RelatedBoardResponseForm(board.getBoardId(), board.getTitle()));
        }
        return responseFormList;
    }

    @Override
    public List<RecentBoardListResponseForm> getRecentBoardList() {
        List<Board> boardList = boardRepository.findRecentBoards();

        List<RecentBoardListResponseForm> recentBoardList = new ArrayList<>();

        for (Board board: boardList) {
            recentBoardList.add(new RecentBoardListResponseForm(
                    board.getBoardId(), board.getTitle(), board.getWriter().getNickname(), board.getBoardCategory(),
                    Date.from(board.getCreateDate().atZone(ZoneId.systemDefault()).toInstant())));
        }
        return recentBoardList;
    }

    @Override
    @Transactional
    public Integer updateViews(Long boardId) {
        return boardRepository.updateViews(boardId);
    }

    @Override
    public Boolean likeBoard(Long boardId, Long accountId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (board.getLikes().contains(account)) {
            return false;
        }
        board.addLike(account);
        boardRepository.save(board);
        return true;
    }

    @Override
    public Boolean unlikeBoard(Long boardId, Long accountId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (!board.getLikes().contains(account)) {
            return false;
        }

        board.removeLike(account);
        boardRepository.save(board);
        return true;
    }

    @Override
    public Boolean isBoardLiked(Long boardId, Long accountId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (account.getLikedBoards().contains(board)) {
            return true;
        }
        return false;
    }
}
