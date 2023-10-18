package cn.iocoder.yudao.module.system.mq.producer.test;

import cn.iocoder.yudao.framework.mq.core.RedisMQTemplate;
import cn.iocoder.yudao.module.system.mq.message.test.TestPubSubMessage;
import cn.iocoder.yudao.module.system.mq.message.test.TestSendMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 测试相关消息的 Producer
 *
 * @author wangjingyi
 * @since 2021/4/19 13:33
 */
@Slf4j
@Component
public class TestPubSubProducer {

    @Resource
    private RedisMQTemplate redisMQTemplate;

    /**
     * 发送 {@link TestSendMessage} 消息
     *
     * @param sendLogId 发送日志编码
     * @param mail 接收邮件地址
     * @param accountId 邮件账号编号
     * @param nickname 邮件发件人
     * @param title 邮件标题
     * @param content 邮件内容
     */
    public void sendMailSendMessage(Long sendLogId, String mail, Long accountId,
                                    String nickname, String title, String content) {
        TestPubSubMessage message = new TestPubSubMessage()
                .setLogId(sendLogId).setMail(mail).setAccountId(accountId)
                .setNickname(nickname).setTitle(title).setContent(content);
        redisMQTemplate.send(message);
    }

}
