package com.example.ssm.shop.service.impl;

import com.example.ssm.shop.entity.Bidding;
import com.example.ssm.shop.entity.Order;
import com.example.ssm.shop.entity.Product;
import com.example.ssm.shop.entity.User;
import com.example.ssm.shop.enums.BiddingStatusEnum;
import com.example.ssm.shop.enums.ProductStatusEnum;
import com.example.ssm.shop.exception.MyBusinessException;
import com.example.ssm.shop.mapper.BiddingMapper;
import com.example.ssm.shop.mapper.OrderMapper;
import com.example.ssm.shop.mapper.ProductMapper;
import com.example.ssm.shop.mapper.UserMapper;
import com.example.ssm.shop.service.ProductService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.installer.IdeFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author admin
 */
@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private BiddingMapper biddingMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Override
    public int deleteById(Long id) {
        return productMapper.deleteById(id);
    }

    @Override
    public int insert(Product product) {
        return productMapper.insert(product);
    }

    @Override
    public Product findById(Long id) {
        return productMapper.findById(id);
    }

    @Override
    public int update(Product product) {
        return productMapper.update(product);
    }

    @Override
    public List<Product> findAll(Map<String, Object> criteria) {
        return productMapper.findAll(criteria);
    }

    @Override
    public PageInfo<Product> findAll(Integer pageIndex, Integer pageSize, Map<String, Object> criteria) {
        PageHelper.startPage(pageIndex, pageSize);
        List<Product> productList = productMapper.findAll(criteria);
        return new PageInfo<>(productList);
    }

    @Override
    public synchronized void updateProductStatus() {
        // ???????????????????????????????????????????????????????????????????????????
        List<Product> productList = productMapper.findNormalAndOvertimeProductList();
        if (productList!=null&&productList.size()>0){
            for (Product product : productList) {
                try {
                    // ??????????????????????????????????????????
                     Bidding bidding = biddingMapper.findBiddingOfMaxPriceByProductId(product.getId());
                    if (bidding != null) {
                        createOrder(bidding, product);
                    } else {
                        product.setStatus(ProductStatusEnum.FAIL.getValue());
                        productMapper.update(product);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void createOrder(Bidding bidding, Product product) {
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            bidding.setStatus(BiddingStatusEnum.SUCCESS.getValue());
            biddingMapper.update(bidding);

            // ??????????????????????????????
            product.setStatus(ProductStatusEnum.SUCCESS.getValue());
            productMapper.update(product);

            //??????????????????
            User buyer = userMapper.findById(bidding.getUserId());
            if (buyer != null) {
                if (buyer.getBalance().compareTo(bidding.getPrice()) > -1) {
                    buyer.setBalance(buyer.getBalance().subtract(bidding.getPrice()));
                } else {
                    System.out.println("??????????????????");
                    throw new MyBusinessException("??????????????????");
                }
                userMapper.update(buyer);
            } else {
                System.out.println("???????????????");
                throw new MyBusinessException("???????????????");
            }
            //??????????????????
            User seller = userMapper.findById(product.getUserId());
            if (seller != null) {
                seller.setBalance(seller.getBalance().add(bidding.getPrice()));
                userMapper.update(seller);
            } else {
                System.out.println("???????????????");
                throw new MyBusinessException("???????????????");
            }
            // ???????????????????????????
            Order order = new Order();
            order.setUserId(bidding.getUserId());
            order.setPrice(bidding.getPrice());
            order.setOrderNumber(generateJOrderNumber(bidding.getUserId()));
            order.setCreateTime(new Date());
            order.setProductId(bidding.getProductId());
            order.setAddress(buyer.getAddress());
            orderMapper.insert(order);
            transactionManager.commit(transactionStatus);
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
            e.printStackTrace();
        }

    }

    /***
     * ???????????????
     * @param userId
     * @return
     */
    private String generateJOrderNumber(Long userId) {
        StringBuilder sb = new StringBuilder("R");
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        sb.append(sf.format(new Date())).append(String.format("%0" + 5 + "d", userId));
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }


    @Override
    public List<Product> findByUserId(Long userId) {
        return productMapper.findByUserId(userId);
    }

    @Override
    public Integer deleteByUserId(Long userId) {
        return productMapper.deleteByUserId(userId);
    }

    /**
     * ????????????ID??????
     *
     * @param id
     * @return
     */
    @Override
    public Integer groundingById(Long id) {
        Product product = new Product();
        product.setId(id);
        product.setStatus(ProductStatusEnum.NORMAL.getValue());
        return productMapper.update(product);
    }

    /**
     * ????????????ID??????
     *
     * @param id
     * @return
     */
    @Override
    public Integer offById(Long id) {
        Product product = new Product();
        product.setId(id);
        product.setStatus(ProductStatusEnum.OFF.getValue());
        return productMapper.update(product);
    }
}
