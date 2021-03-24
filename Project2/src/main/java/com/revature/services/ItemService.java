package com.revature.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.revature.exceptions.InvalidException;
import com.revature.models.Item;
import com.revature.models.Key;
import com.revature.models.Manufacturer;
import com.revature.repositories.BackorderDAO;
import com.revature.repositories.CartDAO;
import com.revature.repositories.ItemDAO;
import com.revature.repositories.ManufacturerDAO;
import com.revature.repositories.TuiDAO;

@Service
public class ItemService {

	@Autowired
	private ItemDAO iDAO;
	@Autowired
	private ManufacturerDAO mDAO;
	@Autowired
	private CartDAO cDAO;
	@Autowired
	private BackorderDAO boDAO;
	@Autowired
	private TuiDAO tuiDAO;
//	@Autowired
//	private AdminService aSvc;

	public List<Item> displayInventory() { //*********
		
		return iDAO.findAll();
	}
	
	public ResponseEntity<String> addItem(Key k, Item i) { //*******************************
		if(i.getIid() != 0) {
			return InvalidException.thrown(String.format("INSERT: Invalid item id %d.", i.getIid()), new RuntimeException());
		}
		iDAO.save(i);
		return ResponseEntity.accepted().body("Item added.");
	}
	
	public ResponseEntity<String> modItem(Key k, Item i) { //**************************************
		if(!iDAO.existsById(i.getIid())) {
			return InvalidException.thrown(String.format("UPDATE: Item %d does not exist.", i.getIid()), new RuntimeException());
		}
		iDAO.save(i);
		return ResponseEntity.ok().body("Item updated.");
	}
	
	public ResponseEntity<String> delItem(Key k, Item i) { //*******************************************
		if(!iDAO.existsById(i.getIid())) {
			return InvalidException.thrown(String.format("DELETE: Item %d does not exist.", i.getIid()), new RuntimeException());
		}
		if(cDAO.existsByIid(i.getIid())
				|| boDAO.existsByIid(i.getIid())
				|| tuiDAO.existsByIid(i.getIid())) {
			return InvalidException.thrown(String.format("DELETE: Item %d is present in a cart/backorder/transaction.", i.getIid()), new RuntimeException());
		}
		iDAO.delete(i);	
		return ResponseEntity.ok().body("Item Deleted");
	}
	
	public ResponseEntity<String> restockItem(Key k, Item i, long amount) { //****************************
		if(!iDAO.existsById(i.getIid())) {
			return InvalidException.thrown(String.format("UPDATE: Item %d does not exist.", i.getIid()), new RuntimeException());
		}
		if(amount < 1) {
			return InvalidException.thrown("UPDATE: Restock amount must be greater than 0.", new RuntimeException());
		}
		i = iDAO.findById(i.getIid()).get();
		i.setQuantity(i.getQuantity()+amount);
		iDAO.save(i);
		return ResponseEntity.ok().body("Item Restocked");
	}

	public ResponseEntity<String> addSupplier(Key k, Manufacturer m) { //************************************
		if (m.getMid() != 0) {
			return InvalidException.thrown(String.format("INSERT: Invalid ID %d passed during Manufacturer insertion.", m.getMid()), new RuntimeException());
		}
		mDAO.save(m);
		return ResponseEntity.accepted().body("Supplier Added");
	}
	
	public ResponseEntity<String> modSupplier(Key k, Manufacturer m) {
		if (m.getMid() < 1) {
			return InvalidException.thrown(String.format("UPDATE: Invalid ID %d passed during Manufacturer modification.", m.getMid()), new RuntimeException());
		}
		mDAO.save(m);
		return ResponseEntity.ok().body("Supplier information modified.");
	}
	
	public ResponseEntity<String> delSupplier(Key k, Manufacturer m) { //****************************
		if (m.getMid() < 1) {
			return InvalidException.thrown(String.format("UPDATE: Invalid ID %d passed during Manufacturer modification.", m.getMid()), new RuntimeException());
		}
		List<Item> is = iDAO.findAllByMid(m.getMid());
		if (is != null && !is.isEmpty()) {
			for(Item i : is) {
				if (tuiDAO.existsById(i.getIid()) || cDAO.existsByIid(i.getIid()) || boDAO.existsByIid(i.getIid())) {
					return InvalidException.thrown(String.format("DELETE: Manufacturer item %s exists in cart/transaction/backorder.%nCannot remove manufacturer %s.", i.getUnitname(), m.getMname()), new RuntimeException());
				}
			}
		}
		iDAO.deleteByMid(m.getMid());
		mDAO.deleteById(m.getMid());
		return ResponseEntity.ok().body("Supplier Deleted");
	}

	public List<Item> displaySupplierItems(long mid) { //******
		
		return iDAO.findAllByMid(mid);
	}
	
	public Manufacturer itemSupplier(long mid) { //*******

		return mDAO.findById(mid);
	}
	
	public List<Manufacturer> displaySuppliers() { //********
		
		return mDAO.findAll();
	}
	
	
}
