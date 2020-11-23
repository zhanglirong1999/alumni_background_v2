package cn.edu.seu.alumni_background.model.dto.accounts.full_info;

import cn.edu.seu.alumni_background.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountFullInfo {

    private Long accountId;
    private String avatar;
    private String name;
    private Boolean isVerified;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date cTime;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date uTime;
    @JsonIgnore
    private Long longBirthday;
    private String birthday;
    private String city;
    private String selfDesc;
    private String email;

    private List<AccountEduInfo> educations = null;
    private List<AccountJobInfo> jobs = null;

    public void setLongBirthday(Long longBirthday) {
        this.longBirthday = longBirthday;
        this.birthday = DateUtil.toDateString(new Date(longBirthday));
    }

}
