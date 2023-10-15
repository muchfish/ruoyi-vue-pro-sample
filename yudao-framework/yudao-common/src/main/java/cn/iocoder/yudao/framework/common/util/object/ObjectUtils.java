package cn.iocoder.yudao.framework.common.util.object;

import java.util.Arrays;

/**
 * Object 工具类
 *
 * @author 芋道源码
 */
public class ObjectUtils {

    @SafeVarargs
    public static <T> boolean equalsAny(T obj, T... array) {
        return Arrays.asList(array).contains(obj);
    }

}
