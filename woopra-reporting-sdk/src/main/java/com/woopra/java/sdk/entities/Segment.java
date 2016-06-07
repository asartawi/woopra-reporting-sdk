package com.woopra.java.sdk.entities;

import javax.validation.Valid;

/**
 * 
 * @author abdulhafeth
 *
 */
public class Segment {

	@Valid
	private Constraints are;

	@Valid
	private Did did;

	public Constraints getAre() {
		return are;
	}

	public void setAre(Constraints are) {
		this.are = are;
	}

	public Did getDid() {
		return did;
	}

	public void setDid(Did did) {
		this.did = did;
	}

	@Override
	public String toString() {
		return "Segment [are=" + are + ", did=" + did + "]";
	}
}
