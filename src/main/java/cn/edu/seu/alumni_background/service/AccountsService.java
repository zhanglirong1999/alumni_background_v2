package cn.edu.seu.alumni_background.service;

import cn.edu.seu.alumni_background.aspect.AroundWebServiceMethod;
import cn.edu.seu.alumni_background.error.ServiceException;
import cn.edu.seu.alumni_background.model.dto.PageResult;
import cn.edu.seu.alumni_background.model.dto.accounts.for_post.AccountBasicInfo;
import cn.edu.seu.alumni_background.model.dto.accounts.brief_info.AccountBriefEduInfo;
import cn.edu.seu.alumni_background.model.dto.accounts.brief_info.AccountBriefInfo;
import cn.edu.seu.alumni_background.model.dto.accounts.brief_info.AccountBriefJobInfo;
import cn.edu.seu.alumni_background.model.dto.accounts.for_post.AccountEduInfo;
import cn.edu.seu.alumni_background.model.dto.accounts.for_post.AccountJobInfo;
import cn.edu.seu.alumni_background.model.dto.accounts.full_info.AccountFullInfo;
import cn.edu.seu.alumni_background.model.dto.accounts.hit_info.AccountHitNumber;
import cn.edu.seu.alumni_background.validation.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface AccountsService {

    @Transactional(propagation = Propagation.REQUIRED)
    @AroundWebServiceMethod
    PageResult getAccountsBriefInfos(
        @Range(min = 0) Integer pageIndex,
        @Range(min = 0) Integer pageSize,
        @Range(min = -1, max = 1) Integer sortedBy,
        @Range(min = -1, max = 1) Integer hasVerified
    ) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED)
    AccountBriefEduInfo getSEUAccountBriefEduInfosByAccountId(
        @NotEmpty Long accountId
    );

    @Transactional(propagation = Propagation.REQUIRED)
    AccountBriefJobInfo getLatestAccountBriefJobInfosByAccountId(
        @NotEmpty Long accountId
    );

    @Transactional(propagation = Propagation.REQUIRED)
    AccountHitNumber getHitNumbers(
        @NotEmpty String keyword,
        @Range(min = -1, max = 1) Integer hasVerified
    );

    void fillAccountsBriefEducationAndJobInfo(
        List<AccountBriefInfo> accountsBriefInfos
    );

    @Transactional(propagation = Propagation.REQUIRED)
    PageResult getHitAccounts(
        @Range(min = 0) Integer pageIndex,
        @Range(min = 0) Integer pageSize,
        @NotEmpty String keyword,
        @NotEmpty Boolean inName,
        @NotEmpty Boolean inCity,
        @NotEmpty Boolean inSelfDesc,
        @NotEmpty Boolean inSchool,
        @NotEmpty Boolean inCollege,
        @NotEmpty Boolean inCompany,
        @NotEmpty Boolean inPosition,
        @Range(min = -1, max = 1) Integer hasVerified,
        @Range(min = -1, max = 1) Integer sortedBy
    ) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED)
    AccountFullInfo getFullInfos(@NotEmpty Long accountId) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED)
    Boolean hasValidAccount(@NotEmpty Long accountId);

    @Transactional(propagation = Propagation.REQUIRED)
    Boolean hasValidEducation(@NotEmpty Long educationId);

    @Transactional(propagation = Propagation.REQUIRED)
    Boolean hasValidJob(@NotEmpty Long jobId);

    @Transactional(propagation = Propagation.REQUIRED)
    void postBasicInfos(@NotEmpty AccountBasicInfo accountBasicInfo) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED)
    void postEducations(@NotEmpty AccountEduInfo accountEduInfo) throws ServiceException;

    @Transactional(propagation = Propagation.REQUIRED)
    void postJobs(@NotEmpty AccountJobInfo accountJobInfo) throws ServiceException;
}
