package cn.iocoder.yudao.framework.common.util.date;

import java.time.LocalDateTime;

/**
 * 时间工具类
 *
 * @author 芋道源码
 */
public class DateUtils {

    public static final String FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND = "yyyy-MM-dd HH:mm:ss";

    public static boolean isExpired(LocalDateTime time) {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(time);
    }
}
