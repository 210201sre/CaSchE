package com.revature.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart")
@IdClass(CipId.class)
@Data @NoArgsConstructor @AllArgsConstructor
public class CartItemProto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5233706010530733672L;
	//uid equivalent to transaction id when requesting items for a transaction
	@Id
	private long uid;
	@Id
	private long iid;
	private long quantity; // also swap FindTuiByTid in CartDAO
	@Column(name = "cid")
	private long cid;
	
	public String toString(boolean b) {
		String nul = "CartItemProto [uid=0, quantity=0, cid=0, i=0]";
		String str = "CartItemProto [uid=" + uid + ", quantity=" + quantity + ", cid=" + cid + ", i=" + iid + "]";
		if (str.equals(nul)) {
			return null;
		} else {
			return str;
		}
	}
//	public String toString(boolean b) {
//		String nul = "CartItem [uid=0, cartQuantity=0, cid=0, i=0]";
//		String str = "CartItem [uid=" + uid + ", cartQuantity=" + cartQuantity + ", cid=" + cid + ", i=" + iid + "]";
//		if (str.equals(nul)) {
//			return null;
//		} else {
//			return str;
//		}
//	}
}
