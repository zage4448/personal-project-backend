package com.example.demo.board.controller;

import com.example.demo.account.entity.Account;
import com.example.demo.account.service.AccountService;
import com.example.demo.board.controller.form.*;
import com.example.demo.board.entity.BoardCategory;
import com.example.demo.board.service.BoardService;
import com.example.demo.board.service.request.BoardRegisterRequest;
import com.example.demo.redis.service.RedisService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    public ReadBoardResponseForm readBoard (@PathVariable("boardId") Long boardId, HttpServletRequest req, HttpServletResponse res) {
        viewsCountUp(boardId, req, res);
        return boardService.read(boardId);
    }

    @PostMapping("/register")
    public Long registerBoard (@RequestBody BoardRegisterForm registerForm) {
        Long accountId = redisService.getValueByKey(registerForm.getUserToken());
        Account account = accountService.findAccountById(accountId);
        return boardService.register(new BoardRegisterRequest(registerForm.getTitle(), account, registerForm.getContent(), registerForm.getCategory()));
    }

    @GetMapping("/list/search/{searchKeyword}")
    public List<SearchBoardListResponseForm> searchBoards (@PathVariable("searchKeyword") String searchKeyword) {
        return boardService.searchBoards(searchKeyword);
    }

    @GetMapping("/list/related-board/{boardId}")
    public List<RelatedBoardResponseForm> getRelatedBoardList (@PathVariable("boardId") Long boardId) {
        return boardService.getRelatedBoardList(boardId);
    }



    private void viewsCountUp(Long boardId, HttpServletRequest req, HttpServletResponse res){
        Cookie oldCookie = null;

        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("boardView")) {
                    oldCookie = cookie;
                }
            }
        }

        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("[" + boardId.toString() + "]")) {
                boardService.updateViews(boardId);
                oldCookie.setValue(oldCookie.getValue() + "_[" + boardId + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24);
                res.addCookie(oldCookie);
            }
        } else {
            boardService.updateViews(boardId);
            Cookie newCookie = new Cookie("boardView","[" + boardId + "]");
            newCookie.setPath("/");
            newCookie.setHttpOnly(true);
            res.addCookie(newCookie);
        }
    }
}
