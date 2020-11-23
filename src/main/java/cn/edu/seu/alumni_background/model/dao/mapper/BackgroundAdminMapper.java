package cn.edu.seu.alumni_background.model.dao.mapper;

import cn.edu.seu.alumni_background.model.dao.entity.BackgroundAdmin;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface BackgroundAdminMapper extends Mapper<BackgroundAdmin> {

    BackgroundAdmin getOneAdminByPhone(String phone);
}