package cn.iocoder.yudao.module.system.service.permission;

import java.util.Collection;
import java.util.Set;

import static java.util.Collections.singleton;

/**
 * 权限 Service 接口
 * <p>
 * 提供用户-角色、角色-菜单、角色-部门的关联权限处理
 *
 * @author 芋道源码
 */
public interface PermissionService {


    // ========== 角色-菜单的相关方法  ==========

    /**
     * 设置角色菜单
     *
     * @param roleId  角色编号
     * @param menuIds 菜单编号集合
     */
    void assignRoleMenu(Long roleId, Set<Long> menuIds);
    /**
     * 设置用户角色
     *
     * @param userId  角色编号
     * @param roleIds 角色编号集合
     */
    void assignUserRole(Long userId, Set<Long> roleIds);

    /**
     * 获得角色拥有的菜单编号集合
     *
     * @param roleId 角色编号
     * @return 菜单编号集合
     */
    default Set<Long> getRoleMenuListByRoleId(Long roleId) {
        return getRoleMenuListByRoleId(singleton(roleId));
    }

    /**
     * 获得角色们拥有的菜单编号集合
     *
     * @param roleIds 角色编号数组
     * @return 菜单编号集合
     */
    Set<Long> getRoleMenuListByRoleId(Collection<Long> roleIds);

    // ========== 用户-角色的相关方法  ==========
    /**
     * 获得用户拥有的角色编号集合
     *
     * @param userId 用户编号
     * @return 角色编号集合
     */
    Set<Long> getUserRoleIdListByUserId(Long userId);



    // ========== 用户-部门的相关方法  ==========


}
