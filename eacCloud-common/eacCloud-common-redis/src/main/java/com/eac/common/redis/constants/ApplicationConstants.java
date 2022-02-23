package com.eac.common.redis.constants;

/**
 * <pre>
 * Title :  Redis的Key的前缀变量
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2021-11-18
 **/
public final class ApplicationConstants {

	public static final String REDIS_PREFIX = "EAC:";
	public static final String CHAR_CAPTCHA_PREFIX = "EAC:CAPTCHA:USERNAME:";
	public static final String SMS_CAPTCHA_PREFIX = "EAC:CAPTCHA:MOBILE:";
	public static final String URL_ROLECODE_PREFIX = "EAC:URL:ROLECODE:";

	private ApplicationConstants() {

	}

}
