package com.revature.models;

import java.io.Serializable;
import java.util.Objects;

public class TuiId implements Serializable {

	private long tid;
	private long iid;
	
	public TuiId() {
		super();
	}
	
	public TuiId(long tid, long iid) {
		super();
		this.tid = tid;
		this.iid = iid;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(iid, tid);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof TuiId)) {
			return false;
		}
		TuiId other = (TuiId) obj;
		return iid == other.iid && tid == other.tid;
	}
	
}
