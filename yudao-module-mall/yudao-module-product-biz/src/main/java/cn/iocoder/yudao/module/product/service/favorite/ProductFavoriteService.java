package cn.iocoder.yudao.module.product.service.favorite;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.product.controller.admin.favorite.vo.ProductFavoritePageReqVO;
import cn.iocoder.yudao.module.product.dal.dataobject.favorite.ProductFavoriteDO;

import javax.validation.Valid;

/**
 * 商品收藏 Service 接口
 *
 * @author jason
 */
public interface ProductFavoriteService {

    /**
     * 分页查询用户收藏列表
     *
     * @param reqVO 请求 vo
     */
    PageResult<ProductFavoriteDO> getFavoritePage(@Valid ProductFavoritePageReqVO reqVO);


}
