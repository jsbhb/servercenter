package com.zm.thirdcenter.bussiness.express;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.thirdcenter.cache.CacheMap;
import com.zm.thirdcenter.constants.Constants;
import com.zm.thirdcenter.pojo.ResultModel;
import com.zm.thirdcenter.pojo.RoteModel;
import com.zm.thirdcenter.utils.HttpClientUtil;
import com.zm.thirdcenter.utils.JSONUtil;

@RestController
public class ExpressController {

	// 电商ID
	private String EBusinessID = "1312738";
	// 电商加密私钥，快递鸟提供，注意保管，不要泄漏
	private String AppKey = "178dbf0c-1d20-450d-9abc-b2cc7c6974a8";
	// 请求url
	private String ReqURL = "http://api.kdniao.cc/Ebusiness/EbusinessOrderHandle.aspx";

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "{version}/express/getRoute", method = RequestMethod.GET)
	public ResultModel getRoute(@PathVariable("version") Double version, HttpServletRequest req,
			HttpServletResponse res) {
		ResultModel model = new ResultModel();
		String carrierName = req.getParameter("carrierName");// 快递公司名称
		String expressID = req.getParameter("expressId");// 快递单号
		try {
			if (Constants.FIRST_VERSION.equals(version)) {
				Map<String,String> carrierMap = (Map<String, String>) CacheMap.getCache().getData().get(Constants.CARRIER);
	        	String carrierID = carrierMap.get(carrierName);//获取快递公司编码
				String result = getOrderTracesByJson(carrierID, expressID);
				if(result == null){
					model.setSuccess(false);
					return model;
				}
				RoteModel rote = JSONUtil.parse(result, RoteModel.class);
				model.setObj(rote);
				model.setSuccess(true);
			}

		} catch (Exception e) {
			e.printStackTrace();
			model.setErrorMsg(e.getMessage());
			model.setSuccess(false);
		}
		return model;
	}

	/**
	 * Json方式 查询订单物流轨迹
	 * 
	 * @throws Exception
	 */
	public String getOrderTracesByJson(String expCode, String expNo) throws Exception {
		String requestData = "{'OrderCode':'','ShipperCode':'" + expCode + "','LogisticCode':'" + expNo + "'}";

		Map<String, String> params = new HashMap<String, String>();
		params.put("RequestData", URLEncoder.encode(requestData, "UTF-8"));
		params.put("EBusinessID", EBusinessID);
		params.put("RequestType", "1002");
		String dataSign = new String(Base64.encodeBase64(DigestUtils.md5Hex(requestData+AppKey).getBytes("UTF-8")));
		params.put("DataSign", URLEncoder.encode(dataSign, "UTF-8"));
		params.put("DataType", "2");

		String result = HttpClientUtil.post(ReqURL, params);

		// 根据公司业务处理返回的信息......

		return result;
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {
		ExpressController api = new ExpressController();
//		Map<String,String> carrierMap = new HashMap<String,String>();
//		System.out.println(new ClassPathResource("ExpressCode.xls").getURL().getPath());
//		List<CarrierModel> list = ExcelUtil.instance().getCache(new ClassPathResource("ExpressCode.xls").getURL().getPath());
//		for(CarrierModel model : list){
//			carrierMap.put(model.getCarrierName(), model.getCarrierID());
//		}
//		CacheMap.getCache().put(Constants.CARRIER, carrierMap);
		try {
//			Map<String,String> carrierMap1 = (Map<String, String>) CacheMap.getCache().getData().get(Constants.CARRIER);
//        	String carrierID = carrierMap1.get("申通快递");//获取快递公司编码
			String result = api.getOrderTracesByJson("STO", "3344704220602");
			System.out.print(result);
			RoteModel model = JSONUtil.parse(result, RoteModel.class);
			System.out.println("\n");
			System.out.println(model);
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}
}
