package com.eac.auth.provider;

import com.eac.auth.mapper.AuthMapper;
import com.eac.common.redis.constants.ApplicationConstants;
import com.eac.common.redis.util.RedisUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * Title : 处理短信登录验证的自定义TokenGranter
 * </pre>
 *
 * @author xiashilong
 * @since 2021-11-18
 **/
public class SmsTokenGranter extends AbstractTokenGranter {

    private final AuthMapper authMapper;
    private final RedisUtils redisUtils;

    public SmsTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory,
                           AuthMapper authMapper, RedisUtils redisUtils) {
        this(tokenServices, clientDetailsService, requestFactory, authMapper, redisUtils, "mobile");
    }

    protected SmsTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory,
                              AuthMapper authMapper, RedisUtils redisUtils, String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.authMapper = authMapper;
        this.redisUtils = redisUtils;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        String mobile = parameters.get("mobile");
        String smsCaptcha = parameters.get("smsCaptcha");
        AbstractAuthenticationToken userAuth;
        try {
            LinkedHashMap<String, String> user = authMapper.getSysUserByMobile(mobile);
            if (user == null) {
                throw new UsernameNotFoundException(String.format("找不到此手机号%s。", mobile));
            }

            String smsCaptchaKey = ApplicationConstants.SMS_CAPTCHA_PREFIX + mobile;
            String smsCaptchaCache = redisUtils.get(smsCaptchaKey);
            if (!smsCaptcha.equals(smsCaptchaCache)) {
                throw new IllegalArgumentException("短信验证码错误或已过期，请重新输入。");
            }
            redisUtils.del(smsCaptchaKey);

            List<String> roleCodeList = authMapper.queryRoleCodeByMobile(mobile);
            UserDetails userDetails = new User(user.get("USERNAME"), user.get("PASSWORD"),
                    AuthorityUtils.createAuthorityList(roleCodeList.toArray(new String[]{})));
            userAuth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            userAuth.setDetails(parameters);
        } catch (AccountStatusException | BadCredentialsException ase) {
            throw new InvalidGrantException(ase.toString());
        }
        if (!userAuth.isAuthenticated()) {
            throw new InvalidGrantException("Could not authenticate mobile: " + mobile);
        }

        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }

}
