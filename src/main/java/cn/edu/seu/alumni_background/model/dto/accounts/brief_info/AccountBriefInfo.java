package cn.edu.seu.alumni_background.model.dto.accounts.brief_info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountBriefInfo {

    private Long accountId;
    private String avatar;
    private String name;
    private String selfDesc;
    private String college = null;
    private String eduInfo = null;
    private String city;
    private String company = null;
    private String position = null;
    private Boolean isVerified;

    public void fillEduInfo(AccountBriefEduInfo _eduInfo) {
        college = _eduInfo.getCollege();
        eduInfo = _eduInfo.buildBriefEduInfo();
    }

    public void fillJobInfo(AccountBriefJobInfo _jobInfo) {
        company = _jobInfo.getCompany();
        position = _jobInfo.getPosition();
    }
}
