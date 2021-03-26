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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.revature.exceptions.InvalidException;
import com.revature.models.BackorderProto;
import com.revature.models.CartItem;
import com.revature.models.CartItemProto;
import com.revature.models.Item;
import com.revature.models.Key;
import com.revature.models.Transaction;
import com.revature.models.User;
import com.revature.repositories.BackorderDAO;
import com.revature.repositories.CartDAO;
import com.revature.repositories.ItemDAO;
import com.revature.repositories.TransactionDAO;
import com.revature.repositories.UserDAO;
import com.revature.services.AdminService;
import com.revature.services.CustomerService;

import io.micrometer.core.instrument.MeterRegistry;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTests {
	
	@Mock
	UserDAO userDAO;
	@Mock
	CartDAO cDAO;
	@Mock
	ItemDAO iDAO;
	@Mock
	TransactionDAO tDAO;
	@Mock
	BackorderDAO boDAO;
	
	@Mock
	MeterRegistry meterRegistry;
	
	@InjectMocks
	CustomerService cServ = new CustomerService();

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
	void addUsr() {
		User b = new User(); b.setFname("Ben"); b.setLname("Cady"); b.setAccesslevel("Customer");
		Mockito.when(userDAO.save(b)).thenReturn(b);
		ResponseEntity<String> res = new ResponseEntity<String>("User Account Created", HttpStatus.ACCEPTED);
		Assertions.assertEquals(res, cServ.addUsr(b));
	}
	
	@Test
	void getMyInfo2() {
		Key k = new Key();
		User b = new User(); b.setFname("Ben"); b.setLname("Cady"); b.setAccesslevel("Customer");
		Mockito.when(userDAO.findById(k.getUid())).thenReturn(Optional.empty());
		User j = null;
		ResponseEntity<User> reu = ResponseEntity.status(400).body(null);
		Assertions.assertEquals(reu, cServ.getMyInfo(k));
	}

	
	@Test
	void getMyInfo() {
		Key k = new Key();
		User b = new User(); b.setFname("Ben"); b.setLname("Cady"); b.setAccesslevel("Customer");
		Optional<User> ob = Optional.ofNullable(b);
		Mockito.when(userDAO.findById(k.getUid())).thenReturn(ob);
		ResponseEntity<User> reu = new ResponseEntity<User>(ob.get(), HttpStatus.OK);
		Assertions.assertEquals(reu, cServ.getMyInfo(k));
	}
	
	@Test
	void delUser() {
		Key k = new Key();
		User b = new User(); b.setFname("Ben"); b.setLname("Cady"); b.setAccesslevel("Customer");
		Optional<User> ob = Optional.ofNullable(b);
		Mockito.when(userDAO.findById(k.getUid())).thenReturn(ob);
		doNothing().when(userDAO).deleteById(b.getUid());
		ResponseEntity<String> res= new ResponseEntity<String>("User account has been deleted.", HttpStatus.OK);
		Assertions.assertEquals(res, cServ.delUser(k));
	}
	
	@Test
	void emptyCart() {
		Key k = new Key();
		Mockito.when(cDAO.countByUid(k.getUid())).thenReturn((long) 5);
		CartItemProto cip1 = new CartItemProto(); CartItemProto cip2 = new CartItemProto();
		List<CartItemProto> cipL = new ArrayList<CartItemProto>(); cipL.add(cip1); cipL.add(cip2);
		Mockito.when(cDAO.findAllByUid(k.getUid())).thenReturn(cipL);
		CartItemProto p = new CartItemProto();
		doNothing().when(cDAO).delete(p);
		ResponseEntity<String> res= new ResponseEntity<String>("Cart has been emptied.", HttpStatus.OK);
		Assertions.assertEquals(res, cServ.emptyCart(k));
	}
	
	@Test
	void buildCartItem() {
		CartItemProto cip1 = new CartItemProto(21, 22, 3, 24);
		Item i1 = new Item(2, "Peaches", null, 0, 0, 0, null, 0, 16);
		Optional<Item> oi = Optional.ofNullable(i1);
		Mockito.when(iDAO.findById(cip1.getIid())).thenReturn(oi);
		CartItem ciAns = new CartItem(21,3,24,i1);
		Assertions.assertEquals(ciAns, cServ.buildCartItem(cip1));
	}
	
	@Test 
	void displayTransactions(){
		Key k = new Key();
		List<Transaction> lt = new ArrayList<Transaction>();
		Mockito.when(tDAO.findAllByUid(k.getUid())).thenReturn(lt);
		Assertions.assertEquals(lt, cServ.displayTransactions(k));
	}
	
	@Test 
	void displayBackorders(){
		Key k = new Key();
		List<BackorderProto> lt = new ArrayList<BackorderProto>();
		Mockito.when(boDAO.findAllByUid(k.getUid())).thenReturn(lt);
		Assertions.assertEquals(lt, cServ.displayBackorders(k));
	}
	
	@Test
	void setNewQuantities() {
		Item i1 = new Item(2, "Peaches", null, 22, 0, 0, null, 0, 16);
		Mockito.when(iDAO.existsById(i1.getIid())).thenReturn(true);
		Mockito.when(iDAO.save(i1)).thenReturn(i1);
		Assertions.assertEquals(true, cServ.setNewQuantities(i1, 4));
	}
	
	@Test
	void delMyCartItem() {
		ResponseEntity<String> res = new ResponseEntity<String>("Cart item deleted.", HttpStatus.OK);
		CartItemProto cip = new CartItemProto(); Key k = new Key();
		doNothing().when(cDAO).delete(cip);
		Assertions.assertEquals(res, cServ.delMyCartItem(cip, k));
	}
	
	@Test
	void resetUnPw() {
		Key k = new Key(); k.setSid((long) 0);
		User b = new User(); Optional<User> bo = Optional.ofNullable(b);
		String un ="username"; String pswrd = "password";
		Mockito.when(userDAO.findByUname(un)).thenReturn(bo);
		Mockito.when(userDAO.findBySid(k.getSid())).thenReturn(bo);
		Mockito.when(userDAO.save(bo.get())).thenReturn(bo.get());
		ResponseEntity<String> res = new ResponseEntity<String>("User credentials successfully changed.", HttpStatus.OK);
		Assertions.assertEquals(res, cServ.resetUnPw(un, pswrd, k));
	}
	
	@Test
	void addToMyCart() {
		CartItemProto cip = new CartItemProto(); Key k =new Key((long) 4444, 3333);
		cip.setCid(24); 
		Mockito.when(iDAO.existsById(cip.getIid())).thenReturn(true);
		Mockito.when(cDAO.save(cip)).thenReturn(cip);
		ResponseEntity<String> res = new ResponseEntity<String>("Item added to cart.", HttpStatus.ACCEPTED);
		Assertions.assertEquals(res, cServ.addToMyCart(cip, k));
	}
	
	@Test
	void modUser() {
		User u1 = new User(); Key k = new Key();
		u1.setUid((long) 45);	k.setUid(45);	
		User u2 = new User(); u2.setUid((long) 5556);
		Mockito.when(userDAO.save(u1)).thenReturn(u1);
		
		ResponseEntity<String> resOK = new ResponseEntity<String>("User Updated", HttpStatus.OK);
		Assertions.assertEquals(resOK, cServ.modUser(u1, k));
		
		ResponseEntity<String> resBAD = InvalidException.thrown(String.format("UPDATE: Invalid User (%d!%d) modification.", u2.getUid(), k.getUid()), new RuntimeException());
		Assertions.assertEquals(resBAD, cServ.modUser(u2, k));
	}
	
	
}
