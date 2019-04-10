package com.seckill.woodseckill.controller;

import com.seckill.woodseckill.domain.SeckillUser;
import com.seckill.woodseckill.redis.RedisService;
import com.seckill.woodseckill.redis.SeckillUserKey;
import com.seckill.woodseckill.service.SeckillUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    SeckillUserService seckillUserService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/to_list")
    public String toList(Model model,
                         @CookieValue(value = SeckillUserService.COOKIE_NAME_TOKEN,
                                 required = false) String cookieToken,
                         @RequestParam(value = SeckillUserService.COOKIE_NAME_TOKEN,
                                 required = false) String paramToken) {
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return "login";
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        SeckillUser user = seckillUserService.getByToken(token);
        model.addAttribute("user", user);
        return "goods_list";
    }
}
