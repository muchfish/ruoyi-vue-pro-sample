package cn.iocoder.yudao.framework.security.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 免鉴权标记注解
 */
@Documented
@Retention(RUNTIME)
@Target({METHOD})
public @interface Authenticated {
}
