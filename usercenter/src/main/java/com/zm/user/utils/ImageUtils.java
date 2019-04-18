package com.zm.user.utils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ImageUtils {

	public byte[] drawShopBillboardDTO(InputStream in, String staticUrl) {
		try {
			// 获取模板
			BufferedImage template = ImageIO.read(new ByteArrayInputStream(
					HttpClientUtil.getByteArr(staticUrl + "/wechat/images/platform/joinUs/joinUsdefault.jpg")));
			// 创建画笔
			Graphics2D g = template.createGraphics();
			// 消除画图锯齿
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			// 获取二维码
			BufferedImage qrcode = ImageIO.read(in);
			// 画二维码
			g.drawImage(qrcode, 185, 770, 380, 380, null);
			
			g.dispose();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.write(template, "png", out);
			return out.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
