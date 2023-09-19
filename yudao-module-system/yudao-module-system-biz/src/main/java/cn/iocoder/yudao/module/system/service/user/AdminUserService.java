package cn.iocoder.yudao.module.system.service.user;

import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;

import java.util.List;

/**
 * 后台用户 Service 接口
 *
 * @author 芋道源码
 */
public interface AdminUserService {



    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * @return 用户对象信息
     */
    AdminUserDO getUserByUsername(String username);


    /**
     * 获得指定状态的用户们
     *
     * @param status 状态
     * @return 用户们
     */
    List<AdminUserDO> getUserListByStatus(Integer status);

    /**
     * 判断密码是否匹配
     *
     * @param rawPassword 未加密的密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    boolean isPasswordMatch(String rawPassword, String encodedPassword);

}
