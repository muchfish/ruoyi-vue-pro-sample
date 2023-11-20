package cn.iocoder.yudao.module.member.service.level;

import cn.iocoder.yudao.module.member.enums.MemberExperienceBizTypeEnum;

/**
 * 会员经验记录 Service 接口
 *
 * @author owen
 */
public interface MemberExperienceRecordService {


    /**
     * 根据业务类型, 创建 经验变动记录
     *
     * @param userId          会员编号
     * @param experience      变动经验值
     * @param totalExperience 会员当前的经验
     * @param bizType         业务类型
     * @param bizId           业务ID
     */
    void createExperienceRecord(Long userId, Integer experience, Integer totalExperience,
                                MemberExperienceBizTypeEnum bizType, String bizId);

}
