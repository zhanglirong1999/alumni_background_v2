package cn.edu.seu.alumni_background.service.impl;

import cn.edu.seu.alumni_background.aspect.AroundWebServiceMethod;
import cn.edu.seu.alumni_background.error.ServiceException;
import cn.edu.seu.alumni_background.model.dao.entity.Account;
import cn.edu.seu.alumni_background.model.dao.entity.Education;
import cn.edu.seu.alumni_background.model.dao.entity.Job;
import cn.edu.seu.alumni_background.model.dao.mapper.AccountMapper;
import cn.edu.seu.alumni_background.model.dao.mapper.EducationMapper;
import cn.edu.seu.alumni_background.model.dao.mapper.JobMapper;
import cn.edu.seu.alumni_background.model.dto.PageResult;
import cn.edu.seu.alumni_background.model.dto.accounts.brief_info.AccountBriefEduInfo;
import cn.edu.seu.alumni_background.model.dto.accounts.brief_info.AccountBriefInfo;
import cn.edu.seu.alumni_background.model.dto.accounts.brief_info.AccountBriefJobInfo;
import cn.edu.seu.alumni_background.model.dto.accounts.for_post.AccountBasicInfo;
import cn.edu.seu.alumni_background.model.dto.accounts.for_post.AccountEduInfo;
import cn.edu.seu.alumni_background.model.dto.accounts.full_info.AccountFullInfo;
import cn.edu.seu.alumni_background.model.dto.accounts.full_info.AccountJobInfo;
import cn.edu.seu.alumni_background.model.dto.accounts.hit_info.AccountHitNumber;
import cn.edu.seu.alumni_background.service.AccountsService;
import cn.edu.seu.alumni_background.validation.NotEmpty;
import com.github.pagehelper.PageHelper;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

@Validated
@Service
public class AccountsServiceImpl implements AccountsService {

    @Autowired
    EducationMapper educationMapper;

    @Autowired
    JobMapper jobMapper;

    @Autowired
    AccountMapper accountMapper;

    @Override
    @AroundWebServiceMethod
    @Transactional(propagation = Propagation.REQUIRED)
    public PageResult getAccountsBriefInfos(
        @Range(min = 0) Integer pageIndex,
        @Range(min = 0) Integer pageSize,
        @Range(min = -1, max = 1) Integer sortedBy,
        @Range(min = -1, max = 1) Integer hasVerified
    ) throws ServiceException {
        PageHelper.startPage(pageIndex, pageSize);
        List<AccountBriefInfo> accountsBriefInfos =
            accountMapper.getAccountsBriefInfos(sortedBy, hasVerified);
        fillAccountsBriefEducationAndJobInfo(accountsBriefInfos);
        return new PageResult().wrapForList(accountsBriefInfos);
    }

    @Override
    public void fillAccountsBriefEducationAndJobInfo(
        List<AccountBriefInfo> accountsBriefInfos
    ) {
        for (AccountBriefInfo accountBriefInfo : accountsBriefInfos) {
            // 填充教育信息
            accountBriefInfo.fillEduInfo(
                getSEUAccountBriefEduInfosByAccountId(
                    accountBriefInfo.getAccountId()
                )
            );
            // 填充工作信息
            accountBriefInfo.fillJobInfo(
                getLatestAccountBriefJobInfosByAccountId(
                    accountBriefInfo.getAccountId()
                )
            );
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public AccountBriefEduInfo getSEUAccountBriefEduInfosByAccountId(
        @NotEmpty Long accountId
    ) {
        List<AccountBriefEduInfo> accountsEducationBriefInfos =
            educationMapper.getAccountsEducationBriefInfos(accountId);
        for (AccountBriefEduInfo info : accountsEducationBriefInfos) {
            if (info.getSchool().equals("东南大学")) {
                return info;
            }
        }
        return (
            accountsEducationBriefInfos.size() > 1 ?
                accountsEducationBriefInfos.get(0) :
                new AccountBriefEduInfo(null, null, null, -1L)
        );
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public AccountBriefJobInfo getLatestAccountBriefJobInfosByAccountId(
        @NotEmpty Long accountId
    ) {
        List<AccountBriefJobInfo> infos =
            jobMapper.getAccountsJobBriefInfos(accountId);
        if (infos.size() >= 1) {
            return infos.get(0);
        } else {
            return new AccountBriefJobInfo(null, null);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public AccountHitNumber getHitNumbers(
        @NotEmpty String keyword,
        @Range(min = -1, max = 1) Integer hasVerified
    ) {
        int hitName, hitSelfDesc, hitCity;
        hitName = accountMapper.getHitName(keyword, hasVerified);
        hitSelfDesc = accountMapper.getHitSelfDesc(keyword, hasVerified);
        hitCity = accountMapper.getHitCity(keyword, hasVerified);
        int hitCompany, hitPosition;
        hitCompany = jobMapper.getHitCompany(keyword, hasVerified);
        hitPosition = jobMapper.getHitPosition(keyword, hasVerified);
        int hitSchool, hitCollege;
        hitSchool = educationMapper.getHitSchool(keyword, hasVerified);
        hitCollege = educationMapper.getHitCollege(keyword, hasVerified);
        return new AccountHitNumber(
            hitName, hitCity, hitSelfDesc,
            hitSchool, hitCollege,
            hitCompany, hitPosition
        );
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PageResult getHitAccounts(
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
    ) throws ServiceException {
        // 先验判断
        boolean hasIn = (
            inName | inSelfDesc | inCity |
                inSchool | inCollege |
                inCompany | inPosition
        );
        // 如果一个都不选, 那么就走无关键字查询
        if (!hasIn) {
            return getAccountsBriefInfos(
                pageIndex, pageSize, sortedBy, hasVerified
            );
        }
        // 至少有一个关键字
        PageHelper.startPage(pageIndex, pageSize);
        List<AccountBriefInfo> hitAccounts = accountMapper.getHitAccounts(
            keyword,
            inName, inCity, inSelfDesc,
            inSchool, inCollege,
            inCompany, inPosition,
            hasVerified, sortedBy
        );
        fillAccountsBriefEducationAndJobInfo(hitAccounts);
        return new PageResult().wrapForList(hitAccounts);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public AccountFullInfo getFullInfos(
        @NotEmpty Long accountId
    ) throws ServiceException {
        // 首先判断当前 accountId 是有效存在的
        if (!hasValidAccount(accountId)) {
            throw new ServiceException("当前查询账户 id: " + accountId + " 不存在!");
        }
        AccountFullInfo accountFullInfo =
            accountMapper.getAccountFullInfo(accountId);
        accountFullInfo.setEducations(
            educationMapper.getAccountFullInfoEducations(accountId)
        );
        List<AccountJobInfo> temp =
            jobMapper.getAccountFullInfoJobs(accountId);
        temp = new LinkedList<>(new HashSet<>(temp));
        accountFullInfo.setJobs(
            temp
        );
        return accountFullInfo;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Boolean hasValidAccount(@NotEmpty Long accountId) {
        Account account = accountMapper.getValidAccount(accountId);
        return account != null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Boolean hasValidEducation(@NotEmpty Long educationId) {
        Education education = educationMapper.getValidEducation(educationId);
        return education != null;
    }

    @Override
    public Boolean hasValidJob(Long jobId) {
        Job job = jobMapper.getValidJob(jobId);
        return job != null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void postBasicInfos(@NotEmpty AccountBasicInfo accountBasicInfo) throws ServiceException {
        if (!hasValidAccount(accountBasicInfo.getAccountId())) {
            throw new ServiceException("当前修改账户 id: " + accountBasicInfo.getAccountId() + " 不存在!");
        }
        Account account = accountBasicInfo.toEntity();
        accountMapper.updateByPrimaryKeySelective(account);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void postEducations(@NotEmpty AccountEduInfo accountEduInfo) throws ServiceException {
        if (!hasValidEducation(accountEduInfo.getEducationId())) {
            throw new ServiceException("当前修改教育 id: " + accountEduInfo.getEducationId() + " 不存在!");
        }
        Education education = accountEduInfo.toEntity();
        educationMapper.updateByPrimaryKeySelective(education);
    }

    @Override
    public void postJobs(
        cn.edu.seu.alumni_background.model.dto.accounts.for_post.AccountJobInfo
            accountJobInfo
    ) throws ServiceException {
        if (!hasValidJob(accountJobInfo.getJobId())) {
            throw new ServiceException("当前修改职位 id: " + accountJobInfo.getJobId() + " 不存在!");
        }
        Job job = accountJobInfo.toEntity();
        jobMapper.updateByPrimaryKeySelective(job);
    }
}
