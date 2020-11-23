package cn.edu.seu.alumni_background.config.interceptor;

import cn.edu.seu.alumni_background.model.dto.WebResponse;
import cn.edu.seu.alumni_background.service.VerifyService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Component
public class TokenInterceptorAdapter extends HandlerInterceptorAdapter {

    public static final String TOKEN_NAME_IN_HEADER = "X-admin-token";

    @Autowired
    private VerifyService verifyService;

    @Override
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler
    ) throws Exception {
        // 首先判断是否方法或者类上有没有验证 token 注解
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        TokenRequired tokenRequired = null;
        tokenRequired = handlerMethod.getMethod().getAnnotation(TokenRequired.class);
        if (tokenRequired == null) {
            tokenRequired = handlerMethod.getBeanType().getAnnotation(TokenRequired.class);
        }

        // 判断怎么处理
        if (tokenRequired == null) {
            return true;
        } else {
            boolean ans = true;
            // 含有 token, 那么就要从 header 中获取到指定的 token-name
            String resultToken, tokenValueInRedis = "";
            if (
                (resultToken = request.getHeader(TOKEN_NAME_IN_HEADER)) == null ||
                    resultToken.equals("")
            ) {
                ans = false;
            } else if (
                (
                    tokenValueInRedis = verifyService.getTokenValueInRedis(resultToken)
                ) == null || tokenValueInRedis.equals("")
            ) {
                ans = false;
                // 删除这个键
                verifyService.deleteTokenKeyInRedis(resultToken);
            }

            // 直接拦截并且配置返回结果
            if (ans) {
                request.setAttribute("adminInfo", tokenValueInRedis);
                return true;
            } else {
                response.reset();

                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/plain;charset=UTF-8");

                PrintWriter pw = response.getWriter();

                pw.write(
                    new Gson().toJson(
                        new WebResponse().failure(
                            WebResponse.WebResponseStatus.LOGIN_ERROR,
                            "请求 token 无效!"
                        )
                    )
                );

                pw.flush();
                pw.close();
                return false;
            }
        }
    }
}
