package com.zm.order.bussiness.component.expressrule.inf;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import com.zm.order.exception.ParameterException;
import com.zm.order.exception.RuleCheckException;
import com.zm.order.pojo.OrderInfo;
import com.zm.order.utils.JSONUtil;

public abstract class AbstractExpressRule {

	/**
	 * @fun 保存数据时校验参数是否正确
	 * @param json
	 * @return
	 * @throws ParameterException
	 */
	public abstract void checkParameter() throws ParameterException;

	/**
	 * @fun 判断订单数据是否符合运费模板规则
	 * @param json
	 * @param info
	 * @throws RuleCheckException
	 */
	public abstract void checkOrderInfoRule(OrderInfo info) throws RuleCheckException;

	/**
	 * @fun 内部对象赋值，由于都是内部类不能再这里用反射方法生成对象
	 * @param json
	 * @param obj
	 * @throws  
	 * @throws NoSuchMethodException 
	 */
	@SuppressWarnings("unchecked")
	public void render(String json, Object obj) throws NoSuchMethodException {
		Map<String, String> jsonMap = JSONUtil.parse(json, Map.class);
		Method method = null;
		for (Map.Entry<String, String> entry : jsonMap.entrySet()) {
			String field = entry.getKey();
				method = obj.getClass().getMethod("set" + field.substring(0, 1).toUpperCase()
						+ field.substring(1, field.length()), String.class);
				method.setAccessible(true);
				try {
					method.invoke(obj, entry.getValue());
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
		}
	}
}
