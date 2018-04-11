package com.zm.order.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.zm.order.log.LogUtil;
import com.zm.order.pojo.bo.GradeBO;

public class TreeNodeUtil {

	/**
	 * @fun 获取所有的父级包括本身，按先进先出排序
	 * @param set
	 * @param curId
	 * @return
	 */
	public static LinkedList<GradeBO> getSuperNode(Set<GradeBO> set, Integer curId) {
		if (set != null && set.size() > 0) {
			Map<Integer, GradeBO> map = new HashMap<Integer, GradeBO>();
			LinkedList<GradeBO> result = new LinkedList<GradeBO>();
			try {
				for (GradeBO grade : set) {
					map.put(grade.getId(), grade);
				}
				GradeBO curObj = map.get(curId);
				if (curObj == null) {
					return null;
				}
				result.offer(curObj);
				do {
					Integer parentId = curObj.getParentId();
					if (parentId == null || parentId.equals(0)) {
						break;
					} else {
						curObj = map.get(parentId);
						if (curObj == null) {
							break;
						} else {
							result.offer(curObj);
						}
					}
				} while (true);
				
				return result;
			} catch (Exception e) {
				LogUtil.writeErrorLog("寻找父节点出错", e);
				return null;
			}

		}
		return null;
	}
}
