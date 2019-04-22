package com.seckill.woodseckill.controller;

import com.seckill.woodseckill.domain.SeckillUser;
import com.seckill.woodseckill.redis.RedisService;
import com.seckill.woodseckill.result.Result;
import com.seckill.woodseckill.service.GoodsService;
import com.seckill.woodseckill.service.SeckillUserService;
import com.seckill.woodseckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    SeckillUserService seckillUserService;

    @RequestMapping("/info")
    @ResponseBody
    public Result<SeckillUser> info(Model model,
                                    SeckillUser user) {
        return Result.success(user);
    }
}
