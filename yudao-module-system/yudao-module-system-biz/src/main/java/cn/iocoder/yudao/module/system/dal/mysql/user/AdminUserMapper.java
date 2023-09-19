package cn.iocoder.yudao.module.system.dal.mysql.user;


import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminUserMapper extends BaseMapper<AdminUserDO> {

    default AdminUserDO selectByUsername(String username) {
        return selectOne(new LambdaQueryWrapper<AdminUserDO>().eq(AdminUserDO::getUsername, username));
    }


    default List<AdminUserDO> selectListByStatus(Integer status) {
        return selectList(new LambdaQueryWrapper<AdminUserDO>().eq(AdminUserDO::getStatus, status));
    }
}
