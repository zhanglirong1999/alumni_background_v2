package cn.edu.seu.alumni_background.config.web;

import cn.edu.seu.alumni_background.model.dto.WebResponse;
import cn.edu.seu.alumni_background.model.dto.WebResponse.WebResponseStatus;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class DefaultExceptionResolverConfigurer {

	@ExceptionHandler(Throwable.class)
	@ResponseBody
	public WebResponse handleException(Throwable e) {
		// 打印异常堆栈信息
		log.error(e.getMessage(), e);
		return new WebResponse(
			WebResponseStatus.DEFAULT_ERROR,
			e.getMessage(),
			Arrays.toString(e.getStackTrace())
		);
	}
}
