package cn.edu.seu.alumni_background.model.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Education {
    @Id
    private Long educationId;

    private Long accountId;

    private String education;

    private String school;

    private String college;

    private Long startTime;

    private Long endTime;

    private Date cTime;

    private Date uTime;

    private Boolean validStatus;
}