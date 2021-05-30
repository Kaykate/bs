package com.example.ssm.shop.service;

import com.example.ssm.shop.entity.Product;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @author admin
 */
public interface ProductService {

    /**
     * 根据ID删除
     *
     * @param id 商品ID
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 添加
     *
     * @param user 商品
     * @return 影响行数
     */
    int insert(Product user);

    /**
     * 根据ID查询
     *
     * @param id 商品ID
     * @return 商品
     */
    Product findById(Long id);

    /**
     * 更新
     *
     * @param product 商品
     * @return 影响行数
     */
    int update(Product product);

    /**
     * 分页查询
     * @param criteria
     * @return
     */
    PageInfo<Product> findAll(Integer pageNo, Integer pageSize, Map<String, Object> criteria);

    /**
     * 查询
     * @param criteria
     * @return
     */
    List<Product> findAll(Map<String, Object> criteria);

    /**
     * 更新到期的商品为流拍状态
     */
    void updateProductStatus();

    /**
     * 根据用户ID获得商品列表
     * @param userId
     * @return
     */
    List<Product> findByUserId(Long userId);

    /**
     * 根据用户ID删除
     * @param userId
     * @return
     */
    Integer deleteByUserId(Long userId);

    /**
     * 根据商品ID上架
     * @param id
     * @return
     */
    Integer groundingById(Long id);

    /**
     * 根据商品ID下架
     * @param id
     * @return
     */
    Integer offById(Long id);
}
