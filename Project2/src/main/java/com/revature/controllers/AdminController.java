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
	public ResponseEntity<String> createUser(@RequestBody User u) {
		
		return aSvc.addUser(usrSvc.validateAdmin(usrSvc.logdin()), u);
	}
	
	@PatchMapping("/user")
	public ResponseEntity<String> modifyUser(@RequestBody User u) {
		
		return aSvc.updateUserAtRequestByUser(usrSvc.validateAdmin(usrSvc.logdin()), u);
	}
	
	@DeleteMapping("/user")
	public ResponseEntity<String> removeUser(@RequestBody User u) {
		
		return aSvc.delCustomer(usrSvc.validateAdmin(usrSvc.logdin()), u);
	}

	@GetMapping("/user")
	public ResponseEntity<List<User>> showAllUsers(){
		
		return ResponseEntity.ok(aSvc.displayAllUsers(usrSvc.validateAdmin(usrSvc.logdin())));
	}
	
	@PatchMapping("/user/hire")
	public ResponseEntity<String> hireUser(@RequestBody User u) {
		
		return aSvc.hireCustomer(usrSvc.validateAdmin(usrSvc.logdin()), u);
	}
	
	@PatchMapping("/user/release")
	public ResponseEntity<String> releaseUser(@RequestBody User u) {
		
		return aSvc.releaseEmployee(usrSvc.validateAdmin(usrSvc.logdin()), u);
	}

	@DeleteMapping("/user/transaction")
	public ResponseEntity<String> removeTransaction(@RequestBody Transaction t) {
		
		return aSvc.delUserTransaction(usrSvc.validateAdmin(usrSvc.logdin()), t);
	}
	
	@DeleteMapping("/user/transaction/item")
	public ResponseEntity<String> removeUserTransactionItem(@RequestBody TuiProto tp) {
		
		return aSvc.delUserTransactionItem(usrSvc.validateAdmin(usrSvc.logdin()), tp);
	}
	
	@GetMapping("/coupon")
	public ResponseEntity<List<Coupon>> showCoupons() {
		
		return ResponseEntity.ok(aSvc.displayCoupons(usrSvc.validateAdmin(usrSvc.logdin())));
	}
	
	@PostMapping("/coupon")
	public ResponseEntity<String> createCoupon(@RequestBody Coupon c) {
		
		return aSvc.addCoupon(usrSvc.validateAdmin(usrSvc.logdin()), c);
	}
	
	@PatchMapping("/coupon")
	public ResponseEntity<String> modifyCoupon(@RequestBody Coupon c) {
		
		return aSvc.modCoupon(usrSvc.validateAdmin(usrSvc.logdin()), c);
	}
	
	@DeleteMapping("/coupon")
	public ResponseEntity<String> removeCoupon(@RequestBody Coupon c) {
		
		return aSvc.delCoupon(usrSvc.validateAdmin(usrSvc.logdin()), c);
	}
}
