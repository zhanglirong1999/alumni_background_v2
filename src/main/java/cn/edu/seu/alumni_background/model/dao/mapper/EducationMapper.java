package cn.edu.seu.alumni_background.model.dao.mapper;

import cn.edu.seu.alumni_background.model.dao.entity.Education;
import cn.edu.seu.alumni_background.model.dto.accounts.brief_info.AccountBriefEduInfo;
import cn.edu.seu.alumni_background.model.dto.accounts.full_info.AccountEduInfo;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface EducationMapper extends Mapper<Education> {

    List<AccountBriefEduInfo> getAccountsEducationBriefInfos(Long accountId);

    int getHitSchool(String keyword, Integer hasVerified);

    int getHitCollege(String keyword, Integer hasVerified);

    List<AccountEduInfo> getAccountFullInfoEducations(Long accountId);

    Education getValidEducation(Long educationId);

    List<Map<String, Object>> getCollegesSEUerNumber();

    List<Map<String, Object>> getGraduateSEUerNumber();
}