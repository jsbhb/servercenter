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
import com.zm.finance.log.LogUtil;
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
			String perfix = Constants.GRADE_ORDER_REBATE + comeIn.getGradeId();
			// 获取冻结金额
			String frozenRebate = hashOperations.get(perfix, Constants.FROZEN_REBATE);
			// 总进-总消费（包括提现中，提现成功，订单支付成功）-冻结金额
			double rebate = CalculationUtils.sub(comeIn.getTotalRebate() == null ? "0" : comeIn.getTotalRebate() + "",
					wtempMoney + "", ctempMoney + "");
			rebate = CalculationUtils.sub(rebate + "", frozenRebate == null ? "0" : frozenRebate);
			
			// 获取redis内待对账和已对账的和
			double redisRebate = CalculationUtils.add(
					hashOperations.get(perfix, Constants.CAN_BE_PRESENTED) == null ? "0"
							: hashOperations.get(perfix, Constants.CAN_BE_PRESENTED),
					hashOperations.get(perfix, Constants.ALREADY_CHECK) == null ? "0"
							: hashOperations.get(perfix, Constants.ALREADY_CHECK));
			if (CalculationUtils.round(2, rebate + "") == CalculationUtils.round(2, redisRebate + "")) {
				//TODO 如果这时候刚好订单超时关闭，返佣返回，这里的rebate是已经扣除冻结的金额，会有问题，需要和订单中心做同步
				hashOperations.put(perfix, Constants.ALREADY_CHECK, rebate + "");
				hashOperations.put(perfix, Constants.CAN_BE_PRESENTED, "0");
			} else {
				sb.append("分级ID:" + comeIn.getGradeId() + ",对账返佣：" + rebate + ",redis内:" + redisRebate + ";其中总返佣："
						+ comeIn.getTotalRebate() + ",提现：" + wtempMoney + "，订单消费：" + ctempMoney + " \r\n");
			}
		}
		LogUtil.writeLog(sb.toString());
		if (sb.length() > 0) {
			FileUtil fileUtil = new FileUtil();
			String filePath = fileUtil.writeToFile(sb.toString());
			// 发送邮件
			MailUtil mailUtil = new MailUtil(true);
			String[] receives = { "caiqiaoling@cncoopay.com", "wangqiyun@cncoopay.com", "wanghaiyang@cncoopay.com",
					"hebin@cncoopay.com" };
			File affix = new File(filePath);
			mailUtil.doSendHtmlEmail("返佣对账出现问题" + DateUtils.getTimeString("yyyy-MM-dd"), "返佣对账出现问题", receives, affix);//
		}

	}

}
