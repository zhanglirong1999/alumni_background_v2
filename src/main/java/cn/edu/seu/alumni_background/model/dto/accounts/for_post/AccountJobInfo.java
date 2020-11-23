package cn.edu.seu.alumni_background.model.dto.accounts.for_post;

import cn.edu.seu.alumni_background.model.dao.entity.Job;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountJobInfo {
    private Long jobId = null;
    private String company = null;
    private String position = null;

    public Job toEntity() {
        Job job = new Job();
        BeanUtils.copyProperties(this, job);
        return job;
    }
}
