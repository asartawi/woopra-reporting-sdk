package com.woopra.java.sdk.entities;

import javax.validation.Valid;

/**
 * 
 * @author abdulhafeth
 *
 */
public class DidFilter {
	
	@Valid
	private Constraints action;
	
	@Valid
	private Timeframe timeframe;
	
	@Valid
	private Constraints visit;
	
	@Valid
	private Aggregation aggregation;

	public Constraints getAction() {
		return action;
	}

	public void setAction(Constraints action) {
		this.action = action;
	}

	public Timeframe getTimeframe() {
		return timeframe;
	}

	public void setTimeframe(Timeframe timeframe) {
		this.timeframe = timeframe;
	}

	public Constraints getVisit() {
		return visit;
	}

	public void setVisit(Constraints visit) {
		this.visit = visit;
	}
	
	

	public Aggregation getAggregation() {
		return aggregation;
	}

	public void setAggregation(Aggregation aggregation) {
		this.aggregation = aggregation;
	}

	@Override
	public String toString() {
		return "DidFilter [action=" + action + ", timeframe=" + timeframe
				+ ", visit=" + visit + "]";
	}

}
