package cn.iocoder.yudao.module.system.mq.consumer.test;

import cn.iocoder.yudao.framework.mq.core.stream.AbstractStreamMessageListener;
import cn.iocoder.yudao.module.system.mq.message.test.TestSendMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 针对 {@link TestSendMessage} 的消费者
 *
 * @author 芋道源码
 */
@Component
@Slf4j
public class TestSendConsumer extends AbstractStreamMessageListener<TestSendMessage> {



    @Override
    public void onMessage(TestSendMessage message) {
        log.info("[onMessage][消息内容({})]", message);
    }

}
