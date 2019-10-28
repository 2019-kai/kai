package com.itheima.test;

import com.itheima.health.job.ClearOrderSettingJob;
import org.junit.Test;

public class JobTest {

    @Test
    public void TestClearOrderSetting() {
        ClearOrderSettingJob clearOrderSettingJob = new ClearOrderSettingJob();
        clearOrderSettingJob.clearOrderSetting();
    }
}
