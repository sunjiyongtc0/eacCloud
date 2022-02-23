package com.eac.common.security.utils;

import com.eac.common.security.provider.CurrentUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 当前用户信息工具类
 *
 */
public class CurrentUserUtils {

	private CurrentUserUtils() {

	}

	public static Map<String, String> getOAuth2AuthenticationDetailsInfo() {
		Map<String, String> oauth2AuthenticationMap = new HashMap<>();
		OAuth2Authentication oauth2Authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) oauth2Authentication
				.getUserAuthentication();
		LinkedHashMap<?, ?> details =(LinkedHashMap<?, ?>) oauth2Authentication.getUserAuthentication().getDetails();
		LinkedHashMap<?, ?> principal= (LinkedHashMap<?, ?>) details.get("principal");
		oauth2AuthenticationMap=(Map<String, String>) principal.get("currentUser");
//		Map<String, String> currentUser=(Map<String, String>) principal.get("currentUser");
//		String tenantCode = (String) ((LinkedHashMap<?, ?>) ((LinkedHashMap<?, ?>) ((LinkedHashMap<?, ?>) usernamePasswordAuthenticationToken.getDetails())
//				.get("userAuthentication")).get("details")).get("tenantCode");
//		oauth2AuthenticationMap.put("tenantCode", tenantCode);
//		oauth2AuthenticationMap.put("name", oauth2Authentication.getName());
		return oauth2AuthenticationMap;
	}

	/**
	 * 获取当前用户信息
	 *
	 * @return
	 */
	public static Map<String, String> getOAuth2AuthenticationInfo() {
		Map<String, String> oauth2AuthenticationMap = new HashMap<>();
		OAuth2Authentication oauth2Authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) oauth2Authentication
				.getUserAuthentication();
		CurrentUser user= (CurrentUser) usernamePasswordAuthenticationToken.getPrincipal();
		oauth2AuthenticationMap=user.getCurrentUser();
		oauth2AuthenticationMap.put("name",user.getUsername());
		return oauth2AuthenticationMap;
	}

//	/**
//	 * 获取当前用户详情信息
//	 *
//	 * @return
//	 */
//	public static Map<String, String> getOAuth2AuthenticationDetailsInfo() {
//		Map<String, String> oauth2AuthenticationMap = new HashMap<>();
//		OAuth2Authentication oauth2Authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
//
//		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) oauth2Authentication
//				.getUserAuthentication();
//		LinkedHashMap<?, ?> details=(LinkedHashMap<?, ?>) oauth2Authentication.getUserAuthentication().getDetails();
//		System.out.println("details==>"+details);
//		Map<String ,Object> principal =(Map<String ,Object>)  details.get("principal");
//		System.out.println("principal==>"+principal);
//		System.out.println("currentUser==>"+principal.get("currentUser"));
//		oauth2AuthenticationMap=(Map<String, String>) principal.get("currentUser");
//
//		return oauth2AuthenticationMap;
//	}
//
//	/**
//	 * 获取当前用户信息
//	 *
//	 * @return
//	 */
//	public static Map<String, String> getOAuth2AuthenticationInfo() {
//		Map<String, String> oauth2AuthenticationMap = new HashMap<>();
//		OAuth2Authentication oauth2Authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
//		LinkedHashMap<?, ?> details=(LinkedHashMap<?, ?>) oauth2Authentication.getUserAuthentication().getDetails();
//		Map<String ,Object> principal =(Map<String ,Object>)  details.get("principal");
//		System.out.println("principal==>"+principal);
//		Map<String ,String> currentUser= (Map<String, String>) principal.get("currentUser");
//		System.out.println("currentUser==>"+currentUser);
//		oauth2AuthenticationMap.put("name" ,oauth2Authentication.getName());
//		oauth2AuthenticationMap.put("tenantCode", currentUser.get("tenantCode"));
//		return oauth2AuthenticationMap;
//	}

}
