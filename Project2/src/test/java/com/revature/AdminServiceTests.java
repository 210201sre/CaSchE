package com.revature;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.revature.models.CartItem;
import com.revature.models.Coupon;
import com.revature.models.Item;
import com.revature.models.Key;
import com.revature.models.User;
import com.revature.repositories.BackorderDAO;
import com.revature.repositories.CouponDAO;
import com.revature.repositories.TuiDAO;
import com.revature.repositories.UserDAO;
import com.revature.services.AdminService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AdminServiceTests {
	
//	@Rule
//	public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.LENIENT);
	
	@Mock
	UserDAO userDAO;
	@Mock
	CouponDAO coupDAO;
	@Mock
	BackorderDAO boDAO;
	@Mock
	TuiDAO tuiDAO;
	
	@InjectMocks
	AdminService adminService;
	
	Key k1 = new Key();
	Key k2 = new Key((long) 5, 78);
	User b = new User(null, "Ben", "Cady", null, null, null, null, null, null, null, null, null, null);
	Optional<User> j = Optional.ofNullable(new User(null, "John", "Cady", null, null, null, null, null, null, null, null, null, null));
	User q =new User();
	
//	@Test
//	void addUser() {
//		ResponseEntity<String> t = new ResponseEntity<String>("User Created", HttpStatus.ACCEPTED);
//		Mockito.when(userDAO.findByUname(b.getUname())).thenReturn(null);
//		//returning null does not work, you need to return a lack of an optional item
//		Assertions.assertEquals(t, adminService.addUser(k1, b));		
//	}
//	
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
	
	@Test
	void modCoupon() {
		Coupon c1 = new Coupon(0, "Food", "Food Discount", 3.2, 45);
		ResponseEntity<String> t = new ResponseEntity<String>("Coupon Updated", HttpStatus.OK);
		Mockito.when(coupDAO.save(c1)).thenReturn(c1);
		Mockito.when(coupDAO.existsById(c1.getCid())).thenReturn(true);
		Assertions.assertEquals(t, adminService.modCoupon(k1, c1));
	}
	
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
	
	@Test
	void addCoupon() {
		Coupon c1 = new Coupon(0, "Test1", "Test Coupon", 3.68, 45); Key k = new Key();
		ResponseEntity<String> r = new ResponseEntity<String>("Coupon Added", HttpStatus.ACCEPTED);
		Mockito.when(coupDAO.save(c1)).thenReturn(c1);
		Assertions.assertEquals(r, adminService.addCoupon(k, c1));
	}
	
//	@Test
//	void delCoupon() {
//		ResponseEntity<String> o = new ResponseEntity<String>("Coupon Deleted", HttpStatus.OK);
//		Coupon c = new Coupon(); Key k = new Key();
//		Mockito.when(coupDAO.existsById(c.getCid())).thenReturn(true);
//		
//		ArrayList<Coupon> coups = new ArrayList<Coupon>();
//		Mockito.when(coupDAO.findAllByCid(c.getCid())).thenReturn(coups);
//		
//		ArrayList<BackorderProto> bop = new ArrayList<BackorderProto>();
//		Mockito.when(boDAO.findAllByCid(c.getCid())).thenReturn(bop);
//		
//		ArrayList<TuiProto> tp = new ArrayList<TuiProto>();
//		Mockito.when(tuiDAO.findAllByCid(c.getCid())).thenReturn(tp);
//		
//		ResponseEntity<String> res = new ResponseEntity<String>("Coupon Deleted", HttpStatus.OK);
//	}
	
	@Test
	void hireCustomer() {
		User b0 = new User((long)999, "Ben", "Cady", null, null, null, null, null, null, null, null, "Admin", null);
		Optional<User> b = Optional.of(new User((long)999, "Ben", "Cady", null, null, null, null, null, null, null, null, "Customer", null));
		Mockito.when(userDAO.findById(b0.getUid())).thenReturn(b);
		Mockito.when(userDAO.save(b0)).thenReturn(b0);
		ResponseEntity<String> sro = new ResponseEntity<String>("Customer Hired", HttpStatus.OK);
		Assertions.assertEquals(sro, adminService.hireCustomer(k1, b0));
	}
	
	
	
}
