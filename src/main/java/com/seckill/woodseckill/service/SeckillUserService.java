package com.seckill.woodseckill.service;

import com.seckill.woodseckill.dao.SeckillUserDao;
import com.seckill.woodseckill.domain.SeckillUser;
import com.seckill.woodseckill.exception.GlobalException;
import com.seckill.woodseckill.redis.RedisService;
import com.seckill.woodseckill.redis.SeckillUserKey;
import com.seckill.woodseckill.result.CodeMsg;
import com.seckill.woodseckill.util.MD5Util;
import com.seckill.woodseckill.util.UUIDUtil;
import com.seckill.woodseckill.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class SeckillUserService {

    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    private SeckillUserDao seckillUserDao;

    @Autowired
    private RedisService redisService;

    public SeckillUser getById(long id) {
        return seckillUserDao.getById(id);
    }

    public SeckillUser getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        SeckillUser user = redisService.get(SeckillUserKey.token, token, SeckillUser.class);
        addCookie(response, user);
        return user;
    }

    public boolean login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();

        // check if mobile number exists
        SeckillUser user = getById(Long.parseLong(mobile));
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        // validate password
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
        if (!calcPass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }

        // login success, generate cookie
        addCookie(response, user);
        return true;
    }

    private void addCookie(HttpServletResponse response, SeckillUser user) {
        String token = UUIDUtil.uuid();
        redisService.set(SeckillUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(SeckillUserKey.token.expireSecond());
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
