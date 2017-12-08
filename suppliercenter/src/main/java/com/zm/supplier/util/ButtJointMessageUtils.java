package com.zm.supplier.util;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.zm.supplier.constants.Constants;
import com.zm.supplier.pojo.OrderBussinessModel;
import com.zm.supplier.pojo.OrderGoods;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.UserInfo;
import com.zm.supplier.supplierinf.model.FuBangOrder;
import com.zm.supplier.supplierinf.model.FuBangOrderGoods;
import com.zm.supplier.supplierinf.model.GetXinYunGoodsParam;
import com.zm.supplier.supplierinf.model.LianYouOrder;
import com.zm.supplier.supplierinf.model.OutOrderGoods;
import com.zm.supplier.supplierinf.model.XinYunCheckStock;
import com.zm.supplier.supplierinf.model.XinYunGetOrderStatus;
import com.zm.supplier.supplierinf.model.XinYunGoods;
import com.zm.supplier.supplierinf.model.XinYunOrder;

public class ButtJointMessageUtils {

	public static String getTianTianOrderMsg(OrderInfo info, UserInfo user, String customer) {
		StringBuilder sb = new StringBuilder();
		String source = "";
		if (Constants.ALI_PAY.equals(info.getOrderDetail().getPayType())) {// 支付方式
			source = "ALIPAY";
		} else if (Constants.WX_PAY.equals(info.getOrderDetail().getPayType())) {
			source = "TENPAY";
		}
		sb.append("<?xml version='1.0' encoding='UTF-8'?>\n");
		sb.append("<request>\n");
		sb.append("<head>\n");
		sb.append("<methodType>");
		sb.append("create"); // 操做类型
		sb.append("</methodType>\n");
		sb.append("<hgCode>");
		sb.append("NBC"); // 海关编码 NBC=宁波海关CQC=重庆海关DGC=沙田
		sb.append("</hgCode>\n");
		sb.append("<busCode>");
		sb.append(customer); // 客户编码
		sb.append("</busCode>\n");
		sb.append("</head>\n");
		sb.append("<body>\n");
		sb.append("<order>\n");
		sb.append("<orderShop>");
		sb.append("11612"); // 店铺代码
		sb.append("</orderShop>\n");
		sb.append("<hgArea>");
		sb.append(3105); // 海关关区北仑保税区
		sb.append("</hgArea>\n");
		sb.append("<orderFrom>");
		sb.append("GXHZ"); // 购物网站代码
		sb.append("</orderFrom>\n");
		sb.append("<busOrderNo>");
		sb.append(info.getOrderId()); // 订单号
		sb.append("</busOrderNo>\n");
		sb.append("<postFee>");
		sb.append(info.getOrderDetail().getPostFee()); // 运费
		sb.append("</postFee>\n");
		sb.append("<amount>");
		sb.append(info.getOrderDetail().getPayment()); // 买家实付金额
		sb.append("</amount>\n");
		sb.append("<buyerAccount>");
		sb.append(user.getPhone()); // 购物网站买家账号
		sb.append("</buyerAccount>\n");
		sb.append("<phone>");
		sb.append(user.getPhone()); // 手机号
		sb.append("</phone>\n");
		sb.append("<taxAmount>");
		sb.append(info.getOrderDetail().getTaxFee()); // 税额
		sb.append("</taxAmount>\n");
		sb.append("<tariffAmount>");
		sb.append(info.getOrderDetail().getTariffTax()); // 关税额
		sb.append("</tariffAmount>\n");
		sb.append("<addedValueTaxAmount>");
		sb.append(info.getOrderDetail().getIncrementTax()); // 增值税额
		sb.append("</addedValueTaxAmount>\n");
		sb.append("<consumptionDutyAmount>");
		sb.append(info.getOrderDetail().getExciseTax()); // 消费税额
		sb.append("</consumptionDutyAmount>\n");
		sb.append("<grossWeight>");
		sb.append(CalculationUtils.div(info.getWeight(), 1000)); // 毛重
		sb.append("</grossWeight>\n");
		sb.append("<disAmount>");
		sb.append(info.getOrderDetail().getDisAmount()); // 优惠金额合计
		sb.append("</disAmount>\n");
		sb.append("<dealDate>");
		sb.append(info.getCreateTime()); // 下单时间
		sb.append("</dealDate>\n");
		sb.append("<promotions>\n");
		sb.append("<promotion>\n");
		sb.append("<proAmount>");
		sb.append(info.getOrderDetail().getDisAmount()); // 优惠金额
		sb.append("</proAmount>\n");
		sb.append("</promotion>\n");
		sb.append("</promotions>\n");
		sb.append("<goods>\n");
		for (OrderGoods goods : info.getOrderGoodsList()) {
			sb.append("<detail>\n");
			sb.append("<productId>");
			sb.append(goods.getSku()); // 货号'3105166008N0000002'
			sb.append("</productId>\n");
			sb.append("<qty>");
			sb.append(goods.getItemQuantity()); // 数量
			sb.append("</qty>\n");
			sb.append("<price>");
			sb.append(goods.getItemPrice()); // 商品单价
			sb.append("</price>\n");
			sb.append("<amount>");
			sb.append(CalculationUtils.mul(goods.getActualPrice(), goods.getItemQuantity())); // 商品金额
			sb.append("</amount>\n");
			sb.append("</detail>\n");
		}
		sb.append("</goods>\n");
		sb.append("<pay>\n");
		sb.append("<paytime>");
		sb.append(info.getOrderDetail().getPayTime()); // 支付时间
		sb.append("</paytime>\n");
		sb.append("<paymentNo>");
		sb.append(info.getOrderDetail().getPayNo()); // 支付单号
		sb.append("</paymentNo>\n");
		sb.append("<orderSeqNo>");
		sb.append(info.getOrderDetail().getPayNo()); // 商家送支付机构订单交易号,如无，请与支付单号一致
		sb.append("</orderSeqNo>\n");
		sb.append("<source>");
		sb.append(source); // 支付方式代码
		sb.append("</source>\n");
		sb.append("<idnum>");
		sb.append(user.getUserDetail().getIdNum()); // 身份证
		sb.append("</idnum>\n");
		sb.append("<name>");
		sb.append(user.getUserDetail().getName()); // 真实姓名
		sb.append("</name>\n");
		sb.append("</pay>\n");
		sb.append("<logistics>");
		sb.append("<logisticsCode>");
		sb.append("ZTO"); // 快递公司代码
								// SF=顺丰速EMS=邮政速递POSTAM=邮政小包ZTO=中通速递STO=申通快递YTO=圆通速递JD=京东快递BEST=百世物流YUNDA=韵达速递TTKDEX=天天快递
		sb.append("</logisticsCode>\n");
		sb.append("<consignee>");
		sb.append(info.getOrderDetail().getReceiveName()); // 收货人名称
		sb.append("</consignee>\n");
		sb.append("<province>");
		sb.append(info.getOrderDetail().getReceiveProvince()); // 省
		sb.append("</province>\n");
		sb.append("<city>");
		sb.append(info.getOrderDetail().getReceiveCity()); // 市
		sb.append("</city>\n");
		sb.append("<district>");
		sb.append(info.getOrderDetail().getReceiveArea()); // 区
		sb.append("</district>\n");
		sb.append("<consigneeAddr>");
		sb.append(info.getOrderDetail().getReceiveProvince() + info.getOrderDetail().getReceiveCity()
				+ info.getOrderDetail().getReceiveArea() + info.getOrderDetail().getReceiveAddress()); // 收货地址
		sb.append("</consigneeAddr>\n");
		sb.append("<consigneeTel>");
		sb.append(info.getOrderDetail().getReceivePhone()); // 收货电话
		sb.append("</consigneeTel>\n");
		sb.append("</logistics>\n");
		sb.append("</order>\n");
		sb.append("</body>\n");
		sb.append("</request>\n");

		return sb.toString();
	}

	public static String getTianTianCheckOrderMsg(List<String> orderIds, String customer) {
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version='1.0' encoding='UTF-8'?>\n");
		sb.append("<request>\n");
		sb.append("<head>\n");
		sb.append("<methodType>");
		sb.append("find"); // 操做类型
		sb.append("</methodType>\n");
		sb.append("<rule>");
		sb.append("1"); // 查询规则0:鑫海订单号，1能容订单号
		sb.append("</rule>\n");
		sb.append("<busCode>");
		sb.append(customer); // 客户编码
		sb.append("</busCode>\n");
		sb.append("</head>\n");
		sb.append("<body>\n");
		for (String orderId : orderIds) {
			sb.append("<orderNo>");
			sb.append(orderId); // 订单号
			sb.append("</orderNo>\n");
		}
		sb.append("</body>\n");
		sb.append("</request>\n");
		return sb.toString();
	}

	public static String getLiangYouOrderMsg(OrderInfo info, UserInfo user) {
		try {

			LianYouOrder order = new LianYouOrder();
			order.setAddress(URLEncoder.encode(info.getOrderDetail().getReceiveAddress(), "utf-8"));
			order.setCity(URLEncoder.encode(info.getOrderDetail().getReceiveCity(), "utf-8"));
			order.setConsignee(URLEncoder.encode(info.getOrderDetail().getReceiveName(), "utf-8"));
			order.setCounty(URLEncoder.encode(info.getOrderDetail().getReceiveArea(), "utf-8"));
			order.setImId(URLEncoder.encode(user.getUserDetail().getIdNum(), "utf-8"));
			order.setOrder_sn(URLEncoder.encode(info.getOrderId(), "utf-8"));
			order.setOut_order_sn(URLEncoder.encode(info.getOrderId(), "utf-8"));
			order.setPhoneMob(URLEncoder.encode(info.getOrderDetail().getReceivePhone(), "utf-8"));
			order.setProvince(URLEncoder.encode(info.getOrderDetail().getReceiveProvince(), "utf-8"));
			order.setRealName(URLEncoder.encode(user.getUserDetail().getName(), "utf-8"));
			List<OutOrderGoods> list = new ArrayList<OutOrderGoods>();
			OutOrderGoods outOrderGoods = null;
			for (OrderGoods goods : info.getOrderGoodsList()) {
				outOrderGoods = new OutOrderGoods();
				outOrderGoods.setGoods_name(URLEncoder.encode(goods.getItemName(), "utf-8"));
				outOrderGoods.setOnly_sku(goods.getSku());
				outOrderGoods.setQuantity(goods.getItemQuantity());
				list.add(outOrderGoods);
			}
			order.setOutOrderGoods(list);

			return JSONUtil.toJson(order);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getXinYunOrderMsg(OrderInfo info, UserInfo user, String appKey, String secret) {

		XinYunOrder order = new XinYunOrder();
		order.setMerchant_id(appKey);
		order.setAccept_name(user.getUserDetail().getName());
		order.setAddress(info.getOrderDetail().getReceiveAddress());
		order.setArea(info.getOrderDetail().getReceiveArea());
		order.setCard_id(user.getUserDetail().getIdNum());
		order.setCity(info.getOrderDetail().getReceiveCity());
		order.setMerchant_order_no(info.getOrderId());
		order.setMessage(info.getRemark());
		order.setMobile(info.getOrderDetail().getReceivePhone());
		order.setTelphone(info.getOrderDetail().getReceivePhone());
		order.setOpcode("add_order");
		order.setPost_code(info.getOrderDetail().getReceiveZipCode());
		order.setProvince(info.getOrderDetail().getReceiveProvince());
		order.setMerchant_id(appKey);
		List<XinYunGoods> list = new ArrayList<XinYunGoods>();
		XinYunGoods xinYunGoods = null;
		for (OrderGoods goods : info.getOrderGoodsList()) {
			xinYunGoods = new XinYunGoods();
			xinYunGoods.setSku_id(goods.getItemCode());
			xinYunGoods.setQuantity(goods.getItemQuantity() + "");
			list.add(xinYunGoods);
		}
		order.setItems(list);

		String sign = SignUtil.XinYunSing(JSONUtil.toJson(order), secret);
		order.setSign(sign);

		return JSONUtil.toJson(order);
	}

	public static String getXinYunOrderStatusMsg(String orderId, String appKey, String secret) {
		XinYunGetOrderStatus orderStatus = new XinYunGetOrderStatus();
		orderStatus.setOpcode("get_order_info");
		orderStatus.setMerchant_id(appKey);
		orderStatus.setOrder_no(orderId);
		String sign = SignUtil.XinYunSing(JSONUtil.toJson(orderStatus), secret);
		orderStatus.setSign(sign);
		return JSONUtil.toJson(orderStatus);
	}

	public static String getXinYunStock(List<OrderBussinessModel> list, String appKey, String secret) {
		StringBuilder sb = new StringBuilder();
		for (OrderBussinessModel model : list) {
			sb.append(model.getItemCode() + ",");
		}
		XinYunCheckStock checkStock = new XinYunCheckStock();
		checkStock.setMerchant_id(appKey);
		checkStock.setOpcode("get_goods_stock");
		checkStock.setSku_list(sb.toString().substring(0, sb.length() - 1));
		
		String sign = SignUtil.XinYunSing(JSONUtil.toJson(checkStock), secret);
		checkStock.setSign(sign);
		
		return JSONUtil.toJson(checkStock);

	}

	public static String getFuBangOrderMsg(OrderInfo info, UserInfo user) {
		FuBangOrder order = new FuBangOrder();
		order.setCity(info.getOrderDetail().getReceiveCity());
		order.setConsignee(info.getOrderDetail().getReceiveName());
		order.setConsignee_addr(info.getOrderDetail().getReceiveAddress());
		order.setConsignee_mobile(info.getOrderDetail().getReceivePhone());
		order.setDistrict(info.getOrderDetail().getReceiveArea());
		order.setOrder_no(info.getOrderId());
		order.setProvince(info.getOrderDetail().getReceiveProvince());
		order.setName(user.getUserDetail().getName());
		order.setId_num(user.getUserDetail().getIdNum());
		order.setLogistics_name("中通速递");
		FuBangOrderGoods goods = null;
		List<FuBangOrderGoods> list = new ArrayList<FuBangOrderGoods>();
		for(OrderGoods temGoods : info.getOrderGoodsList()){
			goods = new FuBangOrderGoods();
			goods.setProduct_no(temGoods.getItemCode());
			goods.setQty(temGoods.getItemQuantity());
			list.add(goods);
		}
		order.setGoods(list);
		
		return JSONUtil.toJson(order);
	}

	public static String getFuBangOrderStatusMsg(List<String> orderIds) {
		
		return "{\"order_no\":"+orderIds.get(0)+"}";
	}

	public static String getFuBangStock(List<OrderBussinessModel> list) {
		
		return "{\"product_no\":"+list.get(0).getItemCode()+"}";
	}

	public static String getFuBangGoodsDetail(String itemCode) {
		return "{\"product_no\":"+itemCode+"}";
	}

	public static String getXinYunGoods(String itemCode, String appKey, String appSecret) {
		GetXinYunGoodsParam param = new GetXinYunGoodsParam();
		param.setMerchant_id(appKey);
		param.setOpcode("goods_detail");
		param.setSku_id(itemCode);
		
		String sign = SignUtil.XinYunSing(JSONUtil.toJson(param), appSecret);
		param.setSign(sign);
		
		return JSONUtil.toJson(param);

	}
}
