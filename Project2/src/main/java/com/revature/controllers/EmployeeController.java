package com.revature.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.models.CartItem;
import com.revature.models.Transaction;
import com.revature.models.TuiProto;
import com.revature.models.User;
import com.revature.services.AdminService;
import com.revature.services.ChkUsrSvc;
import com.revature.services.EmployeeService;

@RestController
@RequestMapping("/staff")
public class EmployeeController {
	
	@Autowired
	private EmployeeService eSvc;
	@Autowired
	private ChkUsrSvc usrSvc;
	@Autowired
	private AdminService aSvc;
	
	@GetMapping("/directory")
	public ResponseEntity<List<User>> showStaffDirectory() {
		
		return ResponseEntity.ok(eSvc.displayInternalDirectory(usrSvc.validateEmployee(usrSvc.logdin())));
	}
	
	@GetMapping("/user/transaction")
	public ResponseEntity<List<Transaction>> showUserTransactions(@RequestBody User u) {
		
		return aSvc.displayUserTransactions(usrSvc.validateEmployee(usrSvc.logdin()), u);
	}
	
	@GetMapping("/user/transaction/item")
	public ResponseEntity<List<CartItem>> showUserTransactionItems(@RequestBody Transaction t) {
		
		return aSvc.displayUserTransactionItems(usrSvc.validateEmployee(usrSvc.logdin()), t);
	}
	
	@PatchMapping("/user/transaction/item")
	public ResponseEntity<String> modifyUserTransactionItem(@RequestBody TuiProto tp) {
		
		return aSvc.modUserTransactionItem(usrSvc.validateEmployee(usrSvc.logdin()), tp);
	}
}
