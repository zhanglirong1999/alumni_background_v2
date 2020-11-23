package cn.edu.seu.alumni_background.model.dto.accounts.brief_info;

import cn.edu.seu.alumni_background.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountBriefEduInfo {

    private String college;
    private String school;
    private String education;
    private Long startTime;

    public String buildBriefEduInfo() {
        if (college == null || college.equals("") || school == null || school.equals("") || education == null || education.equals("") || startTime == null || startTime.equals(-1L)) {
            return "教育信息不完善";
        } else {
            return String.format(
                "%s%s级%s生",
                school, DateUtil.toYearString(new Date(startTime)),
                education
            );
        }
    }
}
