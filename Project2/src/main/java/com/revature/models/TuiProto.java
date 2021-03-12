package com.revature.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_u_i")
@IdClass(TuiId.class)
@Data @NoArgsConstructor @AllArgsConstructor
public class TuiProto implements Serializable {

	//uid equivalent to transaction id when requesting items for a transaction
	@Id
	private long tid;
	@Id
	private long iid;
	private long quantity; // also swap FindTuiByTid in CartDAO
	private long cid;
	
	public String toString(boolean b) {
		String nul = "TuiProto [tid=0, quantity=0, cid=0, i=0]";
		String str = "TuiProto [tid=" + tid + ", quantity=" + quantity + ", cid=" + cid + ", i=" + iid + "]";
		if (str.equals(nul)) {
			return null;
		} else {
			return str;
		}
	}
}
