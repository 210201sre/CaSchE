package com.revature.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Getter
public class Key implements Serializable{
	private static final long serialVersionUID = -5853724009355192449L;
	//The model stored in user session.
	private Long sid = null;
	private long uid;
}
