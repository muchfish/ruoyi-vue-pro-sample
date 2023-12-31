package cn.iocoder.yudao.module.product.convert.favorite;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.product.controller.admin.favorite.vo.ProductFavoriteRespVO;
import cn.iocoder.yudao.module.product.dal.dataobject.favorite.ProductFavoriteDO;
import cn.iocoder.yudao.module.product.dal.dataobject.spu.ProductSpuDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;

@Mapper
public interface ProductFavoriteConvert {

    ProductFavoriteConvert INSTANCE = Mappers.getMapper(ProductFavoriteConvert.class);

    default PageResult<ProductFavoriteRespVO> convertPage(PageResult<ProductFavoriteDO> pageResult, List<ProductSpuDO> spuList) {
        Map<Long, ProductSpuDO> spuMap = convertMap(spuList, ProductSpuDO::getId);
        List<ProductFavoriteRespVO> voList = CollectionUtils.convertList(pageResult.getList(), favorite -> {
            ProductSpuDO spu = spuMap.get(favorite.getSpuId());
            return convert02(spu, favorite);
        });
        return new PageResult<>(voList, pageResult.getTotal());
    }
    @Mapping(target = "id", source = "favorite.id")
    @Mapping(target = "userId", source = "favorite.userId")
    @Mapping(target = "spuId", source = "favorite.spuId")
    @Mapping(target = "createTime", source = "favorite.createTime")
    ProductFavoriteRespVO convert02(ProductSpuDO spu, ProductFavoriteDO favorite);

}
