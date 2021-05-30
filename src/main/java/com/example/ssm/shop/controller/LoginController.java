package com.example.ssm.shop.controller;

import com.example.ssm.shop.dto.CommonConstant;
import com.example.ssm.shop.dto.JsonResult;
import com.example.ssm.shop.entity.User;
import com.example.ssm.shop.enums.UserStatusEnum;
import com.example.ssm.shop.enums.UserTypeEnum;
import com.example.ssm.shop.exception.MyBusinessException;
import com.example.ssm.shop.util.PasswordUtil;
import com.example.ssm.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * 登录控制器
 *
 * @author admin
 */
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 登录页面
     *
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(HttpSession session) {
        User user = (User) session.getAttribute(CommonConstant.USER_SESSION_KEY);
        if (user != null) {
            return "redirect:/";
        }
        return "login";
    }


    /**
     * 用户注册页面
     *
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerPage(HttpSession session) {
        User user = (User) session.getAttribute(CommonConstant.USER_SESSION_KEY);
        if (user != null) {
            return "redirect:/";
        }
        return "register";
    }
    /**
     * 管理员注册页面
     *
     * @return
     */
    @RequestMapping(value = "/managerregister", method = RequestMethod.GET)
    public String managerregisterPage(HttpSession session) {
        User user = (User) session.getAttribute(CommonConstant.USER_SESSION_KEY);
        if (user != null) {
            return "managerregister";
        }
        return "redirect:/";
    }


    /**
     * 登录提交
     *
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult login(@RequestParam("username") String username,
                            @RequestParam("password") String password,
                            HttpSession session) {
        // 校验用户名是否存在
        User user = userService.findByUsernameOrEmail(username);
        if (user == null) {
            return JsonResult.error("用户名或密码错误");
        }

        // 校验密码
        boolean isMatch = PasswordUtil.match(password, user.getPassword());
        if (!isMatch) {
            return JsonResult.error("用户名或密码错误");
        }

        // 登录成功，添加Session
        session.setAttribute(CommonConstant.USER_SESSION_KEY, user);
        return JsonResult.success(user);
    }

    /**
     * 用户注册提交
     *
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult register(@RequestParam("username") String username,
                               @RequestParam("password") String password) {
        // 参数校验
        checkRegister(username, password);

        // 允许注册
        User user = new User();
        user.setUsername(username);
        user.setDisplayName(username);
        user.setPassword(PasswordUtil.encode(password));
        user.setStatus(UserStatusEnum.NORMAL.getValue());
        user.setCreateTime(new Date());
        user.setType(UserTypeEnum.USER.getValue());
        user.setBalance(BigDecimal.ZERO);
        userService.insertUser(user);
        return JsonResult.success();
    }
    /**
     * 管理员注册提交
     *
     * @return
     */
    @RequestMapping(value = "/managerregister", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult managerregister(@RequestParam("username") String username,
                               @RequestParam("password") String password) {
        // 参数校验
        checkRegister(username, password);

        // 允许注册
        User user = new User();
        user.setUsername(username);
        user.setDisplayName(username);
        user.setPassword(PasswordUtil.encode(password));
        user.setStatus(UserStatusEnum.NORMAL.getValue());
        user.setCreateTime(new Date());
        user.setType(UserTypeEnum.ADMIN.getValue());
        user.setBalance(BigDecimal.ZERO);
        userService.insertUser(user);
        return JsonResult.success();
    }


    /**
     * 退出
     *
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        session.removeAttribute(CommonConstant.USER_SESSION_KEY);
        return "redirect:/login";
    }

    /**
     * 用户注册验证
     *
     * @param username
     * @param password
     */
    private void checkRegister(String username, String password) {
        // 校验用户名长度（4-20为）
        if (username.trim().length() > 20 || username.trim().length() < 4) {
            throw new MyBusinessException("用户名长度为4-20位");
        }

        // 校验密码长度（6-20位）
        if (password.trim().length() > 20 || password.trim().length() < 6) {
            throw new MyBusinessException("密码长度为6-20位");
        }

        // 判断用户名是否已经注册
        User user = userService.findByUsername(username);
        if (user != null) {
            throw new MyBusinessException("用户名已存在");
        }
    }

}
