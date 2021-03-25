package com.revature.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data @NoArgsConstructor @AllArgsConstructor
public class CartItem {

//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long utid;
	private long cartQuantity;
	private long cid;
	private Item i;

	public String toString(boolean b) {
		String nul = "CartItem [utid=0, cartQuantity=0, cid=0, i=null]";
		String str = "CartItem [utid=" + utid + ", cartQuantity=" + cartQuantity + ", cid=" + cid + ", i=" + i + "]";
		if (str.equals(nul)) {
			return null;
		} else {
			return str;
		}
	}
}
