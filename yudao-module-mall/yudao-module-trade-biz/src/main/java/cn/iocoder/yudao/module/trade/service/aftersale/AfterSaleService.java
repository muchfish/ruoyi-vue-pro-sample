package cn.iocoder.yudao.module.trade.service.aftersale;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.trade.controller.admin.aftersale.vo.AfterSalePageReqVO;
import cn.iocoder.yudao.module.trade.dal.dataobject.aftersale.AfterSaleDO;

/**
 * 售后订单 Service 接口
 *
 * @author 芋道源码
 */
public interface AfterSaleService {

    /**
     * 【管理员】获得售后订单分页
     *
     * @param pageReqVO 分页查询
     * @return 售后订单分页
     */
    PageResult<AfterSaleDO> getAfterSalePage(AfterSalePageReqVO pageReqVO);


}
