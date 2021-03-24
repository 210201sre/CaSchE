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

import com.revature.models.Item;
import com.revature.models.Key;
import com.revature.models.Manufacturer;
import com.revature.repositories.BackorderDAO;
import com.revature.repositories.CartDAO;
import com.revature.repositories.ItemDAO;
import com.revature.repositories.ManufacturerDAO;
import com.revature.repositories.TuiDAO;
import com.revature.services.ItemService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ItemServiceTests {

	@Mock
	ItemDAO iDAO;
	@Mock
	ManufacturerDAO mDAO;
	@Mock
	CartDAO cDAO;
	@Mock
	BackorderDAO boDAO;
	@Mock
	TuiDAO tuiDAO;
	
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
	
	@Test
	void delItem() {
		Key key = new Key();
		Item i1 = new Item(23, "Peaches", null, 0, 0, 0, null, 0, 16);
		Mockito.when(iDAO.existsById(i1.getIid())).thenReturn(true);
		Mockito.when(cDAO.existsById(i1.getIid())).thenReturn(false);
		Mockito.when(boDAO.existsById(i1.getIid())).thenReturn(false);
		Mockito.when(tuiDAO.existsById(i1.getIid())).thenReturn(false);
		doNothing().when(iDAO).delete(i1);
		ResponseEntity<String> res = new ResponseEntity<String>("Item Deleted", HttpStatus.OK);
		Assertions.assertEquals(res, iServ.delItem(key, i1));
	}
	
	@Test
	void restockItem() {
		Key key = new Key();
		Item d = new Item();
		Optional<Item> i1 = Optional.ofNullable(new Item(23, "Peaches", null, 4, 0, 0, null, 0, 16));
		Mockito.when(iDAO.existsById(d.getIid())).thenReturn(true);
		Mockito.when(iDAO.findById(d.getIid())).thenReturn(i1);
		Mockito.when(iDAO.save(i1.get())).thenReturn(i1.get());
		ResponseEntity<String> res = new ResponseEntity<String>("Item Restocked", HttpStatus.OK);
		Assertions.assertEquals(res, iServ.restockItem(key, d, 16));	
	}
	
	@Test
	void addSupplier() {
		Key key = new Key();
		Manufacturer m = new Manufacturer(); m.setMid(0);
		Mockito.when(mDAO.save(m)).thenReturn(m);
		ResponseEntity<String> res = new ResponseEntity<String>("Supplier Added", HttpStatus.ACCEPTED);
		Assertions.assertEquals(res, iServ.addSupplier(key, m));
	}
	
	@Test
	void delSupplier() {
		Key key = new Key();
		Manufacturer m1 = new Manufacturer(); m1.setMid(23);
		List<Item> allI = new ArrayList<Item>();
		Item i1 = new Item(); Item i2 = new Item(); Item i3 = new Item();
		allI.add(i3); allI.add(i1); allI.add(i2);
		Mockito.when(iDAO.findAllByMid(m1.getMid())).thenReturn(allI);
		Item i = new Item();
		Mockito.when(cDAO.existsById(i.getIid())).thenReturn(false);
		Mockito.when(boDAO.existsById(i.getIid())).thenReturn(false);
		Mockito.when(tuiDAO.existsById(i.getIid())).thenReturn(false);
		doNothing().when(iDAO).deleteByMid(m1.getMid());
		doNothing().when(mDAO).deleteById(m1.getMid());
		ResponseEntity<String> res = new ResponseEntity<String>("Supplier Deleted", HttpStatus.OK);
		Assertions.assertEquals(res, iServ.delSupplier(key, m1));
		
	}
	
	@Test
	void modSupplier() {
		Key k = new Key();
		Manufacturer m = new Manufacturer(); m.setMid(3);
		Mockito.when(mDAO.save(m)).thenReturn(m);
		ResponseEntity<String> res = new ResponseEntity<String>("Supplier information modified.", HttpStatus.OK);
		Assertions.assertEquals(res, iServ.modSupplier(k, m));
	}
}
