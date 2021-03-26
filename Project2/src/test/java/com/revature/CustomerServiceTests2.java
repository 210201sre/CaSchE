package com.revature;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.revature.exceptions.InvalidException;
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
import com.revature.services.CustomerService;

import io.micrometer.core.instrument.MeterRegistry;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTests2 {

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
	void delUser() {
		Key k = new Key();
		Mockito.when(userDAO.findById(k.getUid())).thenReturn(Optional.empty());
		ResponseEntity<String> res = InvalidException.thrown("User does not exist.", new RuntimeException());
		Assertions.assertEquals(res, cServ.delUser(k));
		
		User j = new User(); Optional<User> u2 = Optional.ofNullable(j);
		Mockito.when(userDAO.findById(k.getUid())).thenReturn(u2);
		u2.get().setSid((long) 65000); k.setSid((long) 23);
		ResponseEntity<String> res2 = InvalidException.thrown("Invalid Session", new RuntimeException());
		Assertions.assertEquals(res2, cServ.delUser(k));
	}
	
	@Test
	void addToMyCart(){
		CartItemProto cip = new CartItemProto(); Key k =new Key((long) 4444, 3333);
		k.setSid(null); cip.setUid(3333);
		ResponseEntity<String> res = InvalidException.thrown(String.format("UPDATE: Invalid ID(s) (%d:%d:%d) passed during cart update.",
				cip.getUid(), cip.getCid(), k.getSid()), new RuntimeException());
		Assertions.assertEquals(res, cServ.addToMyCart(cip, k));
	}
	
	@Test
	void addToMyCart2(){
		CartItemProto cip = new CartItemProto(); Key k =new Key((long) 4444, 3333);
		cip.setCid(24); cip.setUid(48);
		Mockito.when(iDAO.existsById(cip.getIid())).thenReturn(false);
		ResponseEntity<String> res = InvalidException.thrown(String.format("SELECT: Item %d does not exist.", cip.getIid()), new RuntimeException());
		Assertions.assertEquals(res, cServ.addToMyCart(cip, k));
	}
	
	@Test
	void delCartItem() {
		CartItemProto cip = new CartItemProto(); Key k = new Key();
		k.setUid(0); cip.setUid(3000);
		ResponseEntity<String> res = InvalidException.thrown(String.format("UPDATE: User %d mismatch %d", cip.getUid(), k.getUid()), new RuntimeException());
		Assertions.assertEquals(res, cServ.delMyCartItem(cip, k));
	}
	
	@Test
	void emptyCart() {
		Key k = new Key();
		Mockito.when(cDAO.countByUid(k.getUid())).thenReturn((long) 0);
		ResponseEntity<String> res = InvalidException.thrown(String.format("DELETE: User %d cart is empty.", k.getUid()), new RuntimeException());
		Assertions.assertEquals(res, cServ.emptyCart(k));
	}
	
	@Test
	void displayTransactionItems() {
		Key k = new Key();
		Transaction t = new Transaction(); t.setTid(30); k.setUid(5); t.setUid(5);
		List<CartItem> lci = null;
		ResponseEntity<List<CartItem>> res = ResponseEntity.status(400).body(lci);
		Assertions.assertEquals(res, cServ.displayTransactionItems(t, k));
	}
	
	@Test
	void setNewQuantities() {
		Item i = new Item();
		Mockito.when(iDAO.existsById(i.getIid())).thenReturn(false);
		Assertions.assertEquals(false, cServ.setNewQuantities(i, (long) 7000));
	}
	
	@Test
	void buildCartItem() {
		CartItemProto cip1 = new CartItemProto(21, 22, 3, 24);
		Mockito.when(iDAO.findById(cip1.getIid())).thenReturn(Optional.empty());
		CartItem p = new CartItem();
		Assertions.assertEquals(p, cServ.buildCartItem(cip1));
		
	}
	
	@Test
	void addUsr2() {
		User b = new User(); Optional<User> bo = Optional.ofNullable(b);
		Mockito.when(userDAO.findByUname(b.getUname())).thenReturn(bo);
		ResponseEntity<String> res = InvalidException.thrown(String.format("INSERT: Username %s already exists.", b.getUname()), new RuntimeException());
		Assertions.assertEquals(res, cServ.addUsr(b));
	}
	
	@Test
	void addUsr3() {
		User b = new User(); 
		Mockito.when(userDAO.findByUname(b.getUname())).thenReturn(Optional.empty());
		b.setUid((long) 6);
		ResponseEntity<String> res = InvalidException.thrown(String.format("INSERT: Invalid ID(s) (%d:%d) passed during insertion.", b.getUid(), b.getSid()), new RuntimeException());
		Assertions.assertEquals(res, cServ.addUsr(b));
	}
	
	@Test
	void addUsr4() {
		User b = new User(); 
		Mockito.when(userDAO.findByUname(b.getUname())).thenReturn(Optional.empty());
		b.setUid(null); b.setSid(null); b.setAccesslevel("Admin");
		ResponseEntity<String> res = InvalidException.thrown(String.format("INSERT: Invalid access. User: %s",b.toString()), new RuntimeException());
		Assertions.assertEquals(res, cServ.addUsr(b));
	}
	
	@Test
	void getMyInfo2() {
		Key k = new Key();
		Mockito.when(userDAO.findById(k.getUid())).thenReturn(Optional.empty());
		User ou = null;
		ResponseEntity<User> reu = ResponseEntity.status(400).body(ou);
		Assertions.assertEquals(reu, cServ.getMyInfo(k));
	}
	
}
