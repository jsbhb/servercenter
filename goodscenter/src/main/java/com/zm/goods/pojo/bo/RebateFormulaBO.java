package com.zm.goods.pojo.bo;
/**
 * @fun 返佣计算公式实体类
 * @author user
 *
 */
public class RebateFormulaBO {

	private Integer gradeTypeId;
	private String formula;
	public Integer getGradeTypeId() {
		return gradeTypeId;
	}
	public void setGradeTypeId(Integer gradeTypeId) {
		this.gradeTypeId = gradeTypeId;
	}
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gradeTypeId == null) ? 0 : gradeTypeId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RebateFormulaBO other = (RebateFormulaBO) obj;
		if (gradeTypeId == null) {
			if (other.gradeTypeId != null)
				return false;
		} else if (!gradeTypeId.equals(other.gradeTypeId))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "RebateFormulaBO [gradeTypeId=" + gradeTypeId + ", formula=" + formula + "]";
	}
	
}
