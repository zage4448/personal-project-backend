package com.example.demo.redis.service;

public interface RedisService {
    void setKeyAndValue(String token, Long accountId);

    Long getValueByKey(String userToken);
}
