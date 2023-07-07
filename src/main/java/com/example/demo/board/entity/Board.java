package com.example.demo.board.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Board {

    @Id
    @Column(name = "boardId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    private String title;
    private String writer;
    private String content;

    @Enumerated(EnumType.STRING)
    private BoardCategory boardCategory;

    public Board(String title, String writer, String content, BoardCategory boardCategory) {
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.boardCategory = boardCategory;
    }

    @CreationTimestamp
    private LocalDateTime createDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;
}
