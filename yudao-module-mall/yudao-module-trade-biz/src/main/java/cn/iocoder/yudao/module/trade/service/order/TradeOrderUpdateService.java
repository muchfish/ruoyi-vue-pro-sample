package cn.iocoder.yudao.module.trade.service.order;

import javax.validation.constraints.NotNull;

/**
 * 交易订单【写】Service 接口
 *
 * @author LeeYan9
 * @since 2022-08-26
 */
public interface TradeOrderUpdateService {

    // =================== Order ===================


    // =================== Order Item ===================

    /**
     * 当售后完成后，更新交易订单项的售后状态
     *
     * @param id          交易订单项编号
     * @param refundPrice 退款金额
     */
    void updateOrderItemWhenAfterSaleSuccess(@NotNull Long id, @NotNull Integer refundPrice);

    /**
     * 当售后取消（用户取消、管理员驳回、管理员拒绝收货）后，更新交易订单项的售后状态
     *
     * @param id 交易订单项编号
     */
    void updateOrderItemWhenAfterSaleCancel(@NotNull Long id);



}
