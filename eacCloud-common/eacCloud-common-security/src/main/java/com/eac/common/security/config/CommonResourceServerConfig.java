package com.eac.common.security.config;

import com.eac.common.security.provider.UserInfoTokenServices;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

/**
 * <pre>
 * Title :  OAuth2资源服务器
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2021-11-18
 **/
public class CommonResourceServerConfig extends ResourceServerConfigurerAdapter {

	private final ResourceServerProperties sso;

	@Autowired
	public CommonResourceServerConfig(ResourceServerProperties sso) {
		this.sso = sso;
	}

	@Bean
	@ConfigurationProperties(prefix = "security.oauth2.client")
	public ClientCredentialsResourceDetails clientCredentialsResourceDetails() {
		return new ClientCredentialsResourceDetails();
	}

	/**
	 * 配置Feign服务调用的请求拦截
	 *
	 * @return
	 */
	@Bean
	public RequestInterceptor oauth2FeignRequestInterceptor() {
		return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), clientCredentialsResourceDetails());
	}

	/**
	 * 配置客户端访问认证的Rest信息
	 *
	 * @return
	 */
	@Bean
	public OAuth2RestTemplate clientCredentialsRestTemplate() {
		return new OAuth2RestTemplate(clientCredentialsResourceDetails());
	}

	/**
	 * 配置用户的token信息
	 *
	 * @return
	 */
	@Bean
	public ResourceServerTokenServices tokenServices() {
		return new UserInfoTokenServices(sso.getUserInfoUri(), sso.getClientId());
	}

}
