package com.example.demo.board.entity;

import com.example.demo.account.entity.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account writer;

    private String content;

    @Enumerated(EnumType.STRING)
    private BoardCategory boardCategory;

    public Board(String title, Account writer, String content, BoardCategory boardCategory) {
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.boardCategory = boardCategory;
    }

    @Setter
    @Column(columnDefinition = "integer default 0", nullable = false)
    private int views;

    @CreationTimestamp
    private LocalDateTime createDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;
}
