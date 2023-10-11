package cn.iocoder.yudao.module.system.api.tenant;

/**
 * 多租户的 API 接口
 *
 * @author 芋道源码
 */
public interface TenantApi {



    /**
     * 校验租户是否合法
     *
     * @param id 租户编号
     */
    void validateTenant(Long id);

}
