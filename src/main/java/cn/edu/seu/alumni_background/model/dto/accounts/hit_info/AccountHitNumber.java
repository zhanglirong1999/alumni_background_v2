package cn.edu.seu.alumni_background.model.dto.accounts.hit_info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountHitNumber {

    private Integer hitName = 0;
    private Integer hitCity = 0;
    private Integer hitSelfDesc = 0;

    private Integer hitSchool = 0;
    private Integer hitCollege = 0;

    private Integer hitCompany = 0;
    private Integer hitPosition = 0;
}
