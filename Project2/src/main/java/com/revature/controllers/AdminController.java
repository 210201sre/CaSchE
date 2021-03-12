package com.revature.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.models.Coupon;
import com.revature.models.Transaction;
import com.revature.models.TuiProto;
import com.revature.models.User;
import com.revature.services.AdminService;
import com.revature.services.ChkUsrSvc;

@RestController
@RequestMapping("/manager")
public class AdminController {

	@Autowired
	private AdminService aSvc;
	@Autowired
	private ChkUsrSvc usrSvc;

	@PostMapping("/user")
	public ResponseEntity<User> createUser(@RequestBody User u) {
		
		return ResponseEntity.accepted().body(aSvc.addUser(usrSvc.validateAdmin(usrSvc.logdin()), u));
	}
	
	@PatchMapping("/user")
	public ResponseEntity<User> modifyUser(@RequestBody User u) {
		
		return ResponseEntity.accepted().body(aSvc.updateUserAtRequestByUser(usrSvc.validateAdmin(usrSvc.logdin()), u));
	}
	
	@DeleteMapping("/user")
	public ResponseEntity<Boolean> removeUser(@RequestBody User u) {
		
		return ResponseEntity.accepted().body(aSvc.delCustomer(usrSvc.validateAdmin(usrSvc.logdin()), u));
	}

	@GetMapping("/user")
	public ResponseEntity<List<User>> showAllUsers(){
		
		return ResponseEntity.ok(aSvc.displayAllUsers(usrSvc.validateAdmin(usrSvc.logdin())));
	}
	
	@PatchMapping("/user/hire")
	public ResponseEntity<User> hireUser(@RequestBody User u) {
		
		return ResponseEntity.accepted().body(aSvc.hireCustomer(usrSvc.validateAdmin(usrSvc.logdin()), u));
	}
	
	@PatchMapping("/user/release")
	public ResponseEntity<User> releaseUser(@RequestBody User u) {
		
		return ResponseEntity.accepted().body(aSvc.releaseEmployee(usrSvc.validateAdmin(usrSvc.logdin()), u));
	}

	@DeleteMapping("/user/transaction")
	public ResponseEntity<Boolean> removeTransaction(@RequestBody Transaction t) {
		
		return ResponseEntity.accepted().body(aSvc.delUserTransaction(usrSvc.validateAdmin(usrSvc.logdin()), t));
	}
	
	@DeleteMapping("/user/transaction/item")
	public ResponseEntity<Boolean> removeUserTransactionItem(@RequestBody TuiProto tp) {
		
		return ResponseEntity.accepted().body(aSvc.delUserTransactionItem(usrSvc.validateAdmin(usrSvc.logdin()), tp));
	}
	
	@GetMapping("/coupon")
	public ResponseEntity<List<Coupon>> showCoupons() {
		
		return ResponseEntity.ok(aSvc.displayCoupons(usrSvc.validateAdmin(usrSvc.logdin())));
	}
	
	@PostMapping("/coupon")
	public ResponseEntity<Coupon> createCoupon(@RequestBody Coupon c) {
		
		return ResponseEntity.accepted().body(aSvc.addCoupon(usrSvc.validateAdmin(usrSvc.logdin()), c));
	}
	
	@PatchMapping("/coupon")
	public ResponseEntity<Coupon> modifyCoupon(@RequestBody Coupon c) {
		
		return ResponseEntity.accepted().body(aSvc.modCoupon(usrSvc.validateAdmin(usrSvc.logdin()), c));
	}
	
	@DeleteMapping("/coupon")
	public ResponseEntity<Boolean> removeCoupon(@RequestBody Coupon c) {
		
		return ResponseEntity.accepted().body(aSvc.delCoupon(usrSvc.validateAdmin(usrSvc.logdin()), c));
	}
}
