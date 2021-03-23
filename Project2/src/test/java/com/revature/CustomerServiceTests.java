package com.revature;

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

import com.revature.models.CartItem;
import com.revature.models.Item;
import com.revature.models.User;
import com.revature.repositories.UserDAO;
import com.revature.services.AdminService;
import com.revature.services.CustomerService;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTests {
	
	@Mock
	UserDAO userDAO;
	
	@InjectMocks
	CustomerService cServ;

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
	
//	@Test
//	void getMyInfo() {
//		Optional<User> b = Optional.of(new User((long) 24000, "Ben", "Cady", null, null, null, null, null, null, null, null, null, null));
//		Mockito.doReturn(b).when(userDAO).findById((long) 24000);
//	}
	
}
