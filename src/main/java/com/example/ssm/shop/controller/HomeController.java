package com.example.ssm.shop.controller;

import com.example.ssm.shop.dto.JsonResult;
import com.example.ssm.shop.entity.Bidding;
import com.example.ssm.shop.entity.Category;
import com.example.ssm.shop.entity.Product;
import com.example.ssm.shop.entity.User;
import com.example.ssm.shop.enums.BiddingStatusEnum;
import com.example.ssm.shop.enums.ProductStatusEnum;
import com.example.ssm.shop.enums.UserStatusEnum;
import com.example.ssm.shop.enums.UserTypeEnum;
import com.example.ssm.shop.service.BiddingService;
import com.example.ssm.shop.service.CategoryService;
import com.example.ssm.shop.service.ProductService;
import com.example.ssm.shop.service.UserService;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author admin
 */
@Controller
public class HomeController extends BaseController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BiddingService biddingService;

    @Autowired
    private UserService userService;

    /**
     * 首页, 商品列表,搜索栏
     *
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    //required表示是否为必需，defaultValue表示默认值
    public String index(@RequestParam(required = false, defaultValue = "1") Integer pageIndex,
                        @RequestParam(required = false, defaultValue = "12") Integer pageSize,
                        @RequestParam(required = false, defaultValue = "") String keywords,
                        @RequestParam(required = false) Long cateId,
                        Model model) {
        // 商品列表
        //创建了一个HashMap
        Map<String, Object> criteria = new HashMap<>();
        //使用put方法保存数据
        criteria.put("keywords", keywords);
        criteria.put("cateId", cateId);
        //正在拍卖
        criteria.put("status", ProductStatusEnum.NORMAL.getValue());
        PageInfo<Product> productPageInfo = productService.findAll(pageIndex, pageSize, criteria);
        model.addAttribute("pageInfo", productPageInfo);
        // 分类
        if (cateId != null) {
            Category category = categoryService.findById(cateId);
            if (category != null) {
                model.addAttribute("category", category);
            }
        }
        return "index";
    }

    /**
     * 竞拍
     *
     * @param productId
     * @param price
     * @return
     */
    @RequestMapping(value = "/bidding", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public JsonResult addBidding(@RequestParam("productId") Long productId,
                                 @RequestParam("price") BigDecimal price) {

        // 获得登录用户
        User user = getLoginUser();
        if (user == null) {
            return JsonResult.error("请先登录");
        }


        if (Objects.equals(user.getType(), UserTypeEnum.ADMIN.getValue())) {
            return JsonResult.error("管理员不能竞价");
        }

        // 判断账号是否禁用
        if (Objects.equals(user.getStatus(), UserStatusEnum.BAN.getValue())) {
            return JsonResult.error("账号被封禁");
        }

        // 判断商品是否存在
        Product product = productService.findById(productId);
        if (product == null) {
            return JsonResult.error("商品不存在");
        }

        if (Objects.equals(product.getUserId(), user.getId())) {
            return JsonResult.error("不能竞拍自己发布的商品");
        }

        if (StringUtils.isBlank(user.getAddress())) {
            return JsonResult.error("请先维护个人地址");
        }

        // 判断商品是否已经到了截止时间
        if (!Objects.equals(product.getStatus(), ProductStatusEnum.NORMAL.getValue()) || product.getEndTime().getTime() <= new Date().getTime()) {
            product.setStatus(ProductStatusEnum.FAIL.getValue());
            productService.update(product);
            return JsonResult.error("商品拍卖已经结束");
        }

        // 判断价格是否大于当前最大价格
        if (price.compareTo(product.getCurrentPrice()) < 1) {
            return JsonResult.error("价格必须大于当前竞价");
        }

        Map<String, Object> criteria = new HashMap<>();
        criteria.put("userId", user.getId());
        //获得当前登录人的竞价记录
        List<Bidding> biddingList = biddingService.findAll(criteria);
        //当前登录人的竞价（已经用去竞拍的钱，相当于预付）
        BigDecimal usedBalance = price;
        //遍历当前登录人的竞价记录（看看对于其他同时竞拍的商品，该登录人出价是否是最高的，算其资金是否够用）
        for (Bidding bidding : biddingList) {
            //对于正在进行的竞拍进行处理
            if (Objects.equals(bidding.getStatus(), BiddingStatusEnum.NORMAL.getValue())) {
                //查询当前人的竞价记录是否是最高竞价记录
                //biddingService.findBiddingOfMaxPriceByProductId(bidding.getProductId())查询某个商品的最高竞价价格
                Bidding maxPriceBidding = biddingService.findBiddingOfMaxPriceByProductId(bidding.getProductId());
                if (maxPriceBidding.getUserId().equals(bidding.getUserId())) {
                    //加入预算资金
                    usedBalance = usedBalance.add(maxPriceBidding.getPrice());
                }
            }

        }
        User currentUser = userService.findById(user.getId());
        // 如果金额不够
        if (currentUser.getBalance().compareTo(usedBalance) < 0) {
            return JsonResult.error("余额不足，还差" + usedBalance.subtract(currentUser.getBalance()) + "元，请充值");
        }

        // 金额够
        // 添加/更新竞价

        Bidding bidding = biddingService.findByUserIdAndProductId(user.getId(), productId);
        if (bidding != null) {
            // 更新竞价
            bidding.setPrice(price);
            bidding.setCreateTime(new Date());
            biddingService.update(bidding);
        } else {
            // 新增竞价
            bidding = new Bidding();
            bidding.setUserId(user.getId());
            bidding.setProductId(productId);
            bidding.setPrice(price);
            bidding.setCreateTime(new Date());
            bidding.setStatus(BiddingStatusEnum.NORMAL.getValue());
            biddingService.insert(bidding);
        }
        // 修改商品当前竞价
        product.setCurrentPrice(price);
        productService.update(product);
        //当前商品用户所有竞价

//        criteria.put("productId",product.getId());
//        List<Bidding> AllbiddingList = biddingService.findAll(criteria);
        Map<String,Object> criteria2 = new HashMap<>();
        criteria2.put("productId",product.getId());
       List<Bidding> AllbiddingList = biddingService.findAll(criteria2);
        return JsonResult.success(AllbiddingList);
    }

    /**
     * 404
     *
     * @return
     */
    @RequestMapping(value = "/404", method = RequestMethod.GET)
    public String error404() {
        return "common/404";
    }


    /**
     * 404
     *
     * @return
     */
    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String error403() {
        return "common/403";
    }
}
