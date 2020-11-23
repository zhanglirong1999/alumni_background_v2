package cn.edu.seu.alumni_background.service.impl;

import cn.edu.seu.alumni_background.aspect.WebResponseMethod;
import cn.edu.seu.alumni_background.model.dao.entity.BackgroundAdmin;
import cn.edu.seu.alumni_background.model.dao.mapper.BackgroundAdminMapper;
import cn.edu.seu.alumni_background.model.dto.WebResponse;
import cn.edu.seu.alumni_background.service.TestService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {

	@Autowired
	private BackgroundAdminMapper backgroundAdminMapper;

	@Override
	public List<BackgroundAdmin> ping() {
		return backgroundAdminMapper.selectAll();
	}
}
