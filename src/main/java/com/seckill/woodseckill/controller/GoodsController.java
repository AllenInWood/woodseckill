package com.seckill.woodseckill.controller;

import com.seckill.woodseckill.domain.SeckillUser;
import com.seckill.woodseckill.redis.RedisService;
import com.seckill.woodseckill.service.GoodsService;
import com.seckill.woodseckill.service.SeckillUserService;
import com.seckill.woodseckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    SeckillUserService seckillUserService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    /**
     * QPS : 1267
     * 5000 * 10
     */
    @RequestMapping("/to_list")
    public String toList(Model model,
                         SeckillUser user) {
        model.addAttribute("user", user);
        // select goods list
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        return "goods_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String toDetail(Model model,
                           SeckillUser user,
                           @PathVariable("goodsId") long goodsId) {
        // snowflake
        model.addAttribute("user", user);
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        long startTime = goods.getStartDate().getTime();
        long endTime = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int seckillStatus = 0;
        int remainSecond = 0;
        if (now < startTime) { // time countdown
            seckillStatus = 0;
            remainSecond = (int) ((startTime - now) / 1000);
        } else if (now > endTime) { // second kill ends
            seckillStatus = 2;
            remainSecond = -1;
        } else { // ing
            seckillStatus = 1;
            remainSecond = 0;
        }
        model.addAttribute("seckillStatus", seckillStatus);
        model.addAttribute("remainSecond", remainSecond);

        return "goods_detail";
    }
}
