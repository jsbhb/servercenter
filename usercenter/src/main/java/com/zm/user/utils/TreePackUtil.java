package com.zm.user.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zm.user.pojo.dto.GradeTypeDTO;
import com.zm.user.pojo.po.GradeTypePO;

public class TreePackUtil {

	public static List<GradeTypeDTO> packGradeTypeChildren(List<GradeTypePO> list) {
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
				if (model.getParentId() == 0 || model.getParentId() == null) {
					rootList.add(tempMap.get(model.getId()));
				} else {
					GradeTypeDTO temp = tempMap.get(model.getParentId());
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
		}
		return rootList;

	}

}
