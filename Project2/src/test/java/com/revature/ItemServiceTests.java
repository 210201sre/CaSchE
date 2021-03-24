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

import com.revature.models.Item;
import com.revature.models.Key;
import com.revature.models.Manufacturer;
import com.revature.repositories.ItemDAO;
import com.revature.repositories.ManufacturerDAO;
import com.revature.services.ItemService;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTests {

	@Mock
	ItemDAO iDAO;
	@Mock
	ManufacturerDAO mDAO;
	
	@InjectMocks
	ItemService iServ;
	
	@Test
	void displaySuppliers() {
		Manufacturer m1 = new Manufacturer();
		Manufacturer m2 = new Manufacturer();
		Manufacturer m3 = new Manufacturer();
		List<Manufacturer> allMan = new ArrayList<Manufacturer>();
		allMan.add(m1); allMan.add(m2); allMan.add(m3);
		Mockito.doReturn(allMan).when(mDAO).findAll();
		Assertions.assertEquals(allMan, iServ.displaySuppliers());
	}
	
	@Test
	void itemSupplier() {
		Manufacturer m1 = new Manufacturer(426, null, null, null, null, null, null, null, null);
		Mockito.doReturn(m1).when(mDAO).findById(426);
		Assertions.assertEquals(m1, iServ.itemSupplier(426));
	}
	
	@Test 
	void displaySupplierItems() {
		Item i1 = new Item(2, "Peaches", null, 0, 0, 0, null, 0, 16);
		Item i2 = new Item(3, "Limes", null, 0, 0, 0, null, 0, 16);
		List<Item> allI = new ArrayList<Item>();
		allI.add(i1); allI.add(i2);
		Mockito.doReturn(allI).when(iDAO).findAllByMid(16);
		Assertions.assertEquals(allI, iServ.displaySupplierItems(16));
	}
	
	@Test
	void displayInventory() {
		Item i1 = new Item(2, "Peaches", null, 0, 0, 0, null, 0, 16);
		Item i2 = new Item(3, "Limes", null, 0, 0, 0, null, 0, 16);
		Item i3 = new Item(4, "Shrimp", null, 0, 0, 0, null, 0, 17);
		List<Item> allI = new ArrayList<Item>();
		allI.add(i1); allI.add(i2); allI.add(i3);
		Mockito.doReturn(allI).when(iDAO).findAll();
		Assertions.assertEquals(allI, iServ.displayInventory());
	}
	
	@Test
	void addItem() {
		Key key = new Key();
		Item i1 = new Item(0, "Peaches", null, 0, 0, 0, null, 0, 16);
		Mockito.when(iDAO.save(i1)).thenReturn(i1);
		ResponseEntity<String> res = new ResponseEntity<String>("Item added.", HttpStatus.ACCEPTED);
		Assertions.assertEquals(res, iServ.addItem(key, i1));
	}
	
	@Test
	void modItem() {
		Key key = new Key();
		Item i1 = new Item(23, "Peaches", null, 0, 0, 0, null, 0, 16);
		Mockito.when(iDAO.existsById(i1.getIid())).thenReturn(true);
		Mockito.when(iDAO.save(i1)).thenReturn(i1);
		ResponseEntity<String> res = new ResponseEntity<String>("Item updated.", HttpStatus.OK);
		Assertions.assertEquals(res, iServ.modItem(key, i1));
	}
}
