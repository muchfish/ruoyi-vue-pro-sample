package cn.iocoder.yudao.framework.datasource.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

/**
 * 数据库配置类
 *
 * @author 芋道源码
 */
@AutoConfiguration
@EnableTransactionManagement(proxyTargetClass = true) // 启动事务管理
public class YudaoDataSourceAutoConfiguration {



}
