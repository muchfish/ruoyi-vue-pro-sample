package cn.iocoder.yudao.module.trade.service.order;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.trade.controller.admin.order.vo.TradeOrderPageReqVO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderDO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderItemDO;

import java.util.Collection;
import java.util.List;

/**
 * 交易订单【读】 Service 接口
 *
 * @author 芋道源码
 */
public interface TradeOrderQueryService {


    /**
     * 【管理员】获得交易订单分页
     *
     * @param reqVO 分页请求
     * @return 交易订单
     */
    PageResult<TradeOrderDO> getOrderPage(TradeOrderPageReqVO reqVO);


    /**
     * 根据交易订单编号数组，查询交易订单项
     *
     * @param orderIds 交易订单编号数组
     * @return 交易订单项数组
     */
    List<TradeOrderItemDO> getOrderItemListByOrderId(Collection<Long> orderIds);

}
