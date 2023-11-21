package cn.iocoder.yudao.module.product.service.comment;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.product.controller.admin.comment.vo.ProductCommentCreateReqVO;
import cn.iocoder.yudao.module.product.controller.admin.comment.vo.ProductCommentPageReqVO;
import cn.iocoder.yudao.module.product.controller.admin.comment.vo.ProductCommentReplyReqVO;
import cn.iocoder.yudao.module.product.controller.admin.comment.vo.ProductCommentUpdateVisibleReqVO;
import cn.iocoder.yudao.module.product.dal.dataobject.comment.ProductCommentDO;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * 商品评论 Service 接口
 *
 * @author wangzhs
 */
@Service
@Validated
public interface ProductCommentService {

    /**
     * 创建商品评论
     * 后台管理员创建评论使用
     *
     * @param createReqVO 商品评价创建 Request VO 对象
     */
    void createComment(ProductCommentCreateReqVO createReqVO);

    /**
     * 修改评论是否可见
     *
     * @param updateReqVO 修改评论可见
     */
    void updateCommentVisible(ProductCommentUpdateVisibleReqVO updateReqVO);

    /**
     * 商家回复
     *
     * @param replyVO     商家回复
     * @param userId 管理后台商家登陆人 ID
     */
    void replyComment(ProductCommentReplyReqVO replyVO, Long userId);

    /**
     * 【管理员】获得商品评价分页
     *
     * @param pageReqVO 分页查询
     * @return 商品评价分页
     */
    PageResult<ProductCommentDO> getCommentPage(ProductCommentPageReqVO pageReqVO);


}
