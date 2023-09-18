package cn.iocoder.yudao.framework.security.core.filter;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.redis.core.utils.RedisUtil;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * Token 过滤器，验证 token 的有效性
 * 验证通过后，获得 {@link LoginUser} 信息，并加入到 Spring Security 上下文
 *
 * @author 芋道源码
 */
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {


    @Override
    @SuppressWarnings("NullableProblems")
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String token = SecurityFrameworkUtils.obtainAuthorization(request, "Authorization");
        if (StrUtil.isNotEmpty(token)) {
            // 1.1 基于 token 构建登录用户
            LoginUser loginUser = buildLoginUserByToken(token);

            // 2. 设置当前用户
            if (loginUser != null) {
                SecurityFrameworkUtils.setLoginUser(loginUser, request);
            }

        }

        // 继续过滤链
        chain.doFilter(request, response);
    }

    private LoginUser buildLoginUserByToken(String token) {
        try {
            Integer userId = RedisUtil.get(token);
            if (Objects.isNull(userId)) {
                return null;
            }

            // 构建登录用户
            return new LoginUser().setId(Long.valueOf(userId));
        } catch (ServiceException serviceException) {
            // 校验 Token 不通过时，考虑到一些接口是无需登录的，所以直接返回 null 即可
            return null;
        }
    }



}
