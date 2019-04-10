package com.seckill.woodseckill.redis;

public class SeckillUserKey extends BaseKeyPrefix {
    public SeckillUserKey(int expireSecond, String prefix) {
        super(expireSecond, prefix);
    }

    public static final int TOKEN_EXPIRE = 3600 * 24 * 2;

    public static SeckillUserKey token = new SeckillUserKey(TOKEN_EXPIRE, "tk");
}
