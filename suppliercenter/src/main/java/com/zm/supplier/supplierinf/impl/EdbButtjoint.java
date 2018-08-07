package com.zm.supplier.supplierinf.impl;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBException;

import com.zm.supplier.pojo.CheckStockModel;
import com.zm.supplier.pojo.OrderBussinessModel;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.OrderStatus;
import com.zm.supplier.pojo.SendOrderResult;
import com.zm.supplier.pojo.ThirdWarehouseGoods;
import com.zm.supplier.pojo.UserInfo;
import com.zm.supplier.supplierinf.AbstractSupplierButtJoint;
import com.zm.supplier.supplierinf.model.EdbGoodsInfoList;
import com.zm.supplier.supplierinf.model.EdbStockList;
import com.zm.supplier.supplierinf.model.EdbStockModel;
import com.zm.supplier.util.ConvertUtil;
import com.zm.supplier.util.HttpClientUtil;
import com.zm.supplier.util.JaxbUtil;
import com.zm.supplier.util.SignUtil;

public class EdbButtjoint extends AbstractSupplierButtJoint {

	private final String dbhost = "edb_a87897";// 主账号(正式时让EDB分配)
	private final String token = "0b34169b82824b05aa269fd74219027f";// (正式时让EDB分配)
	private final String url = "http://vip3013.edb09.net/rest/index.aspx";// 服务器地址(正式时让EDB分配)
	private String appKey = "7670dec6";// (正式时让EDB分配)
	private String appScret = "4b950f6e31494b9cbf398884e266133b";// (正式时让EDB分配)
	private int initPageNum = 1;
	private int pageSize = 100;

	@Override
	public Set<SendOrderResult> sendOrder(OrderInfo info, UserInfo user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<OrderStatus> checkOrderStatus(List<String> orderIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<CheckStockModel> checkStock(List<OrderBussinessModel> list) {
		Map<String, String> params = edbGetCommonParams("edbProductGet");
		params.put("bar_code", list.get(0).getItemCode());	
		EdbStockList edbStockList = sendEDB(EdbStockList.class, params, list.get(0).getItemCode());
		if(edbStockList != null){
			Set<CheckStockModel> result = new HashSet<CheckStockModel>();
			result.addAll(ConvertUtil.ConverToCheckStockModel(edbStockList));
			return result;
		}
		return null;
	}

	@Override
	public Set<ThirdWarehouseGoods> getGoods(String itemCode) {
		Map<String, String> params = edbGetCommonParams("edbProductBaseInfoGet");
		params.put("pagenum", initPageNum + "");
		params.put("pagesize", pageSize + "");
		Set<ThirdWarehouseGoods> result = new HashSet<ThirdWarehouseGoods>();
		loopGetGoods(params, result, pageSize);
		return result;
	}

	private void loopGetGoods(Map<String, String> params, Set<ThirdWarehouseGoods> result, int pageSize) {
		EdbGoodsInfoList entity = sendEDB(EdbGoodsInfoList.class, params, "第" + params.get("pagenum") + "页");
		if(entity != null && entity.getList() != null && entity.getList().size() == pageSize){//不是最后一页商品
			result.addAll(ConvertUtil.ConverToThirdWarehouseGoods(entity));
			params.put("pagenum", Integer.valueOf(params.get("pagenum")) + 1 + "");
			loopGetGoods(params, result, pageSize);//继续获取商品
		} else if(entity != null && entity.getList() != null && entity.getList().size() > 0){//最后一页商品
			result.addAll(ConvertUtil.ConverToThirdWarehouseGoods(entity));
		}
	}

	private <T> T sendEDB(Class<T> clazz, Map<String, String> params, String param) {
		StringBuilder builder = new StringBuilder();
		try {
			for (String key : params.keySet()) {
				String val = "xmlValues".equalsIgnoreCase(key) ? URLEncoder.encode(params.get(key), "UTF-8")
						: params.get(key);
				builder.append(String.format("%1$s=%2$s&", key, URLEncoder.encode(val, "UTF-8")));
			}
			builder.append("sign=" + SignUtil.edbSignature(params, appScret, token, appKey));
			String result = HttpClientUtil.post(url, builder.toString(), "application/x-www-form-urlencoded");
			logger.info("返回：" + param + "===" + result);

			return parseXml(result,clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	private <T> T parseXml(String xml,Class<T> clazz){
		try {
			Object obj = clazz.newInstance();
			obj = JaxbUtil.readString(clazz, xml, false);
			System.out.println(obj.toString());
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取公共参数
	 * 
	 * @param method
	 *            接口名称
	 * @return 公共参
	 */
	public Map<String, String> edbGetCommonParams(String method) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("method", method);// 接口名称
		map.put("dbhost", dbhost);
		map.put("appkey", appKey);
		map.put("format", "XML");// 返回的数据格式
		map.put("timestamp", new SimpleDateFormat("yyyyMMddHHmm").format(new Date()));// timestamp
		map.put("v", "2.0");// 版本号
		map.put("slencry", "1");//
		map.put("ip", "192.168.1.153");// 本机ip
		return map;
	}
	
	public static void main(String[] args) {
		EdbButtjoint edb = new EdbButtjoint();
		OrderBussinessModel model = new OrderBussinessModel();
		model.setItemCode("1206009");
		List<OrderBussinessModel> list = new ArrayList<OrderBussinessModel>();
		list.add(model);
//		edb.getGoods(null);
		edb.checkStock(list);
	}

}
