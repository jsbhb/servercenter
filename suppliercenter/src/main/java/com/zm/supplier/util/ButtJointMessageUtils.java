package com.zm.supplier.util;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.seatent.opensdk.input.hdServiceProvider.CreateOrderInputDto;
import com.seatent.opensdk.input.hdServiceProvider.GetGoodsInfoApiInputDto;
import com.zm.supplier.constants.Constants;
import com.zm.supplier.custominf.model.CustomConfig;
import com.zm.supplier.pojo.OrderBussinessModel;
import com.zm.supplier.pojo.OrderGoods;
import com.zm.supplier.pojo.OrderIdAndSupplierId;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.callback.ReceiveLogisticsCompany;
import com.zm.supplier.supplierinf.impl.HaiDaiButtjoint;
import com.zm.supplier.supplierinf.model.DolphinOrder;
import com.zm.supplier.supplierinf.model.FuBangOrder;
import com.zm.supplier.supplierinf.model.FuBangOrderGoods;
import com.zm.supplier.supplierinf.model.GetXinYunGoodsParam;
import com.zm.supplier.supplierinf.model.JiaBeiAiTeOrder;
import com.zm.supplier.supplierinf.model.JiaBeiAiTeOrderGoods;
import com.zm.supplier.supplierinf.model.LianYouOrder;
import com.zm.supplier.supplierinf.model.LianYouOrder.ShipType;
import com.zm.supplier.supplierinf.model.OutOrderGoods;
import com.zm.supplier.supplierinf.model.XinYunCheckStock;
import com.zm.supplier.supplierinf.model.XinYunGetOrderStatus;
import com.zm.supplier.supplierinf.model.XinYunGoods;
import com.zm.supplier.supplierinf.model.XinYunOrder;

public class ButtJointMessageUtils {

	public static String getTianTianOrderMsg(OrderInfo info, String customer, String unionPayMerId, String shopId) {
		StringBuilder sb = new StringBuilder();
		String source = "";
		if (Constants.ALI_PAY.equals(info.getOrderDetail().getPayType())) {// 支付方式
			source = "ALIPAY";
		} else if (Constants.WX_PAY.equals(info.getOrderDetail().getPayType())) {
			source = "TENPAY";
		} else if (Constants.UNION_PAY.equals(info.getOrderDetail().getPayType())) {
			source = "UNIONPAY";
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
		sb.append(shopId); // 店铺代码正式
		// sb.append("11612"); // 店铺代码测试
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
		sb.append(info.getOrderDetail().getCustomerPhone()); // 购物网站买家账号
		sb.append("</buyerAccount>\n");
		sb.append("<phone>");
		sb.append(info.getOrderDetail().getCustomerPhone()); // 手机号
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
		if (info.getOrderDetail().getDisAmount() > 0) {
			for (OrderGoods goods : info.getOrderGoodsList()) {
				double proAmount = CalculationUtils.sub(goods.getItemPrice(), goods.getActualPrice());
				proAmount = CalculationUtils.mul(proAmount, goods.getItemQuantity());
				sb.append("<promotion>\n");
				sb.append("<proAmount>");
				sb.append(proAmount); // 优惠金额
				sb.append("</proAmount>\n");
				sb.append("</promotion>\n");
			}
		} else {
			sb.append("<promotion>\n");
			sb.append("<proAmount>");
			sb.append(info.getOrderDetail().getDisAmount()); // 优惠金额
			sb.append("</proAmount>\n");
			sb.append("</promotion>\n");
		}
		sb.append("</promotions>\n");
		sb.append("<goods>\n");
		for (OrderGoods goods : info.getOrderGoodsList()) {
			sb.append("<detail>\n");
			sb.append("<productId>");
			sb.append(goods.getItemCode()); // 货号'3105166008N0000002'
			sb.append("</productId>\n");
			sb.append("<qty>");
			sb.append(goods.getItemQuantity()); // 数量
			sb.append("</qty>\n");
			sb.append("<price>");
			sb.append(goods.getItemPrice()); // 商品单价
			sb.append("</price>\n");
			sb.append("<amount>");
			sb.append(CalculationUtils.mul(goods.getItemPrice(), goods.getItemQuantity())); // 商品金额
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
		sb.append(info.getOrderDetail().getCustomerIdNum()); // 身份证
		sb.append("</idnum>\n");
		sb.append("<name>");
		sb.append(info.getOrderDetail().getCustomerName()); // 真实姓名
		sb.append("</name>\n");
		if (Constants.UNION_PAY.equals(info.getOrderDetail().getPayType())) {
			sb.append("<merId>");
			sb.append(unionPayMerId);
			sb.append("</merId>\n");
		}
		sb.append("</pay>\n");
		sb.append("<logistics>");
		sb.append("<logisticsCode>");
		sb.append("YUNDA"); // 快递公司代码
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

	public static String getLiangYouOrderMsg(OrderInfo info, Integer supplierId) {
		try {

			LianYouOrder order = new LianYouOrder();
			if (Constants.LIANGYOU_NB_SUPPLIERID.equals(supplierId)) {
				order.setShipping_id(ShipType.NINGBO_BONDED_EXPRESS.getIndex());
			}
			order.setAddress(URLEncoder.encode(info.getOrderDetail().getReceiveAddress(), "utf-8"));
			order.setCity(URLEncoder.encode(info.getOrderDetail().getReceiveCity(), "utf-8"));
			order.setConsignee(URLEncoder.encode(info.getOrderDetail().getReceiveName(), "utf-8"));
			order.setCounty(URLEncoder.encode(info.getOrderDetail().getReceiveArea(), "utf-8"));
			order.setImId(URLEncoder.encode(info.getOrderDetail().getCustomerIdNum(), "utf-8"));
			order.setOrder_sn(URLEncoder.encode(info.getOrderId(), "utf-8"));
			order.setOut_order_sn(URLEncoder.encode(info.getOrderId(), "utf-8"));
			order.setPhoneMob(URLEncoder.encode(info.getOrderDetail().getReceivePhone(), "utf-8"));
			order.setProvince(URLEncoder.encode(info.getOrderDetail().getReceiveProvince(), "utf-8"));
			order.setRealName(URLEncoder.encode(info.getOrderDetail().getCustomerName(), "utf-8"));
			List<OutOrderGoods> list = new ArrayList<OutOrderGoods>();
			OutOrderGoods outOrderGoods = null;
			for (OrderGoods goods : info.getOrderGoodsList()) {
				outOrderGoods = new OutOrderGoods();
				outOrderGoods.setGoods_name(URLEncoder.encode(goods.getItemName(), "utf-8"));
				outOrderGoods.setOnly_sku(goods.getItemCode());
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

	public static String getLiangYouStock(List<OrderBussinessModel> list) {
		StringBuilder sb = new StringBuilder();
		if (list != null && list.size() > 0) {
			for (OrderBussinessModel model : list) {
				sb.append(model.getItemCode() + ",");
			}
		}
		return sb.toString().substring(0, sb.length() - 1);
	}

	public static String getXinYunOrderMsg(OrderInfo info, String appKey, String secret) {

		XinYunOrder order = new XinYunOrder();
		order.setMerchant_id(appKey);
		order.setAccept_name(info.getOrderDetail().getCustomerName());
		order.setAddress(info.getOrderDetail().getReceiveAddress());
		order.setArea(info.getOrderDetail().getReceiveArea());
		order.setCard_id(info.getOrderDetail().getCustomerIdNum());
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

	public static String getFuBangOrderMsg(OrderInfo info) {
		FuBangOrder order = new FuBangOrder();
		order.setCity(info.getOrderDetail().getReceiveCity());
		order.setConsignee(info.getOrderDetail().getReceiveName());
		order.setConsignee_addr(info.getOrderDetail().getReceiveAddress());
		order.setConsignee_mobile(info.getOrderDetail().getReceivePhone());
		order.setDistrict(info.getOrderDetail().getReceiveArea());
		order.setOrder_no(info.getOrderId());
		order.setProvince(info.getOrderDetail().getReceiveProvince());
		order.setName(info.getOrderDetail().getCustomerName());
		order.setId_num(info.getOrderDetail().getCustomerIdNum());
		order.setLogistics_name("中通速递");
		FuBangOrderGoods goods = null;
		List<FuBangOrderGoods> list = new ArrayList<FuBangOrderGoods>();
		for (OrderGoods temGoods : info.getOrderGoodsList()) {
			goods = new FuBangOrderGoods();
			goods.setProduct_no(temGoods.getItemCode());
			goods.setQty(temGoods.getItemQuantity());
			list.add(goods);
		}
		order.setGoods(list);

		return JSONUtil.toJson(order);
	}

	public static String getFuBangOrderStatusMsg(String orderId) {

		return "{\"order_no\":" + orderId + "}";
	}

	public static String getFuBangStock(List<OrderBussinessModel> list) {

		return "{\"product_no\":" + list.get(0).getItemCode() + "}";
	}

	public static String getFuBangGoodsDetail(String itemCode) {
		return "{\"product_no\":" + itemCode + "}";
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

	public static String getQianFengOrderMsg(OrderInfo info, String customer, String unionPayMerId) {
		StringBuilder sb = new StringBuilder();
		String source = "";
		if (Constants.ALI_PAY.equals(info.getOrderDetail().getPayType())) {// 支付方式
			source = "02";
		} else if (Constants.WX_PAY.equals(info.getOrderDetail().getPayType())) {
			source = "13";
		} else if (Constants.UNION_PAY.equals(info.getOrderDetail().getPayType())) {
			source = "01";
		}
		sb.append("<?xml version='1.0' encoding='UTF-8'?>\n");
		sb.append("<Message>\n");
		sb.append("<Header>\n");
		sb.append("<CustomsCode>" + customer + "</CustomsCode>\n");
		sb.append("<OrgName>" + customer + "</OrgName>\n");
		sb.append("<CreateTime>" + info.getCreateTime() + "</CreateTime>\n");
		sb.append("</Header>\n");
		sb.append("<Body>\n");
		sb.append("<Order>\n");
		sb.append("<Operation>0</Operation>\n");// 0新增
		sb.append("<TybType>1</TybType>\n");// 1总署版
		sb.append("<OrderShop>11612</OrderShop>\n");// 测试
		// sb.append("<OrderShop>17000</OrderShop>\n");//正式
		sb.append("<OrderFrom>GXHZ</OrderFrom>\n");// 购物网站代码
		sb.append("<OrderNo>" + info.getOrderId() + "</OrderNo>\n");
		sb.append("<PostFee>" + info.getOrderDetail().getPostFee() + "</PostFee>\n");
		sb.append("<InsuranceFee>0</InsuranceFee>\n");
		sb.append("<Amount>" + info.getOrderDetail().getPayment() + "</Amount>\n");
		sb.append("<BuyerAccount>" + info.getOrderDetail().getCustomerPhone() + "</BuyerAccount>\n");
		sb.append("<Phone>" + info.getOrderDetail().getCustomerPhone() + "</Phone>\n");
		sb.append("<TaxAmount>" + info.getOrderDetail().getTaxFee() + "</TaxAmount>\n");
		sb.append("<TariffAmount>0</TariffAmount>\n");
		sb.append("<AddedValueTaxAmount>" + info.getOrderDetail().getIncrementTax() + "</AddedValueTaxAmount>\n");
		sb.append("<ConsumptionDutyAmount>" + info.getOrderDetail().getExciseTax() + "</ConsumptionDutyAmount>\n");
		sb.append("<GrossWeight>");
		sb.append(CalculationUtils.div(info.getWeight(), 1000)); // 毛重
		sb.append("</GrossWeight>\n");
		sb.append("<DisAmount>");
		sb.append(info.getOrderDetail().getDisAmount()); // 优惠金额合计
		sb.append("</DisAmount>\n");
		sb.append("<BuyerIdnum>" + info.getOrderDetail().getCustomerIdNum() + "</BuyerIdnum>\n");
		sb.append("<BuyerName>" + info.getOrderDetail().getCustomerName() + "</BuyerName>\n");
		sb.append("<BuyerIsPayer>1</BuyerIsPayer>\n");
		sb.append("<WarehouseCode>2345678910</WarehouseCode>\n");
		sb.append("<LogisticsNo></LogisticsNo>\n");
		sb.append("<LogisticsName>中通速递</LogisticsName>\n");
		sb.append("<Promotions>\n");
		sb.append("<Promotion>\n");
		sb.append("<ProAmount>");
		sb.append(info.getOrderDetail().getDisAmount()); // 优惠金额
		sb.append("</ProAmount>\n");
		sb.append("</Promotion>\n");
		sb.append("</Promotions>\n");
		sb.append("<Goods>\n");
		for (OrderGoods goods : info.getOrderGoodsList()) {
			sb.append("<Detail>\n");
			sb.append("<ProductId>");
			sb.append(goods.getItemCode()); // 货号'3105166008N0000002'
			sb.append("</ProductId>\n");
			sb.append("<GoodsName>" + goods.getItemName() + "</GoodsName>\n");
			sb.append("<Qty>");
			sb.append(goods.getItemQuantity()); // 数量
			sb.append("</Qty>\n");
			sb.append("<Unit>");
			sb.append(goods.getUnit()); // 计量单位
			sb.append("</Unit>\n");
			sb.append("<Price>");
			sb.append(goods.getItemPrice()); // 商品单价
			sb.append("</Price>\n");
			sb.append("<Amount>");
			sb.append(CalculationUtils.mul(goods.getActualPrice(), goods.getItemQuantity())); // 商品金额
			sb.append("</Amount>\n");
			sb.append("</Detail>\n");
		}
		sb.append("</Goods>\n");
		sb.append("</Order>\n");
		sb.append("<Pay>\n");
		sb.append("<Paytime>");
		sb.append(info.getOrderDetail().getPayTime()); // 支付时间
		sb.append("</Paytime>\n");
		sb.append("<PaymentNo>");
		sb.append(info.getOrderDetail().getPayNo()); // 支付单号
		sb.append("</PaymentNo>\n");
		sb.append("<OrderSeqNo>");
		sb.append(info.getOrderDetail().getPayNo()); // 商家送支付机构订单交易号,如无，请与支付单号一致
		sb.append("</OrderSeqNo>\n");
		sb.append("<Source>");
		sb.append(source); // 支付方式代码
		sb.append("</Source>\n");
		sb.append("<Idnum>");
		sb.append(info.getOrderDetail().getCustomerIdNum()); // 身份证
		sb.append("</Idnum>\n");
		sb.append("<Name>");
		sb.append(info.getOrderDetail().getCustomerName()); // 真实姓名
		sb.append("</Name>\n");
		if (Constants.UNION_PAY.equals(info.getOrderDetail().getPayType())) {
			sb.append("<MerId>");
			sb.append(unionPayMerId);
			sb.append("</MerId>\n");
		}
		sb.append("</Pay>\n");
		sb.append("<Logistics>");
		sb.append("<Consignee>");
		sb.append(info.getOrderDetail().getReceiveName()); // 收货人名称
		sb.append("</Consignee>\n");
		sb.append("<Province>");
		sb.append(info.getOrderDetail().getReceiveProvince()); // 省
		sb.append("</Province>\n");
		sb.append("<City>");
		sb.append(info.getOrderDetail().getReceiveCity()); // 市
		sb.append("</City>\n");
		sb.append("<District>");
		sb.append(info.getOrderDetail().getReceiveArea()); // 区
		sb.append("</District>\n");
		sb.append("<ConsigneeAddr>");
		sb.append(info.getOrderDetail().getReceiveProvince() + info.getOrderDetail().getReceiveCity()
				+ info.getOrderDetail().getReceiveArea() + info.getOrderDetail().getReceiveAddress()); // 收货地址
		sb.append("</ConsigneeAddr>\n");
		sb.append("<ConsigneeTel>");
		sb.append(info.getOrderDetail().getReceivePhone()); // 收货电话
		sb.append("</ConsigneeTel>\n");
		sb.append("<MailNo>" + info.getOrderDetail().getReceiveZipCode() + "</MailNo>\n");
		sb.append("</Logistics>\n");
		sb.append("</Body>\n");
		sb.append("</Message>");

		return sb.toString();
	}

	public static String getQianFengCheckOrderMsg(String orderId, String customer) {
		StringBuilder sb = new StringBuilder();
		sb.append("<orderRequest>\n");
		sb.append("<psno></psno>\n");
		sb.append("<corpcode>" + customer + "</corpcode>\n");
		sb.append("<orderno>" + orderId + "</orderno>\n");
		sb.append("</orderRequest>\n");

		return sb.toString();
	}

	public static CreateOrderInputDto getHaiDaiOrderDto(OrderInfo info, HaiDaiButtjoint joint) {
		CreateOrderInputDto dto = new CreateOrderInputDto();
		dto.setAccountId(joint.getAccountId());
		dto.setAddress(info.getOrderDetail().getReceiveProvince() + info.getOrderDetail().getReceiveCity()
				+ info.getOrderDetail().getReceiveArea() + info.getOrderDetail().getReceiveAddress()
				+ info.getOrderDetail().getReceiveName());
		dto.setAppkey(joint.getAppKey());
		dto.setIdentification(info.getOrderDetail().getCustomerIdNum());
		dto.setName(info.getOrderDetail().getCustomerName());
		dto.setCustomOrder(info.getOrderId());
		dto.setMemberId(joint.getMemberId());
		dto.setUrl(joint.getUrl());
		dto.setSecret(joint.getAppSecret());
		dto.setMobile(info.getOrderDetail().getReceivePhone());
		StringBuilder goodsBuilder = new StringBuilder();
		StringBuilder numsBuilder = new StringBuilder();
		StringBuilder productBuilder = new StringBuilder();
		for (OrderGoods goods : info.getOrderGoodsList()) {
			goodsBuilder.append(goods.getItemCode() + ",");
			numsBuilder.append(goods.getItemQuantity() + ",");
			productBuilder.append(goods.getConversion() + ",");
		}
		dto.setGoodsIds(goodsBuilder.substring(0, goodsBuilder.length() - 1));
		dto.setNums(numsBuilder.substring(0, numsBuilder.length() - 1));
		dto.setProductNums(productBuilder.substring(0, productBuilder.length() - 1));
		return dto;
	}

	public static GetGoodsInfoApiInputDto getHaiDaiGoodsInfoDto(String itemCode, HaiDaiButtjoint joint) {
		GetGoodsInfoApiInputDto dto = new GetGoodsInfoApiInputDto();
		dto.setAccountId(joint.getAccountId());
		dto.setAppkey(joint.getAppKey());
		dto.setSecret(joint.getAppSecret());
		dto.setUrl(joint.getUrl());
		dto.setGoodsId(itemCode);
		dto.setMemberId(joint.getMemberId());
		dto.setNeedSEO(0);
		return dto;
	}

	public static String getKJB2COrderMsg(OrderInfo info, String unionPayMerId, String expressId) {
		StringBuilder sb = new StringBuilder();
		String source = "";
		if (Constants.ALI_PAY.equals(info.getOrderDetail().getPayType())) {// 支付方式
			source = "02";
		} else if (Constants.WX_PAY.equals(info.getOrderDetail().getPayType())) {
			source = "13";
		} else if (Constants.UNION_PAY.equals(info.getOrderDetail().getPayType())) {
			source = "01";
		}
		sb.append("<?xml version='1.0' encoding='UTF-8'?>\n");
		sb.append("<Message>\n");
		sb.append("<Header>\n");
		sb.append("<CustomsCode>");
		sb.append("3302462230"); // 电商企业代码
		sb.append("</CustomsCode>\n");
		sb.append("<OrgName>");
		sb.append("宁波鑫海通达贸易有限公司"); // 电商企业名称
		sb.append("</OrgName>\n");
		sb.append("<CreateTime>");
		sb.append(info.getCreateTime()); // 订单创建时间
		sb.append("</CreateTime>\n");
		sb.append("</Header>\n");
		sb.append("<Body>\n");
		sb.append("<Order>\n");
		sb.append("<Operation>");
		sb.append("0");
		sb.append("</Operation>\n");
		sb.append("<OrderShop>");
		sb.append("17000"); // 店铺代码正式
		sb.append("</OrderShop>\n");
		sb.append("<OrderFrom>");
		sb.append("0000"); // 购物网站代码
		sb.append("</OrderFrom>\n");
		sb.append("<OrderNo>");
		sb.append(info.getOrderId()); // 订单号
		sb.append("</OrderNo>\n");
		sb.append("<PostFee>");
		sb.append(info.getOrderDetail().getPostFee()); // 运费
		sb.append("</PostFee>\n");
		sb.append("<Amount>");
		sb.append(info.getOrderDetail().getPayment()); // 买家实付金额
		sb.append("</Amount>\n");
		sb.append("<BuyerAccount>");
		sb.append(info.getOrderDetail().getCustomerPhone()); // 购物网站买家账号
		sb.append("</BuyerAccount>\n");
		sb.append("<Phone>");
		sb.append(info.getOrderDetail().getCustomerPhone()); // 手机号
		sb.append("</Phone>\n");
		sb.append("<TaxAmount>");
		sb.append(info.getOrderDetail().getTaxFee()); // 税额
		sb.append("</TaxAmount>\n");
		sb.append("<TariffAmount>");
		sb.append(info.getOrderDetail().getTariffTax()); // 关税额
		sb.append("</TariffAmount>\n");
		sb.append("<AddedValueTaxAmount>");
		sb.append(info.getOrderDetail().getIncrementTax()); // 增值税额
		sb.append("</AddedValueTaxAmount>\n");
		sb.append("<ConsumptionDutyAmount>");
		sb.append(info.getOrderDetail().getExciseTax()); // 消费税额
		sb.append("</ConsumptionDutyAmount>\n");
		sb.append("<GrossWeight>");
		sb.append(CalculationUtils.div(info.getWeight(), 1000)); // 毛重
		sb.append("</GrossWeight>\n");
		sb.append("<DisAmount>");
		sb.append(info.getOrderDetail().getDisAmount()); // 优惠金额合计
		sb.append("</DisAmount>\n");
		sb.append("<BuyerIdnum>" + info.getOrderDetail().getCustomerIdNum() + "</BuyerIdnum>\n");
		sb.append("<BuyerName>" + info.getOrderDetail().getCustomerName() + "</BuyerName>\n");
		sb.append("<BuyerIsPayer>1</BuyerIsPayer>\n");
		sb.append("<Promotions>\n");
		if (info.getOrderDetail().getDisAmount() > 0) {
			for (OrderGoods goods : info.getOrderGoodsList()) {
				double proAmount = CalculationUtils.sub(goods.getItemPrice(), goods.getActualPrice());
				proAmount = CalculationUtils.mul(proAmount, goods.getItemQuantity());
				sb.append("<Promotion>\n");
				sb.append("<ProAmount>");
				sb.append(proAmount); // 优惠金额
				sb.append("</ProAmount>\n");
				sb.append("</Promotion>\n");
			}
		} else {
			sb.append("<Promotion>\n");
			sb.append("<ProAmount>");
			sb.append(info.getOrderDetail().getDisAmount()); // 优惠金额
			sb.append("</ProAmount>\n");
			sb.append("</Promotion>\n");
		}
		sb.append("</Promotions>\n");
		sb.append("<Goods>\n");
		for (OrderGoods goods : info.getOrderGoodsList()) {
			sb.append("<Detail>\n");
			sb.append("<ProductId>");
			sb.append(goods.getItemCode()); // 货号'3105166008N0000002'
			sb.append("</ProductId>\n");
			sb.append("<GoodsName>");
			sb.append(goods.getItemName()); // 商品名称
			sb.append("</GoodsName>\n");
			sb.append("<Qty>");
			sb.append(goods.getItemQuantity()); // 数量
			sb.append("</Qty>\n");
			sb.append("<Unit>");
			sb.append(goods.getUnit()); // 单位
			sb.append("</Unit>\n");
			sb.append("<Price>");
			sb.append(goods.getItemPrice()); // 商品单价
			sb.append("</Price>\n");
			sb.append("<Amount>");
			sb.append(CalculationUtils.mul(goods.getItemPrice(), goods.getItemQuantity())); // 商品金额
			sb.append("</Amount>\n");
			sb.append("</Detail>\n");
		}
		sb.append("</Goods>\n");
		sb.append("</Order>\n");
		sb.append("<Pay>\n");
		sb.append("<Paytime>");
		sb.append(info.getOrderDetail().getPayTime()); // 支付时间
		sb.append("</Paytime>\n");
		sb.append("<PaymentNo>");
		sb.append(info.getOrderDetail().getPayNo()); // 支付单号
		sb.append("</PaymentNo>\n");
		sb.append("<OrderSeqNo>");
		sb.append(info.getOrderDetail().getPayNo()); // 商家送支付机构订单交易号,如无，请与支付单号一致
		sb.append("</OrderSeqNo>\n");
		sb.append("<Source>");
		sb.append(source); // 支付方式代码
		sb.append("</Source>\n");
		sb.append("<Idnum>");
		sb.append(info.getOrderDetail().getCustomerIdNum()); // 身份证
		sb.append("</Idnum>\n");
		sb.append("<Name>");
		sb.append(info.getOrderDetail().getCustomerName()); // 真实姓名
		sb.append("</Name>\n");
		if (Constants.UNION_PAY.equals(info.getOrderDetail().getPayType())) {
			sb.append("<MerId>");
			sb.append(unionPayMerId);
			sb.append("</MerId>\n");
		}
		sb.append("</Pay>\n");
		sb.append("<Logistics>");
		sb.append("<LogisticsName>");
		sb.append("韵达速递"); // 快递公司代码
							// SF=顺丰速EMS=邮政速递POSTAM=邮政小包ZTO=中通速递STO=申通快递YTO=圆通速递JD=京东快递BEST=百世物流YUNDA=韵达速递TTKDEX=天天快递
		sb.append("</LogisticsName>\n");
		sb.append("<LogisticsNo>");
		sb.append(expressId);
		sb.append("</LogisticsNo>\n");
		sb.append("<Consignee>");
		sb.append(info.getOrderDetail().getReceiveName()); // 收货人名称
		sb.append("</Consignee>\n");
		sb.append("<Province>");
		sb.append(info.getOrderDetail().getReceiveProvince()); // 省
		sb.append("</Province>\n");
		sb.append("<City>");
		sb.append(info.getOrderDetail().getReceiveCity()); // 市
		sb.append("</City>\n");
		sb.append("<District>");
		sb.append(info.getOrderDetail().getReceiveArea()); // 区
		sb.append("</District>\n");
		sb.append("<ConsigneeAddr>");
		sb.append(info.getOrderDetail().getReceiveProvince() + info.getOrderDetail().getReceiveCity()
				+ info.getOrderDetail().getReceiveArea() + info.getOrderDetail().getReceiveAddress()); // 收货地址
		sb.append("</ConsigneeAddr>\n");
		sb.append("<ConsigneeTel>");
		sb.append(info.getOrderDetail().getReceivePhone()); // 收货电话
		sb.append("</ConsigneeTel>\n");
		sb.append("</Logistics>\n");
		sb.append("</Body>\n");
		sb.append("</Message>\n");

		return sb.toString();
	}

	public static String getJiaBeiAiTeOrderMsg(OrderInfo info) {
		// 佳贝艾特订单状态: 1>等待买家付款 2>等待卖家发货 3>卖家部分发货 4>等待买家确认收货
		// 5>买家已签收确认收货 6>交易成功 7>交易自动关闭 8>交易主动关闭 9>退款中
		// 目前暂时只使用: 2>等待卖家发货 9>退款中
		JiaBeiAiTeOrder order = new JiaBeiAiTeOrder();
		order.setTrade_no(info.getOrderId());
		if (Constants.REFUNDS.equals(info.getStatus())) {// 海外购系统状态
			order.setTrade_status("8");// 佳贝艾特系统状态
		} else {
			order.setTrade_status("2");
		}
		order.setTotal_fee(info.getOrderDetail().getPayment() + "");
		order.setPayment(info.getOrderDetail().getPayment() + "");
		order.setPost_fee(info.getOrderDetail().getPostFee() + "");
		order.setDiscount_fee(info.getOrderDetail().getDisAmount() + "");
		order.setModify_time(info.getUpdateTime());
		order.setCreate_time(info.getCreateTime());
		order.setPay_time(info.getOrderDetail().getPayTime());
		order.setConsign_time("");
		order.setBuyer_message(info.getRemark());
		order.setReceiver_name(info.getOrderDetail().getReceiveName());
		order.setReceiver_mobile(info.getOrderDetail().getReceivePhone());
		order.setReceiver_phone(info.getOrderDetail().getReceivePhone());
		order.setReceiver_zip(info.getOrderDetail().getReceiveZipCode());
		order.setReceiver_email("");
		order.setReceiver_province_code("");
		order.setReceiver_state(info.getOrderDetail().getReceiveProvince());
		order.setReceiver_city_code("");
		order.setReceiver_city(info.getOrderDetail().getReceiveCity());
		order.setReceiver_district_code("");
		order.setReceiver_district(info.getOrderDetail().getReceiveArea());
		order.setReceiver_address(info.getOrderDetail().getReceiveAddress());
		order.setLogistics_code("");
		order.setLogistics_company("");
		order.setWaybill_no("");
		order.setInvoice_title("");
		order.setInvoice_content("");
		order.setSeller_memo(info.getRemark());
		List<JiaBeiAiTeOrderGoods> orderGoodsList = new ArrayList<JiaBeiAiTeOrderGoods>();
		for (OrderGoods goods : info.getOrderGoodsList()) {
			JiaBeiAiTeOrderGoods orderGoods = new JiaBeiAiTeOrderGoods();
			orderGoods.setPop_sku_code(goods.getItemCode());
			orderGoods.setItem_sku_id(goods.getItemId());
			orderGoods.setPop_item_title(goods.getItemName());
			orderGoods.setItem_title(goods.getItemName());
			orderGoods.setNum(goods.getItemQuantity() + "");
			orderGoods.setItem_price(goods.getActualPrice() + "");
			orderGoods
					.setTrade_total_payment(CalculationUtils.mul(goods.getActualPrice(), goods.getItemQuantity()) + "");
			orderGoods.setTrade_coupon_payment("0");
			orderGoodsList.add(orderGoods);
		}
		order.setItemList(orderGoodsList);
		return JSONUtil.toJson(order);
	}

	public static String getZhengZhengOrderMsg(OrderInfo info, String accountId, String memberId) {
		String payMentName = "";
		String payCode = "";
		if (Constants.ALI_PAY.equals(info.getOrderDetail().getPayType())) {// 支付方式
			payMentName = "支付宝";
			payCode = "02";
		} else if (Constants.WX_PAY.equals(info.getOrderDetail().getPayType())) {
			payMentName = "财付通";
			payCode = "13";
		}
		Map<String, Object> orderMap = new HashMap<String, Object>();
		orderMap.put("StoreOrderNo", info.getOrderId());
		orderMap.put("ExpressAmount", info.getOrderDetail().getPostFee());
		orderMap.put("TotalAmount", info.getOrderDetail().getPayment());
		orderMap.put("DiscountsAmount", info.getOrderDetail().getDisAmount());
		orderMap.put("TaxAmount", info.getOrderDetail().getTaxFee());
		orderMap.put("AddedValueTaxAmount", info.getOrderDetail().getIncrementTax());
		orderMap.put("ComsumptionTaxAmount", info.getOrderDetail().getExciseTax());
		orderMap.put("CreateTime", info.getCreateTime());
		orderMap.put("PayTime", info.getOrderDetail().getPayTime());
		orderMap.put("PaymentName", payMentName);
		orderMap.put("Payment", payCode);
		orderMap.put("PaySerialsNo", info.getOrderDetail().getPayNo());
		orderMap.put("CustomerIDCard", info.getOrderDetail().getCustomerIdNum());
		orderMap.put("CustomerName", info.getOrderDetail().getCustomerName());
		orderMap.put("CustomerPhone", info.getOrderDetail().getCustomerPhone());
		orderMap.put("Province", info.getOrderDetail().getReceiveProvince());
		orderMap.put("City", info.getOrderDetail().getReceiveCity());
		orderMap.put("Area", info.getOrderDetail().getReceiveArea());
		orderMap.put("Address", info.getOrderDetail().getReceiveAddress());
		orderMap.put("ConsigneePhone", info.getOrderDetail().getReceivePhone());
		orderMap.put("ConsigneeName", info.getOrderDetail().getReceiveName());
		orderMap.put("CustomerAccount", info.getOrderDetail().getCustomerPhone());
		Map<String, Object> goodsMap = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		double goodsAmount = 0;
		for (OrderGoods goods : info.getOrderGoodsList()) {
			goodsAmount = CalculationUtils.add(goodsAmount,
					CalculationUtils.mul(goods.getActualPrice(), goods.getItemQuantity()));
			goodsMap = new HashMap<String, Object>();
			goodsMap.put("StoreItemNumber", goods.getItemCode());
			goodsMap.put("StoreItemName", goods.getItemName());
			goodsMap.put("Quantity", goods.getItemQuantity());
			goodsMap.put("UnitSellPrice", goods.getActualPrice());
			list.add(goodsMap);
		}
		orderMap.put("Goods_infos", list);
		orderMap.put("GoodsAmount", goodsAmount);
		orderMap.put("StoreID", accountId);
		orderMap.put("StoreName", memberId);
		return JSONUtil.toJson(orderMap);
	}

	public static String getYouStongOrder(List<OrderInfo> infoList) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Map<String, Object> orderMap = null;
		Map<String, Object> goodsMap = null;
		List<Map<String, Object>> list = null;
		for (OrderInfo info : infoList) {
			orderMap = new HashMap<String, Object>();
			orderMap.put("ParterOrderNo", info.getOrderId());
			orderMap.put("ContactName", info.getOrderDetail().getCustomerName());
			orderMap.put("IdCardNo", info.getOrderDetail().getCustomerIdNum());
			orderMap.put("Mobile", info.getOrderDetail().getReceivePhone());
			orderMap.put("Province", info.getOrderDetail().getReceiveProvince());
			orderMap.put("City", info.getOrderDetail().getReceiveCity());
			orderMap.put("District", info.getOrderDetail().getReceiveArea());
			orderMap.put("StreetAddress", info.getOrderDetail().getReceiveAddress());
			orderMap.put("NotPayByBalance", false);
			orderMap.put("NotUsePreOrderedInventory", false);
			list = new ArrayList<Map<String, Object>>();
			for (OrderGoods goods : info.getOrderGoodsList()) {
				goodsMap = new HashMap<String, Object>();
				goodsMap.put("SKUNo", goods.getItemCode());
				goodsMap.put("Qty", goods.getItemQuantity());
				list.add(goodsMap);
			}
			orderMap.put("Items", list);
			result.add(orderMap);
		}

		return JSONUtil.toJson(result);
	}

	public static String getYouStongOrderStatus(List<String> orderIds) {
		return JSONUtil.toJson(orderIds);
	}

	public static String getYouStongStock(List<String> itemCodeList) {

		return JSONUtil.toJson(itemCodeList);
	}

	public static String getKJB2COrderStatus(List<OrderIdAndSupplierId> orderList) {
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version='1.0' encoding='UTF-8'?>\n");
		sb.append("<Message>\n");
		sb.append("<Header>\n");
		sb.append("<MftNo>");
		sb.append(orderList.get(0).getThirdOrderId());
		sb.append("</MftNo>\n");
		sb.append("</Header>\n");
		sb.append("</Message>\n");
		return sb.toString();
	}

	public static String getDolphinOrder(OrderInfo orderInfo) {
		DolphinOrder order = new DolphinOrder(orderInfo);
		List<DolphinOrder> orderList = new ArrayList<>();
		orderList.add(order);
		return JSONUtil.toJson(orderList);
	}

	public static String getDolphinOrderStatus(OrderIdAndSupplierId orderIdAndSupplierId) {
		Map<String, String> param = new HashMap<>();
		param.put("orderSn", orderIdAndSupplierId.getOrderId());
		return JSONUtil.toJson(param);
	}

	/**
	 * @fun 杭州海关订单申报
	 * @param info
	 * @param config
	 * @return
	 */
	public static String getHangZhouCustomOrderMsg(OrderInfo info, CustomConfig config,
			ReceiveLogisticsCompany receiveLogisticsCompany) {
		String payCompanyCode = "";
		if (Constants.ALI_PAY.equals(info.getOrderDetail().getPayType())) {// 支付方式
			payCompanyCode = "ZF14021901";
		} else if (Constants.WX_PAY.equals(info.getOrderDetail().getPayType())) {
			payCompanyCode = "ZF14120401";
		}
		double goodsAmount = info.getOrderGoodsList().stream()
				.mapToDouble(g -> CalculationUtils.mul(g.getItemPrice(), g.getItemQuantity())).sum();
		StringBuilder sb = new StringBuilder();
		sb.append("<mo version=\"1.0.0\">");
		sb.append("<head>");
		sb.append("<businessType>IMPORTORDER</businessType>");
		sb.append("</head>");
		sb.append("<body>");
		sb.append("<orderInfoList>");
		sb.append("<orderInfo>");
		sb.append("<jkfSign>");
		sb.append("<companyCode>" + config.getCompanyCode() + "</companyCode>");
		sb.append("<businessNo>" + info.getOrderId() + "</businessNo>");
		sb.append("<businessType>IMPORTORDER</businessType>");
		sb.append("<declareType>1</declareType>");
		sb.append("<cebFlag>02</cebFlag>");
		sb.append("</jkfSign>");
		sb.append("<jkfOrderImportHead>");
		sb.append("<eCommerceCode>" + config.geteCommerceCode() + "</eCommerceCode>");
		sb.append("<eCommerceName>" + config.getCompanyName() + "</eCommerceName>");
		sb.append("<ieFlag>I</ieFlag>");
		sb.append("<payType>03</payType>");
		sb.append("<payCompanyCode>" + payCompanyCode + "</payCompanyCode>");
		sb.append("<payNumber>" + info.getOrderDetail().getPayNo() + "</payNumber>");
		sb.append("<orderTotalAmount>" + info.getOrderDetail().getPayment() + "</orderTotalAmount>");
		sb.append("<orderNo>" + info.getOrderId() + "</orderNo>");
		sb.append("<orderTaxAmount>" + info.getOrderDetail().getTaxFee() + "</orderTaxAmount>");
		sb.append("<orderGoodsAmount>" + goodsAmount + "</orderGoodsAmount>");
		sb.append("<feeAmount>0</feeAmount>");
		sb.append("<insureAmount>0</insureAmount>");
		sb.append("<companyName>" + config.getCompanyName() + "</companyName>");
		sb.append("<companyCode>" + config.getCompanyCode() + "</companyCode>");
		sb.append("<tradeTime>" + info.getCreateTime() + "</tradeTime>");
		sb.append("<currCode>142</currCode>");
		sb.append("<totalAmount>" + goodsAmount + "</totalAmount>");
		sb.append("<consigneeTel>" + info.getOrderDetail().getReceivePhone() + "</consigneeTel>");
		sb.append("<consignee>" + info.getOrderDetail().getReceiveName() + "</consignee>");
		sb.append("<consigneeAddress>" + info.getOrderDetail().getReceiveProvince()
				+ info.getOrderDetail().getReceiveCity() + info.getOrderDetail().getReceiveArea()
				+ info.getOrderDetail().getReceiveAddress() + "</consigneeAddress>");
		sb.append("<totalCount>" + info.getTdq() + "</totalCount>");
		sb.append("<senderCountry>142</senderCountry>");
		sb.append("<senderName>中国供销海外购</senderName>");
		sb.append("<purchaserId>" + info.getOrderDetail().getCustomerPhone() + "</purchaserId>");
		sb.append("<logisCompanyName>" + receiveLogisticsCompany.getLogisCompanyName() + "</logisCompanyName>");
		sb.append("<logisCompanyCode>" + receiveLogisticsCompany.getLogisCompanyCode() + "</logisCompanyCode>");
		sb.append("<zipCode>" + info.getOrderDetail().getReceiveZipCode() + "</zipCode>");
		sb.append("<rate>1</rate>");
		sb.append("<discount>" + info.getOrderDetail().getDisAmount() + "</discount>");
		sb.append(
				"<userProcotol>本人承诺所购买商品系个人合理自用，现委托商家代理申报、代缴税款等通关事宜，本人保证遵守《海关法》和国家相关法律法规，保证所提供的身份信息和收货信息真实完整，无侵犯他人权益的行为，以上委托关系系如实填写，本人愿意接受海关、检验检疫机构及其他监管部门的监管，并承担相应法律责任。</userProcotol>");
		sb.append("</jkfOrderImportHead>");
		sb.append("<jkfOrderDetailList>");
		sb.append("<jkfOrderDetail>");
		for (int i = 0; i < info.getOrderGoodsList().size(); i++) {
			OrderGoods goods = info.getOrderGoodsList().get(i);
			sb.append("<goodsOrder>" + i + 1 + "</goodsOrder>");
			sb.append("<goodsName>" + goods.getItemName() + "</goodsName>");
			sb.append("<goodsModel>" + goods.getItemName() + "," + goods.getBrand() + "," + goods.getItemInfo()
					+ "</goodsModel>");
			sb.append("<codeTs>" + goods.getHsCode() + "</codeTs>");
			sb.append("<unitPrice>" + goods.getActualPrice() + "</unitPrice>");
			sb.append("<goodsUnit>" + UnitCode.get(goods.getUnit()) + "</goodsUnit>");
			sb.append("<goodsCount>" + goods.getItemQuantity() + "</goodsCount>");
			sb.append("<originCountry>" + CountryCode.get(goods.getOrigin()) + "</originCountry>");
			sb.append("<currency>142</currency>");
		}
		sb.append("</jkfOrderDetail>");
		sb.append("</jkfOrderDetailList>");
		sb.append("<jkfGoodsPurchaser>");
		sb.append("<id>" + info.getOrderDetail().getCustomerPhone() + "</id>");
		sb.append("<name>" + info.getOrderDetail().getCustomerName() + "</name>");
		sb.append("<telNumber>" + info.getOrderDetail().getCustomerPhone() + "</telNumber>");
		sb.append("<paperType>01</paperType>");
		sb.append("<paperNumber>" + info.getOrderDetail().getCustomerIdNum() + "</paperNumber>");
		sb.append("</jkfGoodsPurchaser>");
		sb.append("</orderInfo>");
		sb.append("</orderInfoList>");
		sb.append("</body>");
		sb.append("</mo>");
		return sb.toString();
	}

	public static String getKJBAddSignature(OrderInfo info, ReceiveLogisticsCompany receiveLogisticsCompany,
			CustomConfig config) {
		String payCompanyCode = "";
		String payCompanyName = "";
		if (Constants.ALI_PAY.equals(info.getOrderDetail().getPayType())) {// 支付方式
			payCompanyCode = "ZF14021901";
			payCompanyName = "支付宝(中国)网络技术有限公司";
		} else if (Constants.WX_PAY.equals(info.getOrderDetail().getPayType())) {
			payCompanyCode = "ZF14120401";
			payCompanyName = "财付通支付科技有限公司";
		}
		UUID uuid = UUID.randomUUID();
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<ceb:CEB311Message guid=\"" + uuid.toString().toUpperCase()
				+ "\" version=\"1.0\"  xmlns:ceb=\"http://www.chinaport.gov.cn/ceb\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
		sb.append("<ceb:Order>");
		sb.append("<ceb:OrderHead>");
		sb.append("<ceb:guid>" + uuid.toString().toUpperCase() + "</ceb:guid>");
		sb.append("<ceb:appType>1</ceb:appType>");
		sb.append("<ceb:appTime>" + DateUtil.getDateString(new Date(), "YYYYMMDDhhmmss") + "</ceb:appTime>");
		sb.append("<ceb:appStatus>2</ceb:appStatus><ceb:orderType>I</ceb:orderType>");
		sb.append("<ceb:orderNo>" + info.getOrderId() + "</ceb:orderNo>");
		sb.append("<ceb:ebpCode>" + config.geteCommerceCode() + "</ceb:ebpCode>");
		sb.append("<ceb:ebpName>" + config.geteCommerceName() + "</ceb:ebpName>");
		sb.append("<ceb:ebcCode>" + config.getCompanyCode() + "</ceb:ebcCode>");
		sb.append("<ceb:ebcName>" + config.getCompanyName() + "</ceb:ebcName>");
		double goodsAmount = info.getOrderGoodsList().stream()
				.mapToDouble(g -> CalculationUtils.mul(g.getItemPrice(), g.getItemQuantity())).sum();
		sb.append("<ceb:goodsValue>" + goodsAmount + "</ceb:goodsValue>");
		sb.append("<ceb:freight>0</ceb:freight>");
		sb.append("<ceb:discount>0</ceb:discount>");
		sb.append("<ceb:taxTotal>" + info.getOrderDetail().getTaxFee() + "</ceb:taxTotal>");
		sb.append("<ceb:acturalPaid>" + info.getOrderDetail().getPayment() + "</ceb:acturalPaid>");
		sb.append("<ceb:currency>142</ceb:currency>");
		sb.append("<ceb:buyerRegNo>" + info.getOrderDetail().getCustomerPhone() + "</ceb:buyerRegNo>");
		sb.append("<ceb:buyerName>" + info.getOrderDetail().getCustomerName() + "</ceb:buyerName>");
		sb.append("<ceb:buyerTelephone>" + info.getOrderDetail().getCustomerPhone() + "</ceb:buyerTelephone>");
		sb.append("<ceb:buyerIdType>1</ceb:buyerIdType>");
		sb.append("<ceb:buyerIdNumber>" + info.getOrderDetail().getCustomerIdNum() + "</ceb:buyerIdNumber>");
		sb.append("<ceb:payCode>" + payCompanyCode + "</ceb:payCode>");
		sb.append("<ceb:payName>" + payCompanyName + "</ceb:payName>");
		sb.append("<ceb:payTransactionId>" + info.getOrderDetail().getPayNo() + "</ceb:payTransactionId>");
		sb.append("<ceb:consignee>" + info.getOrderDetail().getReceiveName() + "</ceb:consignee>");
		sb.append("<ceb:consigneeTelephone>" + info.getOrderDetail().getReceivePhone() + "</ceb:consigneeTelephone>");
		sb.append("<ceb:consigneeAddress>" + info.getOrderDetail().getReceiveProvince()
				+ info.getOrderDetail().getReceiveCity() + info.getOrderDetail().getReceiveArea()
				+ info.getOrderDetail().getReceiveAddress() + "</ceb:consigneeAddress>");
		sb.append("</ceb:OrderHead>");
		for (int i = 0; i < info.getOrderGoodsList().size(); i++) {
			OrderGoods goods = info.getOrderGoodsList().get(i);
			sb.append("<ceb:OrderList>");
			sb.append("<ceb:gnum>" + i + 1 + "</ceb:gnum>");
			sb.append("<ceb:itemNo>" + goods.getItemId() + "</ceb:itemNo>");
			sb.append("<ceb:itemName>" + goods.getItemName() + "</ceb:itemName>");
			sb.append("<ceb:gmodel>" + goods.getItemName() + "," + goods.getBrand() + "," + goods.getItemInfo()
					+ "</ceb:gmodel>");
			sb.append("<ceb:unit>" + UnitCode.get(goods.getUnit()) + "</ceb:unit>");
			sb.append("<ceb:qty>" + goods.getItemQuantity() + "</ceb:qty>");
			sb.append("<ceb:price>" + goods.getItemPrice() + "</ceb:price>");
			sb.append("<ceb:totalPrice>" + CalculationUtils.mul(goods.getItemPrice(), goods.getItemQuantity())
					+ "</ceb:totalPrice>");
			sb.append("<ceb:currency>142</ceb:currency>");
			sb.append("<ceb:country>" + CountryCode.get(goods.getOrigin()) + "</ceb:country>");
			sb.append("</ceb:OrderList>");
		}
		sb.append("</ceb:Order>");
		sb.append("<ceb:BaseTransfer>");
		sb.append("<ceb:copCode>" + config.getCompanyCode() + "</ceb:copCode>");
		sb.append("<ceb:copName>" + config.getCompanyName() + "</ceb:copName>");
		sb.append("<ceb:dxpMode>DXP</ceb:dxpMode>");
		sb.append("<ceb:dxpId>" + config.getDxPid() + "</ceb:dxpId>");
		sb.append("</ceb:BaseTransfer>");
		sb.append("</ceb:CEB311Message>");
		return sb.toString();
	}
}
