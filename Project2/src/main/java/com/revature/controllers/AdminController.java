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

import com.revature.exceptions.InvalidException;
import com.revature.models.Coupon;
import com.revature.models.Key;
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
		try {
			return aSvc.addUser(usrSvc.validateAdmin(usrSvc.logdin()), u);
		} catch (IllegalArgumentException e) {
			return InvalidException.thrown("SQLException: Invalid data inputed.", e);
		} catch (Exception e) {
			return InvalidException.thrown("Invalid data sent", e);
		}
	}

//	@PatchMapping("/user")
//	public ResponseEntity<String> modifyUser(@RequestBody User u) {
//		try {
//			return aSvc.updateUserAtRequestByUser(usrSvc.validateAdmin(usrSvc.logdin()), u);
//		} catch (IllegalArgumentException e) {
//			return InvalidException.thrown("SQLException: Invalid data inputed.", e);
//		} catch (Exception e) {
//			return InvalidException.thrown("Invalid data sent", e);
//		}
//	}

	@DeleteMapping("/user")
	public ResponseEntity<String> removeUser(@RequestBody User u) {
		try {
			if(usrSvc.validateAdmin(usrSvc.logdin()).equals(new Key())) {
				return InvalidException.thrown("Permission Denied");
			}
			return aSvc.delCustomer(usrSvc.validateAdmin(usrSvc.logdin()), u);
		} catch (IllegalArgumentException e) {
			return InvalidException.thrown("SQLException: Invalid data inputed.", e);
		} catch (Exception e) {
			return InvalidException.thrown("Invalid data sent", e);
		}
	}

	@GetMapping("/user")
	public ResponseEntity<List<User>> showAllUsers() {
		try {
			if(usrSvc.validateAdmin(usrSvc.logdin()).equals(new Key())) {
				return ResponseEntity.status(400).body(null);
			}
			return ResponseEntity.ok(aSvc.displayAllUsers(usrSvc.validateAdmin(usrSvc.logdin())));
		} catch (IllegalArgumentException e) {
			InvalidException.thrown("SQLException: Invalid data inputed.", e);
			return ResponseEntity.status(400).body(null);	// Check for specific 4XX status
		} catch (Exception e) {
			InvalidException.thrown("Invalid data sent", e);
			return ResponseEntity.status(400).body(null);	// Check for specific 4XX status
		}
	}

//	@PatchMapping("/user/hire")
//	public ResponseEntity<String> hireUser(@RequestBody User u) {
//		try {
//			return aSvc.hireCustomer(usrSvc.validateAdmin(usrSvc.logdin()), u);
//		} catch (IllegalArgumentException e) {
//			return InvalidException.thrown("SQLException: Invalid data inputed.", e);
//		} catch (Exception e) {
//			return InvalidException.thrown("Invalid data sent", e);
//		}
//	}
//
//	@PatchMapping("/user/release")
//	public ResponseEntity<String> releaseUser(@RequestBody User u) {
//		try {
//			return aSvc.releaseEmployee(usrSvc.validateAdmin(usrSvc.logdin()), u);
//		} catch (IllegalArgumentException e) {
//			return InvalidException.thrown("SQLException: Invalid data inputed.", e);
//		} catch (Exception e) {
//			return InvalidException.thrown("Invalid data sent", e);
//		}
//	}

	@DeleteMapping("/user/transaction")
	public ResponseEntity<String> removeTransaction(@RequestBody Transaction t) {
		try {
			if(usrSvc.validateAdmin(usrSvc.logdin()).equals(new Key())) {
				return InvalidException.thrown("Permission Denied");
			}
			return aSvc.delUserTransaction(usrSvc.validateAdmin(usrSvc.logdin()), t);
		} catch (IllegalArgumentException e) {
			return InvalidException.thrown("SQLException: Invalid data inputed.", e);
		} catch (Exception e) {
			return InvalidException.thrown("Invalid data sent", e);
		}
	}

	@DeleteMapping("/user/transaction/item")
	public ResponseEntity<String> removeUserTransactionItem(@RequestBody TuiProto tp) {
		try {
			if(usrSvc.validateAdmin(usrSvc.logdin()).equals(new Key())) {
				return InvalidException.thrown("Permission Denied");
			}
			return aSvc.delUserTransactionItem(usrSvc.validateAdmin(usrSvc.logdin()), tp);
		} catch (IllegalArgumentException e) {
			return InvalidException.thrown("SQLException: Invalid data inputed.", e);
		} catch (Exception e) {
			return InvalidException.thrown("Invalid data sent", e);
		}
	}

	@GetMapping("/coupon")
	public ResponseEntity<List<Coupon>> showCoupons() {
		try {
			if(usrSvc.validateAdmin(usrSvc.logdin()).equals(new Key())) {
				return ResponseEntity.status(400).body(null);
			}
			return ResponseEntity.ok(aSvc.displayCoupons(usrSvc.validateAdmin(usrSvc.logdin())));
		} catch (IllegalArgumentException e) {
			InvalidException.thrown("SQLException: Invalid data inputed.", e);
			return ResponseEntity.status(400).body(null);	// Check for specific 4XX status
		} catch (Exception e) {
			InvalidException.thrown("Invalid data sent", e);
			return ResponseEntity.status(400).body(null);	// Check for specific 4XX status
		}
	}

	@PostMapping("/coupon")
	public ResponseEntity<String> createCoupon(@RequestBody Coupon c) {
		try {
			if(usrSvc.validateAdmin(usrSvc.logdin()).equals(new Key())) {
				return InvalidException.thrown("Permission Denied");
			}
			return aSvc.addCoupon(usrSvc.validateAdmin(usrSvc.logdin()), c);
		} catch (IllegalArgumentException e) {
			return InvalidException.thrown("SQLException: Invalid data inputed.", e);
		} catch (Exception e) {
			return InvalidException.thrown("Invalid data sent", e);
		}
	}

	@PatchMapping("/coupon")
	public ResponseEntity<String> modifyCoupon(@RequestBody Coupon c) {
		try {
			if(usrSvc.validateAdmin(usrSvc.logdin()).equals(new Key())) {
				return InvalidException.thrown("Permission Denied");
			}
			return aSvc.modCoupon(usrSvc.validateAdmin(usrSvc.logdin()), c);
		} catch (IllegalArgumentException e) {
			return InvalidException.thrown("SQLException: Invalid data inputed.", e);
		} catch (Exception e) {
			return InvalidException.thrown("Invalid data sent", e);
		}
	}

	@DeleteMapping("/coupon")
	public ResponseEntity<String> removeCoupon(@RequestBody Coupon c) {
		try {
			if(usrSvc.validateAdmin(usrSvc.logdin()).equals(new Key())) {
				return InvalidException.thrown("Permission Denied");
			}
			return aSvc.delCoupon(usrSvc.validateAdmin(usrSvc.logdin()), c);
		} catch (IllegalArgumentException e) {
			return InvalidException.thrown("SQLException: Invalid data inputed.", e);
		} catch (Exception e) {
			return InvalidException.thrown("Invalid data sent", e);
		}
	}
}
