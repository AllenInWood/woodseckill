package com.seckill.woodseckill.controller;

import com.seckill.woodseckill.redis.RedisService;
import com.seckill.woodseckill.result.CodeMsg;
import com.seckill.woodseckill.result.Result;
import com.seckill.woodseckill.service.SeckillUserService;
import com.seckill.woodseckill.util.ValidatorUtil;
import com.seckill.woodseckill.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    SeckillUserService seckillUserService;

    @Autowired
    RedisService redisService;

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(@Valid LoginVo loginVo) {
        log.info(loginVo.toString());
        // attrs validation
//        String passInput = loginVo.getPassword();
//        String mobileInput = loginVo.getMobile();
//        if (StringUtils.isEmpty(passInput)) {
//            return Result.error(CodeMsg.PASSWORD_EMPTY);
//        }
//        if (StringUtils.isEmpty(mobileInput)) {
//            return Result.error(CodeMsg.MOBILE_EMPTY);
//        }
//        if (!ValidatorUtil.isMobile(mobileInput)) {
//            return Result.error(CodeMsg.MOBILE_ERROR);
//        }
        // login
        CodeMsg cm = seckillUserService.login(loginVo);
        if (cm.getCode() == 0) {
            return Result.success(true);
        } else {
            return Result.error(cm);
        }
    }
}
