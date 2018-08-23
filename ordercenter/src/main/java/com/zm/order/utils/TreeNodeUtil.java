package com.zm.order.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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

	public static List<GradeBO> getchildNode(Set<GradeBO> set, Integer gradeId) {
		List<GradeBO> rootList = new ArrayList<GradeBO>();
		if (set != null && set.size() > 0) {
			Map<Integer, GradeBO> tempMap = new HashMap<Integer, GradeBO>();
			List<GradeBO> tempList = new ArrayList<GradeBO>();
			List<GradeBO> children = null;
			for (GradeBO grade : set) {
				grade.setChildren(null);
				if (gradeId != 0) {
					if (grade.getId().equals(gradeId)) {
						rootList.add(grade);
					}
				} else {
					if (grade.getParentId() == null || grade.getParentId() == 0) {
						rootList.add(grade);
					}
				}
				tempList.add(grade);
				tempMap.put(grade.getId(), grade);
			}
			for(GradeBO grade : tempList){
				GradeBO temp = tempMap.get(grade.getParentId());
				if(temp != null){
					if (temp.getChildren() == null) {
						children = new ArrayList<GradeBO>();
						children.add(grade);
						temp.setChildren(children);
					} else {
						temp.getChildren().add(grade);
					}
				}
			}
		}
		return rootList;
	}
	
	public static void getchildNode(List<GradeBO> list, List<Integer> result){
		if (list != null && list.size() > 0) {
			for (GradeBO model : list) {
				if (model.getChildren() != null) {
					getchildNode(model.getChildren(), result);
					result.add(model.getId());
				} else {
					result.add(model.getId());
				}
			}
		}
	}
	
	public static List<Integer> getChild(Set<GradeBO> set, Integer gradeId){
		if(set != null && set.size() > 0){
			List<Integer> list = new ArrayList<Integer>();
			for(GradeBO grade : set){
				if(grade.getParentId().equals(gradeId)){
					list.add(grade.getId());
				}
			}
			if(gradeId != 0){
				list.add(gradeId);
			}
			return list;
		}
		return null;
	}
}
