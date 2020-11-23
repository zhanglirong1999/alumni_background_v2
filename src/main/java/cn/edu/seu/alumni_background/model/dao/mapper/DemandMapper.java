package cn.edu.seu.alumni_background.model.dao.mapper;

import cn.edu.seu.alumni_background.model.dao.entity.Demand;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface DemandMapper extends Mapper<Demand> {

    List<Map<String, Object>> getDemandsNumber();
}