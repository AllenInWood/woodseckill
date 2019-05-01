package com.seckill.woodseckill.controller;

import com.seckill.woodseckill.domain.OrderInfo;
import com.seckill.woodseckill.domain.SeckillOrder;
import com.seckill.woodseckill.domain.SeckillUser;
import com.seckill.woodseckill.rabbitmq.MQSender;
import com.seckill.woodseckill.rabbitmq.SeckillMessage;
import com.seckill.woodseckill.redis.GoodsKey;
import com.seckill.woodseckill.redis.RedisService;
import com.seckill.woodseckill.result.CodeMsg;
import com.seckill.woodseckill.result.Result;
import com.seckill.woodseckill.service.GoodsService;
import com.seckill.woodseckill.service.OrderService;
import com.seckill.woodseckill.service.SeckillService;
import com.seckill.woodseckill.service.SeckillUserService;
import com.seckill.woodseckill.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {

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

    @Autowired
    MQSender mqSender;

    /**
     * Framework will call this method when system is initializing
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if (goodsList == null) {
            return;
        }
        goodsList.forEach(goodsVo -> redisService.set(
                GoodsKey.getSeckillGoodsStock, "" + goodsVo.getId(), goodsVo.getStockCount()));
    }

    @RequestMapping(value = "/do_seckill", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> seckill(Model model,
                            SeckillUser user,
                            @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        // optimize seckill process with help of rabbitMQ
        // pre minus stock
        long stock = redisService.decr(GoodsKey.getSeckillGoodsStock, "" + goodsId);
        if (stock < 0) {
            return Result.error(CodeMsg.SECOND_KILL_OVER);
        }
        // if success kill exists (in case duplicate kill)
        SeckillOrder seckillOrder = orderService.getSeckillOrderByUserIdGoodsId(user.getId(), goodsId);
        if (seckillOrder != null) {
            return Result.error(CodeMsg.REPEAT_SECOND_KILL);
        }
        // push into queue
        SeckillMessage message = new SeckillMessage();
        message.setGoodsId(goodsId);
        message.setSeckillUser(user);
        mqSender.sendSeckillMessage(message);
        return Result.success(0);// wait in queue
        /*
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
        */
    }

    /**
     *
     * @param model
     * @param user
     * @param goodsId
     * @return
     * orderId: success
     * -1: second kill fails
     * 0: wait in queue
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> seckillResult(Model model,
                                   SeckillUser user,
                                   @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result = seckillService.getSeckillResult(user.getId(), goodsId);
        return Result.success(result);
    }
}
