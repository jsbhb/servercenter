package com.zm.thirdcenter.bussiness.maijiayun.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.thirdcenter.bussiness.maijiayun.model.OrderInfoEntityForMJY;
import com.zm.thirdcenter.constants.Constants;
import com.zm.thirdcenter.pojo.ResultModel;
import com.zm.thirdcenter.utils.HttpClientUtil;
import com.zm.thirdcenter.utils.JSONUtil;
import com.zm.thirdcenter.utils.SignUtil;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import net.sf.json.JSONObject;

/**
 * ClassName: ThirdPartPhoneController <br/>
 * Function: 第三方服务-卖家云对接服务. <br/>
 * date: Aug 19, 2017 1:40:27 PM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */
@RestController
public class ThirdPartMaiJiaYunController {
	
	private final static Logger THIRDLOG = LoggerFactory.getLogger("THIRD_LOG");

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "auth/{version}/maijiayun/addStoreSio", method = RequestMethod.POST)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0")})
	public ResultModel addStoreSio(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res, @RequestBody OrderInfoEntityForMJY info) {

		if (Constants.FIRST_VERSION.equals(version)) {

			ResultModel result = new ResultModel();

			if (!info.check()) {
				result.setErrorMsg("参数校验不通过");
				result.setSuccess(false);
				return result;
			}
			
			try {
				String tmpTimeStamp = System.currentTimeMillis() +"";
				Map<String, Object> postParam = new HashMap<String, Object>();
				postParam.put("accessKey", Constants.MJY_ACCESS_KEY);
				postParam.put("version", Constants.MJY_ACCESS_VERSION);
				String jsonParam = JSONUtil.toJson(info);
				THIRDLOG.info("实体类转JSON：" + jsonParam);
				Map<String, Object> params = JSONUtil.parse(jsonParam, Map.class);
				params.put("method", Constants.MJY_METHOD_STORE_SIO_ADD);
				params.put("timestamp", tmpTimeStamp);
				params.put("version", Constants.MJY_ACCESS_VERSION);
				THIRDLOG.info("实体类MAP：" + params.toString());
				postParam.putAll(params);
				String tmpToken = SignUtil.sha1(SignUtil.decrypt(Constants.MJY_ACCESS_KEY, Constants.MJY_ACCESS_SECRET, params));
				postParam.put("token", tmpToken);
				String postJsonParam = JSONUtil.toJson(postParam);
				String httpResult = HttpClientUtil.MJYPost(Constants.MJY_ACCESS_URL, postJsonParam);
				THIRDLOG.info("返回：" + "=====" + httpResult);
				JSONObject jobj = JSONObject.fromObject(httpResult);
				THIRDLOG.info("转换JSON：" + "=====" + jobj);
				if (jobj != null) {
					result.setErrorMsg(jobj.getString("resolveMsg"));
					result.setSuccess(jobj.getBoolean("isOk"));
					result.setErrorCode(jobj.getString("errorCode"));
					return result;
				} else {
					result.setErrorMsg("接口调用返回值为空");
					result.setSuccess(false);
					return result;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.setErrorMsg(e.getMessage());
				result.setSuccess(false);
				return result;
			}
		}

		return new ResultModel(true, null);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "auth/{version}/maijiayun/addStoreSoo", method = RequestMethod.POST)
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "version", dataType = "Double", required = true, value = "版本号，默认1.0")})
	public ResultModel addStoreSoo(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res, @RequestBody OrderInfoEntityForMJY info) {

		if (Constants.FIRST_VERSION.equals(version)) {

			ResultModel result = new ResultModel();

			if (!info.check()) {
				result.setErrorMsg("参数校验不通过");
				result.setSuccess(false);
				return result;
			}
			
			try {
				String tmpTimeStamp = System.currentTimeMillis() +"";
				Map<String, Object> postParam = new HashMap<String, Object>();
				postParam.put("accessKey", Constants.MJY_ACCESS_KEY);
				postParam.put("version", Constants.MJY_ACCESS_VERSION);
				String jsonParam = JSONUtil.toJson(info);
				THIRDLOG.info("实体类转JSON：" + jsonParam);
				Map<String, Object> params = JSONUtil.parse(jsonParam, Map.class);
				params.put("method", Constants.MJY_METHOD_STORE_SOO_ADD);
				params.put("timestamp", tmpTimeStamp);
				params.put("version", Constants.MJY_ACCESS_VERSION);
				THIRDLOG.info("实体类MAP：" + params.toString());
				postParam.putAll(params);
				String tmpToken = SignUtil.sha1(SignUtil.decrypt(Constants.MJY_ACCESS_KEY, Constants.MJY_ACCESS_SECRET, params));
				postParam.put("token", tmpToken);
				String postJsonParam = JSONUtil.toJson(postParam);
				String httpResult = HttpClientUtil.MJYPost(Constants.MJY_ACCESS_URL, postJsonParam);
				THIRDLOG.info("返回：" + "=====" + httpResult);
				JSONObject jobj = JSONObject.fromObject(httpResult);
				THIRDLOG.info("转换JSON：" + "=====" + jobj);
				if (jobj != null) {
					result.setErrorMsg(jobj.getString("resolveMsg"));
					result.setSuccess(jobj.getBoolean("isOk"));
					result.setErrorCode(jobj.getString("errorCode"));
					return result;
				} else {
					result.setErrorMsg("接口调用返回值为空");
					result.setSuccess(false);
					return result;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.setErrorMsg(e.getMessage());
				result.setSuccess(false);
				return result;
			}
		}

		return new ResultModel(true, null);
	}
	
	public static void main(String[] args) throws Exception {
//		OrderInfoEntityForMJY info = new OrderInfoEntityForMJY();
//		info.setCode("GXTEST100001");
//		info.setExpectedSkuQuantity(1);
//		info.setRelatedVoucherCode("GXTEST100001");
//		info.setStoreCode("2");
//		StockInVoucherSku sku = new StockInVoucherSku();
//		sku.setExpectedQuantity(1);
//		sku.setPrice(0.02);
//		sku.setSkuCode("1160961");
//		sku.setMemo("test");
//		List<StockInVoucherSku> list = new ArrayList<>();
//		list.add(sku);
//		info.setStockInVoucherSkus(list);
//		info.setMemo("test");
//		
//		
//		String tmpTimeStamp = System.currentTimeMillis() +"";
//		Map<String, Object> postParam = new HashMap<String, Object>();
//		postParam.put("accessKey", Constants.MJY_ACCESS_KEY);
//		postParam.put("version", Constants.MJY_ACCESS_VERSION);
//		String jsonParam = JSONUtil.toJson(info);
//		THIRDLOG.info("实体类转JSON：" + jsonParam);
//		Map<String, Object> params = JSONUtil.parse(jsonParam, Map.class);
//		params.put("method", Constants.MJY_METHOD_STORE_SIO_ADD);
//		params.put("timestamp", tmpTimeStamp);
//		params.put("version", Constants.MJY_ACCESS_VERSION);
//		THIRDLOG.info("实体类MAP：" + params.toString());
//		postParam.putAll(params);
//		String tmpToken = SignUtil.sha1(SignUtil.decrypt(Constants.MJY_ACCESS_KEY, Constants.MJY_ACCESS_SECRET, params));
//		postParam.put("token", tmpToken);
//		String postJsonParam = JSONUtil.toJson(postParam);
//		String httpResult = HttpClientUtil.MJYPost(Constants.MJY_ACCESS_URL, postJsonParam);
//		THIRDLOG.info("返回：" + "=====" + httpResult);
		
		
//		OrderInfoEntityForMJY info = new OrderInfoEntityForMJY();
//		info.setCode("GXTEST100001");
//		info.setExpectedSkuQuantity(1);
//		info.setRelatedVoucherCode("GXTEST100001");
//		info.setStoreCode("2");
//		StockOutVoucherSku sku = new StockOutVoucherSku();
//		sku.setExpectedQuantity(1);
//		sku.setSkuCode("1160961");
//		sku.setMemo("test");
//		List<StockOutVoucherSku> list = new ArrayList<>();
//		list.add(sku);
//		info.setStockOutVoucherSkus(list);
//		info.setMemo("test");
//		
//		
//		String tmpTimeStamp = System.currentTimeMillis() +"";
//		Map<String, Object> postParam = new HashMap<String, Object>();
//		postParam.put("accessKey", Constants.MJY_ACCESS_KEY);
//		postParam.put("version", Constants.MJY_ACCESS_VERSION);
//		String jsonParam = JSONUtil.toJson(info);
//		THIRDLOG.info("实体类转JSON：" + jsonParam);
//		Map<String, Object> params = JSONUtil.parse(jsonParam, Map.class);
//		params.put("method", Constants.MJY_METHOD_STORE_SOO_ADD);
//		params.put("timestamp", tmpTimeStamp);
//		params.put("version", Constants.MJY_ACCESS_VERSION);
//		THIRDLOG.info("实体类MAP：" + params.toString());
//		postParam.putAll(params);
//		String tmpToken = SignUtil.sha1(SignUtil.decrypt(Constants.MJY_ACCESS_KEY, Constants.MJY_ACCESS_SECRET, params));
//		postParam.put("token", tmpToken);
//		String postJsonParam = JSONUtil.toJson(postParam);
//		String httpResult = HttpClientUtil.MJYPost(Constants.MJY_ACCESS_URL, postJsonParam);
//		THIRDLOG.info("返回：" + "=====" + httpResult);
	}
}
