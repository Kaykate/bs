package com.example.ssm.shop.controller;

import com.example.ssm.shop.dto.CommonConstant;
import com.example.ssm.shop.dto.JsonResult;
import com.example.ssm.shop.entity.*;
import com.example.ssm.shop.enums.ProductStatusEnum;
import com.example.ssm.shop.enums.UserStatusEnum;
import com.example.ssm.shop.enums.UserTypeEnum;
import com.example.ssm.shop.exception.MyBusinessException;
import com.example.ssm.shop.service.*;
import com.example.ssm.shop.util.PasswordUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author admin
 */
@RequestMapping("/account")
@Controller
public class AccountController extends BaseController {

    @Autowired
    private BiddingService biddingService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    public static final Long TWO_DAY = 1000 * 3600 * 24 * 2L;

    /**
     * 账号中心
     *
     * @param model
     * @return
     */
    //指定请求方式GET
    @RequestMapping(method = RequestMethod.GET)
    public String account(Model model) {
        //BaseController里的获得登录用户
        User loginUser = getLoginUser();
        Long id = loginUser.getId();
        //判断是否是管理员 UserTypeEnum.ADMIN.getValue() = 1
        Boolean isAdmin = Objects.equals(loginUser.getType(), UserTypeEnum.ADMIN.getValue());

        // 竞拍记录列表
        List<Bidding> biddingList = isAdmin ? biddingService.findAll(null) : biddingService.findByUserId(id);
        model.addAttribute("biddingList", biddingList);

        // 商品列表
        List<Product> productList = isAdmin ? productService.findAll(null) : productService.findByUserId(id);
        model.addAttribute("productList", productList);

        // 订单列表
        List<Order> orderList = isAdmin ? orderService.findAll() : orderService.findByUserId(id);
        model.addAttribute("orderList", orderList);

        //传输账号基本信息
        model.addAttribute("user", loginUser);

        if ( Objects.equals(loginUser.getType(), UserTypeEnum.ADMIN.getValue())) {
            // 用户管理
            Map<String, Object> map = new HashMap<>();
            map.put("type", UserTypeEnum.USER.getValue());
            List<User> userList = userService.findAll(map);
            model.addAttribute("userList", userList);
            //List<User> managerList = userService.findByType....
            return "account-admin";
        } else {
            return "account-user";
        }
    }


    /**
     * 用户修改用户个人信息
     *
     * @return
     */
    //@ResponseBody的作用其实是将java对象转为json格式的数据。
    //@ResponseBody，返回结果不会被解析为跳转路径，直接返回json数据，防止springMvc解析为视图。
    //@RequestBody接收json字符串，并将其转换为Java对象
    //比如异步获取 json 数据，加上 @ResponseBody 后，会直接返回 json 数据。@RequestBody 将 HTTP 请求正文插入方法中，使用适合的 HttpMessageConverter 将请求体写入某个对象。
    @RequestMapping(value = "/details", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult saveAccount(@RequestBody User user,
                                  HttpSession session) {
        //获得登录用户

        User loginUser = getLoginUser();

        //把数据弄成JSON形式的包在user里？？？
        user.setId(loginUser.getId());

        // 判断用户名是否存在
        User checkUsername = userService.findByUsername(user.getUsername());
        if (checkUsername != null && !Objects.equals(checkUsername.getId(), loginUser.getId())) {
            return JsonResult.error("用户名已存在");
        }

        try {
            userService.updateUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("保存失败");
        }

        //重新查询用户信息
        User updateUser = userService.findById(user.getId());
        session.setAttribute(CommonConstant.USER_SESSION_KEY, updateUser);
        return JsonResult.success("保存成功");
        //JsonReslut中该函数 code = 0 msg = 操作成功
        // public static JsonResult success(Object data) {
        //        return new JsonResult(0, "操作成功", data);
        //    }
    }
    /**
     * 充值
     *
     * @return
     */
    @RequestMapping(value = "/priceupdate", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult addAccount(@RequestBody User user,
                                  HttpSession session) {

        //获得登录用户

        User loginUser = getLoginUser();

        //把数据弄成JSON形式的包在user里？？？
        user.setId(loginUser.getId());
        BigDecimal p = new BigDecimal("0");
        if (user.getBalance().compareTo(p)<1||user.getBalance() == null) {
            return JsonResult.error("请输入有效的金额");
        }
        BigDecimal price = loginUser.getBalance().add(user.getBalance());
        user.setBalance(price);

        try {
            userService.updateUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("保存失败");
        }

        //重新查询用户信息
        User updateUser = userService.findById(user.getId());
        session.setAttribute(CommonConstant.USER_SESSION_KEY, updateUser);
        return JsonResult.success("充值成功",user.getBalance());
    }
    /**
     * 修改密码
     *
     * @return
     */
    @RequestMapping(value = "/password", method = RequestMethod.POST)
    @ResponseBody
    //@RequestParam 用于controller层，是Spring的注解
    //解决前台参数名称与后台接收参数变量名称不一致的问题，等价于request.getParam
    //请求的参数区中name为oldPassword的参数的值传入；
    public JsonResult updatePassword(@RequestParam("oldPassword") String oldPassword,
                                     @RequestParam("newPassword") String newPassword,
                                     @RequestParam("confirmPassword") String confirmPassword) {
        User loginUser = getLoginUser();
        Long userId = loginUser.getId();

        User user = userService.findById(userId);
        Objects.equals(user,loginUser);
        //System.out.println(Objects.equals(user,loginUser));结果为true
        //此处loginuser=user
        if (!PasswordUtil.match(user.getPassword(), oldPassword)) {
            return JsonResult.error("旧密码错误");
        }

        if (!Objects.equals(newPassword, confirmPassword)) {
            return JsonResult.error("两次密码不一致");
        }

        user.setPassword(PasswordUtil.encode(newPassword));
        userService.updateUser(user);

        return JsonResult.success("保存成功");
    }

    /**
     * 商品发布
     *
     * @return
     */
    @RequestMapping(value = "/product/publish", method = RequestMethod.GET)
    public String productPublishPage(Model model) {
        // 获取分类列表
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList", categoryList);
        return "product-publish";
    }

    /**
     * 删除商品
     *
     * @return
     */
    @RequestMapping(value = "/product/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Transactional
    public JsonResult removeProduct(@PathVariable("id") Long id) {
        // 获得登录用户
        User user = getLoginUser();
        if (user == null) {
            return JsonResult.error("请先登录");
        }

        //若已有人出价
        Bidding bidding = biddingService.findBiddingOfMaxPriceByProductId(id);
        if (bidding != null) {
            return JsonResult.error("商品已有人出价，无法删除");
        }
        // 判断记录是否存在
        Product product = productService.findById(id);
        if (product == null) {
            return JsonResult.error("商品不存在");
        }

        // 判断该ID是否属于该用户
        if (!Objects.equals(product.getUserId(), user.getId()) && !Objects.equals(user.getType(), UserTypeEnum.ADMIN.getValue())) {
            return JsonResult.error("无权操作");
        }

        // 删除商品记录
        productService.deleteById(id);
        // 删除竞价
        biddingService.deleteByProductId(id);
        // 删除订单
        orderService.deleteByProductId(id);
        return JsonResult.success("删除成功", null);
    }

    /**
     * 上架商品
     *
     * @return
     */
    @RequestMapping(value = "/productGrounding/{id}", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public JsonResult productGrounding(@PathVariable("id") Long id) {
        // 获得登录用户
        User user = getLoginUser();
        if (user == null) {
            return JsonResult.error("请先登录");
        }

        // 判断记录是否存在
        Product product = productService.findById(id);
        if (product == null) {
            return JsonResult.error("商品不存在");
        }

        if (product.getEndTime().getTime() < System.currentTimeMillis()) {
            return JsonResult.error("商品竞拍时间已过");
        }
        // 判断该ID是否属于该用户
        if (!Objects.equals(product.getUserId(), user.getId()) && !Objects.equals(user.getType(), UserTypeEnum.ADMIN.getValue())) {
            return JsonResult.error("无权操作");
        }

        //上架
        productService.groundingById(id);
        return JsonResult.success("上架成功", null);
    }

    /**
     * 下架商品
     *
     * @return
     */
    @RequestMapping(value = "/productOff/{id}", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public JsonResult productOff(@PathVariable("id") Long id) {
        // 获得登录用户
        User user = getLoginUser();
        if (user == null) {
            return JsonResult.error("请先登录");
        }

        //若已有人出价
        Bidding bidding = biddingService.findBiddingOfMaxPriceByProductId(id);
        if (bidding != null) {
            return JsonResult.error("商品已有人出价，无法下架");
        }
        // 判断记录是否存在
        Product product = productService.findById(id);
        if (product == null) {
            return JsonResult.error("商品不存在");
        }

        // 判断该ID是否属于该用户
        if (!Objects.equals(product.getUserId(), user.getId()) && !Objects.equals(user.getType(), UserTypeEnum.ADMIN.getValue())) {
            return JsonResult.error("无权操作");
        }

        productService.offById(id);
        return JsonResult.success("下架成功", null);
    }


    /**
     * 商品修改1
     *
     * @return
     */
    @RequestMapping(value = "/product/edit/{id}", method = RequestMethod.GET)
    public String productEditPage(@PathVariable("id") Long id, Model model) {

        User user = getLoginUser();

        // 分类列表
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList", categoryList);

        Product product = productService.findById(id);
        if (product == null) {
            throw new MyBusinessException("商品不存在");
        }


        // 管理员和所属人可以删除
        if (!Objects.equals(product.getUserId(), user.getId()) && !user.getType().equals(UserTypeEnum.ADMIN.getValue())) {
            throw new MyBusinessException("无权操作");

        }

        model.addAttribute("product", product);
        return "product-edit";
    }




    /**
     * 商品发布/修改提交
     *
     * @return
     */
    @RequestMapping(value = "/product", method = RequestMethod.POST)
    @ResponseBody

    public JsonResult productPublishSubmit(
            //id存在就是修改，不存在就是发布新商品
            //required是false表示请求中可以没有名字为id的参数，如果没有默认为null
            //如果是true那必须有id这个值，没有就报错404
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam("name") String name,
            @RequestParam("summary") String summary,
            @RequestParam("imgUrl") String imgUrl,
            @RequestParam("cateId") Long cateId,
            @RequestParam("startPrice") BigDecimal startPrice,
            @RequestParam("endTime") String endTime) {

        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(summary) ||
                StringUtils.isEmpty(imgUrl) || StringUtils.isEmpty(endTime) ||
                startPrice == null) {
            return JsonResult.error("请填写完整信息");
        }


        if(startPrice.compareTo(new BigDecimal("0.00"))==0||startPrice.compareTo(new BigDecimal("0.00"))==-1 ) {

            return JsonResult.error("请填写大于0.00的金额");

        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        User user = getLoginUser();
        Date now = new Date();

        Product product = null;
        if (id != null) {
            product = productService.findById(id);
            if (product == null) {
                return JsonResult.error("商品不存在");
            }
            // 管理员和所属人可以删除
            if (!Objects.equals(product.getUserId(), user.getId()) && !user.getType().equals(UserTypeEnum.ADMIN.getValue())) {
                return JsonResult.error("无权操作");
            }
        } else {
            product = new Product();
        }
        product.setId(id);
        product.setName(name);
        product.setSummary(summary);
        product.setImgUrl(imgUrl);
        product.setStartPrice(startPrice);
        product.setCurrentPrice(startPrice);
        product.setStatus(ProductStatusEnum.NORMAL.getValue());
        product.setCateId(cateId);
        product.setUserId(user.getId());

        try {
            Date endTimeDate = sdf.parse(endTime);
            if (endTimeDate.before(now)) {
                return JsonResult.error("截止时间必须大于当前时间");
            }
            product.setEndTime(endTimeDate);

        } catch (ParseException e) {
            e.printStackTrace();
            product.setEndTime(new Date(System.currentTimeMillis() + TWO_DAY));
        }

        if (id == null) {
            product.setCreateTime(now);
            //创建新商品
            productService.insert(product);
        } else {
            //更新商品
            productService.update(product);
        }
        return JsonResult.success(product.getId());
    }


    /**
     * 移除竞价
     * 未使用
     * @return
     */
    @RequestMapping(value = "/bidding/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Transactional
    public JsonResult removeBidding(@PathVariable("id") Long id) {
        // 获得登录用户
        User user = getLoginUser();
        if (user == null) {
            return JsonResult.error("请先登录");
        }

        // 判断记录是否存在
        Bidding bidding = biddingService.findById(id);
        if (bidding == null) {
            return JsonResult.error("记录不存在");
        }

        // 判断该ID是否属于该用户
        if (!Objects.equals(bidding.getUserId(), user.getId()) && !Objects.equals(user.getType(), UserTypeEnum.ADMIN.getValue())) {
            return JsonResult.error("无权操作");
        }

        // 删除竞价记录
        biddingService.deleteById(id);
        return JsonResult.success("移除成功", null);
    }

    /**
     * 禁用用户
     *
     * @return
     */
    @RequestMapping(value = "/user/status/{id}", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult disableCart(@PathVariable("id") Long id) {
        // 获得登录用户
        User loginUser = getLoginUser();
        if (loginUser == null) {
            return JsonResult.error("请先登录");
        }

        // 判断记录是否存在
        User user = userService.findById(id);
        if (user == null) {
            return JsonResult.error("记录不存在");
        }

        // 判断该ID是否属于该用户
        if (!Objects.equals(loginUser.getType(), UserTypeEnum.ADMIN.getValue())) {
            return JsonResult.error("无权操作");
        }

        if (Objects.equals(user.getStatus(), UserStatusEnum.NORMAL.getValue())) {
            user.setStatus(UserStatusEnum.BAN.getValue());
            userService.updateUser(user);
            return JsonResult.success("禁用成功", null);

        } else {
            user.setStatus(UserStatusEnum.NORMAL.getValue());
            userService.updateUser(user);
            return JsonResult.success("启用成功", null);
        }
    }

    /**
     * 删除用户
     *
     * @return
     */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    @Transactional
    public JsonResult removeUser(@PathVariable("id") Long id) {
        // 获得登录用户
        User loginUser = getLoginUser();
        if (loginUser == null) {
            return JsonResult.error("请先登录");
        }


        // 判断是否为管理员
        if (!Objects.equals(loginUser.getType(), UserTypeEnum.ADMIN.getValue())) {
            return JsonResult.error("无权操作");
        }

        // 删除用户
        userService.deleteUser(id);
        // 删除订单
        orderService.deleteByUserId(id);
        // 删除竞价
        biddingService.deleteByUserId(id);
        return JsonResult.success("删除成功");
    }

    /**
     * 管理员编辑用户
     *
     * @return
     */
    @RequestMapping(value = "/user/edit/{id}", method = RequestMethod.GET)
    public String editUser(@PathVariable("id") Long id, Model model) {
        // 获得登录用户
        User loginUser = getLoginUser();
        if (loginUser == null) {
            throw new MyBusinessException("请先登录");
        }

        // 判断是否为管理员
        if (!Objects.equals(loginUser.getType(), UserTypeEnum.ADMIN.getValue())) {
            throw new MyBusinessException("无权操作");
        }
        User user = userService.findById(id);
        if (user == null) {
            throw new MyBusinessException("用户不存在");
        }
        model.addAttribute("user", user);
        return "user-edit";
    }

    /**
     * 管理员修改用户信息
     *
     * @return
     */
    @RequestMapping(value = "/user/adminUpdateUser", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult adminUpdateUser(@RequestBody User user) {
        User loginUser = getLoginUser();
        if (loginUser == null) {
            return JsonResult.error("请先登录");
        }
        if (!Objects.equals(loginUser.getType(), UserTypeEnum.ADMIN.getValue())) {
            return JsonResult.error("无权限操作");
        }
        // 判断用户名是否存在
        User checkUsername = userService.findByUsername(user.getUsername());
        if (checkUsername != null && !Objects.equals(checkUsername.getId(), user.getId())) {
            return JsonResult.error("用户名已存在");
        }
        //密码
        User byIdUser = userService.findById(user.getId());
        if (PasswordUtil.match(byIdUser.getPassword(), user.getPassword()) || PasswordUtil.match(byIdUser.getPassword(), PasswordUtil.encode(user.getPassword()))) {
            user.setPassword(byIdUser.getPassword());
        } else {
            user.setPassword(PasswordUtil.encode(user.getPassword()));
        }
        try {
            userService.updateUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("修改失败");
        }
        return JsonResult.success("修改成功");
    }

    /**
     * 订单地址修改页面
     *
     * @return
     */
    @RequestMapping(value = "/order/edit/{id}", method = RequestMethod.GET)
    public String orderEditPage(@PathVariable("id") Long id, Model model) {

        User user = getLoginUser();

        // 分类列表
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList", categoryList);

        Order order = orderService.findById(id);
        if (order == null) {
            throw new MyBusinessException("订单不存在");
        }
        // 管理员可以修改
        if (!user.getType().equals(UserTypeEnum.ADMIN.getValue())) {
            throw new MyBusinessException("无权操作");

        }
        User orderUser = userService.findById(order.getId());
        order.setUser(orderUser);

        Product product = productService.findById(order.getProductId());
        order.setProduct(product);
        model.addAttribute("order", order);
        return "order-edit";
    }

    /**
     * 订单地址修改
     *
     * @return
     */
    @RequestMapping(value = "/order/orderAddressUpdate", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult orderAddressUpdate(@RequestBody Order order) {

        User user = getLoginUser();
        // 管理员可以修改
        if (!user.getType().equals(UserTypeEnum.ADMIN.getValue())) {
            return JsonResult.error("无权操作");

        }
        orderService.update(order);
        return JsonResult.success("修改成功");
    }

}
