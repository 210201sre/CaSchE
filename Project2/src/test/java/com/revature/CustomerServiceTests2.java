package com.revature;

import static org.mockito.Mockito.doNothing;

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
import com.revature.models.CartItemProto;
import com.revature.models.Key;
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
}
