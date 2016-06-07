package com.woopra.java.sdk.entities;

import java.util.List;

import javax.validation.Valid;

/**
 * 
 * @author abdulhafeth
 *
 */
public class WoopraFunnelRequest extends BaseWoopraRequest {

	@Valid
	private List<Segment> segments;

	@Valid
	private GroupBy groupBy;

	private String orderBy = new String();

	@Valid
	private List<Goal> goals;

	public List<Segment> getSegments() {
		return segments;
	}

	public void setSegments(List<Segment> segments) {
		this.segments = segments;
	}

	public GroupBy getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(GroupBy groupBy) {
		this.groupBy = groupBy;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public List<Goal> getGoals() {
		return goals;
	}

	public void setGoals(List<Goal> goals) {
		this.goals = goals;
	}

	@Override
	public String toString() {
		return "WoopraFunnelRequest [website=" + website + ", dateFormat="
				+ dateFormat + ", startDate=" + startDate + ", endDate="
				+ endDate + ", limit=" + limit + ", segments=" + segments
				+ ", groupBy=" + groupBy + ", orderBy=" + orderBy + ", goals="
				+ goals + "]";
	}
}
