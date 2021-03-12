package com.revature.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.models.BackorderProto;
import com.revature.models.CartItem;
import com.revature.models.CartItemProto;
import com.revature.models.Credentials;
import com.revature.models.Transaction;
import com.revature.models.User;
import com.revature.services.ChkUsrSvc;
import com.revature.services.CustomerService;

@RestController
@RequestMapping("/user")
public class CustomerController {
	
	//@Autowired
	//private HttpServletRequest req;
	@Autowired
	private ChkUsrSvc usrSvc;
	@Autowired
	private CustomerService custSvc;
	//@Autowired
	//private AdminService admSvc;

	
	@PostMapping("/login")
	public ResponseEntity<User> login(@RequestBody Credentials cred) {
		
		return ResponseEntity.ok(custSvc.login(cred.getUname(), cred.getPswrd()));
	}
	@PostMapping("/logout")
	public ResponseEntity<Void> logout() {
		custSvc.logout(usrSvc.logdin());
		
		return ResponseEntity.accepted().build();
	}
	
//	@ValidateCustomer
	@GetMapping
	public ResponseEntity<User> myInfo() {
		
		// Call following if method requires access validation
//		Key k = usrSvc.logdin();
//		usrSvc.validateCustomer(k);
//		return ResponseEntity.ok(custService.getMyInfo(k));

// custSvc.doSomething(usrSvc.validateCustomer(logdin()), ...)
		// Get user's session data in CustomerService
		return ResponseEntity.ok(custSvc.getMyInfo(usrSvc.logdin()));
	}

	@PostMapping
	public ResponseEntity<User> newUser(@RequestBody User u){
		
		return ResponseEntity.accepted().body(custSvc.addUsr(u));
	}
	
	@PatchMapping
//	public ResponseEntity<Integer> modUser(@RequestBody User u) {
	public ResponseEntity<User> modUser(@RequestBody User u) {
		
		return ResponseEntity.accepted().body(custSvc.modUser(u, usrSvc.logdin()));
	}
	
	@PatchMapping("/resetlogin")
	public ResponseEntity<User> resetUnPw(@RequestBody Credentials c) {
		
		return ResponseEntity.accepted().body(custSvc.resetUnPw(c.getUname(), c.getPswrd(), usrSvc.logdin()));
	}

	@DeleteMapping
	public ResponseEntity<Boolean> removeUser() {
		
		return ResponseEntity.accepted().body(custSvc.delUser(usrSvc.logdin()));
	}
	
	@PostMapping("/cart")
	public ResponseEntity<CartItem> addToCart(@RequestBody CartItemProto cip) {
		
		return ResponseEntity.accepted().body(custSvc.addToMyCart(cip, usrSvc.logdin()));
	}
	
	@PatchMapping("/cart")
	public ResponseEntity<CartItem> modCartItem(@RequestBody CartItemProto cip) {
		
		return ResponseEntity.accepted().body(custSvc.modMyCart(cip, usrSvc.logdin()));
	}
	
	@DeleteMapping("/cart")
	public ResponseEntity<Boolean> removeCartItem(@RequestBody CartItemProto cip) {
		
		return ResponseEntity.accepted().body(custSvc.delMyCartItem(cip, usrSvc.logdin()));
	}
	
	@DeleteMapping("/cart/all")
	public ResponseEntity<Boolean> emptyCart() {
		
		return ResponseEntity.accepted().body(custSvc.emptyCart(usrSvc.logdin()));
	}
	
	@GetMapping("/cart")
	public ResponseEntity<List<CartItem>> showCart() {
		
		return ResponseEntity.ok(custSvc.displayCart(usrSvc.logdin()));
	}
	
	@PutMapping("/checkout")
	public ResponseEntity<Boolean> checkout() {
		
		return ResponseEntity.ok(custSvc.checkout(usrSvc.logdin())); 
	}
	
	@GetMapping("/transaction")
	public ResponseEntity<List<Transaction>> showTransactions() {
		
		return ResponseEntity.ok(custSvc.displayTransactions(usrSvc.logdin()));
	}
	
	@GetMapping("/transaction/contents")
	public ResponseEntity<List<CartItem>> showTransactionItems(@RequestBody Transaction t) {
		
		return ResponseEntity.ok(custSvc.displayTransactionItems(t, usrSvc.logdin()));
	}
	
	@GetMapping("/backorder")
	public ResponseEntity<List<BackorderProto>> showBackorders() {
		
		return ResponseEntity.ok(custSvc.displayBackorders(usrSvc.logdin()));
	}
}
