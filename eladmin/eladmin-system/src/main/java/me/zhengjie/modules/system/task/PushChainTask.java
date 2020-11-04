package me.zhengjie.modules.system.task;

import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.system.service.DepositCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时查询上链中的存证，重新发生上链请求
 */

@Slf4j
@Component
@EnableScheduling
public class PushChainTask {

    @Autowired
    private DepositCertificateService depositCertificateService;

    /**
     * 定时查询上链中的存证，重新发生上链请求
     * 间隔 10 分钟
     */
//    @Scheduled(cron = "0 */10 * * * ?")
    public void task() {
        log.info("定时查询上链中的存证，重新发生上链请求.............开始");
        depositCertificateService.pushChainTask();
        log.info("定时查询上链中的存证，重新发生上链请求.............结束");
    }
}
