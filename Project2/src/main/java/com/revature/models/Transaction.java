package com.revature.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transactions")
@Data @NoArgsConstructor @AllArgsConstructor
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(insertable = false, updatable = false)
	private long tid;
	private long uid;
	
	//@Column(columnDefinition = "money")
	private double totalcost;
	
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(insertable = false, updatable = false)
	private String stamp;	

	public String toString(boolean b) {
		String nul = "Transaction [tid=0, uid=0, totalcost=0.0, stamp=null]";
		String str ="Transaction [tid=" + tid + ", uid=" + uid + ", totalcost=" + totalcost + ", stamp=" + stamp + "]"; 
		if(str.equals(nul)) {
			return null;
		} else {
			return str;
		}
	}
}
