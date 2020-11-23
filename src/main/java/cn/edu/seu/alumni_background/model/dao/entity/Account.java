package cn.edu.seu.alumni_background.model.dao.entity;

import java.util.Date;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    private Long accountId;

    private String name;

    private Integer gender;

    private Long birthday;

    private String selfDesc;

    private String avatar;

    private String city;

    private String openid;

    private String phone;

    private String wechat;

    private String email;

    private String industry;

    private Boolean type;

    private Boolean step1Finished;

    private Boolean registered;

    private Date cTime;

    private Date uTime;

    private Boolean validStatus;

    private Boolean verified;
}