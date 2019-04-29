package com.seckill.woodseckill.controller;

import com.seckill.woodseckill.domain.User;
import com.seckill.woodseckill.rabbitmq.MQSender;
import com.seckill.woodseckill.redis.RedisService;
import com.seckill.woodseckill.redis.UserKey;
import com.seckill.woodseckill.result.CodeMsg;
import com.seckill.woodseckill.result.Result;
import com.seckill.woodseckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    MQSender sender;

    //swagger
    @RequestMapping("/mq/header")
    @ResponseBody
    public Result<String> header() {
        sender.sendHeader("Hello, RabbitMQ");
        return Result.success("hello, result");
    }

    @RequestMapping("/mq/fanout")
    @ResponseBody
    public Result<String> fanout() {
        sender.sendFanout("Hello, RabbitMQ");
        return Result.success("hello, result");
    }

    @RequestMapping("/mq/topic")
    @ResponseBody
    public Result<String> topic() {
        sender.sendTopic("Hello, RabbitMQ");
        return Result.success("hello, result");
    }

    @RequestMapping("/mq")
    @ResponseBody
    public Result<String> mq() {
        sender.send("Hello, RabbitMQ");
        return Result.success("hello, result");
    }

    @RequestMapping("/")
    @ResponseBody
    public String home() {
        return "hello world!";
    }

    @RequestMapping("/hello")
    @ResponseBody
    public Result<String> hello() {
        return Result.success("hello, result");
//        return new Result(0, "success", "hello");
    }

    @RequestMapping("/helloError")
    @ResponseBody
    public Result<String> helloError() {
        return Result.error(CodeMsg.SERVER_ERROR);
//        return new Result(500100, "XXX");
    }

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model) {
        model.addAttribute("name", "Allen");
        return "hello";
    }

    @RequestMapping("/db/get")
    @ResponseBody
    public Result<User> dbGet() {
        User user = userService.getById(1);
        return Result.success(user);
    }

    @RequestMapping("/db/tx")
    @ResponseBody
    public Result<Boolean> dbTx() {
        userService.tx();
        return Result.success(true);
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet() {
        User user = redisService.get(UserKey.getById, "" + 1, User.class);
        return Result.success(user);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet() {
        User user = new User();
        user.setId(1);
        user.setName("11111");
        redisService.set(UserKey.getById, "" + 1, user);
        return Result.success(true);
    }
}
