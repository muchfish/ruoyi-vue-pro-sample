package cn.iocoder.yudao.framework.security.config;

import cn.iocoder.yudao.framework.security.core.annotation.Authenticated;
import cn.iocoder.yudao.framework.security.core.annotation.LoginFree;
import cn.iocoder.yudao.framework.security.core.filter.TokenAuthenticationFilter;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

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
    @Resource
    private ApplicationContext applicationContext;

    private final Multimap<HttpMethod, String> loginFreeUrls = HashMultimap.create();
    private final Multimap<HttpMethod, String> authenticatedUrls = HashMultimap.create();

    @PostConstruct
    private void init() {
        initUrlsFromAnnotations();
    }


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
                // 1.1 设置 @LoginFree 无需认证
                .antMatchers(HttpMethod.GET, loginFreeUrls.get(HttpMethod.GET).toArray(new String[0])).permitAll()
                .antMatchers(HttpMethod.POST, loginFreeUrls.get(HttpMethod.POST).toArray(new String[0])).permitAll()
                .antMatchers(HttpMethod.PUT, loginFreeUrls.get(HttpMethod.PUT).toArray(new String[0])).permitAll()
                .antMatchers(HttpMethod.DELETE, loginFreeUrls.get(HttpMethod.DELETE).toArray(new String[0])).permitAll()
                // 1.2 设置 @Authenticated 无需鉴权
                .antMatchers(HttpMethod.GET, authenticatedUrls.get(HttpMethod.GET).toArray(new String[0])).authenticated()
                .antMatchers(HttpMethod.POST, authenticatedUrls.get(HttpMethod.POST).toArray(new String[0])).authenticated()
                .antMatchers(HttpMethod.PUT, authenticatedUrls.get(HttpMethod.PUT).toArray(new String[0])).authenticated()
                .antMatchers(HttpMethod.DELETE, authenticatedUrls.get(HttpMethod.DELETE).toArray(new String[0])).authenticated()
                // ③：兜底规则，必须认证
                .anyRequest().authenticated()
        ;

        // 添加 Token Filter
        httpSecurity.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }



    /**
     * 获得 @LoginFree 带来的 URL 列表，免登录<br>
     * 获得 @Authenticated 带来的 URL 列表，免鉴权
     */
    private void initUrlsFromAnnotations() {
        // 获得接口对应的 HandlerMethod 集合
        RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping)
                applicationContext.getBean("requestMappingHandlerMapping");
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = requestMappingHandlerMapping.getHandlerMethods();
        // 获得有 @LoginFree 和@Authenticated 注解的接口
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethodMap.entrySet()) {
            HandlerMethod handlerMethod = entry.getValue();
            if (!handlerMethod.hasMethodAnnotation(LoginFree.class) && !handlerMethod.hasMethodAnnotation(Authenticated.class)) {
                continue;
            }
            Set<String> urls = entry.getKey().getDirectPaths();
            // 根据请求方法，添加到 result 结果
            entry.getKey().getMethodsCondition().getMethods().forEach(requestMethod -> {
                //免登录接口
                if (handlerMethod.hasMethodAnnotation(LoginFree.class)) {
                    switch (requestMethod) {
                        case GET:
                            loginFreeUrls.putAll(HttpMethod.GET, urls);
                            break;
                        case POST:
                            loginFreeUrls.putAll(HttpMethod.POST, urls);
                            break;
                        case PUT:
                            loginFreeUrls.putAll(HttpMethod.PUT, urls);
                            break;
                        case DELETE:
                            loginFreeUrls.putAll(HttpMethod.DELETE, urls);
                            break;
                    }
                }
                //免鉴权接口
                if (handlerMethod.hasMethodAnnotation(Authenticated.class)) {
                    switch (requestMethod) {
                        case GET:
                            authenticatedUrls.putAll(HttpMethod.GET, urls);
                            break;
                        case POST:
                            authenticatedUrls.putAll(HttpMethod.POST, urls);
                            break;
                        case PUT:
                            authenticatedUrls.putAll(HttpMethod.PUT, urls);
                            break;
                        case DELETE:
                            authenticatedUrls.putAll(HttpMethod.DELETE, urls);
                            break;
                    }
                }
            });
        }
    }

}
