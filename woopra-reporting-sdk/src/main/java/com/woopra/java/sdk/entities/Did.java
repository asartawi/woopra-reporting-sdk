package com.woopra.java.sdk.entities;

import java.util.List;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 
 * @author abdulhafeth
 *
 */
public class Did {

	@NotBlank
	private String operator;
	
	@Valid
	@NotEmpty
	private List<DidFilter> filters;

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public List<DidFilter> getFilters() {
		return filters;
	}

	public void setFilters(List<DidFilter> filters) {
		this.filters = filters;
	}

	@Override
	public String toString() {
		return "Did [operator=" + operator + ", filters=" + filters + "]";
	}
}
