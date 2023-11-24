package cn.iocoder.yudao.module.trade.dal.mysql.order;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface TradeOrderItemMapper extends BaseMapperX<TradeOrderItemDO> {

    default List<TradeOrderItemDO> selectListByOrderId(Collection<Long> orderIds) {
        return selectList(TradeOrderItemDO::getOrderId, orderIds);
    }
}
