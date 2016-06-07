package com.woopra.java.sdk.entities;

import java.util.List;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 
 * @author abdulhafeth
 *
 */
public class GroupBy {
	
	@NotEmpty
	private String scope;
	
	@NotEmpty
	private String key;
	
	@Valid
	private List<Transform> transforms;

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<Transform> getTransforms() {
		return transforms;
	}

	public void setTransforms(List<Transform> transforms) {
		this.transforms = transforms;
	}

	@Override
	public String toString() {
		return "GroupBy [scope=" + scope + ", key=" + key + ", transforms="
				+ transforms + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((scope == null) ? 0 : scope.hashCode());
		result = prime * result
				+ ((transforms == null) ? 0 : transforms.hashCode());
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
		GroupBy other = (GroupBy) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (scope == null) {
			if (other.scope != null)
				return false;
		} else if (!scope.equals(other.scope))
			return false;
		if (transforms == null) {
			if (other.transforms != null)
				return false;
		} else if (!transforms.equals(other.transforms))
			return false;
		return true;
	}

}
