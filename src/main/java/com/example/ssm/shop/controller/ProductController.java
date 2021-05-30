package com.example.ssm.shop.controller;

import com.example.ssm.shop.entity.Bidding;
import com.example.ssm.shop.entity.Category;
import com.example.ssm.shop.entity.Product;
import com.example.ssm.shop.entity.User;
import com.example.ssm.shop.enums.ProductStatusEnum;
import com.example.ssm.shop.service.BiddingService;
import com.example.ssm.shop.service.CategoryService;
import com.example.ssm.shop.service.ProductService;
import com.example.ssm.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author admin
 */
@Controller
public class ProductController extends BaseController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private BiddingService biddingService;

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
    public String detail(@PathVariable("id") Long id, Model model) {
        // 商品信息
        Product product = productService.findById(id);
        if (product == null) {
            return "forward:/404";
        }
        // 用户信息
        User user = userService.findById(product.getUserId());
        product.setUser(user);

        // 分类信息
        Category category = categoryService.findById(product.getCateId());
        product.setCategory(category);

        // 竞价信息
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("productId", id);
        List<Bidding> biddingList = biddingService.findAll(criteria);
        product.setBiddingList(biddingList);

        User loginUser = getLoginUser();

        //若已成交，查询成交者
        if (Objects.equals(product.getStatus(), ProductStatusEnum.SUCCESS.getValue())) {
            Bidding bidding = biddingList.get(0);
            User buyUser = userService.findById(bidding.getUserId());
            if (buyUser != null) {
                model.addAttribute("buyUser", buyUser.getUsername());
            }
        }
        // 是否竞拍
        boolean isAddBidding = false;
        if (loginUser != null) {
            // 判断是否竞拍
            Bidding bidding = biddingService.findByUserIdAndProductId(loginUser.getId(), id);
            if (bidding != null) {
                isAddBidding = true;
                model.addAttribute("biddingPrice", bidding.getPrice());
            }
        }

        model.addAttribute("isAddBidding", isAddBidding);

        model.addAttribute("product", product);
        return "product-details";
    }


}
