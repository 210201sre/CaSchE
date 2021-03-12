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
	public ResponseEntity<Item> newItem(@RequestBody Item i) {
		
		return ResponseEntity.accepted().body(iSvc.addItem(usrSvc.validateAdmin(usrSvc.logdin()), i));
	}
	
	@PatchMapping("/item")
	public ResponseEntity<Item> modItem(@RequestBody Item i) {
		
		return ResponseEntity.accepted().body(iSvc.modItem(usrSvc.validateAdmin(usrSvc.logdin()), i));
	}
	
	@PatchMapping("/item/restock")
	public ResponseEntity<Item> restockItem(@RequestBody Item i) {
		
		return ResponseEntity.accepted().body(iSvc.restockItem(usrSvc.validateEmployee(usrSvc.logdin()), i, i.getQuantity()));
	}
	
	@DeleteMapping("/item")
	public ResponseEntity<Boolean> delItem(@RequestBody Item i) {
		
		return ResponseEntity.accepted().body(iSvc.delItem(usrSvc.validateAdmin(usrSvc.logdin()), i));
	}
	
	//Manufacturer Routing
	@PostMapping("/manufacturer")
	public ResponseEntity<Manufacturer> newManufacturer(@RequestBody Manufacturer m) {
		
		return ResponseEntity.accepted().body(iSvc.addSupplier(usrSvc.validateAdmin(usrSvc.logdin()), m));
	}
	
	@PatchMapping("/manufacturer")
	public ResponseEntity<Manufacturer> modManufacturer(@RequestBody Manufacturer m) {
		
		return ResponseEntity.accepted().body(iSvc.modSupplier(usrSvc.validateAdmin(usrSvc.logdin()), m));
	}
	
	@DeleteMapping("/manufacturer")
	public ResponseEntity<Boolean> removeManufacturer(@RequestBody Manufacturer m) {
		
		return ResponseEntity.accepted().body(iSvc.delSupplier(usrSvc.validateAdmin(usrSvc.logdin()), m));
	}
	
	
}
