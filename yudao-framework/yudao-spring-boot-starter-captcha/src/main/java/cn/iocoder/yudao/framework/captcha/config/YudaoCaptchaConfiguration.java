package cn.iocoder.yudao.framework.captcha.config;

import cn.iocoder.yudao.framework.captcha.core.service.RedisCaptchaServiceImpl;
import com.xingyuv.captcha.properties.AjCaptchaProperties;
import com.xingyuv.captcha.service.CaptchaCacheService;
import com.xingyuv.captcha.service.impl.CaptchaServiceFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

@AutoConfiguration
public class YudaoCaptchaConfiguration {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Bean
    public CaptchaCacheService captchaCacheService(AjCaptchaProperties config) {
        // 缓存类型 redis/local/....
        CaptchaCacheService ret = CaptchaServiceFactory.getCache(config.getCacheType().name());
        if (ret instanceof RedisCaptchaServiceImpl) {
            //CaptchaCacheService 类中@Resource StringRedisTemplate和此处set，只需要保证其一即可
            ((RedisCaptchaServiceImpl) ret).setStringRedisTemplate(stringRedisTemplate);
        }
        return ret;
    }

}