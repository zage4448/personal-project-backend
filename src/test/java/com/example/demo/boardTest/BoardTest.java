package com.example.demo.boardTest;

import com.example.demo.account.controller.form.AccountRegisterForm;
import com.example.demo.account.entity.Account;
import com.example.demo.account.repository.AccountRepository;
import com.example.demo.account.service.AccountService;
import com.example.demo.account.service.AccountServiceImpl;
import com.example.demo.account.service.request.AccountRegisterRequest;
import com.example.demo.board.controller.form.*;
import com.example.demo.board.entity.Board;
import com.example.demo.board.entity.BoardCategory;
import com.example.demo.board.repository.BoardRepository;
import com.example.demo.board.service.BoardService;
import com.example.demo.board.service.BoardServiceImpl;
import com.example.demo.board.service.request.BoardRegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.demo.board.entity.BoardCategory.Asia;
import static com.example.demo.board.entity.BoardCategory.Europe;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class BoardTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private BoardServiceImpl boardService;
    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach // 최초 테스트 초기화
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        boardService = new BoardServiceImpl(boardRepository);
        accountService = new AccountServiceImpl(accountRepository);
    }

//    @Autowired
//    private AccountService accountService;
//
//    @Autowired
//    private BoardService boardService;
//
//    @Test
//    @DisplayName("게시물 등록 테스트")
//    void registerBoardTest () {
//        final Long accountId = 1l;
//        final String title = "test board title";
//        final String content = "test board content";
//        final BoardCategory category = Asia;
//
//        Account account = accountService.findAccountById(accountId);
//
//
//        BoardRegisterRequest registerRequest = new BoardRegisterRequest(title, account, content, category);
//
//        Long boardId = boardService.register(registerRequest);
//        ReadBoardResponseForm board = boardService.read(boardId);
//        assertEquals(title, board.getTitle());
//        assertEquals(content, board.getContent());
//        assertEquals(account.getNickname(), board.getWriter());
//        assertEquals(category, board.getBoardCategory());
//    }
//
//    @Test
//    @DisplayName("게시글 불러오기 테스트")
//    void readBoardTest () {
//        final Long boardId = 1l;
//        final String title = "test board title";
//        final String content = "test board content";
//
//        ReadBoardResponseForm board = boardService.read(boardId);
//
//        assertEquals(title, board.getTitle());
//        assertEquals(content, board.getContent());
//    }
//
//    @Test
//    @DisplayName("카테고리별 게시글 리스트 불러오기")
//    void bringCategorizedBoardList () {
//        final BoardCategory category = Europe;
//        final String title = "test board title";
//        final Long boardId = 1l;
//
//        List<CategoryBoardListResponseForm> responseFormList = boardService.getListByCategory(category);
//        CategoryBoardListResponseForm responseForm = responseFormList.get(0);
//
//        assertEquals(title, responseForm.getTitle());
//        assertEquals(boardId, responseForm.getBoardId());
//    }
//
//    @Test
//    @DisplayName("카테고리와 카테고리별 게시글 수량 리스트 불러오기")
//    void bringCategoryListTest() {
//        final BoardCategory category = Europe;
//        final Long posts = 1l;
//
//        List<CategoryListForm> categoryList = boardService.getCategoryList();
//
//        BoardCategory actualCategory = categoryList.get(1).getBoardCategory();
//        Long actualPosts = categoryList.get(1).getPosts();
//        assertEquals(category, actualCategory);
//        assertEquals(posts, actualPosts);
//    }

    @Test
    @DisplayName("게시물 등록 테스트")
    void registerBoardTest() {
        final Long boardId = 1L;
        final String title = "test board title";
        final String content = "test board content";
        final BoardCategory category = BoardCategory.Asia;

        Account account = new Account("test@test.com", "password", "tester");

        BoardRegisterRequest registerRequest = new BoardRegisterRequest(title, account, content, category);

        Mockito.when(boardRepository.save(Mockito.any(Board.class))).thenAnswer(invocation -> {
            Board savedBoard = invocation.getArgument(0);
            Field field = savedBoard.getClass().getDeclaredField("boardId");
            field.setAccessible(true);
            field.set(savedBoard, boardId);
            return savedBoard;
        });

        Long actual = boardService.register(registerRequest);

        assertEquals(boardId, actual);
    }

    @Test
    @DisplayName("게시글 불러오기 테스트")
    void readBoardTest() throws NoSuchFieldException, IllegalAccessException {
        Account account = new Account("test@test.com", "password", "tester");
        final String title = "test board title";
        final String content = "test board content";
        LocalDateTime now = LocalDateTime.now();

        Board board = createBoardWithDate(title, account, content, Asia, now, now);


        Mockito.when(boardRepository.findById(0L)).thenReturn(Optional.of(board));

        ReadBoardResponseForm actual = boardService.read(0L);

        assertEquals(board.getTitle(), actual.getTitle());
        assertEquals(content, actual.getContent());
    }

    @Test
    @DisplayName("카테고리별 게시글 리스트 불러오기")
    void bringCategorizedBoardList() {
        final BoardCategory category = BoardCategory.Europe;

        Mockito.when(boardRepository.findAllByCategory(category)).thenReturn(Collections.emptyList());

        List<CategoryBoardListResponseForm> responseFormList = boardService.getListByCategory(category);

        assertTrue(responseFormList.isEmpty());
    }




    @Test
    @DisplayName("해당 게시글과 같은 카테고리의 최신 2개 게시글 가져오기")
    void bringRelatedBoardList() throws NoSuchFieldException, IllegalAccessException {
        BoardCategory boardCategory = Asia;
        Long boardId = 1L;

        LocalDateTime now = LocalDateTime.now();

        Board selectedBoard = new Board ("selectedTitle", null, "selectedContent", boardCategory);
        Board board1 = createBoardWithDate("Title1", null, "Content1", boardCategory, now, now);
        Board board2 = createBoardWithDate("Title2", null, "Content2", boardCategory, now, now);

        Mockito.when(boardRepository.findRelatedBoardsByCategoryAndBoardIdNot(boardCategory, boardId,
                        PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createDate"))))
                .thenReturn(List.of(board1, board2));

        Mockito.when(boardRepository.findById(boardId)).thenReturn(Optional.of(selectedBoard));


        List<RelatedBoardResponseForm> result = boardService.getRelatedBoardList(boardId);

        assertEquals(result.get(0).getTitle(), board1.getTitle());
        assertEquals(result.get(1).getTitle(), board2.getTitle());
    }

    @Test
    @DisplayName("게시글들을 최신 순으로 나열한다")
    void bringRecentBoardsTest() throws NoSuchFieldException, IllegalAccessException {
        Account account = new Account(null, null, "tester");
        LocalDateTime now = LocalDateTime.now();
        Board board1 = createBoardWithDate("Title1", account, "Content1", Asia, now.minusHours(1), now);
        Board board2 = createBoardWithDate("Title2", account, "Content2", Asia, now.minusHours(2), now);


        Mockito.when(boardRepository.findRecentBoards()).thenReturn(List.of(board1,board2));
        List<RecentBoardListResponseForm> result = boardService.getRecentBoardList();

        assertEquals(result.get(0).getTitle(), board1.getTitle());
        assertEquals(result.get(1).getTitle(), board2.getTitle());
    }


    private Board createBoardWithDate(String title, Account account, String content, BoardCategory boardCategory, LocalDateTime createDate, LocalDateTime updateDate) throws NoSuchFieldException, IllegalAccessException {
        Board board = new Board(title, account, content, boardCategory);
        Field createDateField = Board.class.getDeclaredField("createDate");
        createDateField.setAccessible(true);
        createDateField.set(board, createDate);
        Field updateDateField = Board.class.getDeclaredField("updateDate");
        updateDateField.setAccessible(true);
        updateDateField.set(board, updateDate);
        return board;
    }
}
