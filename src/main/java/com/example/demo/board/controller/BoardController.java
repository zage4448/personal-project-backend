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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public Page<CategoryBoardListResponseForm> categoryBoardList(@PathVariable("category") BoardCategory category,
                                                                 @RequestParam(defaultValue = "0") int currentPage,
                                                                 @RequestParam(defaultValue = "8") int pageSize) {

        log.info("currentPage" + currentPage);
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        return boardService.getListByCategory(category, pageable);
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
        return boardService.register(new BoardRegisterRequest(registerForm.getTitle(), account, registerForm.getContent(), registerForm.getCategory(), registerForm.getThumbNailName(), registerForm.getImageNameList()));
    }

    @GetMapping("/list/search/{searchKeyword}")
    public Page<BoardListWithCategoryResponseForm> searchBoards (@PathVariable("searchKeyword") String searchKeyword,
                                                                 @RequestParam(defaultValue = "0") int currentPage,
                                                                 @RequestParam(defaultValue = "8") int pageSize) {
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        return boardService.searchBoards(searchKeyword, pageable);
    }

    @GetMapping("/list/related-board/{boardId}")
    public List<RelatedBoardResponseForm> getRelatedBoardList (@PathVariable("boardId") Long boardId) {
        return boardService.getRelatedBoardList(boardId);
    }

    @GetMapping("/list/recent")
    public Page<BoardListWithCategoryResponseForm> getRecentList (@RequestParam(defaultValue = "0") int currentPage,
                                                                  @RequestParam(defaultValue = "8") int pageSize) {
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        return boardService.getRecentBoardList(pageable);
    }

    @GetMapping("/list/most-liked")
    public Page<BoardListWithCategoryResponseForm> getMostLikedList (@RequestParam(defaultValue = "0") int currentPage,
                                                                  @RequestParam(defaultValue = "8") int pageSize) {
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        return boardService.getMostLikedBoardList(pageable);
    }

    @PostMapping("/likeBoard")
    public void likeBoard (@RequestBody BoardLikeRequestForm requestForm) {
        Long accountId = redisService.getValueByKey(requestForm.getUserToken());
        boardService.likeBoard(requestForm.getBoardId(), accountId);
    }

    @PostMapping("/unLikeBoard")
    public void unLikeBoard (@RequestBody BoardLikeRequestForm requestForm) {
        Long accountId = redisService.getValueByKey(requestForm.getUserToken());
        boardService.unlikeBoard(requestForm.getBoardId(), accountId);
    }

    @GetMapping("/checkBoardLiked")
    public Boolean isBoardLiked(@RequestParam("userToken") String userToken, @RequestParam("boardId") Long boardId) {
        if (userToken == null) {
            return false;
        }

        Long accountId = redisService.getValueByKey(userToken);
        return boardService.isBoardLiked(boardId, accountId);
    }

    @GetMapping("/{userToken}/my-boards")
    public Page<MyBoardsResponseForm> getMyBoards(@PathVariable("userToken") String userToken,
                                                  @RequestParam(defaultValue = "0") int currentPage,
                                                  @RequestParam(defaultValue = "8") int pageSize) {

        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Long accountId = redisService.getValueByKey(userToken);
        return boardService.getMyBoardList(accountId, pageable);
    }

    @PutMapping("/{boardId}/modify-board")
    public void modifyBoard(@PathVariable("boardId") Long boardId, @RequestBody BoardModifyRequestForm requestForm) {
        boardService.modify(boardId, requestForm);
    }

    @DeleteMapping("/{boardId}/delete")
    public void deleteBoard(@PathVariable("boardId") Long boardId) {
        boardService.delete(boardId);
    }

    @GetMapping("/{userToken}/my-liked-boards")
    public Page<BoardListWithCategoryResponseForm> getMyLikedBoards(@PathVariable("userToken") String userToken,
                                                                    @RequestParam(defaultValue = "0") int currentPage,
                                                                    @RequestParam(defaultValue = "8") int pageSize) {
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Long accountId = redisService.getValueByKey(userToken);
        return boardService.getMyLikedBoardList(accountId, pageable);
    }

    @GetMapping("/{boardId}/for-modify")
    public BoardReadForModifyResponseForm getBoardForModify(@PathVariable("boardId") Long boardId) {
        return boardService.readForModify(boardId);
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
