package cn.iocoder.yudao.module.system.service.tenant;

import cn.iocoder.yudao.module.system.dal.dataobject.tenant.TenantDO;
import cn.iocoder.yudao.module.system.dal.mysql.tenant.TenantMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * 租户 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class TenantServiceImpl implements TenantService {



    @Resource
    private TenantMapper tenantMapper;

    @Override
    public TenantDO getTenantByName(String name) {
        return tenantMapper.selectByName(name);
    }


}
