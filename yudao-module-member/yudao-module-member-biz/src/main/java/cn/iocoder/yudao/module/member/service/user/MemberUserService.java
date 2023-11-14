package cn.iocoder.yudao.module.member.service.user;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.member.controller.admin.user.vo.MemberUserPageReqVO;
import cn.iocoder.yudao.module.member.controller.admin.user.vo.MemberUserUpdateReqVO;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;

import javax.validation.Valid;

/**
 * 会员用户 Service 接口
 *
 * @author 芋道源码
 */
public interface MemberUserService {


    /**
     * 通过用户 ID 查询用户
     *
     * @param id 用户ID
     * @return 用户对象信息
     */
    MemberUserDO getUser(Long id);


    /**
     * 【管理员】更新会员用户
     *
     * @param updateReqVO 更新信息
     */
    void updateUser(@Valid MemberUserUpdateReqVO updateReqVO);

    /**
     * 【管理员】获得会员用户分页
     *
     * @param pageReqVO 分页查询
     * @return 会员用户分页
     */
    PageResult<MemberUserDO> getUserPage(MemberUserPageReqVO pageReqVO);


}
