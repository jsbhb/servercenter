package com.zm.supplier.util;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.seatent.opensdk.input.hdServiceProvider.CreateOrderInputDto;
import com.seatent.opensdk.input.hdServiceProvider.GetGoodsInfoApiInputDto;
import com.zm.supplier.constants.Constants;
import com.zm.supplier.pojo.OrderBussinessModel;
import com.zm.supplier.pojo.OrderGoods;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.UserInfo;
import com.zm.supplier.supplierinf.impl.HaiDaiButtjoint;
import com.zm.supplier.supplierinf.model.AiJiaBeiTeOrder;
import com.zm.supplier.supplierinf.model.AiJiaBeiTeOrderGoods;
import com.zm.supplier.supplierinf.model.FuBangOrder;
import com.zm.supplier.supplierinf.model.FuBangOrderGoods;
import com.zm.supplier.supplierinf.model.GetXinYunGoodsParam;
import com.zm.supplier.supplierinf.model.LianYouOrder;
import com.zm.supplier.supplierinf.model.LianYouOrder.ShipType;
import com.zm.supplier.supplierinf.model.OutOrderGoods;
import com.zm.supplier.supplierinf.model.XinYunCheckStock;
import com.zm.supplier.supplierinf.model.XinYunGetOrderStatus;
import com.zm.supplier.supplierinf.model.XinYunGoods;
import com.zm.supplier.supplierinf.model.XinYunOrder;

public class ButtJointMessageUtils {

	public static String getTianTianOrderMsg(OrderInfo info, UserInfo user, String customer, String unionPayMerId,
			String shopId) {
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
		sb.append(user.getUserDetail().getIdNum()); // 身份证
		sb.append("</idnum>\n");
		sb.append("<name>");
		sb.append(user.getUserDetail().getName()); // 真实姓名
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

	public static String getLiangYouOrderMsg(OrderInfo info, UserInfo user, Integer supplierId) {
		try {

			LianYouOrder order = new LianYouOrder();
			if (Constants.LIANGYOU_NB_SUPPLIERID.equals(supplierId)) {
				order.setShipping_id(ShipType.NINGBO_BONDED_EXPRESS.getIndex());
			}
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
		for (OrderGoods temGoods : info.getOrderGoodsList()) {
			goods = new FuBangOrderGoods();
			goods.setProduct_no(temGoods.getItemCode());
			goods.setQty(temGoods.getItemQuantity());
			list.add(goods);
		}
		order.setGoods(list);

		return JSONUtil.toJson(order);
	}

	public static String getFuBangOrderStatusMsg(List<String> orderIds) {

		return "{\"order_no\":" + orderIds.get(0) + "}";
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

	public static String getQianFengOrderMsg(OrderInfo info, UserInfo user, String customer, String unionPayMerId) {
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
		sb.append("<BuyerAccount>" + user.getPhone() + "</BuyerAccount>\n");
		sb.append("<Phone>" + user.getPhone() + "</Phone>\n");
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
		sb.append("<BuyerIdnum>" + user.getUserDetail().getIdNum() + "</BuyerIdnum>\n");
		sb.append("<BuyerName>" + user.getUserDetail().getName() + "</BuyerName>\n");
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
		sb.append(user.getUserDetail().getIdNum()); // 身份证
		sb.append("</Idnum>\n");
		sb.append("<Name>");
		sb.append(user.getUserDetail().getName()); // 真实姓名
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

	public static String getQianFengCheckOrderMsg(List<String> orderIds, String customer) {
		StringBuilder sb = new StringBuilder();
		sb.append("<orderRequest>\n");
		sb.append("<psno></psno>\n");
		sb.append("<corpcode>" + customer + "</corpcode>\n");
		sb.append("<orderno>" + orderIds.get(0) + "</orderno>\n");
		sb.append("</orderRequest>\n");

		return sb.toString();
	}

	public static CreateOrderInputDto getHaiDaiOrderDto(OrderInfo info, UserInfo user, HaiDaiButtjoint joint) {
		CreateOrderInputDto dto = new CreateOrderInputDto();
		dto.setAccountId(joint.getAccountId());
		dto.setAddress(info.getOrderDetail().getReceiveProvince() + info.getOrderDetail().getReceiveCity()
				+ info.getOrderDetail().getReceiveArea() + info.getOrderDetail().getReceiveAddress()
				+ info.getOrderDetail().getReceiveName());
		dto.setAppkey(joint.getAppKey());
		dto.setIdentification(user.getUserDetail().getIdNum());
		dto.setName(user.getUserDetail().getName());
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

	public static String getKJB2COrderMsg(OrderInfo info, UserInfo user, String unionPayMerId) {
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
		sb.append("<request>\n");
		sb.append("<head>\n");
		sb.append("<CustomsCode>");
		sb.append("3302462230 "); // 电商企业代码
		sb.append("</CustomsCode>\n");
		sb.append("<OrgName>");
		sb.append("宁波鑫海通达贸易有限公司"); // 电商企业名称
		sb.append("</OrgName>\n");
		sb.append("<CreateTime>");
		sb.append(info.getCreateTime()); // 订单创建时间
		sb.append("</CreateTime>\n");
		sb.append("</head>\n");
		sb.append("<body>\n");
		sb.append("<order>\n");
		sb.append("<Operation>");
		sb.append("0");
		sb.append("</Operation>\n");
//		sb.append("<orderShop>");
//		sb.append("17000"); // 店铺代码正式
//		sb.append("</orderShop>\n");
		sb.append("<orderFrom>");
		sb.append("0000"); // 购物网站代码
		sb.append("</orderFrom>\n");
		sb.append("<OrderNo>");
		sb.append(info.getOrderId()); // 订单号
		sb.append("</OrderNo>\n");
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
		sb.append(user.getUserDetail().getIdNum()); // 身份证
		sb.append("</idnum>\n");
		sb.append("<name>");
		sb.append(user.getUserDetail().getName()); // 真实姓名
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

	public static String getAiJiaBeiTeOrderMsg(OrderInfo info, UserInfo user) {
		//佳贝艾特订单状态: 1>等待买家付款  2>等待卖家发货 3>卖家部分发货 4>等待买家确认收货 
		//5>买家已签收确认收货 6>交易成功 7>交易自动关闭 8>交易主动关闭 9>退款中
		//目前暂时只使用: 2>等待卖家发货 9>退款中
		AiJiaBeiTeOrder order = new AiJiaBeiTeOrder();
		order.setTrade_no(info.getOrderId());
		if (Constants.ORDER_PAY.equals(info.getStatus())) {// 海外购系统状态
			order.setTrade_status("2");// 佳贝艾特系统状态
		} else if (Constants.REFUNDS.equals(info.getStatus())) {
			order.setTrade_status("9");
		}
		order.setTotal_fee(info.getOrderDetail().getPayment()+"");
		order.setPayment(info.getOrderDetail().getPayment()+"");
		order.setPost_fee(info.getOrderDetail().getPostFee()+"");
		order.setDiscount_fee(info.getOrderDetail().getDisAmount()+"");
		order.setModify_time(info.getUpdateTime());
		order.setCreate_time(info.getCreateTime());
		order.setPay_time(info.getOrderDetail().getPayTime());
		order.setConsign_time("");
		order.setBuyer_message(info.getRemark().trim());
		order.setReceiver_name(info.getOrderDetail().getReceiveName());
		order.setReceiver_mobile(info.getOrderDetail().getReceivePhone());
		order.setReceiver_phone(info.getOrderDetail().getReceivePhone());
		order.setReceiver_zip(info.getOrderDetail().getReceiveZipCode());
		order.setReceiver_email(user.getEmail());
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
		order.setSeller_memo(info.getRemark().trim());
		List<AiJiaBeiTeOrderGoods> orderGoodsList = new ArrayList<AiJiaBeiTeOrderGoods>();
		for (OrderGoods goods : info.getOrderGoodsList()) {
			AiJiaBeiTeOrderGoods orderGoods = new AiJiaBeiTeOrderGoods();
			orderGoods.setPop_sku_code(goods.getItemId());
			orderGoods.setItem_sku_id(goods.getItemCode());
			orderGoods.setPop_item_title(goods.getItemName());
			orderGoods.setItem_title(goods.getItemName());
			orderGoods.setNum(goods.getItemQuantity()+"");
			orderGoods.setItem_price(goods.getActualPrice()+"");
			orderGoods.setTrade_total_payment(CalculationUtils.mul(goods.getActualPrice(), goods.getItemQuantity())+"");
			orderGoods.setTrade_coupon_payment("0");
			orderGoodsList.add(orderGoods);
		}
		order.setItemList(orderGoodsList);
		return JSONUtil.toJson(order);
	}
}
