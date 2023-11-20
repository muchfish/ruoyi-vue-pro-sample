package cn.iocoder.yudao.module.member.convert.level;

import cn.iocoder.yudao.module.member.dal.dataobject.level.MemberExperienceRecordDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 会员经验记录 Convert
 *
 * @author owen
 */
@Mapper
public interface MemberExperienceRecordConvert {

    MemberExperienceRecordConvert INSTANCE = Mappers.getMapper(MemberExperienceRecordConvert.class);

    MemberExperienceRecordDO convert(Long userId, Integer experience, Integer totalExperience,
                                     String bizId, Integer bizType,
                                     String title, String description);


}
