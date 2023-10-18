package cn.iocoder.yudao.module.system.mq.consumer.test.pubsub;

import cn.iocoder.yudao.framework.mq.core.pubsub.AbstractChannelMessageListener;
import cn.iocoder.yudao.module.system.mq.message.test.TestPubSubMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TestPubSubConsumer2 extends AbstractChannelMessageListener<TestPubSubMessage> {

    @Override
    public void onMessage(TestPubSubMessage message) {
        log.info("[onMessage][pubsub2][消息内容({})]", message);
    }
}
