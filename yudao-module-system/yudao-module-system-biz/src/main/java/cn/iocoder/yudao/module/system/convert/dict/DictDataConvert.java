package cn.iocoder.yudao.module.system.convert.dict;

import cn.iocoder.yudao.module.system.controller.admin.dict.vo.DictDataSimpleRespVO;
import cn.iocoder.yudao.module.system.dal.dataobject.dict.DictDataDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DictDataConvert {

    DictDataConvert INSTANCE = Mappers.getMapper(DictDataConvert.class);

    List<DictDataSimpleRespVO> convertList(List<DictDataDO> list);



}
