package com.example.demo.account.entity;

import com.example.demo.board.entity.Board;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    private String email;
    private String password;
    private String nickname;

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    @ManyToMany(mappedBy = "likes", fetch = FetchType.EAGER)
    private Set<Board> likedBoards = new HashSet<>();

    public Account(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
}
