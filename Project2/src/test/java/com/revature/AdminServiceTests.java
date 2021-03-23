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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.revature.models.CartItem;
import com.revature.models.Coupon;
import com.revature.models.Item;
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
//		Mockito.doReturn("User Created").when(userDAO).save(b);
//		Mockito.doReturn(null).when(userDAO).findByUname("bcady");
//		Assertions.assertEquals("User Created", adminService.addUser(k1, b));		
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
	
//	@Test
//	void modCoupon() {
//		Coupon c1 = new Coupon(0, "Food", "Food Discount", 3.2, 45);
//		Mockito.doReturn("Coupon Updated").when(coupDAO).save(c1);
//		Mockito.doReturn(true).when(coupDAO).existsById(c1.getCid());
//		ResponseEntity<String> t = new ResponseEntity<String>("Coupon Updated", HttpStatus.ACCEPTED);
//		Assertions.assertEquals(t, adminService.modCoupon(k1, c1));
//	}
	
	@Test
	void calculateTotal() {
		Item i1 = new Item(456, "Cateloupe", "Fruit", 1, 3.20, 1, null, 4.00, 6);
		Item i2 = new Item(2478, "Cheese", "", 1, 1.20, 1, null, 2.00, 6);
		CartItem ci1 = new CartItem(1,1,1, i1);
		CartItem ci2 = new CartItem(2,1,2,i2);
		List<CartItem> cis = new ArrayList<CartItem>(); cis.add(ci1); cis.add(ci2);
		AdminService as = new AdminService();
		Assertions.assertEquals(4.40, as.calculateTotal(cis));
	}
	
}
