package com.seckill.woodseckill.redis;

public interface KeyPrefix {
    int expireSecond();
    String getPrefix();
}
