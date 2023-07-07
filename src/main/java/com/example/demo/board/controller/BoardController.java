package com.example.demo.board.controller;

import com.example.demo.board.controller.form.CategoryListForm;
import com.example.demo.board.entity.BoardCategory;
import com.example.demo.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    final private BoardService boardService;

    @GetMapping("/category-list")
    public List<CategoryListForm> getCategoryList() {
        return boardService.getCategoryList();
    }
}
