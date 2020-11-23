package cn.edu.seu.alumni_background.model.dao.mapper;

import cn.edu.seu.alumni_background.model.dao.entity.Account;
import cn.edu.seu.alumni_background.model.dto.accounts.brief_info.AccountBriefInfo;
import cn.edu.seu.alumni_background.model.dto.accounts.full_info.AccountFullInfo;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public interface AccountMapper extends Mapper<Account> {

    List<AccountBriefInfo> getAccountsBriefInfos(Integer sortedBy, Integer hasVerified);

    int getHitName(String keyword, Integer hasVerified);

    int getHitSelfDesc(String keyword, Integer hasVerified);

    int getHitCity(String keyword, Integer hasVerified);

    List<AccountBriefInfo> getHitAccounts(
        String keyword,
        Boolean inName, Boolean inCity, Boolean inSelfDesc,
        Boolean inSchool, Boolean inCollege,
        Boolean inCompany, Boolean inPosition,
        Integer hasVerified, Integer sortedBy
    );

    AccountFullInfo getAccountFullInfo(Long accountId);

    Account getValidAccount(Long accountId);

    Set<String> getAllAvatars();

    Long getTotalSEUerNumber();

    List<Map<String, Object>> getCitiesSEUerNumber();

    List<Map<String, Object>> getAgesSEUerNumber();

    List<Map<String, Object>> getNewcomersNumber();
}