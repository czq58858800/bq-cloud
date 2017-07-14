package com.bast.web;

import com.bast.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * Created by chenzq on 2017/7/5 0005 18:30
 * Biao Qing Technology
 */
@RestController
@RequestMapping("/user")
public class TestController {

    @Autowired
    private RedisService redisService;

    @RequestMapping("/uid")
    String uid(HttpSession session) {
        UUID uid = (UUID) session.getAttribute("uid");
        if (uid == null) {
            uid = UUID.randomUUID();
        }
        session.setAttribute("uid", uid);
        return session.getId();
    }

    @RequestMapping("/redis")
    String reids(HttpSession session) {
        redisService.set("test","test");
        return "test";
    }

}
