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
	public ResponseEntity<String> login(@RequestBody Credentials cred) {
		
		return custSvc.login(cred.getUname(), cred.getPswrd());
	}
	@PostMapping("/logout")
	public ResponseEntity<String> logout() {
		
		return custSvc.logout(usrSvc.logdin());
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
		return custSvc.getMyInfo(usrSvc.logdin());
	}

	@PostMapping
	public ResponseEntity<String> newUser(@RequestBody User u){
		
		return custSvc.addUsr(u);
	}
	
	@PatchMapping
//	public ResponseEntity<Integer> modUser(@RequestBody User u) {
	public ResponseEntity<String> modUser(@RequestBody User u) {
		
		return custSvc.modUser(u, usrSvc.logdin());
	}
	
	@PatchMapping("/resetlogin")
	public ResponseEntity<String> resetUnPw(@RequestBody Credentials c) {
		
		return custSvc.resetUnPw(c.getUname(), c.getPswrd(), usrSvc.logdin());
	}

	@DeleteMapping
	public ResponseEntity<String> removeUser() {
		
		return custSvc.delUser(usrSvc.logdin());
	}
	
	@PostMapping("/cart")
	public ResponseEntity<String> addToCart(@RequestBody CartItemProto cip) {
		
		return custSvc.addToMyCart(cip, usrSvc.logdin());
	}
	
	@PatchMapping("/cart")
	public ResponseEntity<String> modCartItem(@RequestBody CartItemProto cip) {
		
		return custSvc.modMyCart(cip, usrSvc.logdin());
	}
	
	@DeleteMapping("/cart")
	public ResponseEntity<String> removeCartItem(@RequestBody CartItemProto cip) {
		
		return custSvc.delMyCartItem(cip, usrSvc.logdin());
	}
	
	@DeleteMapping("/cart/all")
	public ResponseEntity<String> emptyCart() {
		
		return custSvc.emptyCart(usrSvc.logdin());
	}
	
	@GetMapping("/cart")
	public ResponseEntity<List<CartItem>> showCart() {
		
		return ResponseEntity.ok(custSvc.displayCart(usrSvc.logdin()));
	}
	
	@PutMapping("/checkout")
	public ResponseEntity<String> checkout() {
		
		return custSvc.checkout(usrSvc.logdin()); 
	}
	
	@GetMapping("/transaction")
	public ResponseEntity<List<Transaction>> showTransactions() {
		
		return ResponseEntity.ok(custSvc.displayTransactions(usrSvc.logdin()));
	}
	
	@GetMapping("/transaction/contents")
	public ResponseEntity<List<CartItem>> showTransactionItems(@RequestBody Transaction t) {
		
		return custSvc.displayTransactionItems(t, usrSvc.logdin());
	}
	
	@GetMapping("/backorder")
	public ResponseEntity<List<BackorderProto>> showBackorders() {
		
		return ResponseEntity.ok(custSvc.displayBackorders(usrSvc.logdin()));
	}
}
