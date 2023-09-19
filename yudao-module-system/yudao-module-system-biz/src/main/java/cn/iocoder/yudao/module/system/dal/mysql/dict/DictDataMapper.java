package cn.iocoder.yudao.module.system.dal.mysql.dict;

import cn.iocoder.yudao.module.system.dal.dataobject.dict.DictDataDO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DictDataMapper extends BaseMapper<DictDataDO> {

   default List<DictDataDO> selectList(){
       return selectList(new QueryWrapper<>());
    }
}