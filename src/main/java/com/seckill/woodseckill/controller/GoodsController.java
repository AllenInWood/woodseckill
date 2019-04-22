package com.seckill.woodseckill.controller;

import com.seckill.woodseckill.domain.SeckillUser;
import com.seckill.woodseckill.redis.GoodsKey;
import com.seckill.woodseckill.redis.RedisService;
import com.seckill.woodseckill.service.GoodsService;
import com.seckill.woodseckill.service.SeckillUserService;
import com.seckill.woodseckill.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.context.webflux.SpringWebFluxContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    /**
     * QPS : 1267
     * 5000 * 10
     */
    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String toList(HttpServletRequest request,
                         HttpServletResponse response,
                         Model model,
                         SeckillUser user) {
        model.addAttribute("user", user);
        // select goods list
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
//        return "goods_list";
        // page level cache
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        WebContext ctx = new WebContext(
                request, response, request.getServletContext(),
                request.getLocale(), model.asMap());
        // manually resolve
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsList, "", html);
        }
        return html;
    }

    @RequestMapping(value = "/to_detail/{goodsId}", produces = "text/html")
    @ResponseBody
    public String toDetail(HttpServletRequest request,
                           HttpServletResponse response,
                           Model model,
                           SeckillUser user,
                           @PathVariable("goodsId") long goodsId) {
        // snowflake
        model.addAttribute("user", user);
        // url level cache (same as page level cache)
        String html = redisService.get(GoodsKey.getGoodsDetail, "" + goodsId, String.class);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }

        // manually resolve
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

//        return "goods_detail";
        WebContext ctx = new WebContext(
                request, response, request.getServletContext(),
                request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", ctx);
        if (!StringUtils.isEmpty(html)) {
            redisService.set(GoodsKey.getGoodsDetail, "" + goodsId, html);
        }
        return html;
    }
}
