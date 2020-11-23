package cn.edu.seu.alumni_background.model.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BackgroundAdmin {

    private String phone;

    private String username;

    private Date cTime;

    private Date uTime;
}