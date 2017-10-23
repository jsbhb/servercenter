package com.zm.order.pojo;

public class Tax {
	
	private Integer id;

	private Double exciseTax;
	
	private Double incrementTax;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getExciseTax() {
		return exciseTax;
	}

	public void setExciseTax(Double exciseTax) {
		this.exciseTax = exciseTax;
	}

	public Double getIncrementTax() {
		return incrementTax;
	}

	public void setIncrementTax(Double incrementTax) {
		this.incrementTax = incrementTax;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((exciseTax == null) ? 0 : exciseTax.hashCode());
		result = prime * result + ((incrementTax == null) ? 0 : incrementTax.hashCode());
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
		Tax other = (Tax) obj;
		if (exciseTax == null) {
			if (other.exciseTax != null)
				return false;
		} else if (!exciseTax.equals(other.exciseTax))
			return false;
		if (incrementTax == null) {
			if (other.incrementTax != null)
				return false;
		} else if (!incrementTax.equals(other.incrementTax))
			return false;
		return true;
	}
	
}
