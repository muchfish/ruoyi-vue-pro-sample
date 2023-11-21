package cn.iocoder.yudao.module.product.dal.mysql.spu;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.product.controller.admin.spu.vo.ProductSpuExportReqVO;
import cn.iocoder.yudao.module.product.controller.admin.spu.vo.ProductSpuPageReqVO;
import cn.iocoder.yudao.module.product.dal.dataobject.spu.ProductSpuDO;
import cn.iocoder.yudao.module.product.enums.ProductConstants;
import cn.iocoder.yudao.module.product.enums.spu.ProductSpuStatusEnum;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductSpuMapper extends BaseMapperX<ProductSpuDO> {

    /**
     * 获取商品 SPU 分页列表数据
     *
     * @param reqVO 分页请求参数
     * @return 商品 SPU 分页列表数据
     */
    default PageResult<ProductSpuDO> selectPage(ProductSpuPageReqVO reqVO) {
        Integer tabType = reqVO.getTabType();
        LambdaQueryWrapperX<ProductSpuDO> queryWrapper = new LambdaQueryWrapperX<ProductSpuDO>()
                .likeIfPresent(ProductSpuDO::getName, reqVO.getName())
                .eqIfPresent(ProductSpuDO::getCategoryId, reqVO.getCategoryId())
                .betweenIfPresent(ProductSpuDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ProductSpuDO::getSort);
        appendTabQuery(tabType, queryWrapper);
        return selectPage(reqVO, queryWrapper);
    }

    /**
     * 查询触发警戒库存的 SPU 数量
     *
     * @return 触发警戒库存的 SPU 数量
     */
    default Long selectCount() {
        LambdaQueryWrapperX<ProductSpuDO> queryWrapper = new LambdaQueryWrapperX<>();
        // 库存小于等于警戒库存
        queryWrapper.le(ProductSpuDO::getStock, ProductConstants.ALERT_STOCK)
                // 如果库存触发警戒库存且状态为回收站的话则不计入触发警戒库存的个数
                .notIn(ProductSpuDO::getStatus, ProductSpuStatusEnum.RECYCLE.getStatus());
        return selectCount(queryWrapper);
    }


    /**
     * 更新商品 SPU 库存
     *
     * @param id        商品 SPU 编号
     * @param incrCount 增加的库存数量
     */
    default void updateStock(Long id, Integer incrCount) {
        LambdaUpdateWrapper<ProductSpuDO> updateWrapper = new LambdaUpdateWrapper<ProductSpuDO>()
                // 负数，所以使用 + 号
                .setSql(" stock = stock +" + incrCount)
                .eq(ProductSpuDO::getId, id);
        update(null, updateWrapper);
    }

    /**
     * 获得 Spu 列表
     *
     * @param reqVO 查询条件
     * @return Spu 列表
     */
    default List<ProductSpuDO> selectList(ProductSpuExportReqVO reqVO) {
        Integer tabType = reqVO.getTabType();
        LambdaQueryWrapperX<ProductSpuDO> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.eqIfPresent(ProductSpuDO::getName, reqVO.getName());
        queryWrapper.eqIfPresent(ProductSpuDO::getCategoryId, reqVO.getCategoryId());
        queryWrapper.betweenIfPresent(ProductSpuDO::getCreateTime, reqVO.getCreateTime());
        appendTabQuery(tabType, queryWrapper);
        return selectList(queryWrapper);
    }

    /**
     * 添加后台 Tab 选项的查询条件
     *
     * @param tabType      标签类型
     * @param query 查询条件
     */
    static void appendTabQuery(Integer tabType, LambdaQueryWrapperX<ProductSpuDO> query) {
        // 出售中商品
        if (ObjectUtil.equals(ProductSpuPageReqVO.FOR_SALE, tabType)) {
            query.eqIfPresent(ProductSpuDO::getStatus, ProductSpuStatusEnum.ENABLE.getStatus());
        }
        // 仓储中商品
        if (ObjectUtil.equals(ProductSpuPageReqVO.IN_WAREHOUSE, tabType)) {
            query.eqIfPresent(ProductSpuDO::getStatus, ProductSpuStatusEnum.DISABLE.getStatus());
        }
        // 已售空商品
        if (ObjectUtil.equals(ProductSpuPageReqVO.SOLD_OUT, tabType)) {
            query.eqIfPresent(ProductSpuDO::getStock, 0);
        }
        // 警戒库存
        if (ObjectUtil.equals(ProductSpuPageReqVO.ALERT_STOCK, tabType)) {
            query.le(ProductSpuDO::getStock, ProductConstants.ALERT_STOCK)
                    // 如果库存触发警戒库存且状态为回收站的话则不在警戒库存列表展示
                    .notIn(ProductSpuDO::getStatus, ProductSpuStatusEnum.RECYCLE.getStatus());
        }
        // 回收站
        if (ObjectUtil.equals(ProductSpuPageReqVO.RECYCLE_BIN, tabType)) {
            query.eqIfPresent(ProductSpuDO::getStatus, ProductSpuStatusEnum.RECYCLE.getStatus());
        }
    }

}
