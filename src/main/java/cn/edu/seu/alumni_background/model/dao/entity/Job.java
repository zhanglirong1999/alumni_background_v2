package cn.edu.seu.alumni_background.model.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Job {
    @Id
    private Long jobId;

    private Long accountId;

    private String company;

    private String position;

    private Long startTime;

    private Long endTime;

    private Date cTime;

    private Date uTime;

    private Boolean validStatus;
}