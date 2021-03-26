package com.revature.services;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.exceptions.InvalidException;
import com.revature.models.Key;
import com.revature.models.User;
import com.revature.repositories.UserDAO;

@Service
public class ChkUsrSvc {

	private String key = "projectzero";
	private String permErrMsg = "Permission Denied";
	private String admin = "Admin";
	private User u;
	
	@Autowired
	private HttpServletRequest r;

	@Autowired
	private UserDAO userDAO;
	
	HttpSession session;

	public Key logdin() {

		session = r.getSession(false);
		if (session.isNew() || session.getAttribute(key) == null || !userDAO.findBySid(((Key) session.getAttribute(key)).getSid()).isPresent()) {
			InvalidException.thrown("User not logged in.", new RuntimeException());
			return new Key();
		}
		return (Key) session.getAttribute(key);
	}

	public Key validateCustomer(Key k) {
		return validate(k,"Customer");
	}
	
	public Key validateEmployee(Key k) {
		return validate(k,"Employee");
	}
	
	public Key validateAdmin(Key k) {
		return validate(k,admin);
	}
	
	public Key validate(Key k, String level) {
		Optional<User> u2 = userDAO.findById(k.getUid());
		
		if (u2.isPresent()) {
			u = u2.get();
		} else {
			InvalidException.thrown(String.format("SELECT: User %d does not exist.", k.getUid()), new RuntimeException());
			return new Key();
		}
		if (level.equals("Customer")) {
			if(!u.getAccesslevel().equals(level)&&!u.getAccesslevel().equals("employee")&&!u.getAccesslevel().equals(admin)) {
				InvalidException.thrown(permErrMsg, new RuntimeException());
				return new Key();
			}
		} else if (level.equals("Employee")) {
			if(!u.getAccesslevel().equals(level)&&!u.getAccesslevel().equals(admin)) {
				InvalidException.thrown(permErrMsg, new RuntimeException());
				return new Key();
			}
		} else if (level.equals(admin)&&!u.getAccesslevel().equals(level)) {
			InvalidException.thrown(permErrMsg, new RuntimeException());
			return new Key();
		}
		
		return k;
	}
}
