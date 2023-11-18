package cn.iocoder.yudao.module.member.service.level;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.module.member.controller.admin.level.vo.level.MemberLevelCreateReqVO;
import cn.iocoder.yudao.module.member.controller.admin.level.vo.level.MemberLevelListReqVO;
import cn.iocoder.yudao.module.member.controller.admin.level.vo.level.MemberLevelUpdateReqVO;
import cn.iocoder.yudao.module.member.convert.level.MemberLevelConvert;
import cn.iocoder.yudao.module.member.dal.dataobject.level.MemberLevelDO;
import cn.iocoder.yudao.module.member.dal.mysql.level.MemberLevelMapper;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;

/**
 * 会员等级 Service 实现类
 *
 * @author owen
 */
@Slf4j
@Service
@Validated
public class MemberLevelServiceImpl implements MemberLevelService {

    @Resource
    private MemberLevelMapper memberLevelMapper;

    @Resource
    private MemberUserService memberUserService;

    @Override
    public Long createLevel(MemberLevelCreateReqVO createReqVO) {
        // 校验配置是否有效
        validateConfigValid(null, createReqVO.getName(), createReqVO.getLevel(), createReqVO.getExperience());

        // 插入
        MemberLevelDO level = MemberLevelConvert.INSTANCE.convert(createReqVO);
        memberLevelMapper.insert(level);
        // 返回
        return level.getId();
    }

    @Override
    public void updateLevel(MemberLevelUpdateReqVO updateReqVO) {
        // 校验存在
        validateLevelExists(updateReqVO.getId());
        // 校验配置是否有效
        validateConfigValid(updateReqVO.getId(), updateReqVO.getName(), updateReqVO.getLevel(), updateReqVO.getExperience());

        // 更新
        MemberLevelDO updateObj = MemberLevelConvert.INSTANCE.convert(updateReqVO);
        memberLevelMapper.updateById(updateObj);
    }

    @Override
    public void deleteLevel(Long id) {
        // 校验存在
        validateLevelExists(id);
        // 校验分组下是否有用户
        validateLevelHasUser(id);
        // 删除
        memberLevelMapper.deleteById(id);
    }

    @VisibleForTesting
    MemberLevelDO validateLevelExists(Long id) {
        MemberLevelDO levelDO = memberLevelMapper.selectById(id);
        if (levelDO == null) {
            throw exception(LEVEL_NOT_EXISTS);
        }
        return levelDO;
    }

    @VisibleForTesting
    void validateNameUnique(List<MemberLevelDO> list, Long id, String name) {
        for (MemberLevelDO levelDO : list) {
            if (ObjUtil.notEqual(levelDO.getName(), name)) {
                continue;
            }
            if (id == null || !id.equals(levelDO.getId())) {
                throw exception(LEVEL_NAME_EXISTS, levelDO.getName());
            }
        }
    }

    @VisibleForTesting
    void validateLevelUnique(List<MemberLevelDO> list, Long id, Integer level) {
        for (MemberLevelDO levelDO : list) {
            if (ObjUtil.notEqual(levelDO.getLevel(), level)) {
                continue;
            }

            if (id == null || !id.equals(levelDO.getId())) {
                throw exception(LEVEL_VALUE_EXISTS, levelDO.getLevel(), levelDO.getName());
            }
        }
    }

    @VisibleForTesting
    void validateExperienceOutRange(List<MemberLevelDO> list, Long id, Integer level, Integer experience) {
        for (MemberLevelDO levelDO : list) {
            if (levelDO.getId().equals(id)) {
                continue;
            }

            if (levelDO.getLevel() < level) {
                // 经验大于前一个等级
                if (experience <= levelDO.getExperience()) {
                    throw exception(LEVEL_EXPERIENCE_MIN, levelDO.getName(), levelDO.getExperience());
                }
            } else if (levelDO.getLevel() > level) {
                //小于下一个级别
                if (experience >= levelDO.getExperience()) {
                    throw exception(LEVEL_EXPERIENCE_MAX, levelDO.getName(), levelDO.getExperience());
                }
            }
        }
    }

    @VisibleForTesting
    void validateConfigValid(Long id, String name, Integer level, Integer experience) {
        List<MemberLevelDO> list = memberLevelMapper.selectList();
        // 校验名称唯一
        validateNameUnique(list, id, name);
        // 校验等级唯一
        validateLevelUnique(list, id, level);
        // 校验升级所需经验是否有效: 大于前一个等级，小于下一个级别
        validateExperienceOutRange(list, id, level, experience);
    }

    @VisibleForTesting
    void validateLevelHasUser(Long id) {
        Long count = memberUserService.getUserCountByLevelId(id);
        if (count > 0) {
            throw exception(LEVEL_HAS_USER);
        }
    }

    @Override
    public MemberLevelDO getLevel(Long id) {
        return id != null && id > 0 ? memberLevelMapper.selectById(id) : null;
    }

    @Override
    public List<MemberLevelDO> getLevelList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return memberLevelMapper.selectBatchIds(ids);
    }

    @Override
    public List<MemberLevelDO> getLevelList(MemberLevelListReqVO listReqVO) {
        return memberLevelMapper.selectList(listReqVO);
    }

    @Override
    public List<MemberLevelDO> getLevelListByStatus(Integer status) {
        return memberLevelMapper.selectListByStatus(status);
    }


}
