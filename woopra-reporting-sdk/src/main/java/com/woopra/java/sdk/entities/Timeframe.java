package com.woopra.java.sdk.entities;

/**
 * 
 * @author abdulhafeth
 *
 */
public class Timeframe {
	private String method;
	private Integer from;
	private Integer to;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Integer getFrom() {
		return from;
	}

	public void setFrom(Integer from) {
		this.from = from;
	}

	public Integer getTo() {
		return to;
	}

	public void setTo(Integer to) {
		this.to = to;
	}

	@Override
	public String toString() {
		return "Timeframe [method=" + method + ", from=" + from + ", to=" + to
				+ "]";
	}

}
