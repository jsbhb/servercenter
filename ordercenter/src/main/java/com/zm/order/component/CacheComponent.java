package com.zm.order.component;

import java.util.concurrent.ConcurrentSkipListSet;

import com.zm.order.pojo.bo.GradeBO;

public class CacheComponent {

	private ConcurrentSkipListSet<GradeBO> gradeSet = new ConcurrentSkipListSet<GradeBO>();
	
	private static volatile CacheComponent instance;
	
	private CacheComponent(){}
	
	public static CacheComponent getInstance(){
		if(instance == null){
			synchronized (CacheComponent.class) {
				if(instance == null){
					instance = new CacheComponent();
				}
			}
		}
		return instance;
	}
	
	public void addGrade(GradeBO grade){
		gradeSet.add(grade);
	}
	
	public ConcurrentSkipListSet<GradeBO> getSet(){
		return gradeSet;
	}
}
