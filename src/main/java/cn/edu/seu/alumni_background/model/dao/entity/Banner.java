package cn.edu.seu.alumni_background.model.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Banner {
    private Long bannerId;

    private String img;

    private String link;

    private Integer validStatus;

    private String cTime;

    private String uTime;


}