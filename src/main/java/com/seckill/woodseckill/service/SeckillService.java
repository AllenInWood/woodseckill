package com.seckill.woodseckill.service;

import com.seckill.woodseckill.dao.GoodsDao;
import com.seckill.woodseckill.domain.Goods;
import com.seckill.woodseckill.domain.OrderInfo;
import com.seckill.woodseckill.domain.SeckillUser;
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

    @Transactional
    public OrderInfo seckill(SeckillUser user, GoodsVo goods) {
        // 1. minus stock 2. create order 3. write into seckill order
        goodsService.reduceStock(goods);
        // order_info, seckill_order
        return orderService.createOrder(user, goods);
    }
}
