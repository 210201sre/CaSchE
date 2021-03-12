package com.revature.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

	public List<Item> displayInventory() {
		
		return iDAO.findAll();
	}
	
	public Item addItem(Key k, Item i) {
		if(i.getIid() != 0) {
			throw new InvalidException(String.format("INSERT: Invalid item id %d.", i.getIid()));
		}
		return iDAO.save(i);
	}
	
	public Item modItem(Key k, Item i) {
		if(!iDAO.existsById(i.getIid())) {
			throw new InvalidException(String.format("UPDATE: Item %d does not exist.", i.getIid()));
		}
		return iDAO.save(i);
	}
	
	public boolean delItem(Key k, Item i) {
		if(!iDAO.existsById(i.getIid())) {
			throw new InvalidException(String.format("DELETE: Item %d does not exist.", i.getIid()));
		}
		if(cDAO.existsByIid(i.getIid())
				|| boDAO.existsByIid(i.getIid())
				|| tuiDAO.existsByIid(i.getIid())) {
			throw new InvalidException(String.format("DELETE: Item %d is present in a cart/backorder/transaction.", i.getIid()));
		}
		iDAO.delete(i);	
		return true;
	}
	
	public Item restockItem(Key k, Item i, long amount) {
		if(!iDAO.existsById(i.getIid())) {
			throw new InvalidException(String.format("UPDATE: Item %d does not exist.", i.getIid()));
		}
		if(amount < 1) {
			throw new InvalidException("UPDATE: Restock amount must be greater than 0.");
		}
		i = iDAO.findById(i.getIid()).get();
		i.setQuantity(i.getQuantity()+amount);
		return iDAO.save(i);
	}

	public Manufacturer addSupplier(Key k, Manufacturer m) {
		if (m.getMid() != 0) {
			throw new InvalidException (String.format("INSERT: Invalid ID %d passed during Manufacturer insertion.", m.getMid()));
		}
		return mDAO.save(m);
	}
	
	public Manufacturer modSupplier(Key k, Manufacturer m) {
		if (m.getMid() < 1) {
			throw new InvalidException (String.format("UPDATE: Invalid ID %d passed during Manufacturer modification.", m.getMid()));
		}
		return mDAO.save(m);
	}
	
	public boolean delSupplier(Key k, Manufacturer m) {
		if (m.getMid() < 1) {
			throw new InvalidException (String.format("UPDATE: Invalid ID %d passed during Manufacturer modification.", m.getMid()));
		}
		List<Item> is = iDAO.findAllByMid(m.getMid());
		if (is != null && !is.isEmpty()) {
			for(Item i : is) {
				if (tuiDAO.existsById(i.getIid()) || cDAO.existsByIid(i.getIid()) || boDAO.existsByIid(i.getIid())) {
					throw new InvalidException(String.format("DELETE: Manufacturer item %s exists in cart/transaction/backorder.%nCannot remove manufacturer %s.", i.getUnitname(), m.getMname()));
				}
			}
		}
		iDAO.deleteByMid(m.getMid());
		mDAO.deleteById(m.getMid());
		return true;
	}

	public List<Item> displaySupplierItems(long mid) {
		
		return iDAO.findAllByMid(mid);
	}
	
	public Manufacturer itemSupplier(long mid) {

		return mDAO.findById(mid);
	}
	
	public List<Manufacturer> displaySuppliers() {
		
		return mDAO.findAll();
	}
	
	
}
