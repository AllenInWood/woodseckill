package com.seckill.woodseckill.controller;

import com.seckill.woodseckill.domain.SeckillUser;
import com.seckill.woodseckill.redis.RedisService;
import com.seckill.woodseckill.service.SeckillUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    SeckillUserService seckillUserService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/to_list")
    public String toList(Model model,
                         SeckillUser user) {
        model.addAttribute("user", user);
        return "goods_list";
    }
}
