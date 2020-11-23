package cn.edu.seu.alumni_background.service.impl;

import cn.edu.seu.alumni_background.component.redis.StringToObjectRedisStringManager;
import cn.edu.seu.alumni_background.component.sms.AbstractSMSManager;
import cn.edu.seu.alumni_background.component.sms.SMSManagerResult;
import cn.edu.seu.alumni_background.error.ServiceException;
import cn.edu.seu.alumni_background.model.dao.entity.BackgroundAdmin;
import cn.edu.seu.alumni_background.model.dao.mapper.BackgroundAdminMapper;
import cn.edu.seu.alumni_background.model.dto.verify.PhoneUsernameCodeVerifier;
import cn.edu.seu.alumni_background.model.dto.verify.PhoneUsernameVerifier;
import cn.edu.seu.alumni_background.service.VerifyService;
import cn.edu.seu.alumni_background.util.IDUtil;
import cn.edu.seu.alumni_background.util.TokenUtil;
import cn.edu.seu.alumni_background.validation.NotEmpty;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Validated
@Service
public class VerifyServiceImpl implements VerifyService {

    private Integer maxErrorTimes = 5;
    private TimeUnit blockTimeUnit = TimeUnit.HOURS;
    private Long blockTime = 12L;
    private String guardPhone = "15850505966";

    private Long verifyCodeValidTime = 5L;
    private TimeUnit verifyCodeValidTimeUnit = TimeUnit.MINUTES;

    private Long tokenValidTime = 12L;
    private TimeUnit tokenValidTimeUnit = TimeUnit.HOURS;

    @Autowired
    private BackgroundAdminMapper backgroundAdminMapper;

    @Autowired
    private StringToObjectRedisStringManager redisStringManager;

    @Autowired
    private AbstractSMSManager smsManager;

    public VerifyServiceImpl() {
        Properties properties = new Properties();
        try {
            properties.load(
                Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("security/verify-code-guard.properties"))
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String key = (String) entry.getKey();
            switch (key) {
                case "maxErrorTimes":   // 如果是最大的错误次数
                    maxErrorTimes = Integer.parseInt((String) entry.getValue());
                    break;
                case "blockTimeUnit":
                    key = key.toLowerCase();
                    switch (key) {
                        case "d":
                            blockTimeUnit = TimeUnit.DAYS;
                            break;
                        case "m":
                            blockTimeUnit = TimeUnit.MINUTES;
                            break;
                        case "s":
                            blockTimeUnit = TimeUnit.SECONDS;
                            break;
                        default:
                            blockTimeUnit = TimeUnit.HOURS;
                            break;
                    }
                    break;
                case "blockTime":
                    String v = ((String) entry.getValue()).replace("L", "").replace("l", "");
                    blockTime = Long.parseLong(v);
                    break;
                case "guardPhone":
                    guardPhone = (String) entry.getValue();
                    break;
                case "verifyCodeValidTime":
                    String s = ((String) entry.getValue()).replace("L", "").replace("l", "");
                    verifyCodeValidTime = Long.parseLong(s);
                    break;
                case "verifyCodeValidTimeUnit":
                    key = key.toLowerCase();
                    switch (key) {
                        case "d":
                            verifyCodeValidTimeUnit = TimeUnit.DAYS;
                            break;
                        case "m":
                            verifyCodeValidTimeUnit = TimeUnit.MINUTES;
                            break;
                        case "s":
                            verifyCodeValidTimeUnit = TimeUnit.SECONDS;
                            break;
                        default:
                            verifyCodeValidTimeUnit = TimeUnit.HOURS;
                            break;
                    }
                    break;
                case "tokenValidTime":
                    String t = ((String) entry.getValue()).replace("L", "").replace("l", "");
                    tokenValidTime = Long.parseLong(t);
                    break;
                case "tokenValidTimeUnit":
                    key = key.toLowerCase();
                    switch (key) {
                        case "d":
                            tokenValidTimeUnit = TimeUnit.DAYS;
                            break;
                        case "m":
                            tokenValidTimeUnit = TimeUnit.MINUTES;
                            break;
                        case "s":
                            tokenValidTimeUnit = TimeUnit.SECONDS;
                            break;
                        default:
                            tokenValidTimeUnit = TimeUnit.HOURS;
                            break;
                    }
                    break;
            }
        }
    }

    @Override
    public String getRequestIP(
        @NotEmpty HttpServletRequest request
    ) throws ServiceException {
        String ip = null;

        // X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            // Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            // WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            // HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            // X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }

        // 有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }

        // 还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    @Override
    public void postVerifyCode(
        @NotEmpty HttpServletRequest request,
        @NotEmpty PhoneUsernameVerifier phoneUsernameVerifier
    ) throws ServiceException, TencentCloudSDKException,
        NoSuchAlgorithmException {
        // 获取基本信息
        String phone = phoneUsernameVerifier.getPhone(),
            username = phoneUsernameVerifier.getUsername();
        if (phone == null || phone.equals("")) {
            throw new ServiceException("管理员电话为空!");
        }
        if (username == null || username.equals("")) {
            throw new ServiceException("管理员姓名名为空!");
        }

        // 首先获取当前 ip, 判断是否为恶意访问
        String ip = getRequestIP(request);
        String ipKey = buildErrorIPKey(ip);

        // 获取失败请求次数
        Integer times =
            (Integer) redisStringManager.getValue(ipKey);
        if (times != null && times >= maxErrorTimes) {
            Long ttl =
                redisStringManager.getTemplate().getExpire(
                    ipKey, TimeUnit.MINUTES
                );
            throw new ServiceException(
                String.format(
                    "当前 IP 错误请求次数超过上线 %d 次. 请 %d 分钟后重试, 或者联系管理员 %s 解除封禁.",
                    maxErrorTimes, ttl, guardPhone
                )
            );
        }

        // 先尝试验证
        BackgroundAdmin admin =
            backgroundAdminMapper.getOneAdminByPhone(phone);

        // 当前电话号码错误或者管理员姓名错误
        if (admin == null || !admin.getUsername().equals(username)) {
            redisStringManager.incrementOrNew(
                ipKey, blockTime, blockTimeUnit
            );
            throw new ServiceException(
                String.format(
                    "输入的管理员%s错误! 请联系管理员 %s 验证您的身份!",
                    (admin == null ? "电话号码" : "姓名"), guardPhone
                )
            );
        }

        // 做到这里, 说明输入参数都是正确的, 走发起短信的流程
        SMSManagerResult.OneSMSResult smsResult = smsManager.verify(phone);

        // 请求失败
        if (!smsResult.isSuccessful()) {
            throw new ServiceException(smsResult.getMessage());
        }

        // 请求成功
        Map.Entry<String, String> entry =
            buildSuccessfulVerifyCodeEntry(phone, username, smsResult.getVerifyCode());
        String verifyCodeKey = entry.getKey(), verifyCodeValue = entry.getValue();

        // 将这个结果插入到 redis 中, 等待下一次验证
        // 验证码五分钟内有效
        redisStringManager.setValueWithTTL(
            verifyCodeKey, verifyCodeValue, verifyCodeValidTime, verifyCodeValidTimeUnit
        );
    }

    @Override
    public String buildErrorIPKey(
        @NotEmpty String ip
    ) {
        return "verify:error_param_ips:" + ip.replace(".", "_").replace(":", "_");
    }

    @Override
    public String buildPhoneUserVerifyCodeKey(
        @NotEmpty String phone, @NotEmpty String username
    ) {
        return String.format(
            "verify:codes:%s%s",
            phone, username
        );
    }

    @Override
    public Map.Entry<String, String> buildSuccessfulVerifyCodeEntry(
        @NotEmpty String phone,
        @NotEmpty String username,
        @NotEmpty String verifyCode
    ) throws NoSuchAlgorithmException {
        // 首先创建验证码的键
        String key = buildPhoneUserVerifyCodeKey(phone, username);
        // 然后创建验证码的 MD5 码后的 Base64 结果
        String value = IDUtil.buildVerifyCodeByMD5(phone, username, verifyCode);
        // 获取结果
        HashMap<String, String> helper = new HashMap<>();
        helper.put(key, value);
        for (Map.Entry<String, String> entry : helper.entrySet()) {
            return entry;
        }
        return null;
    }

    @Override
    public String postLogin(
        @NotEmpty HttpServletRequest request,
        @NotEmpty PhoneUsernameCodeVerifier verifier
    ) throws NoSuchAlgorithmException, ServiceException {
        // 获取所有信息
        String phone = verifier.getPhone(),
            username = verifier.getUsername(),
            verifyCode = verifier.getCode();
        if (phone == null || phone.equals("")) {
            throw new ServiceException("管理员电话为空!");
        }
        if (username == null || username.equals("")) {
            throw new ServiceException("管理员姓名名为空!");
        }
        if (verifyCode == null || verifyCode.equals("")) {
            throw new ServiceException("管理员验证码为空!");
        }
        if (!isPossibleVerifyCode(verifyCode)) {
            throw new ServiceException("管理员验证码格式非法!");
        }

        // 判断当前 IP 是否已经被屏蔽
        String ip = getRequestIP(request);
        String errorIPKey = buildErrorIPKey(ip);

        // 错误请求次数
        Integer errorTimes =
            (Integer) redisStringManager.getValue(errorIPKey);
        if (errorTimes != null && errorTimes >= maxErrorTimes) {
            Long ttl =
                redisStringManager.getTemplate().getExpire(
                    errorIPKey, TimeUnit.MINUTES
                );
            throw new ServiceException(
                String.format(
                    "当前 IP 错误请求次数超过上线 %d 次. 请 %d 分钟后重试, 或者联系管理员 %s 解除封禁.",
                    maxErrorTimes, ttl, guardPhone
                )
            );
        }

        // 先判断当前的电话与姓名是否正确
        BackgroundAdmin admin =
            backgroundAdminMapper.getOneAdminByPhone(phone);

        // 当前电话号码错误或者管理员姓名错误
        if (admin == null || !admin.getUsername().equals(username)) {
            // 第一次请求发验证码
            redisStringManager.incrementOrNew(
                errorIPKey, blockTime, blockTimeUnit
            );
            throw new ServiceException(
                String.format(
                    "输入的管理员%s错误! 请联系管理员 %s 验证您的身份!",
                    (admin == null ? "电话号码" : "姓名"), guardPhone
                )
            );
        }

        // 获取到之前登录时设置的 verifyCode 在 redis 中的键名与值
        Map.Entry<String, String> entry =
            buildSuccessfulVerifyCodeEntry(
                phone, username, verifyCode
            );
        String verifyCodeKey = entry.getKey(),
            verifyCodeValue = entry.getValue();

        // 验证键名与值是否正确
        String targetValue =
            (String) redisStringManager.getValue(verifyCodeKey);

        // 首先如果没有或者过期了, 那么不行
        if (targetValue == null) {
            throw new ServiceException("验证码已失效, 请重新获取!");
        }

        // 如果验证结果不正确
        if (!targetValue.equals(verifyCodeValue)) {
            redisStringManager.incrementOrNew(
                errorIPKey, blockTime, blockTimeUnit
            );
            throw new ServiceException("验证码不正确, 请重新获取!");
        }

        // 验证结果正确, 使用这个 verifyCodeValue 作为新的 tokenKey
        // tokenRawValue = 18851897099:梅磊
        String tokenRawValue = phone + ":" + username;
        // MD5 然后进行 Base64 = asd7adsfsdaf8dsdf
        String resultToken = TokenUtil.buildToken(tokenRawValue);
        // 最后一个 tokenKey = verify:tokens:asd7adsfsdaf8dsdf
        String tokenKey = buildTokenKey(resultToken);

        // 加入到 redis 中去, 12 小时内免登录
        redisStringManager.setValueWithTTL(
            tokenKey, tokenRawValue, tokenValidTime, tokenValidTimeUnit
        );

        return resultToken;
    }

    @Override
    public boolean isPossibleVerifyCode(String verifyCode) {
        if (verifyCode == null || verifyCode.length() != 6) {
            return false;
        }
        try {
            int i = Integer.parseInt(verifyCode);
            return i >= 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String buildTokenKey(
        @NotEmpty String tokenValue
    ) {
        return "verify:tokens:" + tokenValue;
    }

    @Override
    public String getTokenValueInRedis(@NotEmpty String resultToken) {
        String tokenKey = buildTokenKey(resultToken);
        return (String) redisStringManager.getValue(tokenKey);
    }

    @Override
    public void deleteTokenKeyInRedis(@NotEmpty String resultToken) {
        String tokenKey = buildTokenKey(resultToken);
        redisStringManager.getTemplate().delete(tokenKey);
    }
}
