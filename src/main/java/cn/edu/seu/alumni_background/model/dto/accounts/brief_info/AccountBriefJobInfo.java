package cn.edu.seu.alumni_background.model.dto.accounts.brief_info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountBriefJobInfo {
    private String company = "";
    private String position = "";
}
