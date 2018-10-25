package com.zm.finance.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {

	public String writeToFile(String str) {
		String fileName = DateUtils.getTimeString("yyyy-MM-dd") + "返佣对账错误信息.txt";
		
		File dir = new File("/opt/server/financecenter/rebatecheck");
		if(!dir.exists()){
			dir.mkdirs();
		}
		String filePaht = "/opt/server/financecenter/rebatecheck/" + fileName;
		File file = new File(filePaht);

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileWriter fw;
		try {
			fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(str);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return filePaht;
	}
}
