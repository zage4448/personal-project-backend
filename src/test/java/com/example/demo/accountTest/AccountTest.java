package com.example.demo.accountTest;

import com.example.demo.account.controller.form.AccountLoginRequestForm;
import com.example.demo.account.controller.form.AccountRegisterForm;
import com.example.demo.account.entity.Account;
import com.example.demo.account.repository.AccountRepository;
import com.example.demo.account.service.AccountService;
import com.example.demo.account.service.AccountServiceImpl;
import com.example.demo.redis.service.RedisService;
import com.example.demo.redis.service.RedisServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;


@SpringBootTest
public class AccountTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations valueOperations;

    @InjectMocks
    private AccountServiceImpl accountService;

    @InjectMocks
    private RedisServiceImpl redisService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        accountService = new AccountServiceImpl(accountRepository);
        redisService = new RedisServiceImpl(redisTemplate);
    }

    @Test
    @DisplayName("사용자 회원가입 테스트")
    void registerAccountTest () {
        final String email = "test@test.com";
        final String password = "test";
        final String nickname = "test";

        AccountRegisterForm registerForm = new AccountRegisterForm(email, password, nickname);

        Mockito.when(accountRepository.save(any(Account.class))).thenReturn(new Account(email, password, nickname));

        Boolean isRegistered = accountService.register(registerForm.toAccountRegisterRequest());
        assertTrue(isRegistered);
    }

    @Test
    @DisplayName("이메일 중복확인 테스트")
    void checkDuplicateTest () {
        final String email = "test@test.com";

        Boolean isDuplicate = accountService.checkDuplicateEmail(email);

        assertTrue(isDuplicate);
    }

    @Test
    @DisplayName("로그인 테스트")
    void loginTest () throws NoSuchFieldException, IllegalAccessException {
        final Long id = 1L;
        final String email = "test@test.com";
        final String password = "test";

        Account account = createAccountWithId(email, password, null, id);
        AccountLoginRequestForm loginForm = new AccountLoginRequestForm(email, password);

        Mockito.when(accountRepository.findByEmail(email)).thenReturn(Optional.of(account));
        Long accountId = accountService.login(loginForm);

        if (accountId != null) {
            UUID userToken = UUID.randomUUID();

            given(redisTemplate.opsForValue()).willReturn(valueOperations);
            doNothing().when(valueOperations).set(anyString(), any());

            redisService.setKeyAndValue(userToken.toString(), accountId);

            Long actualAccountId = redisService.getValueByKey(userToken.toString());

            Mockito.when(accountRepository.findById(actualAccountId)).thenReturn(Optional.of(account));
            Account actualAccount = accountService.findAccountById(actualAccountId);
            assertEquals(email, actualAccount.getEmail());
        }
    }

    @Test
    @DisplayName("로그아웃 테스트")
    void LogoutTest () throws NoSuchFieldException, IllegalAccessException {
        final Long id = 1L;
        final String email = "test@test.com";
        final String password = "test";
        UUID userToken = UUID.randomUUID();

        Account account = createAccountWithId(email, password, null, id);
        AccountLoginRequestForm loginForm = new AccountLoginRequestForm(email, password);

        Mockito.when(accountRepository.findByEmail(email)).thenReturn(Optional.of(account));
        Long accountId = accountService.login(loginForm);

        if (accountId != null) {
            given(redisTemplate.opsForValue()).willReturn(valueOperations);
            doNothing().when(valueOperations).set(anyString(), any());
            redisService.setKeyAndValue(userToken.toString(), accountId);
        }

        redisService.deleteByKey(userToken.toString());
        Long testAccountId = redisService.getValueByKey(userToken.toString());

        assertEquals(null, testAccountId);
    }

    @Test
    @DisplayName("닉네임 중복확인 테스트")
    void checkDuplicateNicknameTest () {
        final String nickname = "test";

        Mockito.when(accountRepository.save(any(Account.class))).thenReturn(new Account(null, null, nickname));
        Boolean isDuplicate = accountService.checkDuplicateNickname(nickname);

        assertTrue(isDuplicate);
    }

    private Account createAccountWithId(String email, String password, String nickname, Long accountId) throws NoSuchFieldException, IllegalAccessException {
        Account account = new Account(email, password, nickname);
        Field idField = Account.class.getDeclaredField("accountId");
        idField.setAccessible(true);
        idField.set(account, accountId);
        return account;
    }
}


