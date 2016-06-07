package com.woopra.java.sdk.entities;

import org.json.JSONObject;

/**
 * 
 * @author abdulhafeth
 *
 */
public class WoopraProfileResponse {
	
	JSONObject summary;
	
	JSONObject customInfo;

	public JSONObject getSummary() {
		return summary;
	}

	public void setSummary(JSONObject summary) {
		this.summary = summary;
	}

	public JSONObject getCustomInfo() {
		return customInfo;
	}

	public void setCustomInfo(JSONObject customInfo) {
		this.customInfo = customInfo;
	}

	@Override
	public String toString() {
		return "WoopraProfileResponse [summary=" + summary + ", customInfo="
				+ customInfo + "]";
	}
	
}
