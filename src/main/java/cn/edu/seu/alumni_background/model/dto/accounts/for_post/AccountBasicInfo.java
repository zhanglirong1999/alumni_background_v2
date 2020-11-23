package cn.edu.seu.alumni_background.model.dto.accounts.for_post;

import cn.edu.seu.alumni_background.model.dao.entity.Account;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountBasicInfo {
    private Long accountId = null;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday = null;
    private String city = null;
    private String selfDesc = null;
    private String email = null;
    private Boolean isVerified = null;

    public Account toEntity() {
        Account account = new Account();
        account.setAccountId(accountId);
        account.setBirthday((birthday == null ? null : birthday.getTime()));
        account.setCity(city);
        account.setSelfDesc(selfDesc);
        account.setEmail(email);
        account.setVerified(isVerified);
        return account;
    }
}
