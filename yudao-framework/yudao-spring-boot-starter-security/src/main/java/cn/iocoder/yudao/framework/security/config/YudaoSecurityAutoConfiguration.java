package cn.iocoder.yudao.framework.security.config;

import cn.iocoder.yudao.framework.security.core.filter.TokenAuthenticationFilter;
import cn.iocoder.yudao.framework.security.core.handler.AuthenticationEntryPointImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

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


    /**
     * 认证失败处理类 Bean
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationEntryPointImpl();
    }
    /**
     * Spring Security 加密器
     * 考虑到安全性，这里采用 BCryptPasswordEncoder 加密器
     *
     * @see <a href="http://stackabuse.com/password-encoding-with-spring-security/">Password Encoding with Spring Security</a>
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        //strength – 要使用的原木轮数，介于 4 和 31 之间
        return new BCryptPasswordEncoder(4);
    }
}
