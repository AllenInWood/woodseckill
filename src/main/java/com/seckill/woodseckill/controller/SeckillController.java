package com.seckill.woodseckill.controller;

import com.seckill.woodseckill.domain.OrderInfo;
import com.seckill.woodseckill.domain.SeckillOrder;
import com.seckill.woodseckill.domain.SeckillUser;
import com.seckill.woodseckill.redis.RedisService;
import com.seckill.woodseckill.result.CodeMsg;
import com.seckill.woodseckill.result.Result;
import com.seckill.woodseckill.service.GoodsService;
import com.seckill.woodseckill.service.OrderService;
import com.seckill.woodseckill.service.SeckillService;
import com.seckill.woodseckill.service.SeckillUserService;
import com.seckill.woodseckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    SeckillUserService seckillUserService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;

    @RequestMapping(value = "/do_seckill", method = RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfo> seckill(Model model,
                            SeckillUser user,
                            @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        // if seckill stock > 0
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            return Result.error(CodeMsg.SECOND_KILL_OVER);
        }
        // if success kill exists (in case duplicate kill)
        SeckillOrder seckillOrder = orderService.getSeckillOrderByUserIdGoodsId(user.getId(), goodsId);
        if (seckillOrder != null) {
            return Result.error(CodeMsg.REPEAT_SECOND_KILL);
        }
        // can second kill: 1. minus stock 2. create order 3. write into seckill order
        OrderInfo orderInfo = seckillService.seckill(user, goods);
        return Result.success(orderInfo);
    }
}
