package com.seckill.woodseckill.redis;

public class OrderKey extends BaseKeyPrefix {
    public OrderKey(int expireSecond, String prefix) {
        super(expireSecond, prefix);
    }
}
