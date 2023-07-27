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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

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
                                        board.getComments().stream().distinct().collect(Collectors.toList()).size(),
                                        board.getImageNameList());
        }
        return null;
    }

    @Override
    public Page<CategoryBoardListResponseForm> getListByCategory(BoardCategory category, Pageable pageable) {
        Page<Board> boardList = boardRepository.findAllByCategory(category, pageable);
        return boardList.map(this::convertToCategoryBoardListResponseForm);
    }

    @Override
    public List<CategoryListForm> getCategoryList() {
        List<CategoryListForm> categoryList = new ArrayList<>();
        for (BoardCategory category: BoardCategory.values()) {
            Long posts =  boardRepository.findPostNumberByCategory(category);
            categoryList.add(
                    new CategoryListForm( category, posts));
        }
        return categoryList;
    }

    @Override
    public Page<BoardListWithCategoryResponseForm> searchBoards(String searchKeyword, Pageable pageable) {
        Page<Board> searchedBoardList = boardRepository.findByKeywordSorted(searchKeyword, pageable);
        return searchedBoardList.map(this::convertToBoardListWithCategoryResponseForm);
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
    public Page<BoardListWithCategoryResponseForm> getRecentBoardList(Pageable pageable) {
        Page<Board> boardList = boardRepository.findRecentBoards(pageable);
        return boardList.map(this::convertToBoardListWithCategoryResponseForm);
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

    @Override
    public Page<MyBoardsResponseForm> getMyBoardList(Long accountId, Pageable pageable) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        Page<Board> foundBoardList = boardRepository.findAllByAccount(account, pageable);
        return foundBoardList.map(this::convertToMyBoardsResponseForm);
    }

    @Override
    public void modify(Long boardId, BoardModifyRequestForm requestForm) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        board.setTitle(requestForm.getTitle());
        board.setContent(requestForm.getContent().replace("\n", "<br>"));
        board.setThumbNailName(requestForm.getNewThumbNailName());
        board.setImageNameList(requestForm.getNewImageNameList());

        boardRepository.save(board);
    }

    @Override
    public void delete(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        boardRepository.delete(board);
    }

    @Override
    public Page<BoardListWithCategoryResponseForm> getMyLikedBoardList(Long accountId, Pageable pageable) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        Page<Board> boardList = boardRepository.findAllByLikedAccount(account, pageable);
        return boardList.map(this::convertToBoardListWithCategoryResponseForm);
    }

    @Override
    public BoardReadForModifyResponseForm readForModify(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        return new BoardReadForModifyResponseForm(board);
    }



    private CategoryBoardListResponseForm convertToCategoryBoardListResponseForm(Board board) {
        return new CategoryBoardListResponseForm(board);
    }

    private BoardListWithCategoryResponseForm convertToBoardListWithCategoryResponseForm(Board board) {
        return new BoardListWithCategoryResponseForm(board);
    }

    private MyBoardsResponseForm convertToMyBoardsResponseForm(Board board) {
        return new MyBoardsResponseForm(board);
    }
}
