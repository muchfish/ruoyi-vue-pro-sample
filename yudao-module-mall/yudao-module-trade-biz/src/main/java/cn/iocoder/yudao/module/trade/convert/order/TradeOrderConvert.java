package cn.iocoder.yudao.module.trade.convert.order;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.ip.core.utils.AreaUtils;
import cn.iocoder.yudao.module.member.api.user.dto.MemberUserRespDTO;
import cn.iocoder.yudao.module.trade.controller.admin.base.member.user.MemberUserRespVO;
import cn.iocoder.yudao.module.trade.controller.admin.order.vo.TradeOrderPageItemRespVO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderDO;
import cn.iocoder.yudao.module.trade.dal.dataobject.order.TradeOrderItemDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMultiMap;

@Mapper
public interface TradeOrderConvert {

    TradeOrderConvert INSTANCE = Mappers.getMapper(TradeOrderConvert.class);



    default PageResult<TradeOrderPageItemRespVO> convertPage(PageResult<TradeOrderDO> pageResult,
                                                             List<TradeOrderItemDO> orderItems,
                                                             Map<Long, MemberUserRespDTO> memberUserMap) {
        Map<Long, List<TradeOrderItemDO>> orderItemMap = convertMultiMap(orderItems, TradeOrderItemDO::getOrderId);
        // 转化 List
        List<TradeOrderPageItemRespVO> orderVOs = CollectionUtils.convertList(pageResult.getList(), order -> {
            List<TradeOrderItemDO> xOrderItems = orderItemMap.get(order.getId());
            TradeOrderPageItemRespVO orderVO = convert(order, xOrderItems);
            // 处理收货地址
            orderVO.setReceiverAreaName(AreaUtils.format(order.getReceiverAreaId()));
            // 增加用户信息
            orderVO.setUser(convertUser(memberUserMap.get(orderVO.getUserId())));
            // 增加推广人信息
            orderVO.setBrokerageUser(convertUser(memberUserMap.get(orderVO.getBrokerageUserId())));
            return orderVO;
        });
        return new PageResult<>(orderVOs, pageResult.getTotal());
    }

    MemberUserRespVO convertUser(MemberUserRespDTO memberUserRespDTO);

    TradeOrderPageItemRespVO convert(TradeOrderDO order, List<TradeOrderItemDO> items);











}
