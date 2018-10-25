package com.zm.finance.bussiness.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.zm.finance.bussiness.dao.RebateConsumeMapper;
import com.zm.finance.bussiness.dao.RebateMapper;
import com.zm.finance.bussiness.dao.WithdrawalsMapper;
import com.zm.finance.bussiness.service.RebateCheckService;
import com.zm.finance.constants.Constants;
import com.zm.finance.pojo.rebate.RebateComeIn;
import com.zm.finance.pojo.rebate.RebateConsume;
import com.zm.finance.pojo.rebate.RebateWithdrawals;
import com.zm.finance.util.CalculationUtils;
import com.zm.finance.util.DateUtils;
import com.zm.finance.util.FileUtil;
import com.zm.finance.util.MailUtil;

@Service
public class RebateCheckServiceImpl implements RebateCheckService {

	@Resource
	RebateMapper rebateMapper;

	@Resource
	WithdrawalsMapper withdrawalsMapper;

	@Resource
	RebateConsumeMapper rebateConsumeMapper;

	@Resource
	RedisTemplate<String, String> template;

	private final String CAN_BE_PRESENTED = "canBePresented";
	private final String ALREADY_CHECK = "alreadyCheck";

	@Override
	public void rebateCheck() {
		List<RebateComeIn> comeInList = rebateMapper.listRebateComeIn();
		List<RebateWithdrawals> withdrawalsList = withdrawalsMapper.listRebateWithdrawals();
		List<RebateConsume> rebateConsumeList = rebateConsumeMapper.listRebateConsume();

		if (comeInList == null || comeInList.size() == 0) {
			return;
		}
		// 封装数据
		Map<Integer, RebateWithdrawals> rebateWithdrawalsMap = new HashMap<Integer, RebateWithdrawals>();
		Map<Integer, RebateConsume> rebateConsumeMap = new HashMap<Integer, RebateConsume>();
		for (RebateWithdrawals rebateWithdrawals : withdrawalsList) {
			rebateWithdrawalsMap.put(rebateWithdrawals.getGradeId(), rebateWithdrawals);
		}
		for (RebateConsume rebateConsume : rebateConsumeList) {
			rebateConsumeMap.put(rebateConsume.getGradeId(), rebateConsume);
		}
		// 计算所有返佣的进-出，记录对账错误的信息，处理对账正确的信息
		HashOperations<String, String, String> hashOperations = template.opsForHash();
		RebateWithdrawals wtemp = null;
		RebateConsume ctemp = null;
		StringBuilder sb = new StringBuilder();
		for (RebateComeIn comeIn : comeInList) {
			wtemp = rebateWithdrawalsMap.get(comeIn.getGradeId());
			double wtempMoney = 0;// 提现额
			double ctempMoney = 0;// 消费额
			if (wtemp != null) {
				wtempMoney = wtemp.getTotalWithdrawals();
			}
			ctemp = rebateConsumeMap.get(comeIn.getGradeId());
			if (ctemp != null) {
				ctempMoney = ctemp.getTotalConsume();
			}
			double rebate = CalculationUtils.sub(comeIn.getTotalRebate() == null ? "0" : comeIn.getTotalRebate() + "",
					wtempMoney + "", ctempMoney + "");
			String perfix = Constants.GRADE_ORDER_REBATE + comeIn.getGradeId();
			double redisRebate = CalculationUtils.add(
					hashOperations.get(perfix, CAN_BE_PRESENTED) == null ? "0"
							: hashOperations.get(perfix, CAN_BE_PRESENTED),
					hashOperations.get(perfix, ALREADY_CHECK) == null ? "0"
							: hashOperations.get(perfix, ALREADY_CHECK));
			if (CalculationUtils.round(2, rebate + "") == CalculationUtils.round(2, redisRebate + "")) {
				hashOperations.put(perfix, ALREADY_CHECK, rebate + "");
				hashOperations.put(perfix, CAN_BE_PRESENTED, "0");
			} else {
				sb.append("分级ID:" + comeIn.getGradeId() + ",对账返佣：" + rebate + ",redis内:" + redisRebate + " \r\n");
			}
		}

		if (sb.length() > 0) {
			FileUtil fileUtil = new FileUtil();
			String filePath = fileUtil.writeToFile(sb.toString());
			// 发送邮件
			MailUtil mailUtil = new MailUtil(true);
			String[] receives = { "caiqiaoling@nkhwg.com", "wangqiyun@nkhwg.com", "wanghaiyang@nkhwg.com",
					"hebin@nkhwg.com" };
			File affix = new File(filePath);
			mailUtil.doSendHtmlEmail("返佣对账出现问题" + DateUtils.getTimeString("yyyy-MM-dd"), "返佣对账出现问题", receives, affix);//
		}

	}

}
