package cn.iocoder.yudao.module.system.controller;

import cn.hutool.core.util.RandomUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.security.core.annotation.LoginFree;
import cn.iocoder.yudao.module.system.mq.producer.test.TestProducer;
import cn.iocoder.yudao.module.system.mq.producer.test.TestPubSubProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@RestController
public class TestController {

    @Autowired
    private TestProducer testProducer;
    @Autowired
    private TestPubSubProducer testPubSubProducer;

    @GetMapping("/test")
    public Object test() {
        return "测试";
    }



    @GetMapping("/message")
    @LoginFree
    public CommonResult<Boolean> message() {
        testProducer.sendMailSendMessage(RandomUtil.randomLong(0, Long.MAX_VALUE),"1319105206@qq.com",1L,"戴文成","测试消息","测试消息内容");
        return success(true);
    }

    @GetMapping("/message/pubsub")
    @LoginFree
    public CommonResult<Boolean> messagepubsub() {
        testPubSubProducer.sendMailSendMessage(RandomUtil.randomLong(0, Long.MAX_VALUE),"1319105206@qq.com",1L,"戴文成","测试消息","测试消息内容");
        return success(true);
    }
}
