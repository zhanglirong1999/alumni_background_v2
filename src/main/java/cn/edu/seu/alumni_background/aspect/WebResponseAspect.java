package cn.edu.seu.alumni_background.aspect;

import cn.edu.seu.alumni_background.model.dto.WebResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
public class WebResponseAspect {

	private Logger logger = LoggerFactory.getLogger(WebResponseAspect.class);

	@Pointcut("@annotation(cn.edu.seu.alumni_background.aspect.WebResponseMethod)")
	public void methodAPI() {
	}

	@Pointcut("@within(cn.edu.seu.alumni_background.aspect.WebResponseController)")
	public void typeAPI() {
	}

	@Around("methodAPI()")
	public Object wrapResultForMethod(ProceedingJoinPoint proceedingJoinPoint) {
		Object ans;
		try {
			ans = proceedingJoinPoint.proceed();
		} catch (Throwable throwable) {
			ans = new WebResponse.FailureMessage(throwable.getMessage());
		}
		return WebResponse.wrap(ans);
	}

	@Around("typeAPI()")
	public Object wrapResultForController(ProceedingJoinPoint proceedingJoinPoint) {
		Object ans;
		try {
			ans = proceedingJoinPoint.proceed();
		} catch (Throwable throwable) {
			ans = new WebResponse.FailureMessage(throwable.getMessage());
		}
		return WebResponse.wrap(ans);
	}
}
