package com.revature;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.revature.models.Coupon;
import com.revature.models.Key;
import com.revature.models.User;
import com.revature.repositories.CouponDAO;
import com.revature.repositories.UserDAO;
import com.revature.services.AdminService;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTests {
	
	@Mock
	UserDAO userDAO;
	@Mock
	CouponDAO coupDAO;
	
	@InjectMocks
	AdminService adminService;
	
	Key k1 = new Key();
	Key k2 = new Key((long) 5, 78);
	User b = new User(null, "Ben", "Cady", "bcady", "password1", null, null, null, null, null, null, null, null);
	User q =new User();
	
//	@Test
//	void addUser() {
//		Mockito.doReturn(null).when(userDAO).findByUname("bcady");
//		Assertions.assertEquals("User Created", adminService.addUser(k1, b));
//		
//	}
	
	@Test
	void displayAllUsers() {
		List<User> usrList = new ArrayList<User>();
		usrList.add(b); usrList.add(q);
		Mockito.doReturn(usrList).when(userDAO).findAll();
		Assertions.assertEquals(usrList, adminService.displayAllUsers(k1));
	}
	
	@Test
	void displayCoupons() {
		List<Coupon> cpList = new ArrayList<Coupon>();
		Coupon c1 = new Coupon(); Coupon c2 = new Coupon();
		cpList.add(c1); cpList.add(c2);
		Mockito.doReturn(cpList).when(coupDAO).findAll();
		Assertions.assertEquals(cpList, adminService.displayCoupons(k1));
	}
	
}
