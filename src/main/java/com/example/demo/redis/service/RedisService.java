package com.example.demo.redis.service;

import java.util.UUID;

public interface RedisService {
    void setKeyAndValue(String token, Long accountId);

    Long getValueByKey(String userToken);

    void deleteByKey(String userToken);
}
