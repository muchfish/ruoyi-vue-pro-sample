package cn.iocoder.yudao.module.member.service.address;

import cn.iocoder.yudao.module.member.dal.dataobject.address.MemberAddressDO;
import cn.iocoder.yudao.module.member.dal.mysql.address.MemberAddressMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户收件地址 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class AddressServiceImpl implements AddressService {

    @Resource
    private MemberAddressMapper memberAddressMapper;


    @Override
    public List<MemberAddressDO> getAddressList(Long userId) {
        return memberAddressMapper.selectListByUserIdAndDefaulted(userId, null);
    }


}
