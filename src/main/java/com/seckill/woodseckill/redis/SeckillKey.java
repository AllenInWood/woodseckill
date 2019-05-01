package com.seckill.woodseckill.redis;

public class SeckillKey extends BaseKeyPrefix {
    private SeckillKey(String prefix) {
        super(prefix);
    }

    public static SeckillKey isGoodsOver = new SeckillKey("go");
}
