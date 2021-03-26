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

import com.revature.exceptions.InvalidException;
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

	@Autowired
	private ChkUsrSvc usrSvc;
	@Autowired
	private CustomerService custSvc;

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody Credentials cred) {
		try {
			return custSvc.login(cred.getUname(), cred.getPswrd());
		} catch (IllegalArgumentException e) {
			return InvalidException.thrown("SQLException: Invalid data inputed.", e);
		} catch (Exception e) {
			return InvalidException.thrown("Invalid data sent", e);
		}
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout() {
		try {
			return custSvc.logout(usrSvc.logdin());
		} catch (IllegalArgumentException e) {
			return InvalidException.thrown("SQLException: Invalid data inputed.", e);
		} catch (Exception e) {
			return InvalidException.thrown("Invalid data sent", e);
		}
	}

	@GetMapping
	public ResponseEntity<User> myInfo() {
		try {
			return custSvc.getMyInfo(usrSvc.logdin());
		} catch (IllegalArgumentException e) {
			InvalidException.thrown("SQLException: Invalid data inputed.", e);
			return ResponseEntity.status(400).body(null);	// Check for specific 4XX status
		} catch (Exception e) {
			InvalidException.thrown("Invalid data sent", e);
			return ResponseEntity.status(400).body(null);	// Check for specific 4XX status
		}
	}

	@PostMapping
	public ResponseEntity<String> newUser(@RequestBody User u) {
		try {
			return custSvc.addUsr(u);
		} catch (IllegalArgumentException e) {
			return InvalidException.thrown("SQLException: Invalid data inputed.", e);
		} catch (Exception e) {
			return InvalidException.thrown("Invalid data sent.", e);
		}
	}

	@PatchMapping
	public ResponseEntity<String> modUser(@RequestBody User u) {
		try {
			return custSvc.modUser(u, usrSvc.logdin());
		} catch (IllegalArgumentException e) {
			return InvalidException.thrown("SQLException: Invalid data inputed.", e);
		} catch (Exception e) {
			return InvalidException.thrown("Invalid data sent", e);
		}
	}

	@PatchMapping("/resetlogin")
	public ResponseEntity<String> resetUnPw(@RequestBody Credentials c) {
		try {
			return custSvc.resetUnPw(c.getUname(), c.getPswrd(), usrSvc.logdin());
		} catch (IllegalArgumentException e) {
			return InvalidException.thrown("SQLException: Invalid data inputed.", e);
		} catch (Exception e) {
			return InvalidException.thrown("Invalid data sent", e);
		}
	}

	@DeleteMapping
	public ResponseEntity<String> removeUser() {
		try {
			return custSvc.delUser(usrSvc.logdin());
		} catch (IllegalArgumentException e) {
			return InvalidException.thrown("SQLException: Invalid data inputed.", e);
		} catch (Exception e) {
			return InvalidException.thrown("Invalid data sent", e);
		}
	}

	@PostMapping("/cart")
	public ResponseEntity<String> addToCart(@RequestBody CartItemProto cip) {
		try {
			return custSvc.addToMyCart(cip, usrSvc.logdin());
		} catch (IllegalArgumentException e) {
			return InvalidException.thrown("SQLException: Invalid data inputed.", e);
		} catch (Exception e) {
			return InvalidException.thrown("Invalid data sent", e);
		}
	}

//	@PatchMapping("/cart")
//	public ResponseEntity<String> modCartItem(@RequestBody CartItemProto cip) {
//		try {
//			return custSvc.modMyCart(cip, usrSvc.logdin());
//		} catch (IllegalArgumentException e) {
//			return InvalidException.thrown("SQLException: Invalid data inputed.", e);
//		} catch (Exception e) {
//			return InvalidException.thrown("Invalid data sent", e);
//		}
//	}

	@DeleteMapping("/cart")
	public ResponseEntity<String> removeCartItem(@RequestBody CartItemProto cip) {
		try {
			return custSvc.delMyCartItem(cip, usrSvc.logdin());
		} catch (IllegalArgumentException e) {
			return InvalidException.thrown("SQLException: Invalid data inputed.", e);
		} catch (Exception e) {
			return InvalidException.thrown("Invalid data sent", e);
		}
	}

	@DeleteMapping("/cart/all")
	public ResponseEntity<String> emptyCart() {
		try {
			return custSvc.emptyCart(usrSvc.logdin());
		} catch (IllegalArgumentException e) {
			return InvalidException.thrown("SQLException: Invalid data inputed.", e);
		} catch (Exception e) {
			return InvalidException.thrown("Invalid data sent", e);
		}
	}

	@GetMapping("/cart")
	public ResponseEntity<List<CartItem>> showCart() {
		try {
			return ResponseEntity.ok(custSvc.displayCart(usrSvc.logdin()));
		} catch (IllegalArgumentException e) {
			InvalidException.thrown("SQLException: Invalid data inputed.", e);
			return ResponseEntity.status(400).body(null);	// Check for specific 4XX status
		} catch (Exception e) {
			InvalidException.thrown("Invalid data sent", e);
			return ResponseEntity.status(400).body(null);	// Check for specific 4XX status
		}
	}

	@PutMapping("/checkout")
	public ResponseEntity<String> checkout() {
		try {
			return custSvc.checkout(usrSvc.logdin());
		} catch (IllegalArgumentException e) {
			return InvalidException.thrown("SQLException: Invalid data inputed.", e);
		} catch (Exception e) {
			return InvalidException.thrown("Invalid data sent", e);
		}
	}

	@GetMapping("/transaction")
	public ResponseEntity<List<Transaction>> showTransactions() {
		try {
			return ResponseEntity.ok(custSvc.displayTransactions(usrSvc.logdin()));
		} catch (IllegalArgumentException e) {
			InvalidException.thrown("SQLException: Invalid data inputed.", e);
			return ResponseEntity.status(400).body(null);	// Check for specific 4XX status
		} catch (Exception e) {
			InvalidException.thrown("Invalid data sent", e);
			return ResponseEntity.status(400).body(null);	// Check for specific 4XX status
		}
	}

	@GetMapping("/transaction/contents")
	public ResponseEntity<List<CartItem>> showTransactionItems(@RequestBody Transaction t) {
		try {
			return custSvc.displayTransactionItems(t, usrSvc.logdin());
		} catch (IllegalArgumentException e) {
			InvalidException.thrown("SQLException: Invalid data inputed.", e);
			return ResponseEntity.status(400).body(null);	// Check for specific 4XX status
		} catch (Exception e) {
			InvalidException.thrown("Invalid data sent", e);
			return ResponseEntity.status(400).body(null);	// Check for specific 4XX status
		}
	}

	@GetMapping("/backorder")
	public ResponseEntity<List<BackorderProto>> showBackorders() {
		try {
			return ResponseEntity.ok(custSvc.displayBackorders(usrSvc.logdin()));
		} catch (IllegalArgumentException e) {
			InvalidException.thrown("SQLException: Invalid data inputed.", e);
			return ResponseEntity.status(400).body(null);	// Check for specific 4XX status
		} catch (Exception e) {
			InvalidException.thrown("Invalid data sent", e);
			return ResponseEntity.status(400).body(null);	// Check for specific 4XX status
		}
	}
}
