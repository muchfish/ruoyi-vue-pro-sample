package cn.iocoder.yudao.module.system.service.user;

import cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO;
import cn.iocoder.yudao.module.system.dal.mysql.user.AdminUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 后台用户 Service 实现类
 *
 * @author 芋道源码
 */
@Service("adminUserService")
@Slf4j
public class AdminUserServiceImpl implements AdminUserService {



    @Resource
    private AdminUserMapper userMapper;
    @Resource
    private PasswordEncoder passwordEncoder;


    @Override
    public AdminUserDO getUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public List<AdminUserDO> getUserListByStatus(Integer status) {
        return userMapper.selectListByStatus(status);
    }

    @Override
    public boolean isPasswordMatch(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword,encodedPassword);
    }


}
