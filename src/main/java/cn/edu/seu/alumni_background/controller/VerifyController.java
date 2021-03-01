package cn.edu.seu.alumni_background.controller;

import cn.edu.seu.alumni_background.aspect.WebResponseController;
import cn.edu.seu.alumni_background.error.ServiceException;
import cn.edu.seu.alumni_background.log.annotation.LogController;
import cn.edu.seu.alumni_background.model.dto.verify.PhoneUsernameCodeVerifier;
import cn.edu.seu.alumni_background.model.dto.verify.PhoneUsernameVerifier;
import cn.edu.seu.alumni_background.service.VerifyService;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;

@LogController
@RestController
@RequestMapping("/verify")
@WebResponseController
public class VerifyController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private VerifyService verifyService;

    @PostMapping("/code")
    public Object postVerifyCode(
        @RequestBody PhoneUsernameVerifier phoneUsernameVerifier
    ) throws ServiceException, TencentCloudSDKException, NoSuchAlgorithmException {
        verifyService.postVerifyCode(request, phoneUsernameVerifier);
        return "验证码已发送!";
    }

    @PostMapping("/login")
    public Object postLogin(
        @RequestBody PhoneUsernameCodeVerifier verifier
    ) throws ServiceException, NoSuchAlgorithmException {
        return verifyService.postLogin(request, verifier);
    }
}
