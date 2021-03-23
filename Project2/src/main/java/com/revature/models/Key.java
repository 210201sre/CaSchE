package com.revature.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Getter
public class Key {
	//The model stored in user session.
	private Long sid = null;
	private long uid;
}
