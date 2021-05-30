package com.example.ssm.shop.schedule;

import com.example.ssm.shop.dto.CommonConstant;
import com.example.ssm.shop.entity.User;
import com.example.ssm.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时器
 * @author admin
 */
@Component
public class TimerTask {

    @Autowired
    private ProductService productService;

    /**
     * 每分钟执行一次
     * 更新已经到了截止时间的状态为流拍
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void updateProductStatus() {
        productService.updateProductStatus();

    }
}
