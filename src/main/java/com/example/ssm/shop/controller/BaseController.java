package com.example.ssm.shop.controller;

import com.example.ssm.shop.dto.CommonConstant;
import com.example.ssm.shop.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

/**
 * @author admin
 */
@Controller
public class BaseController {

    @Autowired
    private HttpServletRequest request;


    /**
     * 获得登录用户
     * @return
     */
    public User getLoginUser() {
        //获得Session对象中含有的关键字是key的对象"user"
        User user = (User) request.getSession().getAttribute(CommonConstant.USER_SESSION_KEY);
        return user;
    }
}
