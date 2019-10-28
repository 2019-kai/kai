package com.itheima.health.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.service.OrderSettingService;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ClearOrderSettingJob {

    @Reference
    private OrderSettingService orderSettingService;

    public void clearOrderSetting() {
        try {
            orderSettingService.clearOrderSetting(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
