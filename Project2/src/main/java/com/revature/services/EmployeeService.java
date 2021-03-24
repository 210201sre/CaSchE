package com.revature.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.exceptions.InvalidException;
import com.revature.exceptions.UserNotFoundException;
import com.revature.models.Key;
import com.revature.models.User;
import com.revature.repositories.UserDAO;

@Service
public class EmployeeService /*extends CustomerService*/ {

	@Autowired
	private UserDAO userDAO;
	
	public List<User> displayInternalDirectory(Key k) {
		MDC.put("Action", "Emp Display Directory");
		Optional<List<User>> adir1 = userDAO.findAllByAccesslevel("Employee");
		Optional<List<User>> adir2 = userDAO.findAllByAccesslevel("Admin");
		List<User> dir1;
		List<User> dir2;
		if(adir1.isPresent()) {
			dir1 = adir1.get();
		} else {
			dir1 = new ArrayList<>();
		}
		if(adir2.isPresent()) {
			dir2 = adir2.get();
		} else {
			dir2 = new ArrayList<>();
		}
		
		dir1 = Stream.concat(dir1.stream(), dir2.stream()).collect(Collectors.toList());
		if (dir1.isEmpty()&&dir2.isEmpty()) {
			InvalidException.thrown("SELECT: Internal directory is empty.", new UserNotFoundException());
			return new ArrayList<>();
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
