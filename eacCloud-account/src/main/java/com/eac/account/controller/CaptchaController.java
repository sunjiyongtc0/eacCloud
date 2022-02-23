package com.eac.account.controller;


import com.eac.account.utils.CaptchaUtils;
import com.eac.common.core.result.ActionResult;
import com.eac.common.core.result.ResultBuilder;
import com.eac.common.redis.constants.ApplicationConstants;
import com.eac.common.redis.util.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

//import com.eac.notification.client.AliyunSmsClient;

/**
 * <pre>
 * Title :  验证码的控制层
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2021-11-18
 **/

@RestController
@RequestMapping("/captcha")
public class CaptchaController {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private RedisUtils redisUtils;
	@Autowired
//	private AliyunSmsClient aliyunSmsClient;

	/**
	 * 生成图像验证码
	 *
	 * @param response 响应对象
	 * @return
	 */
	@GetMapping(path = "/generateImageCaptcha")//HttpServletResponse response,@RequestParam(name = "key", required = true) String key
	public ResponseEntity<Resource> generateImageCaptcha(HttpServletRequest request) {
		ResponseEntity<Resource> responseEntity = null;
		try {
			String key=request.getParameter("key");
			String charCaptcha = CaptchaUtils.generateCharCaptcha();
			//或者获取请求的ip
//			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//			redisUtils.psetex(ApplicationConstants.CHAR_CAPTCHA_PREFIX + authentication.getName(), charCaptcha);
			redisUtils.psetex(ApplicationConstants.CHAR_CAPTCHA_PREFIX + key, charCaptcha);
			byte[] bytes = CaptchaUtils.generateImageCaptcha(charCaptcha);
			Resource resource = new ByteArrayResource(bytes);
			responseEntity = new ResponseEntity<>(resource, CaptchaUtils.getResponseHeaders(), HttpStatus.OK);
		} catch (Exception e) {
			logger.warn(e.toString());
		}
		return responseEntity;
	}

	/**
	 * 获取短信验证码并发送短信
	 *
	 * @param mobile 手机号
	 * @return
	 */
	@GetMapping(path = "/getSmsCaptcha")
	public ActionResult getSmsCaptcha(@RequestParam(name = "mobile", required = true) String mobile) {
//		String smsCaptcha = aliyunSmsClient.getRandomCode(6);
//		String phoneNumber = mobile; // 手机号
//		String signName = "阿里云短信签名"; // 阿里云短信签名
//		String templateCode = "阿里云短信模板Code"; // 阿里云短信模板Code
//		JSONObject jsonObject = new JSONObject();// JSON模板参数字符串
//		logger.info("短信验证码为：{}", smsCaptcha);
//		jsonObject.put("code", smsCaptcha);
//		redisUtils.psetex(ApplicationConstants.SMS_CAPTCHA_PREFIX + mobile, smsCaptcha);
//		aliyunSmsClient.sendSms(phoneNumber, signName, templateCode, jsonObject.toString());
		return ResultBuilder.buildActionSuccess();
	}

}
