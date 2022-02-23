package com.eac.common.core.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <pre>
 * Title :  数字验证器
 * </pre>
 *
 * @author : SunJiYong
 * @since : 2021-11-18
 **/
@Documented
@Constraint(validatedBy = {})
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@ReportAsSingleViolation
@Pattern(regexp = "^\\d+$")
public @interface NumericCharacters {

	String message() default "{com.eac.component.commons.validator.NumericCharacters.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
