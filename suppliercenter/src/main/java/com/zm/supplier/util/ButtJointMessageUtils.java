package com.zm.supplier.util;

import java.util.List;

import com.zm.supplier.constants.Constants;
import com.zm.supplier.pojo.OrderGoods;
import com.zm.supplier.pojo.OrderInfo;
import com.zm.supplier.pojo.UserInfo;

public class ButtJointMessageUtils {

	public static String getTianTianOrderMsg(OrderInfo info, UserInfo user, String customer) {
		StringBuilder sb = new StringBuilder();
		String source = "";
//		if (Constants.ALI_PAY.equals(info.getOrderDetail().getPayType())) {// 支付方式
			source = "ALIPAY";
//		} else if (Constants.WX_PAY.equals(info.getOrderDetail().getPayType())) {
//			source = "TENPAY";
//		}
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
		sb.append(0000); // 购物网站代码
		sb.append("</orderFrom>\n");
		sb.append("<busOrderNo>");
		sb.append("test123"); // 订单号
		sb.append("</busOrderNo>\n");
		sb.append("<postFee>");
		sb.append(0); // 运费
		sb.append("</postFee>\n");
		sb.append("<amount>");
		sb.append(1); // 买家实付金额
		sb.append("</amount>\n");
		sb.append("<buyerAccount>");
		sb.append("13456789121"); // 购物网站买家账号
		sb.append("</buyerAccount>\n");
		sb.append("<phone>");
		sb.append("13456789121"); // 手机号
		sb.append("</phone>\n");
		sb.append("<taxAmount>");
		sb.append(1); // 税额
		sb.append("</taxAmount>\n");
		sb.append("<tariffAmount>");
		sb.append(0); // 关税额
		sb.append("</tariffAmount>\n");
		sb.append("<addedValueTaxAmount>");
		sb.append(1); // 增值税额
		sb.append("</addedValueTaxAmount>\n");
		sb.append("<consumptionDutyAmount>");
		sb.append(1); // 消费税额
		sb.append("</consumptionDutyAmount>\n");
		sb.append("<grossWeight>");
		sb.append(12); // 毛重
		sb.append("</grossWeight>\n");
		sb.append("<disAmount>");
		sb.append(0); // 优惠金额合计
		sb.append("</disAmount>\n");
		sb.append("<dealDate>");
		sb.append("2017-01-02 12:12:12"); // 下单时间
		sb.append("</dealDate>\n");
		sb.append("<promotions>\n");
		sb.append("<promotion>\n");
		sb.append("<proAmount>");
		sb.append(0); // 优惠金额
		sb.append("</proAmount>\n");
		sb.append("</promotion>\n");
		sb.append("</promotions>\n");
		sb.append("<goods>\n");
//		for (OrderGoods goods : info.getOrderGoodsList()) {
			sb.append("<detail>\n");
			sb.append("<productId>");
			sb.append("3105166008N0000002"); // 货号'3105166008N0000002'
			sb.append("</productId>\n");
			sb.append("<qty>");
			sb.append(1); // 数量
			sb.append("</qty>\n");
			sb.append("<price>");
			sb.append(1); // 商品单价
			sb.append("</price>\n");
			sb.append("<amount>");
			sb.append(1); // 商品金额
			sb.append("</amount>\n");
			sb.append("</detail>\n");
//		}
		sb.append("</goods>\n");
		sb.append("<pay>\n");
		sb.append("<paytime>");
		sb.append("2017-01-02 12:12:12"); // 支付时间
		sb.append("</paytime>\n");
		sb.append("<paymentNo>");
		sb.append("test1"); // 支付单号
		sb.append("</paymentNo>\n");
		sb.append("<orderSeqNo>");
		sb.append("test1"); // 商家送支付机构订单交易号,如无，请与支付单号一致
		sb.append("</orderSeqNo>\n");
		sb.append("<source>");
		sb.append(source); // 支付方式代码
		sb.append("</source>\n");
		sb.append("<idnum>");
		sb.append("330206199912123412"); // 身份证
		sb.append("</idnum>\n");
		sb.append("<name>");
		sb.append("test"); // 真实姓名
		sb.append("</name>\n");
		sb.append("</pay>\n");
		sb.append("<logistics>");
		sb.append("<logisticsCode>");
		sb.append("TTKDEX"); // 快递公司代码
								// SF=顺丰速EMS=邮政速递POSTAM=邮政小包ZTO=中通速递STO=申通快递YTO=圆通速递JD=京东快递BEST=百世物流YUNDA=韵达速递TTKDEX=天天快递
		sb.append("</logisticsCode>\n");
		sb.append("<consignee>");
		sb.append("test"); // 收货人名称
		sb.append("</consignee>\n");
		sb.append("<province>");
		sb.append("test"); // 省
		sb.append("</province>\n");
		sb.append("<city>");
		sb.append("test"); // 市
		sb.append("</city>\n");
		sb.append("<district>");
		sb.append("test"); // 区
		sb.append("</district>\n");
		sb.append("<consigneeAddr>");
		sb.append("test"); // 收货地址
		sb.append("</consigneeAddr>\n");
		sb.append("<consigneeTel>");
		sb.append("13412312312"); // 收货电话
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
		sb.append("0"); // 查询规则0:鑫海订单号，1能容订单号
		sb.append("</rule>\n");
		sb.append("<busCode>");
		sb.append(customer); // 客户编码
		sb.append("</busCode>\n");
		sb.append("</head>\n");
		sb.append("<body>\n");
		for(String orderId : orderIds){
			sb.append("<orderNo>");
			sb.append(orderId); // 订单号
			sb.append("</orderNo>\n");
		}
		sb.append("</body>\n");
		sb.append("</request>\n");
		return sb.toString();
	}
}
