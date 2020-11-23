package cn.edu.seu.alumni_background.controller;

import cn.edu.seu.alumni_background.aspect.WebResponseController;
import cn.edu.seu.alumni_background.config.interceptor.TokenRequired;
import cn.edu.seu.alumni_background.error.ServiceException;
import cn.edu.seu.alumni_background.model.dto.accounts.for_post.AccountBasicInfo;
import cn.edu.seu.alumni_background.model.dto.accounts.for_post.AccountEduInfo;
import cn.edu.seu.alumni_background.model.dto.accounts.for_post.AccountJobInfo;
import cn.edu.seu.alumni_background.service.AccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@TokenRequired
@WebResponseController
@RestController
@RequestMapping("/accounts")
public class AccountsController {

    @Autowired
    private AccountsService accountsService;

    @GetMapping("/briefInfos")
    public Object getAccountsBriefInfos(
        @RequestParam Integer pageIndex,
        @RequestParam Integer pageSize,
        @RequestParam(required = false, defaultValue = "1")
            Integer sortedBy,
        @RequestParam(required = false, defaultValue = "0")
            Integer hasVerified
    ) throws ServiceException {
        return accountsService.getAccountsBriefInfos(
            pageIndex, pageSize, sortedBy, hasVerified
        );
    }

    @GetMapping("/hitNumbers")
    public Object getHitNumbers(
        @RequestParam String keyword,
        @RequestParam(required = false, defaultValue = "0")
            Integer hasVerified
    ) {
        return accountsService.getHitNumbers(keyword, hasVerified);
    }

    @GetMapping("/hitAccounts")
    public Object getHitAccounts(
        @RequestParam Integer pageIndex,
        @RequestParam Integer pageSize,
        @RequestParam String keyword,
        @RequestParam(required = false, defaultValue = "false")
            Boolean inName,
        @RequestParam(required = false, defaultValue = "false")
            Boolean inCity,
        @RequestParam(required = false, defaultValue = "false")
            Boolean inSelfDesc,
        @RequestParam(required = false, defaultValue = "false")
            Boolean inSchool,
        @RequestParam(required = false, defaultValue = "false")
            Boolean inCollege,
        @RequestParam(required = false, defaultValue = "false")
            Boolean inCompany,
        @RequestParam(required = false, defaultValue = "false")
            Boolean inPosition,
        @RequestParam(required = false, defaultValue = "0")
            Integer hasVerified,
        @RequestParam(required = false, defaultValue = "1")
            Integer sortedBy
    ) throws ServiceException {
        return accountsService.getHitAccounts(
            pageIndex, pageSize, keyword,
            inName, inCity, inSelfDesc,
            inSchool, inCollege,
            inCompany, inPosition,
            hasVerified, sortedBy
        );
    }

    @GetMapping("/fullInfos")
    public Object getFullInfos(
        @RequestParam Long accountId
    ) throws ServiceException {
        return accountsService.getFullInfos(accountId);
    }

    @PostMapping("/basicInfos")
    public Object postBasicInfos(
        @RequestBody AccountBasicInfo accountBasicInfo
    ) throws ServiceException {
        accountsService.postBasicInfos(accountBasicInfo);
        return "修改基本信息成功!";
    }

    @PostMapping("/educations")
    public Object postEducations(
        @RequestBody AccountEduInfo accountEduInfo
    ) throws ServiceException {
        accountsService.postEducations(accountEduInfo);
        return "修改学历信息成功!";
    }

    @PostMapping("/jobs")
    public Object postJobs(
        @RequestBody AccountJobInfo accountJobInfo
    ) throws ServiceException {
        accountsService.postJobs(accountJobInfo);
        return "修改工作信息成功!";
    }

}
