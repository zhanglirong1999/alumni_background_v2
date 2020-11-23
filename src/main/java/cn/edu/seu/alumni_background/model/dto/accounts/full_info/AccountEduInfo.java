package cn.edu.seu.alumni_background.model.dto.accounts.full_info;

import cn.edu.seu.alumni_background.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountEduInfo {
    private Long educationId;
    private String school;
    private String college;
    private String education;

    @JsonIgnore
    private Long longStartTime;
    @JsonIgnore
    private Long longEndTime;

    private String startTime;
    private String endTime;

    public void setLongStartTime(Long longStartTime) {
        this.longStartTime = longStartTime;
        this.startTime = DateUtil.toYearString(new Date(longStartTime));
    }

    public void setLongEndTime(Long longEndTime) {
        this.longEndTime = longEndTime;
        this.endTime = DateUtil.toYearString(new Date(longEndTime));
    }
}