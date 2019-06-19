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

import org.springframework.util.StringUtils;
/**
 * @fun 在字段上加入该注解，进行入参检查，值必须被指定数组包含(可以为空，为空时不判断)
 * @author user
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Contain.ContainValid.class)
@Documented
public @interface Contain {

	String message() default "参数不符合规则";
	 
    Class<?>[] groups() default {};
 
    Class<? extends Payload>[] payload() default {};
    
    String[] value();
    
    boolean ignoreCase();//是否忽略大小写
    
    class ContainValid implements ConstraintValidator<Contain, String>{

    	private String[] value;
    	private boolean ignoreCase;
		@Override
		public void initialize(Contain arg0) {
			this.value = arg0.value();
			this.ignoreCase = arg0.ignoreCase();
		}

		@Override
		public boolean isValid(String arg0, ConstraintValidatorContext arg1) {
			if(StringUtils.isEmpty(arg0)){
				return true;
			}
			for(String s : value){
				if(ignoreCase){
					if(s.equalsIgnoreCase(arg0)){
						return true;
					}
				} else {
					if(s.equals(arg0)){
						return true;
					}
				}
			}
			return false;
		}
    	
    }
}
