package cn.iocoder.yudao.module.product.service.sku;

import cn.iocoder.yudao.module.product.controller.admin.sku.vo.ProductSkuCreateOrUpdateReqVO;
import cn.iocoder.yudao.module.product.dal.dataobject.sku.ProductSkuDO;

import java.util.Collection;
import java.util.List;

/**
 * 商品 SKU Service 接口
 *
 * @author 芋道源码
 */
public interface ProductSkuService {

    /**
     * 获得商品 SKU 信息
     *
     * @param id 编号
     * @return 商品 SKU 信息
     */
    ProductSkuDO getSku(Long id);

    /**
     * 对 sku 的组合的属性等进行合法性校验
     *
     * @param list sku组合的集合
     */
    void validateSkuList(List<ProductSkuCreateOrUpdateReqVO> list, Boolean specType);

    /**
     * 批量创建 SKU
     *
     * @param spuId 商品 SPU 编号
     * @param list  SKU 对象集合
     */
    void createSkuList(Long spuId, List<ProductSkuCreateOrUpdateReqVO> list);

    /**
     * 根据 SPU 编号，批量更新它的 SKU 信息
     *
     * @param spuId SPU 编码
     * @param skus  SKU 的集合
     */
    void updateSkuList(Long spuId, List<ProductSkuCreateOrUpdateReqVO> skus);


    /**
     * 获得商品 SKU 集合
     *
     * @param spuId spu 编号
     * @return 商品sku 集合
     */
    List<ProductSkuDO> getSkuListBySpuId(Long spuId);

    /**
     * 获得 spu 对应的 SKU 集合
     *
     * @param spuIds spu 编码集合
     * @return 商品 sku 集合
     */
    List<ProductSkuDO> getSkuListBySpuId(Collection<Long> spuIds);

    /**
     * 通过 spuId 删除 sku 信息
     *
     * @param spuId spu 编码
     */
    void deleteSkuBySpuId(Long spuId);


    /**
     * 更新 sku 属性
     *
     * @param propertyId   属性 id
     * @param propertyName 属性名
     * @return int 影响的行数
     */
    int updateSkuProperty(Long propertyId, String propertyName);

    /**
     * 更新 sku 属性值
     *
     * @param propertyValueId   属性值 id
     * @param propertyValueName 属性值名字
     * @return int 影响的行数
     */
    int updateSkuPropertyValue(Long propertyValueId, String propertyValueName);

}
