package com.example.demo.board.entity;

import com.example.demo.account.entity.Account;
import com.example.demo.comment.entity.Comment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Board {

    @Id
    @Column(name = "boardId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Setter
    private String title;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account writer;

    @Setter
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

    @ManyToMany
    @JoinTable(name = "board_likes",
            joinColumns = @JoinColumn(name = "board_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id"))
    private Set<Account> likes = new HashSet<>();


    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    public void addLike(Account account) {
        likes.add(account);
        account.getLikedBoards().add(this);
    }

    public void removeLike(Account account) {
        likes.remove(account);
        account.getLikedBoards().remove(this);
    }

    @CreationTimestamp
    private LocalDateTime createDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;
}
