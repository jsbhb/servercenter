package com.zm.goods.bussiness.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.zm.goods.bussiness.dao.GoodsOpenInterfaceMapper;
import com.zm.goods.bussiness.service.GoodsOpenInterfaceService;
import com.zm.goods.constants.Constants;
import com.zm.goods.enummodel.ErrorCodeEnum;
import com.zm.goods.feignclient.UserFeignClient;
import com.zm.goods.log.LogUtil;
import com.zm.goods.pojo.GoodsDetail;
import com.zm.goods.pojo.GoodsStock;
import com.zm.goods.pojo.ResultModel;
import com.zm.goods.pojo.bo.ButtjointUserBO;
import com.zm.goods.utils.HttpClientUtil;
import com.zm.goods.utils.JSONUtil;

@Service
public class GoodsOpenInterfaceServiceImpl implements GoodsOpenInterfaceService {

	@Resource
	GoodsOpenInterfaceMapper goodsOpenInterfaceMapper;

	@Resource
	UserFeignClient userFeignClient;

	@Resource
	RedisTemplate<String, String> template;

	private final int MAX_SIZE = 100;

	@SuppressWarnings("unchecked")
	@Override
	public ResultModel getGoodsStock(String data) {

		Map<String, String> param = null;
		try {
			param = JSONUtil.parse(data, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultModel(false, ErrorCodeEnum.FORMAT_ERROR.getErrorCode(),
					ErrorCodeEnum.FORMAT_ERROR.getErrorMsg());
		}
		String itemId = param.get("itemId");
		if (itemId == null) {
			return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
					ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
		}
		String[] itemIdArr = itemId.split(",");
		if (itemIdArr.length > MAX_SIZE) {
			return new ResultModel(false, ErrorCodeEnum.EXCEED_MAX_SIZE.getErrorCode(),
					ErrorCodeEnum.EXCEED_MAX_SIZE.getErrorMsg());
		}

		List<GoodsStock> list = goodsOpenInterfaceMapper.listGoodsStock(itemIdArr);
		if (list == null || list.size() == 0) {
			return new ResultModel(false, ErrorCodeEnum.NO_DATA_ERROR.getErrorCode(),
					ErrorCodeEnum.NO_DATA_ERROR.getErrorMsg());
		}

		if (list.size() != itemIdArr.length) {
			Map<String, GoodsStock> temp = new HashMap<String, GoodsStock>();
			for (GoodsStock detail : list) {
				temp.put(detail.getItemId(), detail);
			}
			StringBuilder sb = new StringBuilder();
			for (String id : itemIdArr) {
				if (temp.get(id) == null) {
					sb.append(id + ",");
				}
			}
			String s = sb.substring(0, sb.length() - 1);
			return new ResultModel(false, ErrorCodeEnum.GOODS_DOWNSHELVES.getErrorCode(),
					"itemId:" + s + ErrorCodeEnum.GOODS_DOWNSHELVES.getErrorMsg());
		}

		return new ResultModel(true, list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResultModel getGoodsDetail(String data) {

		Map<String, Object> param = null;
		try {
			param = JSONUtil.parse(data, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultModel(false, ErrorCodeEnum.FORMAT_ERROR.getErrorCode(),
					ErrorCodeEnum.FORMAT_ERROR.getErrorMsg());
		}

		String itemId = param.get("itemId") == null ? null : param.get("itemId").toString();
		if (itemId != null) {
			String[] itemIdArr = itemId.split(",");
			if (itemIdArr.length > MAX_SIZE) {
				return new ResultModel(false, ErrorCodeEnum.EXCEED_MAX_SIZE.getErrorCode(),
						ErrorCodeEnum.EXCEED_MAX_SIZE.getErrorMsg());
			}
			List<GoodsDetail> list = goodsOpenInterfaceMapper.listGoodsDetail(itemIdArr);
			if (list == null || list.size() == 0) {
				return new ResultModel(false, ErrorCodeEnum.NO_DATA_ERROR.getErrorCode(),
						ErrorCodeEnum.NO_DATA_ERROR.getErrorMsg());
			}
			// 判断是否有商品已经下架
			if (list.size() != itemIdArr.length) {
				Map<String, GoodsDetail> temp = new HashMap<String, GoodsDetail>();
				for (GoodsDetail detail : list) {
					temp.put(detail.getItemId(), detail);
				}
				StringBuilder sb = new StringBuilder();
				for (String id : itemIdArr) {
					if (temp.get(id) == null) {
						sb.append(id + ",");
					}
				}
				String s = sb.substring(0, sb.length() - 1);
				return new ResultModel(false, ErrorCodeEnum.GOODS_DOWNSHELVES.getErrorCode(),
						"itemId:" + s + ErrorCodeEnum.GOODS_DOWNSHELVES.getErrorMsg());
			}
			// 规格信息格式化
			infoFormat(list);
			// 包邮包税设定
			postAndTaxSetting(list);
			return new ResultModel(true, list);
		}
		String pageStr = param.get("page") == null ? null : param.get("page").toString();
		String pageSizeStr = param.get("pageSize") == null ? null : param.get("pageSize").toString();
		if (pageStr == null || pageSizeStr == null) {
			return new ResultModel(false, ErrorCodeEnum.MISSING_PARAM.getErrorCode(),
					ErrorCodeEnum.MISSING_PARAM.getErrorMsg());
		}
		Integer page = null;
		Integer pageSize = null;
		try {
			page = Integer.valueOf(pageStr);
			pageSize = Integer.valueOf(pageSizeStr);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultModel(false, ErrorCodeEnum.NUMBER_FORMAT_ERROR.getErrorCode(),
					ErrorCodeEnum.NUMBER_FORMAT_ERROR.getErrorMsg());
		}
		if (pageSize > MAX_SIZE) {
			return new ResultModel(false, ErrorCodeEnum.EXCEED_MAX_SIZE.getErrorCode(),
					ErrorCodeEnum.EXCEED_MAX_SIZE.getErrorMsg());
		}
		int startRow = page > 0 ? (page - 1) * pageSize : 0;
		Map<String, Object> queryParam = new HashMap<String, Object>();
		queryParam.put("startRow", startRow);
		queryParam.put("pageSize", pageSize);

		List<GoodsDetail> list = goodsOpenInterfaceMapper.listGoodsDetailByPage(queryParam);
		if (list == null || list.size() == 0) {
			return new ResultModel(false, ErrorCodeEnum.NO_DATA_ERROR.getErrorCode(),
					ErrorCodeEnum.NO_DATA_ERROR.getErrorMsg());
		}
		// 规格信息格式化
		infoFormat(list);
		// 包邮包税设定
		postAndTaxSetting(list);
		return new ResultModel(true, list);
	}

	private void postAndTaxSetting(List<GoodsDetail> list) {
		Map<Integer, List<GoodsDetail>> tempMap = new HashMap<Integer, List<GoodsDetail>>();
		List<GoodsDetail> tempList = null;
		HashOperations<String, String, String> hashOperations = template.opsForHash();
		for (GoodsDetail item : list) {
			if (tempMap.get(item.getSupplierId()) == null) {
				tempList = new ArrayList<GoodsDetail>();
				tempList.add(item);
				tempMap.put(item.getSupplierId(), tempList);
			} else {
				tempMap.get(item.getSupplierId()).add(item);
			}
		}
		for (Map.Entry<Integer, List<GoodsDetail>> entry : tempMap.entrySet()) {
			Map<String, String> map = hashOperations.entries(Constants.POST_TAX + entry.getKey());
			if (map != null) {
				String post = map.get("post");
				String tax = map.get("tax");
				for (GoodsDetail item : entry.getValue()) {
					item.setFreePost(Integer.valueOf(post == null ? "0" : post));
					item.setFreeTax(Integer.valueOf(tax == null ? "0" : tax));
				}
			}
		}

	}

	private void infoFormat(List<GoodsDetail> list) {
		for (GoodsDetail detail : list) {
			detail.infoFilter();
		}
	}

	@Override
	public void sendGoodsInfo(List<String> itemIdList) {
		Set<String> set = template.opsForSet().members(Constants.BUTT_JOINT_USER_PREFIX);
		List<GoodsDetail> list = goodsOpenInterfaceMapper
				.listGoodsDetail(itemIdList.toArray(new String[itemIdList.size()]));
		if (set != null && set.size() > 0 && list != null && list.size() > 0) {
			// 规格信息格式化
			infoFormat(list);
			String nonceStr = System.currentTimeMillis() + "";
			Map<String, Object> param = new HashMap<String, Object>();
			param.put(METHOD, GOODS_UP_SHELVES);
			param.put(NONCESTR, nonceStr);
			param.put(VERSION, 1.0);
			param.put(DATA, JSONUtil.toJson(list));
			ButtjointUserBO bo = null;
			for (String str : set) {
				bo = JSONUtil.parse(str, ButtjointUserBO.class);
				LogUtil.writeLog("下发上架信息：" + bo.getAppKey());
				if (bo.getUrl() == null || "".equals(bo.getUrl())) {
					continue;
				}
				param.put(APP_KEY, bo.getAppKey());
				param.put(APP_SECRET, bo.getAppSecret());
				param.put(SIGN, sign(param));
				param.remove(APP_SECRET);
				String result = HttpClientUtil.post(bo.getUrl(), param);
				LogUtil.writeLog(
						"下发上架信息:" + itemIdList.toString() + "******" + bo.getAppKey() + "返回结果：" + "===" + result);
			}
		}
	}

	@Override
	public void sendGoodsDownShelves(List<String> itemIdList) {
		Set<String> set = template.opsForSet().members(Constants.BUTT_JOINT_USER_PREFIX);
		if (set != null && set.size() > 0) {
			String nonceStr = System.currentTimeMillis() + "";
			Map<String, Object> param = new HashMap<String, Object>();
			param.put(METHOD, GOODS_DOWN_SHELVES);
			param.put(NONCESTR, nonceStr);
			param.put(VERSION, 1.0);
			param.put(DATA, JSONUtil.toJson(itemIdList));
			ButtjointUserBO bo = null;
			for (String str : set) {
				bo = JSONUtil.parse(str, ButtjointUserBO.class);
				LogUtil.writeLog("下发下架信息：" + bo.getAppKey());
				if (bo.getUrl() == null || "".equals(bo.getUrl())) {
					continue;
				}
				param.put(APP_KEY, bo.getAppKey());
				param.put(APP_SECRET, bo.getAppSecret());
				param.put(SIGN, sign(param));
				param.remove(APP_SECRET);
				String result = HttpClientUtil.post(bo.getUrl(), param);
				LogUtil.writeLog(
						"下发下架信息:" + itemIdList.toString() + "*****" + bo.getAppKey() + "返回结果：" + "===" + result);
			}
		}
	}

	private final String GOODS_UP_SHELVES = "upShelves";
	private final String GOODS_DOWN_SHELVES = "downShelves";
	private final String METHOD = "method";
	private final String APP_SECRET = "appSecret";
	private final String APP_KEY = "appKey";
	private final String SIGN = "sign";
	private final String DATA = "data";
	private final String VERSION = "version";
	private final String NONCESTR = "nonceStr";

	private String sign(Map<String, Object> param) {
		String s = sort(param);
		String str = s.substring(0, s.length() - 1);
		return DigestUtils.md5Hex(str);
	}

	private String sort(Map<String, Object> params) {
		if (params == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		List<String> keyList = new ArrayList<String>(params.keySet());
		Collections.sort(keyList);
		for (String s : keyList) {
			if (!"sign".equalsIgnoreCase(s) && !"sign_type".equalsIgnoreCase(s)
					&& !StringUtils.isEmpty(params.get(s))) {
				sb.append(s + "=" + String.valueOf(params.get(s)) + "&");
			}
		}
		return sb.toString();
	}

}
