package com.zm.user.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zm.user.pojo.bo.GradeBO;
import com.zm.user.pojo.dto.GradeTypeDTO;
import com.zm.user.pojo.po.GradeTypePO;

public class TreePackUtil {

	public static List<GradeTypeDTO> packGradeTypeChildren(List<GradeTypePO> list, Integer id) {
		List<GradeTypeDTO> rootList = new ArrayList<GradeTypeDTO>();
		if (list != null && list.size() > 0) {
			Map<Integer, GradeTypeDTO> tempMap = new HashMap<Integer, GradeTypeDTO>();
			GradeTypeDTO dto = null;
			for (GradeTypePO model : list) {
				dto = new GradeTypeDTO();
				dto.setId(model.getId());
				dto.setParentId(model.getParentId());
				dto.setName(model.getName());
				tempMap.put(dto.getId(), dto);
			}
			List<GradeTypeDTO> tempList = null;
			for (GradeTypePO model : list) {
				if (id != null) {
					if (model.getParentId() == id) {
						rootList.add(tempMap.get(model.getId()));
						continue;
					}
				} else {
					if (model.getParentId() == 0 || model.getParentId() == null) {
						rootList.add(tempMap.get(model.getId()));
						continue;
					}
				}
				GradeTypeDTO temp = tempMap.get(model.getParentId());
				if (temp == null) {
					continue;
				}
				dto = tempMap.get(model.getId());
				if (temp.getChildern() == null) {
					tempList = new ArrayList<GradeTypeDTO>();
					tempList.add(dto);
					temp.setChildern(tempList);
				} else {
					temp.getChildern().add(dto);
				}
			}
		}
		return rootList;

	}
	
	public static void packGradeChildren(List<GradeBO> list, List<GradeBO> result, Integer gradeId) {
		if (list != null && list.size() > 0) {
			for (GradeBO model : list) {
				if (gradeId.equals(model.getParentId())) {
					packGradeChildren(list, result, model.getId());
					result.add(model);
				}
			}
		}
	}

	public static List<GradeBO> packGradeChildren(List<GradeBO> list, Integer gradeId) {
		List<GradeBO> rootList = new ArrayList<GradeBO>();
		if (list != null && list.size() > 0) {
			Map<Integer, GradeBO> tempMap = new HashMap<Integer, GradeBO>();
			List<GradeBO> tempList = new ArrayList<GradeBO>();
			List<GradeBO> children = null;
			for (GradeBO grade : list) {
				grade.setChildren(null);
				if (gradeId != 0) {
//					if (grade.getId() == gradeId) {
//						rootList.add(grade);
//					}
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

}
