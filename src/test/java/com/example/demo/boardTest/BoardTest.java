package com.example.demo.boardTest;

import com.example.demo.account.entity.Account;
import com.example.demo.account.service.AccountService;
import com.example.demo.board.controller.form.BoardRegisterForm;
import com.example.demo.board.controller.form.CategoryBoardListResponseForm;
import com.example.demo.board.controller.form.CategoryListForm;
import com.example.demo.board.controller.form.ReadBoardResponseForm;
import com.example.demo.board.entity.Board;
import com.example.demo.board.entity.BoardCategory;
import com.example.demo.board.service.BoardService;
import com.example.demo.board.service.request.BoardRegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.example.demo.board.entity.BoardCategory.Asia;
import static com.example.demo.board.entity.BoardCategory.Europe;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BoardTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private BoardService boardService;

    @Test
    @DisplayName("게시물 등록 테스트")
    void registerBoardTest () {
        final Long accountId = 1l;
        final String title = "test board title";
        final String content = "test board content";
        final BoardCategory category = Asia;

        Account account = accountService.findAccountById(accountId);


        BoardRegisterRequest registerRequest = new BoardRegisterRequest(title, account, content, category);

        Long boardId = boardService.register(registerRequest);
        ReadBoardResponseForm board = boardService.read(boardId);
        assertEquals(title, board.getTitle());
        assertEquals(content, board.getContent());
        assertEquals(account.getNickname(), board.getWriter());
        assertEquals(category, board.getBoardCategory());
    }

    @Test
    @DisplayName("게시글 불러오기 테스트")
    void readBoardTest () {
        final Long boardId = 1l;
        final String title = "test board title";
        final String content = "test board content";

        ReadBoardResponseForm board = boardService.read(boardId);

        assertEquals(title, board.getTitle());
        assertEquals(content, board.getContent());
    }

    @Test
    @DisplayName("카테고리별 게시글 리스트 불러오기")
    void bringCategorizedBoardList () {
        final BoardCategory category = Europe;
        final String title = "test board title";
        final Long boardId = 1l;

        List<CategoryBoardListResponseForm> responseFormList = boardService.getListByCategory(category);
        CategoryBoardListResponseForm responseForm = responseFormList.get(0);

        assertEquals(title, responseForm.getTitle());
        assertEquals(boardId, responseForm.getBoardId());
    }

    @Test
    @DisplayName("카테고리와 카테고리별 게시글 수량 리스트 불러오기")
    void bringCategoryListTest() {
        final BoardCategory category = Europe;
        final Long posts = 1l;

        List<CategoryListForm> categoryList = boardService.getCategoryList();

        BoardCategory actualCategory = categoryList.get(1).getBoardCategory();
        Long actualPosts = categoryList.get(1).getPosts();
        assertEquals(category, actualCategory);
        assertEquals(posts, actualPosts);
    }
}
