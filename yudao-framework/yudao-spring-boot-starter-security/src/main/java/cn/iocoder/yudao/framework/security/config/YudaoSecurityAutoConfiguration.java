package cn.iocoder.yudao.framework.security.config;

import cn.iocoder.yudao.framework.security.core.filter.TokenAuthenticationFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Spring Security 自动配置类，主要用于相关组件的配置
 *
 * 注意，不能和 {@link YudaoWebSecurityConfigurerAdapter} 用一个，原因是会导致初始化报错。
 * 参见 https://stackoverflow.com/questions/53847050/spring-boot-delegatebuilder-cannot-be-null-on-autowiring-authenticationmanager 文档。
 *
 * @author 芋道源码
 */
@AutoConfiguration
public class YudaoSecurityAutoConfiguration {


    /**
     * Token 认证过滤器 Bean
     */
    @Bean
    public TokenAuthenticationFilter authenticationTokenFilter() {
        return new TokenAuthenticationFilter();
    }



}
