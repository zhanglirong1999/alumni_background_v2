package cn.edu.seu.alumni_background.service;

import cn.edu.seu.alumni_background.error.ServiceException;
import cn.edu.seu.alumni_background.model.dto.verify.PhoneUsernameCodeVerifier;
import cn.edu.seu.alumni_background.model.dto.verify.PhoneUsernameVerifier;
import cn.edu.seu.alumni_background.validation.NotEmpty;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Validated
public interface VerifyService {

    String getRequestIP(@NotEmpty HttpServletRequest request) throws ServiceException;

    void postVerifyCode(
        @NotEmpty HttpServletRequest request,
        @NotEmpty PhoneUsernameVerifier phoneUsernameVerifier
    ) throws ServiceException, TencentCloudSDKException, NoSuchAlgorithmException;

    String buildErrorIPKey(
        @NotEmpty String ip
    );

    String buildPhoneUserVerifyCodeKey(
        @NotEmpty String phone, @NotEmpty String username
    );

    Map.Entry<String, String> buildSuccessfulVerifyCodeEntry(
        @NotEmpty String phone,
        @NotEmpty String username,
        @NotEmpty String verifyCode
    ) throws NoSuchAlgorithmException;

    String postLogin(
        @NotEmpty HttpServletRequest request,
        @NotEmpty PhoneUsernameCodeVerifier verifier
    ) throws NoSuchAlgorithmException, ServiceException;

    boolean isPossibleVerifyCode(String verifyCode);

    String buildTokenKey(
        @NotEmpty String verifyCodeValue
    );

    String getTokenValueInRedis(@NotEmpty String resultToken);

    void deleteTokenKeyInRedis(@NotEmpty String resultToken);
}
