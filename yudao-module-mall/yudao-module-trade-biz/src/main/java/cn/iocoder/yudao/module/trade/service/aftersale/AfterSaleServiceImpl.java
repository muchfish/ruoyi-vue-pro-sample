package cn.iocoder.yudao.module.trade.service.aftersale;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.trade.controller.admin.aftersale.vo.AfterSaleDisagreeReqVO;
import cn.iocoder.yudao.module.trade.controller.admin.aftersale.vo.AfterSalePageReqVO;
import cn.iocoder.yudao.module.trade.controller.admin.aftersale.vo.AfterSaleRefuseReqVO;
import cn.iocoder.yudao.module.trade.dal.dataobject.aftersale.AfterSaleDO;
import cn.iocoder.yudao.module.trade.dal.mysql.aftersale.AfterSaleMapper;
import cn.iocoder.yudao.module.trade.enums.aftersale.AfterSaleOperateTypeEnum;
import cn.iocoder.yudao.module.trade.enums.aftersale.AfterSaleStatusEnum;
import cn.iocoder.yudao.module.trade.enums.aftersale.AfterSaleWayEnum;
import cn.iocoder.yudao.module.trade.framework.aftersale.core.annotations.AfterSaleLog;
import cn.iocoder.yudao.module.trade.framework.aftersale.core.utils.AfterSaleLogUtils;
import cn.iocoder.yudao.module.trade.service.order.TradeOrderUpdateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.trade.enums.ErrorCodeConstants.*;

/**
 * 售后订单 Service 实现类
 *
 * @author 芋道源码
 */
@Slf4j
@Service
@Validated
public class AfterSaleServiceImpl implements AfterSaleService {

    @Resource
    private TradeOrderUpdateService tradeOrderUpdateService;
    @Resource
    private AfterSaleMapper tradeAfterSaleMapper;

    @Override
    public PageResult<AfterSaleDO> getAfterSalePage(AfterSalePageReqVO pageReqVO) {
        return tradeAfterSaleMapper.selectPage(pageReqVO);
    }

    @Override
    public AfterSaleDO getAfterSale(Long id) {
        return tradeAfterSaleMapper.selectById(id);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @AfterSaleLog(operateType = AfterSaleOperateTypeEnum.ADMIN_AGREE_APPLY)
    public void agreeAfterSale(Long userId, Long id) {
        // 校验售后单存在，并状态未审批
        AfterSaleDO afterSale = validateAfterSaleAuditable(id);

        // 更新售后单的状态
        // 情况一：退款：标记为 WAIT_REFUND 状态。后续等退款发起成功后，在标记为 COMPLETE 状态
        // 情况二：退货退款：需要等用户退货后，才能发起退款
        Integer newStatus = afterSale.getWay().equals(AfterSaleWayEnum.REFUND.getWay()) ?
                AfterSaleStatusEnum.WAIT_REFUND.getStatus() : AfterSaleStatusEnum.SELLER_AGREE.getStatus();
        updateAfterSaleStatus(afterSale.getId(), AfterSaleStatusEnum.APPLY.getStatus(), new AfterSaleDO()
                .setStatus(newStatus).setAuditUserId(userId).setAuditTime(LocalDateTime.now()));

        // 记录售后日志
        AfterSaleLogUtils.setAfterSaleInfo(afterSale.getId(), afterSale.getStatus(), newStatus);

        // TODO 发送售后消息
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @AfterSaleLog(operateType = AfterSaleOperateTypeEnum.ADMIN_DISAGREE_APPLY)
    public void disagreeAfterSale(Long userId, AfterSaleDisagreeReqVO auditReqVO) {
        // 校验售后单存在，并状态未审批
        AfterSaleDO afterSale = validateAfterSaleAuditable(auditReqVO.getId());

        // 更新售后单的状态
        Integer newStatus = AfterSaleStatusEnum.SELLER_DISAGREE.getStatus();
        updateAfterSaleStatus(afterSale.getId(), AfterSaleStatusEnum.APPLY.getStatus(), new AfterSaleDO()
                .setStatus(newStatus).setAuditUserId(userId).setAuditTime(LocalDateTime.now())
                .setAuditReason(auditReqVO.getAuditReason()));

        // 记录售后日志
        AfterSaleLogUtils.setAfterSaleInfo(afterSale.getId(), afterSale.getStatus(), newStatus);

        // TODO 发送售后消息

        // 更新交易订单项的售后状态为【未申请】
        tradeOrderUpdateService.updateOrderItemWhenAfterSaleCancel(afterSale.getOrderItemId());
    }

    /**
     * 校验售后单是否可审批（同意售后、拒绝售后）
     *
     * @param id 售后编号
     * @return 售后单
     */
    private AfterSaleDO validateAfterSaleAuditable(Long id) {
        AfterSaleDO afterSale = tradeAfterSaleMapper.selectById(id);
        if (afterSale == null) {
            throw exception(AFTER_SALE_NOT_FOUND);
        }
        if (ObjectUtil.notEqual(afterSale.getStatus(), AfterSaleStatusEnum.APPLY.getStatus())) {
            throw exception(AFTER_SALE_AUDIT_FAIL_STATUS_NOT_APPLY);
        }
        return afterSale;
    }

    private void updateAfterSaleStatus(Long id, Integer status, AfterSaleDO updateObj) {
        int updateCount = tradeAfterSaleMapper.updateByIdAndStatus(id, status, updateObj);
        if (updateCount == 0) {
            throw exception(AFTER_SALE_UPDATE_STATUS_FAIL);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @AfterSaleLog(operateType = AfterSaleOperateTypeEnum.ADMIN_AGREE_RECEIVE)
    public void receiveAfterSale(Long userId, Long id) {
        // 校验售后单存在，并状态为已退货
        AfterSaleDO afterSale = validateAfterSaleReceivable(id);

        // 更新售后单的状态
        updateAfterSaleStatus(afterSale.getId(), AfterSaleStatusEnum.BUYER_DELIVERY.getStatus(), new AfterSaleDO()
                .setStatus(AfterSaleStatusEnum.WAIT_REFUND.getStatus()).setReceiveTime(LocalDateTime.now()));

        // 记录售后日志
        AfterSaleLogUtils.setAfterSaleInfo(afterSale.getId(), afterSale.getStatus(),
                AfterSaleStatusEnum.WAIT_REFUND.getStatus());

        // TODO 发送售后消息
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @AfterSaleLog(operateType = AfterSaleOperateTypeEnum.ADMIN_DISAGREE_RECEIVE)
    public void refuseAfterSale(Long userId, AfterSaleRefuseReqVO refuseReqVO) {
        // 校验售后单存在，并状态为已退货
        AfterSaleDO afterSale = tradeAfterSaleMapper.selectById(refuseReqVO.getId());
        if (afterSale == null) {
            throw exception(AFTER_SALE_NOT_FOUND);
        }
        if (ObjectUtil.notEqual(afterSale.getStatus(), AfterSaleStatusEnum.BUYER_DELIVERY.getStatus())) {
            throw exception(AFTER_SALE_CONFIRM_FAIL_STATUS_NOT_BUYER_DELIVERY);
        }

        // 更新售后单的状态
        updateAfterSaleStatus(afterSale.getId(), AfterSaleStatusEnum.BUYER_DELIVERY.getStatus(), new AfterSaleDO()
                .setStatus(AfterSaleStatusEnum.SELLER_REFUSE.getStatus()).setReceiveTime(LocalDateTime.now())
                .setReceiveReason(refuseReqVO.getRefuseMemo()));

        // 记录售后日志
        AfterSaleLogUtils.setAfterSaleInfo(afterSale.getId(), afterSale.getStatus(),
                AfterSaleStatusEnum.SELLER_REFUSE.getStatus(),
                MapUtil.of("reason", refuseReqVO.getRefuseMemo()));

        // TODO 发送售后消息

        // 更新交易订单项的售后状态为【未申请】
        tradeOrderUpdateService.updateOrderItemWhenAfterSaleCancel(afterSale.getOrderItemId());
    }

    /**
     * 校验售后单是否可收货，即处于买家已发货
     *
     * @param id 售后编号
     * @return 售后单
     */
    private AfterSaleDO validateAfterSaleReceivable(Long id) {
        AfterSaleDO afterSale = tradeAfterSaleMapper.selectById(id);
        if (afterSale == null) {
            throw exception(AFTER_SALE_NOT_FOUND);
        }
        if (ObjectUtil.notEqual(afterSale.getStatus(), AfterSaleStatusEnum.BUYER_DELIVERY.getStatus())) {
            throw exception(AFTER_SALE_CONFIRM_FAIL_STATUS_NOT_BUYER_DELIVERY);
        }
        return afterSale;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @AfterSaleLog(operateType = AfterSaleOperateTypeEnum.ADMIN_REFUND)
    public void refundAfterSale(Long userId, String userIp, Long id) {
        // 校验售后单的状态，并状态待退款
        AfterSaleDO afterSale = tradeAfterSaleMapper.selectById(id);
        if (afterSale == null) {
            throw exception(AFTER_SALE_NOT_FOUND);
        }
        if (ObjectUtil.notEqual(afterSale.getStatus(), AfterSaleStatusEnum.WAIT_REFUND.getStatus())) {
            throw exception(AFTER_SALE_REFUND_FAIL_STATUS_NOT_WAIT_REFUND);
        }

        // 发起退款单。注意，需要在事务提交后，再进行发起，避免重复发起
        createPayRefund(userIp, afterSale);

        // 更新售后单的状态为【已完成】
        updateAfterSaleStatus(afterSale.getId(), AfterSaleStatusEnum.WAIT_REFUND.getStatus(), new AfterSaleDO()
                .setStatus(AfterSaleStatusEnum.COMPLETE.getStatus()).setRefundTime(LocalDateTime.now()));

        // 记录售后日志
        AfterSaleLogUtils.setAfterSaleInfo(afterSale.getId(), afterSale.getStatus(),
                AfterSaleStatusEnum.COMPLETE.getStatus());

        // TODO 发送售后消息

        // 更新交易订单项的售后状态为【已完成】
        tradeOrderUpdateService.updateOrderItemWhenAfterSaleSuccess(afterSale.getOrderItemId(), afterSale.getRefundPrice());
    }

    private void createPayRefund(String userIp, AfterSaleDO afterSale) {
        //todo Dai
/*        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
                // 创建退款单
                PayRefundCreateReqDTO createReqDTO = AfterSaleConvert.INSTANCE.convert(userIp, afterSale, tradeOrderProperties);
                Long payRefundId = payRefundApi.createRefund(createReqDTO);
                // 更新售后单的退款单号
                tradeAfterSaleMapper.updateById(new AfterSaleDO().setId(afterSale.getId()).setPayRefundId(payRefundId));
            }
        });*/
    }


}
