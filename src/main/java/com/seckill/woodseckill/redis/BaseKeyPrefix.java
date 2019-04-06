package com.seckill.woodseckill.redis;

public abstract class BaseKeyPrefix implements KeyPrefix {

    private int expireSecond;
    private String prefix;

    public BaseKeyPrefix(String prefix) {// 0 means never expire
        this(0, prefix);
    }

    public BaseKeyPrefix(int expireSecond, String prefix) {
        this.expireSecond = expireSecond;
        this.prefix = prefix;
    }

    @Override
    public int expireSecond() { // default 0 means never expire
        return expireSecond;
    }

    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className + ":" + prefix;
    }
}
