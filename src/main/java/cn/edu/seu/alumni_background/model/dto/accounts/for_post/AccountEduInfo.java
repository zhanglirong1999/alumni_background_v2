package cn.edu.seu.alumni_background.model.dto.accounts.for_post;

import cn.edu.seu.alumni_background.model.dao.entity.Education;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountEduInfo {

    private Long educationId = null;
    private String school = null;
    private String college = null;
    private String education = null;
    @JsonFormat(pattern = "yyyy")
    private Date startTime = null;
    @JsonFormat(pattern = "yyyy")
    private Date endTime = null;

    public Education toEntity() {
        Education education = new Education();
        education.setEducationId(educationId);
        education.setSchool(school);
        education.setCollege(college);
        education.setStartTime((startTime == null ? null : startTime.getTime()));
        education.setEndTime((endTime == null ? null : endTime.getTime()));
        return education;
    }

}
