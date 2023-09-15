package cn.iocoder.yudao.module.system.service.dict;

import cn.iocoder.yudao.module.system.dal.dataobject.dict.DictDataDO;

import java.util.List;

/**
 * 字典数据 Service 接口
 *
 * @author ruoyi
 */
public interface DictDataService {



    /**
     * 获得字典数据列表
     *
     * @return 字典数据全列表
     */
    List<DictDataDO> getDictDataList();


}
