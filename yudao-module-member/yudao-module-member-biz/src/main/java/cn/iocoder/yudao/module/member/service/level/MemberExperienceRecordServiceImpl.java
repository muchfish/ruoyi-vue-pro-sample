package cn.iocoder.yudao.module.member.service.level;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.module.member.convert.level.MemberExperienceRecordConvert;
import cn.iocoder.yudao.module.member.dal.dataobject.level.MemberExperienceRecordDO;
import cn.iocoder.yudao.module.member.dal.mysql.level.MemberExperienceRecordMapper;
import cn.iocoder.yudao.module.member.enums.MemberExperienceBizTypeEnum;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * 会员经验记录 Service 实现类
 *
 * @author owen
 */
@Service
@Validated
public class MemberExperienceRecordServiceImpl implements MemberExperienceRecordService {

    @Resource
    private MemberExperienceRecordMapper experienceLogMapper;

    @Override
    public void createExperienceRecord(Long userId, Integer experience, Integer totalExperience,
                                       MemberExperienceBizTypeEnum bizType, String bizId) {
        String description = StrUtil.format(bizType.getDescription(), experience);
        MemberExperienceRecordDO record = MemberExperienceRecordConvert.INSTANCE.convert(
                userId, experience, totalExperience,
                bizId, bizType.getType(), bizType.getTitle(), description);
        experienceLogMapper.insert(record);
    }

}
