package com.woopra.java.sdk.enums;

/**
 * 
 * @author abdulhafeth
 *
 */
public enum WoopraEvent {
	
	PAGE_VIEW ("pv"),
	DETAILS_EVENT ("Details_view"),
    ADDED_JOB_TO_SHORTLIST ("Shortlisted_job");

    private final String name;       

    private WoopraEvent(String s) {
        name = s;
    }

    public String toString() {
       return this.name;
    }
    
    public static WoopraEvent getWoopraEventByName(String name){
    	
    	if (name != null) {
	      for (WoopraEvent woopraEvent : WoopraEvent.values()) {
	        if (name.equalsIgnoreCase(woopraEvent.name)) {
	          return woopraEvent;
	        }
	      }
	    }
	    return null;
    }
}
