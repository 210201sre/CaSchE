package com.revature.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.revature.exceptions.InvalidException;
import com.revature.models.Item;
import com.revature.models.Manufacturer;
import com.revature.services.ChkUsrSvc;
import com.revature.services.ItemService;

@RestController
@RequestMapping("/inv")
public class InventoryController {

	@Autowired
	private ItemService iSvc;
	@Autowired
	private ChkUsrSvc usrSvc;
	
	@GetMapping
	public ResponseEntity<List<Item>> showItems(){
		try {
			return ResponseEntity.ok(iSvc.displayInventory());
		} catch(IllegalArgumentException e) {
			InvalidException.thrown("SQLException: Invalid data inputed.", e);
			return ResponseEntity.status(400).body(null);
		} catch(Exception e) {
			InvalidException.thrown("Invalid data sent", e);
			return ResponseEntity.status(400).body(null);
		} //edited this method, hopefully it is aligned 
		
	}
	
	@GetMapping("/manufacturer/{mid}")
	public ResponseEntity<Manufacturer> showManufacturer(@PathVariable("mid") long i){
		try {
			return ResponseEntity.ok(iSvc.itemSupplier(i));
		} catch(IllegalArgumentException e) {
			InvalidException.thrown("SQLException: Invalid data inputed.", e);
			return ResponseEntity.status(400).body(null);
		} catch(Exception e) {
			InvalidException.thrown("Invalid data sent", e);
			return ResponseEntity.status(400).body(null);
		}
		
	}
	
	@GetMapping("/{mid}")
	public ResponseEntity<List<Item>> showManufacturerItems(@PathVariable("mid") long m) {
		try {
			return ResponseEntity.ok(iSvc.displaySupplierItems(m));
		} catch(IllegalArgumentException e) {
			InvalidException.thrown("SQLException: Invalid data inputed.", e);
			return ResponseEntity.status(400).body(null);
		} catch(Exception e) {
			InvalidException.thrown("Invalid data sent", e);
			return ResponseEntity.status(400).body(null);
		}
		
	}

	@GetMapping("/manufacturer")
	public ResponseEntity<List<Manufacturer>> showManufacturers() {
		try {
			return ResponseEntity.ok(iSvc.displaySuppliers());
		} catch(IllegalArgumentException e) {
			InvalidException.thrown("SQLException: Invalid data inputed.", e);
			return ResponseEntity.status(400).body(null);
		} catch(Exception e) {
			InvalidException.thrown("Invalid data sent", e);
			return ResponseEntity.status(400).body(null);
		}
		
	}
	
	//User Specific Services
	//Item Routing
	@PostMapping("/item")
	public ResponseEntity<String> newItem(@RequestBody Item i) {
		try {
			return iSvc.addItem(usrSvc.validateAdmin(usrSvc.logdin()), i);
		} catch(IllegalArgumentException e) {
			return InvalidException.thrown("SQLException: Invalid data inputed.", e);
		} catch(Exception e) {
			return InvalidException.thrown("Invalid data sent", e);
		}	
	}
	
	@PatchMapping("/item")
	public ResponseEntity<String> modItem(@RequestBody Item i) {
		try {
			return iSvc.modItem(usrSvc.validateAdmin(usrSvc.logdin()), i);
		} catch(IllegalArgumentException e) {
			return InvalidException.thrown("SQLException: Invalid data inputed.", e);

		} catch(Exception e) {
			return InvalidException.thrown("Invalid data sent", e);            
		}	
	}
	
	@PatchMapping("/item/restock")
	public ResponseEntity<String> restockItem(@RequestBody Item i) {
		try {
			return iSvc.restockItem(usrSvc.validateEmployee(usrSvc.logdin()), i, i.getQuantity());
		} catch(IllegalArgumentException e) {
			return InvalidException.thrown("SQLException: Invalid data inputed.", e);
            
		} catch(Exception e) {
			return InvalidException.thrown("Invalid data sent", e);
            
		}	
	}
	
	@DeleteMapping("/item")
	public ResponseEntity<String> delItem(@RequestBody Item i) {
		try {
			return iSvc.delItem(usrSvc.validateAdmin(usrSvc.logdin()), i);
		} catch(IllegalArgumentException e) {
			return InvalidException.thrown("SQLException: Invalid data inputed.", e);
            
		} catch(Exception e) {
			return InvalidException.thrown("Invalid data sent", e);
            
		}
		
	}
	
	//Manufacturer Routing
	@PostMapping("/manufacturer")
	public ResponseEntity<String> newManufacturer(@RequestBody Manufacturer m) {
		try {
			return iSvc.addSupplier(usrSvc.validateAdmin(usrSvc.logdin()), m);
		} catch(IllegalArgumentException e) {
			return InvalidException.thrown("SQLException: Invalid data inputed.", e);
            
		} catch(Exception e) {
			return InvalidException.thrown("Invalid data sent", e);
            
		}
		
	}
	
	@PatchMapping("/manufacturer")
	public ResponseEntity<String> modManufacturer(@RequestBody Manufacturer m) {
		try {
			return iSvc.modSupplier(usrSvc.validateAdmin(usrSvc.logdin()), m);
		} catch(IllegalArgumentException e) {
			return InvalidException.thrown("SQLException: Invalid data inputed.", e);
            
		} catch(Exception e) {
			return InvalidException.thrown("Invalid data sent", e);
            
		}
		
	}
	
	@DeleteMapping("/manufacturer")
	public ResponseEntity<String> removeManufacturer(@RequestBody Manufacturer m) {
		try {
			return iSvc.delSupplier(usrSvc.validateAdmin(usrSvc.logdin()), m);
		} catch(IllegalArgumentException e) {
			return InvalidException.thrown("SQLException: Invalid data inputed.", e);
            
		} catch(Exception e) {
			return InvalidException.thrown("Invalid data sent", e);
            
		} 	
	}
}
