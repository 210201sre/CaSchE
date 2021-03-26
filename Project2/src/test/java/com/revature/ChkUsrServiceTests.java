package com.revature;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.revature.models.Key;
import com.revature.models.User;
import com.revature.repositories.UserDAO;
import com.revature.services.ChkUsrSvc;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ChkUsrServiceTests {
	
	@Mock
	UserDAO userDAO;
	
	@Mock
	HttpSession s;
	
	@Mock
	HttpServletRequest r;
	
	@InjectMocks
	ChkUsrSvc cus;
	
	@Test
	void logdin1() {
		Mockito.when(r.getSession(false)).thenReturn(s);
		Key k = new Key(); k.setSid(0L); String key = "";
		HttpSession session = r.getSession();
		Mockito.when(userDAO.findBySid((k).getSid())).thenReturn(Optional.empty());
		Mockito.when(s.getAttribute(key)).thenReturn(null);
		Assertions.assertEquals(new Key(), cus.logdin());
	}
	
//	@Test
//	void logdin2() {
//		Mockito.when(r.getSession(false)).thenReturn(s);
//		Key k = new Key(); k.setSid(0L); k.setUid(0L); String key = "";
//		HttpSession session = r.getSession();
//		Mockito.when(s.getAttribute("")).thenReturn(k);
//		Mockito.when(userDAO.findBySid(0L)).thenReturn(Optional.ofNullable(new User()));
//		Mockito.when(s.isNew()).thenReturn(false);
//		Assertions.assertEquals(k, cus.logdin());
//	}
	
	@Test
	void validate() {
		Key k = new Key();
		User b = new User(); b.setFname("Ben"); b.setLname("Cady"); b.setUid((long) 23); b.setAccesslevel("Admin");
		Optional<User> bo = Optional.ofNullable(b);
		Mockito.when(userDAO.findById(k.getUid())).thenReturn(bo);
		Assertions.assertEquals(k, cus.validate(k, "Admin"));
	}
	
	@Test
	void validate2() {
		Key k = new Key();
		User b = new User(); b.setFname("Ben"); b.setLname("Cady"); b.setUid((long) 23); b.setAccesslevel("Employee");
		Optional<User> bo = Optional.ofNullable(b);
		Mockito.when(userDAO.findById(k.getUid())).thenReturn(bo);
		Assertions.assertEquals(k, cus.validate(k, "Employee"));
	}
	
	@Test
	void validate3() {
		Key k = new Key();
		User b = new User(); b.setFname("Ben"); b.setLname("Cady"); b.setUid((long) 23); b.setAccesslevel("Customer");
		Optional<User> bo = Optional.ofNullable(b);
		Mockito.when(userDAO.findById(k.getUid())).thenReturn(bo);
		Assertions.assertEquals(k, cus.validate(k, "Customer"));
	}
	
	@Test
	void validate4() {
		Key k = new Key();
		User b = new User(); b.setFname("Ben"); b.setLname("Cady"); b.setUid((long) 23); b.setAccesslevel("Admin");
		Mockito.when(userDAO.findById(k.getUid())).thenReturn(Optional.empty());
		Assertions.assertEquals(k, cus.validate(k, "Admin"));
	}
	
	@Mock
	User u;
	
	@Test 
	void validate5() {
		Key k = new Key();
		User u2 = new User(); 
		String level = "Customer";
		u2.setAccesslevel(level);
		Mockito.when(userDAO.findById(0L)).thenReturn(Optional.ofNullable(u2));
		
		Mockito.when(u.getAccesslevel()).thenReturn(level);
		Assertions.assertEquals(new Key(), cus.validate(k, "level"));
		level = "Employee";
		u2.setAccesslevel(level);
		
		Mockito.when(u.getAccesslevel()).thenReturn(level);
		Assertions.assertEquals(new Key(), cus.validate(k, "level"));
		level = "Admin";
		u2.setAccesslevel(level);
		
		Mockito.when(u.getAccesslevel()).thenReturn(level);
		Assertions.assertEquals(new Key(), cus.validate(k, "level"));
	}
	
	@Test
	void validateCustomer() {
		Key k = new Key();
		Assertions.assertEquals(k, cus.validateCustomer(k));
	}
	
	@Test
	void validateEmployee() {
		Key k = new Key();
		Assertions.assertEquals(k, cus.validateEmployee(k));
	}
	
	@Test
	void validateAdmin() {
		Key k = new Key();
		Assertions.assertEquals(k, cus.validateAdmin(k));
	}
}
