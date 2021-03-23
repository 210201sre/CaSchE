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
}
