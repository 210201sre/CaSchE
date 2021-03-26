package com.revature;

import static org.mockito.Mockito.doNothing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.revature.exceptions.InvalidException;
import com.revature.models.BackorderProto;
import com.revature.models.CartItem;
import com.revature.models.CartItemProto;
import com.revature.models.Coupon;
import com.revature.models.Item;
import com.revature.models.Key;
import com.revature.models.Transaction;
import com.revature.models.TuiProto;
import com.revature.models.User;
import com.revature.repositories.BackorderDAO;
import com.revature.repositories.CartDAO;
import com.revature.repositories.CouponDAO;
import com.revature.repositories.ItemDAO;
import com.revature.repositories.TransactionDAO;
import com.revature.repositories.TuiDAO;
import com.revature.repositories.UserDAO;
import com.revature.services.AdminService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AdminServiceTests {
	
	@Mock
	UserDAO userDAO;
	@Mock
	CouponDAO coupDAO;
	@Mock
	BackorderDAO boDAO;
	@Mock
	TuiDAO tuiDAO;
	@Mock
	ItemDAO iDAO;
	@Mock
	TransactionDAO tDAO;
	@Mock
	CartDAO cDAO;
	
	@InjectMocks
	AdminService adminService;
	
	Key k1 = new Key();
	Key k2 = new Key((long) 5, 78);
	User b = new User(null, "Ben", "Cady", null, null, null, null, null, null, null, null, null, null);
	Optional<User> j = Optional.ofNullable(new User(null, "John", "Cady", null, null, null, null, null, null, null, null, null, null));
	User q =new User();
	
	@Test
	void addUser() {
		ResponseEntity<String> t = new ResponseEntity<String>("User Created", HttpStatus.ACCEPTED);
		User u = new User();
		Mockito.when(userDAO.save(u)).thenReturn(u);
		Assertions.assertEquals(t, adminService.addUser(k1, u));		
	}
	
	@Test
	void addUser2() {
		User u = new User(); Optional<User> uo = Optional.ofNullable(u);
		Mockito.when(userDAO.findByUname(u.getUname())).thenReturn(uo);
		ResponseEntity<String> t = InvalidException.thrown(String.format("INSERT: Username %s already exists.", u.getUname()),
				new RuntimeException());
		Assertions.assertEquals(t, adminService.addUser(k1, u));
	}
	
	@Test
	void addUser3() {
		User u = new User(); 
		Mockito.when(userDAO.findByUname(u.getUname())).thenReturn(Optional.empty());
		u.setUid(5l);
		ResponseEntity<String> t = InvalidException.thrown(
				String.format("INSERT: Invalid ID(s) (%d:%d) passed during insertion.", u.getUid(), u.getSid()),
				new RuntimeException());
		Assertions.assertEquals(t, adminService.addUser(k1, u));
	}
	
	@Test
	void addUser4() {
		User u = new User(); 
		Mockito.when(userDAO.findByUname(u.getUname())).thenReturn(Optional.empty());
		u.setSid(5l);
		ResponseEntity<String> t = InvalidException.thrown(
				String.format("INSERT: Invalid ID(s) (%d:%d) passed during insertion.", u.getUid(), u.getSid()),
				new RuntimeException());
		Assertions.assertEquals(t, adminService.addUser(k1, u));
	}
	
	@Test
	void addUser5() {
		User u = new User(); 
		Mockito.when(userDAO.findByUname(u.getUname())).thenReturn(Optional.empty());
		u.setSid(5l); u.setUid(4l);
		ResponseEntity<String> t = InvalidException.thrown(
				String.format("INSERT: Invalid ID(s) (%d:%d) passed during insertion.", u.getUid(), u.getSid()),
				new RuntimeException());
		Assertions.assertEquals(t, adminService.addUser(k1, u));
	}
	
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
	void modCoupon2() {
		Coupon c1 = new Coupon(0, "Food", "Food Discount", 3.2, 45);
		ResponseEntity<String> t = InvalidException.thrown(String.format("UPDATE: Coupon %d does not exist.", c1.getCid()),
				new RuntimeException());
		Mockito.when(coupDAO.existsById(c1.getCid())).thenReturn(false);
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
	
	@Test
	void addCoupon2() {
		Coupon c1 = new Coupon(0, "Test1", "Test Coupon", 3.68, 45); Key k = new Key(); c1.setCid(8l);
		ResponseEntity<String> r= InvalidException.thrown(String.format("INSERT: Invalid coupon id %d.", c1.getCid()),
				new RuntimeException());
		Assertions.assertEquals(r, adminService.addCoupon(k, c1));
	}
	
	@Test
	void delCoupon() {
		Coupon c = new Coupon(); Key k = new Key();
		Mockito.when(coupDAO.existsById(c.getCid())).thenReturn(true);
		ArrayList<Coupon> coups = new ArrayList<Coupon>();
		Mockito.when(coupDAO.findAllByCid(c.getCid())).thenReturn(coups);	
		ArrayList<BackorderProto> bop = new ArrayList<BackorderProto>();
		Mockito.when(boDAO.findAllByCid(c.getCid())).thenReturn(bop);
		ArrayList<TuiProto> tp = new ArrayList<TuiProto>();
		Mockito.when(tuiDAO.findAllByCid(c.getCid())).thenReturn(tp);
		ArrayList<CartItemProto> cIt = new ArrayList<CartItemProto>();
		Mockito.when(cDAO.findAllByCid(c.getCid())).thenReturn(cIt);
		doNothing().when(coupDAO).delete(c);
		ResponseEntity<String> res = new ResponseEntity<String>("Coupon Deleted", HttpStatus.OK);
		Assertions.assertEquals(res, adminService.delCoupon(k, c));
	}
	
	@Test
	void delCoupon2() {
		Coupon c = new Coupon(); Key k = new Key();
		Mockito.when(coupDAO.existsById(c.getCid())).thenReturn(false);
		ResponseEntity<String> res = InvalidException.thrown(String.format("UPDATE: Coupon %d does not exist.", c.getCid()),
				new RuntimeException());
		Assertions.assertEquals(res, adminService.delCoupon(k, c));
	}
	
	@Test
	void delCoupon3() {
		CartItemProto cip = new CartItemProto();
		Coupon c = new Coupon(); Key k = new Key(); List<CartItemProto> ci = new ArrayList<CartItemProto>(); ci.add(cip);
		Mockito.when(coupDAO.existsById(c.getCid())).thenReturn(true);
		Mockito.when(cDAO.findAllByCid(c.getCid())).thenReturn(ci);
		ResponseEntity<String> res = InvalidException.thrown(
				String.format("DELETE: Coupon %d is present in a cart/backorder/transaction.", c.getCid()),
				new RuntimeException());
		Assertions.assertEquals(res, adminService.delCoupon(k, c));
	}
	
//	@Test
//	void hireCustomer() {
//		User b0 = new User((long)999, "Ben", "Cady", null, null, null, null, null, null, null, null, "Admin", null);
//		Optional<User> b = Optional.of(new User((long)999, "Ben", "Cady", null, null, null, null, null, null, null, null, "Customer", null));
//		Mockito.when(userDAO.findById(b0.getUid())).thenReturn(b);
//		Mockito.when(userDAO.save(b0)).thenReturn(b0);
//		ResponseEntity<String> sro = new ResponseEntity<String>("Customer Hired", HttpStatus.OK);
//		Assertions.assertEquals(sro, adminService.hireCustomer(k1, b0));
//	}
	
//	@Test
//	void releaseEmployee() {
//		User b = new User((long)999, "Ben", "Cady", null, null, null, null, null, null, null, null, "Customer", null);
//		Optional<User> b1 = Optional.of(new User((long)999, "Ben", "Cady", null, null, null, null, null, null, null, null, "Employee", null));	
//		ResponseEntity<String> res = new ResponseEntity<String>("Employee Released", HttpStatus.OK);
//		Mockito.when(userDAO.findById(b.getUid())).thenReturn(b1);
//		Mockito.when(userDAO.save(b)).thenReturn(b);
//		Assertions.assertEquals(res, adminService.releaseEmployee(k1, b));
//	}
	
	@Test
	void setNewQuantities() {
		Item it = new Item(6000, "Carrots", "Veggie", 16, 0, 0, null, 0, 0);
		Mockito.when(iDAO.existsById(it.getIid())).thenReturn(true);
		Mockito.when(iDAO.save(it)).thenReturn(it);
		Assertions.assertEquals(true, adminService.setNewQuantities(it, (long) 9));
	}
	
	@Test
	void setNewQuantities2() {
		Item it = new Item(6000, "Carrots", "Veggie", 16, 0, 0, null, 0, 0);
		Mockito.when(iDAO.existsById(it.getIid())).thenReturn(false);
		Assertions.assertEquals(false, adminService.setNewQuantities(it, 10l));
	}
	
	@Test
	void buildTui() {
		TuiProto tp = new TuiProto(1000, 2000, 3000, 4000);
		Optional<Item> i1 = Optional.ofNullable(new Item(456, "Cateloupe", "Fruit", 1, 3.20, 1, null, 4.00, 6));
		Mockito.when(iDAO.findById(tp.getIid())).thenReturn(i1);
		CartItem cartItem = new CartItem( 1000, 3000, 4000, i1.get());
		Assertions.assertEquals(cartItem, adminService.buildTui(tp));
	}
	
	@Test 
	void displayUserTransactions() {
		User b = new User((long)999, "Ben", "Cady", null, null, null, null, null, null, null, null, "Customer", null);
		Mockito.when(userDAO.existsById(b.getUid())).thenReturn(true);
		Transaction t1 = new Transaction(); Transaction t2 = new Transaction(); Transaction t3 = new Transaction();
		t1.setUid(999); t2.setUid(999); t3.setUid(999);
		List<Transaction> lt = new ArrayList<Transaction>(); lt.add(t1); lt.add(t2); lt.add(t3);
		ResponseEntity<List<Transaction>> relt = new ResponseEntity<List<Transaction>>(lt, HttpStatus.OK);
		Mockito.when(tDAO.findAllByUid(b.getUid())).thenReturn(lt);
		Assertions.assertEquals(relt, adminService.displayUserTransactions(k1, b));
	}
	
	@Test
	void displayUserTransactions2() {
		User u = new User();
		Mockito.when(userDAO.existsById(b.getUid())).thenReturn(false);
		List<Transaction> ls = null;
		ResponseEntity<List<Transaction>> rest = ResponseEntity.status(400).body(ls);
		Assertions.assertEquals(rest, adminService.displayUserTransactions(k1, u));
	}
	
	@Test
	void delUserTransactionItem() {
		Key k = new Key(); TuiProto tp = new TuiProto(1000, 2000, 3, 4000);
		Mockito.when(tDAO.existsById(tp.getTid())).thenReturn(true);
		doNothing().when(tuiDAO).delete(tp);
		ResponseEntity<String> rest = new ResponseEntity<String>("Transaction Item Deleted", HttpStatus.OK);
		Assertions.assertEquals(rest, adminService.delUserTransactionItem(k, tp));
	}
	
	@Test
	void delCustomer() {
		User u = new User((long)2883, "John", "Cady", null, null, null, null, null, null, null, null, null, null);
		Key k = new Key();
		Mockito.when(userDAO.existsById(u.getUid())).thenReturn(true);
		List<Transaction> ts = new ArrayList<Transaction>(); Transaction t1 = new Transaction();
		Transaction t2 = new Transaction(); Transaction t3 = new Transaction(); ts.add(t3); ts.add(t2); ts.add(t1);
		Mockito.when(tDAO.findAllByUid(u.getUid())).thenReturn(ts); Transaction t = new Transaction(); 
		doNothing().when(tDAO).deleteByUid(u.getUid()); 
		doNothing().when(boDAO).deleteByUid(u.getUid());
		doNothing().when(cDAO).deleteByUid(u.getUid());
		doNothing().when(userDAO).deleteById(t.getUid());
		ResponseEntity<String> rest = new ResponseEntity<String>("Customer Deleted", HttpStatus.OK);
		Assertions.assertEquals(rest, adminService.delCustomer(k, u));
	}
	
	@Test
	void delCustomer2() {
		User u = new User();
		Mockito.when(userDAO.existsById(u.getUid())).thenReturn(false);
		ResponseEntity<String> res = InvalidException.thrown(String.format("DELETE: User %d does not exist.", u.getUid()),
				new RuntimeException());
		Assertions.assertEquals(res, adminService.delCustomer(k1, u));
	}
	
	@Test
	void delUserTransactions() {                     
		Transaction t1 = new Transaction(333, 343, 2.33, "stamp1");
		Key k = new Key();
		Optional<Transaction> t2 = Optional.ofNullable(t1);
		Mockito.when(tDAO.findById(t1.getTid())).thenReturn(t2);
		doNothing().when(tuiDAO).deleteByTid(t1.getTid());
		doNothing().when(tDAO).deleteById(t1.getTid());
		ResponseEntity<String> rest = new ResponseEntity<String>("User Deleted", HttpStatus.OK);
		Assertions.assertEquals(rest, adminService.delUserTransaction(k, t1));
	}
	
	@Test
	void delUserTransactions2() {
		Transaction t = new Transaction(333, 343, 2.33, "stamp1");
		Key k = new Key();
		Mockito.when(tDAO.findById(t.getTid())).thenReturn(Optional.empty());
		ResponseEntity<String> rest = InvalidException.thrown(String.format("DELETE: Transaction %d does not exist.", t.getTid()),
				new RuntimeException());
		Assertions.assertEquals(rest, adminService.delUserTransaction(k, t));
	}
	
	@Test
	void delUserTransactions3() {
		Transaction t1 = new Transaction(333, 343, 2.33, "stamp1");
		Key k = new Key(); k.setUid(60l); 
		Optional<Transaction> t2 = Optional.ofNullable(t1); t2.get().setUid(60l);
		Mockito.when(tDAO.findById(t1.getTid())).thenReturn(t2);
		ResponseEntity<String> rest = InvalidException.thrown(
				String.format("DELETE: User %d attempted to delete their own transaction.", k.getUid()),
				new RuntimeException());
		Assertions.assertEquals(rest, adminService.delUserTransaction(k, t1));
	}
	
	@Test
	void displayUserTransactionItems() {
		Transaction t = new Transaction();
		Mockito.when(tDAO.existsById(t.getTid())).thenReturn(false);
		List<CartItem> lci = null;
		ResponseEntity<List<CartItem>> rel = ResponseEntity.status(400).body(lci);
		Assertions.assertEquals(rel, adminService.displayUserTransactionItems(k1, t));
		
	}
	
	@Test
	void displayUserTransactionItems2() {
		Transaction t = new Transaction();
		Mockito.when(tDAO.existsById(t.getTid())).thenReturn(true);
		List<TuiProto> ltp = new ArrayList<TuiProto>();
		Mockito.when(tuiDAO.findAllByTid(t.getTid())).thenReturn(ltp);
		List<CartItem> lci = new ArrayList<CartItem>();
		ResponseEntity<List<CartItem>> rlci = ResponseEntity.ok().body(lci);
		Assertions.assertEquals(rlci, adminService.displayUserTransactionItems(k1, t));
	}
	
	

}
