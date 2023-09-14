package cn.iocoder.yudao.module.system.service.tenant;

import cn.iocoder.yudao.module.system.dal.dataobject.tenant.TenantDO;

/**
 * 租户 Service 接口
 *
 * @author 芋道源码
 */
public interface TenantService {

    /**
     * 获得名字对应的租户
     *
     * @param name 组户名
     * @return 租户
     */
    TenantDO getTenantByName(String name);
}
