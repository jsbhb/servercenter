package com.zm.supplier.util;

import com.zm.supplier.constants.Constants;
import com.zm.supplier.pojo.OrderGoods;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.UserInfo;

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
		sb.append("17000"); // 店铺代码
		sb.append("</orderShop>\n");
		sb.append("<hgArea>");
		sb.append(3105); // 海关关区北仑保税区
		sb.append("</hgArea>\n");
		sb.append("<orderFrom>");
		sb.append(0000); // 购物网站代码
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
		sb.append("TTKDEX"); // 快递公司代码
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
}
