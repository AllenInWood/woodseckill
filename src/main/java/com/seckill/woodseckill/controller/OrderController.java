package com.seckill.woodseckill.controller;

import com.seckill.woodseckill.domain.OrderInfo;
import com.seckill.woodseckill.domain.SeckillUser;
import com.seckill.woodseckill.result.CodeMsg;
import com.seckill.woodseckill.result.Result;
import com.seckill.woodseckill.service.GoodsService;
import com.seckill.woodseckill.service.OrderService;
import com.seckill.woodseckill.service.SeckillUserService;
import com.seckill.woodseckill.vo.GoodsVo;
import com.seckill.woodseckill.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    SeckillUserService seckillUserService;

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(Model model,
                                      SeckillUser user,
                                      @RequestParam("orderId") long orderId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        OrderInfo order = orderService.getOrderById(orderId);
        if (order == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        long goodsId = order.getGoodsId();
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        OrderDetailVo detailVo = new OrderDetailVo();
        detailVo.setOrder(order);
        detailVo.setGoods(goodsVo);
        return Result.success(detailVo);
    }
}
