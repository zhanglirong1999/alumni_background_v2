package cn.edu.seu.alumni_background.model.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Demand {
    @Id
    private Long demandId;

    private String demandName;

    private Long accountId;

    private Integer type;

    private String tag1;

    private String tag2;

    private String tag3;

    private String tag4;

    private String tag5;

    private String details;

    private String img1;

    private String img2;

    private String img3;

    private String img4;

    private String img5;

    private String img6;

    private Boolean validStatus;

    private Date cTime;

    private Date uTime;
}