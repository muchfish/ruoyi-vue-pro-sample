package cn.iocoder.yudao.module.system.dal.mysql.dept;


import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.module.system.controller.admin.dept.vo.dept.DeptListReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.dept.DeptDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Mapper
public interface DeptMapper extends BaseMapper<DeptDO> {

    default List<DeptDO> selectList(DeptListReqVO reqVO) {
        LambdaQueryWrapper<DeptDO> wrapper = new LambdaQueryWrapper<DeptDO>();
        if (StrUtil.isNotBlank(reqVO.getName())) {
            wrapper.like(DeptDO::getName, reqVO.getName());
        }
        if (Objects.nonNull(reqVO.getStatus())) {
            wrapper.eq(DeptDO::getStatus, reqVO.getStatus());
        }
        return selectList(wrapper);
    }

    default DeptDO selectByParentIdAndName(Long parentId, String name) {
        LambdaQueryWrapper<DeptDO> wrapper = new LambdaQueryWrapper<DeptDO>().eq(DeptDO::getParentId, parentId).eq(DeptDO::getName, name);
        return selectOne(wrapper);
    }

    default Long selectCountByParentId(Long parentId) {
        LambdaQueryWrapper<DeptDO> wrapper = new LambdaQueryWrapper<DeptDO>().eq(DeptDO::getParentId, parentId);
        return selectCount(wrapper);
    }

    default List<DeptDO> selectListByParentId(Collection<Long> parentIds) {
        LambdaQueryWrapper<DeptDO> wrapper = new LambdaQueryWrapper<DeptDO>().in(DeptDO::getParentId, parentIds);
        return selectList(wrapper);
    }

}
