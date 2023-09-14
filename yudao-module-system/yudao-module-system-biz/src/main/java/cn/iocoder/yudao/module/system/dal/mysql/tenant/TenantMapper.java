package cn.iocoder.yudao.module.system.dal.mysql.tenant;

import cn.iocoder.yudao.module.system.dal.dataobject.tenant.TenantDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 租户 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface TenantMapper extends BaseMapper<TenantDO> {

    default TenantDO selectByName(String name) {
        return selectOne(new LambdaQueryWrapper<TenantDO>().eq(TenantDO::getName, name));
    }



}
