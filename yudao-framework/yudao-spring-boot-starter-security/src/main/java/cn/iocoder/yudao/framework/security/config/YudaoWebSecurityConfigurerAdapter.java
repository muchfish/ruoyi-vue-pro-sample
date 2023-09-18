package cn.iocoder.yudao.framework.security.config;

import cn.iocoder.yudao.framework.security.core.filter.TokenAuthenticationFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * 自定义的 Spring Security 配置适配器实现
 *
 * @author 芋道源码
 */
@AutoConfiguration
@EnableWebSecurity
public class YudaoWebSecurityConfigurerAdapter {



    /**
     * Token 认证过滤器 Bean
     */
    @Resource
    private TokenAuthenticationFilter authenticationTokenFilter;



    /**
     * 配置 URL 的安全配置
     *
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * permitAll           |   用户可以任意访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * authenticated       |   用户登录后可访问
     */
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // CSRF 禁用
                .csrf().disable();
        // 设置每个请求的权限
        httpSecurity
                // ①：全局共享规则
                .authorizeRequests()
                // 允许任何人访问
                .antMatchers("/admin-api/system/auth/login","/admin-api/system/tenant/get-id-by-name","/admin-api/system/captcha/get", "/admin-api/system/captcha/check").permitAll()
                // ③：兜底规则，必须认证
                .anyRequest().authenticated()
        ;

        // 添加 Token Filter
        httpSecurity.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }





}
