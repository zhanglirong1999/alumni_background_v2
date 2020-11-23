package cn.edu.seu.alumni_background.controller;

import cn.edu.seu.alumni_background.aspect.WebResponseMethod;
import cn.edu.seu.alumni_background.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@Autowired
	TestService testService;

	@GetMapping("/ping")
	@WebResponseMethod
	public Object ping() {
		return testService.ping();
	}
}
