package com.zm.goods.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.zm.goods.pojo.bo.GoodsBillboard;
import com.zm.goods.pojo.vo.GoodsVO;

public class ImageUtils {

//	private String crossImg = "/images/template/crossImg.png";
//	private String generalTrade = "/images/template/generalTrade.png";

	public byte[] drawGoodsBillboardDTO(GoodsVO item, GoodsBillboard board, InputStream in, String staticUrl) {
		try {
			// 获取模板
			BufferedImage template = ImageIO
					.read(new ByteArrayInputStream(HttpClientUtil.getByteArr(board.getTemplatePath())));
			// 创建画笔
			Graphics2D g = template.createGraphics();
			// 消除画图锯齿
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			// 获取商品图片
			try {
				BufferedImage goodsImage = ImageIO.read(
						new ByteArrayInputStream(HttpClientUtil.getByteArr(item.getGoodsFileList().get(0).getPath())));
				// 画商品主图
				g.drawImage(goodsImage, board.getPic_X_Coordinates(), board.getPic_Y_Coordinates(), board.getPicWidth(),
						board.getPicHeight(), null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 获取二维码
			BufferedImage qrcode = ImageIO.read(in);
			// 画二维码
			g.drawImage(qrcode, board.getCode_X_Coordinates(), board.getCode_Y_Coordinates(), board.getCodeWidth(),
					board.getCodeHeight(), null);
					// 获取商品类型标签图片
					// BufferedImage typeTag = null;
					// if (Constants.O2O_TYPE.equals(item.getType())) {
					// typeTag = ImageIO.read(new
					// ByteArrayInputStream(HttpClientUtil.getByteArr(staticUrl
					// + crossImg)));
					// }
					// if (Constants.GENERAL_TRADE.equals(item.getType())) {
					// typeTag = ImageIO.read(new
					// ByteArrayInputStream(HttpClientUtil.getByteArr(staticUrl
					// + generalTrade)));
					// }
					// // 画标签
					// g.drawImage(typeTag, board.getType_X_Coordinates(),
					// board.getType_Y_Coordinates(), null);

			// 消除字体锯齿
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			// 写商品名称
			Font goodsNameFont = new Font(board.getNameFont(), Font.PLAIN, board.getNameFontSize());
			String[] colorArr = board.getNameColor().split(",");
			Color goodsColor = new Color(Integer.valueOf(colorArr[0]), Integer.valueOf(colorArr[1]),
					Integer.valueOf(colorArr[2]));
			g.setFont(goodsNameFont);
			g.setColor(goodsColor);
			List<String> goodsNameList = spliteGoodsName(item.getGoodsName(), board.getNameNumPerLine(),
					board.getNameRows());
			for (int i = 0; i < goodsNameList.size(); i++) {
				if (i >= board.getNameRows()) {
					break;
				}
				g.drawString(goodsNameList.get(i), board.getName_X_Coordinates(),
						board.getName_Y_Coordinates() + (i * board.getNameSpacing()));
			}

			// 写价格
			Font priceFont = new Font(board.getPriceFont(), Font.BOLD, board.getPriceFontSize());
			colorArr = board.getPriceColor().split(",");
			Color priceColor = new Color(Integer.valueOf(colorArr[0]), Integer.valueOf(colorArr[1]),
					Integer.valueOf(colorArr[2]));
			g.setFont(priceFont);
			g.setColor(priceColor);
			g.drawString(item.getMinPrice() + "", board.getPrice_X_Coordinates(), board.getPrice_Y_Coordinates());

			g.dispose();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.write(template, "png", out);
			return out.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @fun 计算商品名称长度，每一行11个字，2个英文算一个
	 * @param customGoodsName
	 * @return
	 */
	private static List<String> spliteGoodsName(String customGoodsName, int perLine, int rows) {
		char[] charArr = customGoodsName.toCharArray();
		char[] temp = new char[perLine * 2];
		List<String> result = new ArrayList<>();
		double j = 0;// 标记长度是否到perLine
		int k = 0;// 标记数组下标
		try {
			for (int i = 0, length = charArr.length; i < length; i++) {
				char c = charArr[i];
				temp[k] = c;
				k++;
				if (String.valueOf(c).getBytes("UTF-8").length > 1) {
					j = CalculationUtils.add(j, 1);
				} else {
					j = CalculationUtils.add(j, 0.5);
				}
				if (j >= perLine) {
					j = 0;
					k = 0;
					result.add(String.valueOf(temp).trim());
					temp = new char[perLine * 2];
				}
			}
			result.add(String.valueOf(temp).trim());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (result.size() > rows) {
			String name = result.get(1);
			name = name + "...";
			result.set(1, name);
		}
		return result;
	}

	public static void main(String[] args) {
		String s = "澳大利亚 Ego意高 儿童防晒霜 滚珠型 SPF50+ 50毫升（18年10月）";
		List<String> list = spliteGoodsName(s, 11, 2);
		for (String str : list) {
			System.out.println(str);
		}
	}
}
