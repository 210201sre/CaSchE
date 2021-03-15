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
		
		return ResponseEntity.ok(iSvc.displayInventory());
	}
	
	@GetMapping("/manufacturer/{mid}")
	public ResponseEntity<Manufacturer> showManufacturer(@PathVariable("mid") long i){
		
		return ResponseEntity.ok(iSvc.itemSupplier(i));
	}
	
	@GetMapping("/{mid}")
	public ResponseEntity<List<Item>> showManufacturerItems(@PathVariable("mid") long m) {
		
		return ResponseEntity.ok(iSvc.displaySupplierItems(m));
	}

	@GetMapping("/manufacturer")
	public ResponseEntity<List<Manufacturer>> showManufacturers() {
		
		return ResponseEntity.ok(iSvc.displaySuppliers());
	}
	
	//User Specific Services
	//Item Routing
	@PostMapping("/item")
	public ResponseEntity<String> newItem(@RequestBody Item i) {
		
		return iSvc.addItem(usrSvc.validateAdmin(usrSvc.logdin()), i);
	}
	
	@PatchMapping("/item")
	public ResponseEntity<String> modItem(@RequestBody Item i) {
		
		return iSvc.modItem(usrSvc.validateAdmin(usrSvc.logdin()), i);
	}
	
	@PatchMapping("/item/restock")
	public ResponseEntity<String> restockItem(@RequestBody Item i) {
		
		return iSvc.restockItem(usrSvc.validateEmployee(usrSvc.logdin()), i, i.getQuantity());
	}
	
	@DeleteMapping("/item")
	public ResponseEntity<String> delItem(@RequestBody Item i) {
		
		return iSvc.delItem(usrSvc.validateAdmin(usrSvc.logdin()), i);
	}
	
	//Manufacturer Routing
	@PostMapping("/manufacturer")
	public ResponseEntity<String> newManufacturer(@RequestBody Manufacturer m) {
		
		return iSvc.addSupplier(usrSvc.validateAdmin(usrSvc.logdin()), m);
	}
	
	@PatchMapping("/manufacturer")
	public ResponseEntity<String> modManufacturer(@RequestBody Manufacturer m) {
		
		return iSvc.modSupplier(usrSvc.validateAdmin(usrSvc.logdin()), m);
	}
	
	@DeleteMapping("/manufacturer")
	public ResponseEntity<String> removeManufacturer(@RequestBody Manufacturer m) {
		
		return iSvc.delSupplier(usrSvc.validateAdmin(usrSvc.logdin()), m);
	}
	
	
}
