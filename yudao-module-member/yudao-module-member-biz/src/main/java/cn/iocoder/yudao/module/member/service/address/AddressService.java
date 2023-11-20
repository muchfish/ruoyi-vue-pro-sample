package cn.iocoder.yudao.module.member.service.address;

import cn.iocoder.yudao.module.member.dal.dataobject.address.MemberAddressDO;

import java.util.List;

/**
 * 用户收件地址 Service 接口
 *
 * @author 芋道源码
 */
public interface AddressService {


    /**
     * 获得用户收件地址列表
     *
     * @param userId 用户编号
     * @return 用户收件地址列表
     */
    List<MemberAddressDO> getAddressList(Long userId);


}
