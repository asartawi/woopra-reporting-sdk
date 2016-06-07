package com.woopra.java.sdk.entities;

/**
 * 
 * @author abdulhafeth
 *
 */
public class Aggregation {
	private String method;
	private String by;
	private String match;
	private String value;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getBy() {
		return by;
	}

	public void setBy(String by) {
		this.by = by;
	}

	public String getMatch() {
		return match;
	}

	public void setMatch(String match) {
		this.match = match;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Aggregation [method=" + method + ", by=" + by + ", match="
				+ match + ", value=" + value + "]";
	}

}
