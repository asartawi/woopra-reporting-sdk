package com.woopra.java.sdk.entities;

import java.util.List;

import org.json.JSONArray;

/**
 * 
 * @author abdulhafeth
 *
 */
public class WoopraReportResponse {

	private List<Long> columnsTotal;
	
	private Long count;

	private JSONArray rows;

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public JSONArray getRows() {
		return rows;
	}

	public void setRows(JSONArray rows) {
		this.rows = rows;
	}

	public List<Long> getColumnsTotal() {
		return columnsTotal;
	}

	public void setColumnsTotal(List<Long> columnsTotal) {
		this.columnsTotal = columnsTotal;
	}

}
