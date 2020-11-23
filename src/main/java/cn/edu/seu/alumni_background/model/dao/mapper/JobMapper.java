package cn.edu.seu.alumni_background.model.dao.mapper;

import cn.edu.seu.alumni_background.model.dao.entity.Job;
import cn.edu.seu.alumni_background.model.dto.accounts.brief_info.AccountBriefJobInfo;
import cn.edu.seu.alumni_background.model.dto.accounts.full_info.AccountJobInfo;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface JobMapper extends Mapper<Job> {

    List<AccountBriefJobInfo> getAccountsJobBriefInfos(Long accountId);

    int getHitCompany(String keyword, Integer hasVerified);

    int getHitPosition(String keyword, Integer hasVerified);

    List<AccountJobInfo> getAccountFullInfoJobs(Long accountId);

    Job getValidJob(Long jobId);

    List<Map<String, Object>> getIndustriesNumber();

    List<Map<String, Object>> getJobsNumber();
}