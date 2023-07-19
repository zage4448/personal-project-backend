package com.example.demo.account.service;

import com.example.demo.account.controller.form.AccountInfoResponseForm;
import com.example.demo.account.controller.form.AccountLoginRequestForm;
import com.example.demo.account.controller.form.AccountRegisterForm;
import com.example.demo.account.controller.form.ChangePasswordRequestForm;
import com.example.demo.account.entity.Account;
import com.example.demo.account.repository.AccountRepository;
import com.example.demo.account.service.request.AccountRegisterRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{

    final private AccountRepository accountRepository;
    @Override
    public Boolean register(AccountRegisterRequest registerRequest) {
        accountRepository.save(registerRequest.toAccount());
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
            log.info("존재하지 않는 패스워드 입니다.");
            return null;
        }

        Account account = maybeAccount.get();

        if(account.getPassword().equals(loginForm.getPassword())){

            return account;
        }

        log.info("존재하지 않는 아이디입니다.");
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

        if (account.getPassword().equals(password)) {
            return true;
        }
        return false;
    }

    @Override
    public void changeNickname(Long accountId, String newNickname) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
        account.setNickname(newNickname);
        accountRepository.save(account);
    }

    @Override
    public Boolean changePassword(Long accountId, ChangePasswordRequestForm requestForm) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (account.getPassword().equals(requestForm.getPassword())) {
            account.setPassword(requestForm.getNewPassword());
            accountRepository.save(account);
            return true;
        }
        return false;
    }

    @Override
    public void deleteAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        accountRepository.delete(account);
    }
}
