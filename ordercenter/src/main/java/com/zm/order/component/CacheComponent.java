package com.zm.order.component;

import java.util.Comparator;
import java.util.concurrent.ConcurrentSkipListSet;

import com.zm.order.pojo.bo.GradeBO;

public class CacheComponent {

	//比较器如果写在对象里面时，判断重复是根据比较器来，如果小于0则判断为重复
	private ConcurrentSkipListSet<GradeBO> gradeSet = new ConcurrentSkipListSet<GradeBO>(new Comparator<GradeBO>() {
		public int compare(GradeBO k1, GradeBO k2) {
			return k1.getId() - k2.getId();
		}
	});

	private static volatile CacheComponent instance;

	private CacheComponent() {
	}

	public static CacheComponent getInstance() {
		if (instance == null) {
			synchronized (CacheComponent.class) {
				if (instance == null) {
					instance = new CacheComponent();
				}
			}
		}
		return instance;
	}
	
	public void addGrade(GradeBO grade) {
		if(gradeSet.contains(grade)){
			gradeSet.remove(grade);
		}
		gradeSet.add(grade);
	}

	public ConcurrentSkipListSet<GradeBO> getSet() {
		return gradeSet;
	}
}
