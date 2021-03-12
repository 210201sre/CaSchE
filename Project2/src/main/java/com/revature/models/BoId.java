package com.revature.models;

import java.io.Serializable;
import java.util.Objects;

public class BoId implements Serializable {

	private long uid;
	private long iid;
	
	public BoId() {
		super();
	}
	
	public BoId(long uid, long iid) {
		super();
		this.uid = uid;
		this.iid = iid;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(iid, uid);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BoId)) {
			return false;
		}
		BoId other = (BoId) obj;
		return iid == other.iid && uid == other.uid;
	}
	
	
}
