package com.revature.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.exceptions.UserNotFoundException;
import com.revature.models.CartItem;
import com.revature.models.CartItemProto;
import com.revature.models.Key;
import com.revature.models.Transaction;
import com.revature.models.TuiProto;
import com.revature.models.User;
import com.revature.repositories.UserDAO;

@Service
public class EmployeeService /*extends CustomerService*/ {

	@Autowired
	private UserDAO userDAO;
	
	public List<User> displayInternalDirectory(Key k) {
		MDC.put("Action", "Emp Display Directory");
		List<User> dir1 = userDAO.findAllByAccesslevel("Employee").orElseThrow(() -> new UserNotFoundException("SELECT: No Employees found."));
		List<User> dir2 = userDAO.findAllByAccesslevel("Admin").orElseThrow(() -> new UserNotFoundException("SELECT: No Admins found."));
		dir1 = Stream.concat(dir1.stream(), dir2.stream()).collect(Collectors.toList());
		if (dir1.isEmpty()) {
			throw new UserNotFoundException("SELECT: Internal directory is empty.");
		}
		dir2 = new ArrayList<>();
		for (User u : dir1) {
			u.setPswrd(null);
			u.setUname(null);
			dir2.add(u);
		}
		return dir2;
	}
}
