package cn.edu.seu.alumni_background.aspect;

import cn.edu.seu.alumni_background.error.ServiceException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AroundWebServiceMethodAspect {

    @Pointcut("@annotation(cn.edu.seu.alumni_background.aspect.AroundWebServiceMethod)")
    public void pointCut() {}

    @Around("pointCut()")
    public Object doWebServiceMethod(
        ProceedingJoinPoint proceedingJoinPoint
    ) throws Throwable {
        Object ans;
        // 判断是否所有参数都不是 ""
        for (Object arg: proceedingJoinPoint.getArgs()) {
            if (arg != null && arg.equals("")) {
                throw new ServiceException("请求参数含有无效参数!");
            }
        }
        ans = proceedingJoinPoint.proceed();
        return ans;
    }
}
