package cn.iocoder.yudao.module.product.convert.comment;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.product.controller.admin.comment.vo.ProductCommentCreateReqVO;
import cn.iocoder.yudao.module.product.controller.admin.comment.vo.ProductCommentRespVO;
import cn.iocoder.yudao.module.product.dal.dataobject.comment.ProductCommentDO;
import cn.iocoder.yudao.module.product.dal.dataobject.sku.ProductSkuDO;
import cn.iocoder.yudao.module.product.dal.dataobject.spu.ProductSpuDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 商品评价 Convert
 *
 * @author wangzhs
 */
@Mapper
public interface ProductCommentConvert {

    ProductCommentConvert INSTANCE = Mappers.getMapper(ProductCommentConvert.class);

    ProductCommentRespVO convert(ProductCommentDO bean);

    PageResult<ProductCommentRespVO> convertPage(PageResult<ProductCommentDO> page);

    /**
     * 计算综合评分
     *
     * @param descriptionScores 描述星级
     * @param benefitScores     服务星级
     * @return 综合评分
     */
    @Named("convertScores")
    default Integer convertScores(Integer descriptionScores, Integer benefitScores) {
        // 计算评价最终综合评分 最终星数 = （商品评星 + 服务评星） / 2
        BigDecimal sumScore = new BigDecimal(descriptionScores + benefitScores);
        BigDecimal divide = sumScore.divide(BigDecimal.valueOf(2L), 0, RoundingMode.DOWN);
        return divide.intValue();
    }

    @Mapping(target = "visible", constant = "true")
    @Mapping(target = "replyStatus", constant = "false")
    @Mapping(target = "userId", constant = "0L")
    @Mapping(target = "orderId", constant = "0L")
    @Mapping(target = "orderItemId", constant = "0L")
    @Mapping(target = "anonymous", expression = "java(Boolean.FALSE)")
    @Mapping(target = "scores",
            expression = "java(convertScores(createReq.getDescriptionScores(), createReq.getBenefitScores()))")
    ProductCommentDO convert(ProductCommentCreateReqVO createReq);

    default ProductCommentDO convert(ProductCommentCreateReqVO createReq, ProductSpuDO spu, ProductSkuDO sku) {
        ProductCommentDO commentDO = convert(createReq);
        if (spu != null) {
            commentDO.setSpuId(spu.getId()).setSpuName(spu.getName());
        }
        if (sku != null) {
            commentDO.setSkuPicUrl(sku.getPicUrl()).setSkuProperties(sku.getProperties());
        }
        return commentDO;
    }

}
