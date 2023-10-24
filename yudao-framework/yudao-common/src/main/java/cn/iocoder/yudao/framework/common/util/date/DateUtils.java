package cn.iocoder.yudao.framework.common.util.date;

import cn.hutool.core.date.LocalDateTimeUtil;

import java.time.*;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author 芋道源码
 */
public class DateUtils {

    /**
     * 时区 - 默认
     */
    public static final String TIME_ZONE_DEFAULT = "GMT+8";

    public static final String FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND = "yyyy-MM-dd HH:mm:ss";

    public static boolean isExpired(LocalDateTime time) {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(time);
    }

    public static Date max(Date a, Date b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        return a.compareTo(b) > 0 ? a : b;
    }

    public static LocalDateTime max(LocalDateTime a, LocalDateTime b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        return a.isAfter(b) ? a : b;
    }

    /**
     * 是否今天
     *
     * @param date 日期
     * @return 是否
     */
    public static boolean isToday(LocalDateTime date) {
        return LocalDateTimeUtil.isSameDay(date, LocalDateTime.now());
    }

}
