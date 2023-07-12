package com.example.demo.board.controller;

import com.example.demo.account.entity.Account;
import com.example.demo.account.service.AccountService;
import com.example.demo.board.controller.form.BoardRegisterForm;
import com.example.demo.board.controller.form.CategoryBoardListResponseForm;
import com.example.demo.board.controller.form.CategoryListForm;
import com.example.demo.board.controller.form.ReadBoardResponseForm;
import com.example.demo.board.entity.BoardCategory;
import com.example.demo.board.service.BoardService;
import com.example.demo.board.service.request.BoardRegisterRequest;
import com.example.demo.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    final private BoardService boardService;
    final private AccountService accountService;
    final private RedisService redisService;

    @GetMapping("/category-list")
    public List<CategoryListForm> getCategoryList() {
        return boardService.getCategoryList();
    }

    @GetMapping("/list/{category}")
    public List<CategoryBoardListResponseForm> categoryBoardList (@PathVariable("category") BoardCategory category) {

        return boardService.getListByCategory(category);
    }

    @GetMapping("/{boardId}")
    public ReadBoardResponseForm readBoard (@PathVariable("boardId") Long boardId) {
        boardService.updateViews(boardId);
        return boardService.read(boardId);
    }

    @PostMapping("/register")
    public Long registerBoard (@RequestBody BoardRegisterForm registerForm) {
        Long accountId = redisService.getValueByKey(registerForm.getUserToken());
        Account account = accountService.findAccountById(accountId);
        return boardService.register(new BoardRegisterRequest(registerForm.getTitle(), account, registerForm.getContent(), registerForm.getCategory()));
    }
}
