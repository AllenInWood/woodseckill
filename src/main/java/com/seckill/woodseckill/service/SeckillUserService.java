package com.seckill.woodseckill.service;

import com.seckill.woodseckill.dao.SeckillUserDao;
import com.seckill.woodseckill.domain.SeckillUser;
import com.seckill.woodseckill.exception.GlobalException;
import com.seckill.woodseckill.result.CodeMsg;
import com.seckill.woodseckill.util.MD5Util;
import com.seckill.woodseckill.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeckillUserService {

    @Autowired
    private SeckillUserDao seckillUserDao;

    public SeckillUser getById(long id) {
        return seckillUserDao.getById(id);
    }

    public boolean login(LoginVo loginVo) {
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
        return true;
    }
}
