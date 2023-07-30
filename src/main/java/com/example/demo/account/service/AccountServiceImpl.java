package com.example.demo.account.service;

import com.example.demo.account.controller.form.AccountInfoResponseForm;
import com.example.demo.account.controller.form.AccountLoginRequestForm;
import com.example.demo.account.controller.form.AccountRegisterForm;
import com.example.demo.account.controller.form.ChangePasswordRequestForm;
import com.example.demo.account.entity.Account;
import com.example.demo.account.repository.AccountRepository;
import com.example.demo.account.service.request.AccountRegisterRequest;
import com.example.demo.board.entity.Board;
import com.example.demo.board.repository.BoardRepository;
import com.example.demo.comment.entity.Comment;
import com.example.demo.comment.repository.CommentRepository;
import com.example.demo.config.SecurityConfig;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{

    final private AccountRepository accountRepository;
    final private BCryptPasswordEncoder passwordEncoder;
    final private CommentRepository commentRepository;
    final private BoardRepository boardRepository;

    @Override
    public Boolean register(AccountRegisterRequest registerRequest) {
        accountRepository.save(new Account(
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword()),
                registerRequest.getNickname()));

        return true;
    }

    @Override
    public String findAccountNicknameByEmail(String email) {
        if (email == null) {
            return null;
        }

        final Optional<Account> maybeAccount = accountRepository.findByEmail(email);

        if (maybeAccount.isEmpty()) {
            return null;
        }

        return maybeAccount.get().getNickname();
    }

    @Override
    public Boolean checkDuplicateEmail(String email) {
        final Optional<Account> maybeAccount = accountRepository.findByEmail(email);
        if (maybeAccount.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public Account login(AccountLoginRequestForm loginForm) {
        Optional<Account> maybeAccount = accountRepository.findByEmail(loginForm.getEmail());

        if(maybeAccount.isEmpty()){
            log.info("존재하지 않는 이메일 입니다.");
            return null;
        }

        Account account = maybeAccount.get();

        if(passwordEncoder.matches(loginForm.getPassword(), account.getPassword())){

            return account;
        }

        log.info("존재하지 않는 계정입니다.");
        return null;
    }

    @Override
    public Account findAccountById(Long accountId) {
        Optional<Account> maybeAccount = accountRepository.findById(accountId);

        return maybeAccount.get();
    }

    @Override
    public Boolean checkDuplicateNickname(String nickname) {
        final Optional<Account> maybeAccount = accountRepository.findByNickname(nickname);
        if (maybeAccount.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public AccountInfoResponseForm getAccountInfo(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        AccountInfoResponseForm responseForm = new AccountInfoResponseForm(
                account.getEmail(), account.getNickname(), account.getBoards().size()
        );
        return responseForm;
    }

    @Override
    public Boolean checkPassword(Long accountId, String password) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (passwordEncoder.matches(password, account.getPassword())) {
            return true;
        }
        return false;
    }

    @Override
    public String changeNickname(Long accountId, String newNickname) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        account.setNickname(newNickname);
        accountRepository.save(account);
        return account.getNickname();
    }

    @Override
    public Boolean changePassword(Long accountId, ChangePasswordRequestForm requestForm) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (passwordEncoder.matches(requestForm.getPassword(), account.getPassword())) {
            String newPassword = passwordEncoder.encode(requestForm.getNewPassword());
            account.setPassword(newPassword);
            accountRepository.save(account);
            return true;
        }
        return false;
    }

    @Override
    public void deleteAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        for (Board board : account.getLikedBoards()) {
            board.removeLike(account);
        }

        List<Comment> commentList = commentRepository.findAllByAccount(account);
        log.info("list: " + commentList);
        for (Comment comment : commentList) {
            Long boardId = comment.getBoard().getBoardId();
            Board board = boardRepository.findById(boardId)
                    .orElseThrow(() -> new IllegalArgumentException("Account not found"));

            board.getComments().remove(comment);
            commentRepository.delete(comment);
        }

        accountRepository.delete(account);
    }
}
