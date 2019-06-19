package com.zm.goods.annotation.valid;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
/**
 * @fun 在字段上加入该注解，进行入参检查，值需要大于等于指定的值
 * @author user
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Min.MinIntValidator.class)
@Documented
public @interface Min {

	String message() default "参数不符合规则";
	 
    Class<?>[] groups() default {};
 
    Class<? extends Payload>[] payload() default {};
    
    int value();

    
    class MinIntValidator implements ConstraintValidator<Min, Integer> {

    	private int value;
		@Override
		public void initialize(Min arg0) {
			value = arg0.value();
		}

		@Override
		public boolean isValid(Integer min, ConstraintValidatorContext arg1) {
			if(min >= value){
				return true;
			}
			return false;
		}
    	
    }
}
