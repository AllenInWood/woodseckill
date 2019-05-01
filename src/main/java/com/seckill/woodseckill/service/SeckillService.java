package com.seckill.woodseckill.service;

import com.seckill.woodseckill.dao.GoodsDao;
import com.seckill.woodseckill.domain.Goods;
import com.seckill.woodseckill.domain.OrderInfo;
import com.seckill.woodseckill.domain.SeckillOrder;
import com.seckill.woodseckill.domain.SeckillUser;
import com.seckill.woodseckill.redis.RedisService;
import com.seckill.woodseckill.redis.SeckillKey;
import com.seckill.woodseckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeckillService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisService redisService;

    @Transactional
    public OrderInfo seckill(SeckillUser user, GoodsVo goods) {
        // 1. minus stock 2. create order 3. write into seckill order
        boolean success = goodsService.reduceStock(goods);
        if (success) {
            // order_info, seckill_order
            return orderService.createOrder(user, goods);
        } else {
            setGoodsOver(goods.getId());
            return null;
        }
    }

    public long getSeckillResult(Long userId, long goodsId) {
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(userId, goodsId);
        if (order != null) {// second kill successfully
            return order.getOrderId();
        } else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(SeckillKey.isGoodsOver, "" + goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(SeckillKey.isGoodsOver, "" + goodsId);
    }
}
