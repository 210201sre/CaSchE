package com.revature.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.revature.exceptions.InvalidException;
import com.revature.models.BackorderProto;
import com.revature.models.CartItem;
import com.revature.models.Coupon;
import com.revature.models.Item;
import com.revature.models.Key;
import com.revature.models.Transaction;
import com.revature.models.TuiProto;
import com.revature.models.User;
import com.revature.repositories.BackorderDAO;
import com.revature.repositories.CartDAO;
import com.revature.repositories.CouponDAO;
import com.revature.repositories.ItemDAO;
import com.revature.repositories.TransactionDAO;
import com.revature.repositories.TuiDAO;
import com.revature.repositories.UserDAO;

@Service
public class AdminService /* extends EmployeeService */ {

	private static final Logger log = LoggerFactory.getLogger(AdminService.class);

	@Autowired
	private UserDAO userDAO;
	@Autowired
	private TransactionDAO tDAO;
	@Autowired
	private ItemDAO iDAO;
	@Autowired
	private TuiDAO tuiDAO;
	@Autowired
	private CartDAO cDAO;
	@Autowired
	private BackorderDAO boDAO;
	@Autowired
	private CouponDAO coupDAO;
	
	private String labelAction = "Action";

	public ResponseEntity<String> addUser(Key k, User u) { //************************************
		MDC.put(labelAction, "Adm Add User");
		if (userDAO.findByUname(u.getUname()).isPresent()) {
			return InvalidException.thrown(String.format("INSERT: Username %s already exists.", u.getUname()),
					new RuntimeException());
		}
		if (u.getUid() != null || u.getSid() != null) {
			return InvalidException.thrown(
					String.format("INSERT: Invalid ID(s) (%d:%d) passed during insertion.", u.getUid(), u.getSid()),
					new RuntimeException());
		}
		userDAO.save(u);
		return ResponseEntity.accepted().body("User Created");
	}

//	public ResponseEntity<String> hireCustomer(Key k, User u) { //***********************
//		MDC.put(labelAction, "Adm Hire Customer");
//		Optional<User> u2 = userDAO.findById(u.getUid());
//		if (u2.isPresent()) {
//			if (!u2.get().getAccesslevel().equals("Customer")) {
//				return InvalidException.thrown(String.format("UPDATE: User %d is not a customer to hire.", u.getUid()),
//						new RuntimeException());
//			}
//		} else {
//			return InvalidException.thrown(String.format("UPDATE: Customer %d does not exist.", u.getUid()),
//					new RuntimeException());
//		}
//		if ((!u.getAccesslevel().equals("Employee") && !u.getAccesslevel().equals("Admin"))
//				|| u.getAccesslevel().equals("Customer")) {
//			return InvalidException.thrown("UPDATE: User accesslevel has not been changed.", new RuntimeException());
//		}
//		userDAO.save(u);
//		return ResponseEntity.ok().body("Customer Hired");
//	}
//
//	public ResponseEntity<String> releaseEmployee(Key k, User u) { //*************
//		MDC.put(labelAction, "Adm Release Employee");
//		Optional<User> u2 = userDAO.findById(u.getUid());
//		if (u2.isPresent()) { 
//			if (u2.get().getAccesslevel().equals("Customer")) { //originally was if (!u2.get().getAccesslevel().equals("Customer")), might've been and error
//				return InvalidException.thrown(
//						String.format("UPDATE: User %d is not an employee to release.", u.getUid()),
//						new RuntimeException());
//			}
//		} else {
//			return InvalidException.thrown(String.format("UPDATE: Employee %d does not exist.", u.getUid()),
//					new RuntimeException());
//		}
//		if (!u.getAccesslevel().equals("Customer")) {
//			return InvalidException.thrown("UPDATE: User accesslevel is invalid.", new RuntimeException());
//		}
//		userDAO.save(u);
//		return ResponseEntity.ok().body("Employee Released");
//	}

	public List<User> displayAllUsers(Key k) { //*********************
		MDC.put(labelAction, "Adm Display Users");
		return userDAO.findAll();
	}

	public ResponseEntity<String> delCustomer(Key k, User u) { //****************************************
		MDC.put(labelAction, "Adm Delete Customer");
		if (!userDAO.existsById(u.getUid())) {
			return InvalidException.thrown(String.format("DELETE: User %d does not exist.", u.getUid()),
					new RuntimeException());
		}
		// if require by table constraints
		List<Transaction> ts = tDAO.findAllByUid(u.getUid());
		for (Transaction t : ts) {
			tuiDAO.deleteByTid(t.getTid());
		}
		tDAO.deleteByUid(u.getUid());
		boDAO.deleteByUid(u.getUid());
		cDAO.deleteByUid(u.getUid());
		userDAO.deleteById(u.getUid());
		return ResponseEntity.ok().body("Customer Deleted");
	}


	public ResponseEntity<List<Transaction>> displayUserTransactions(Key k, User u) { //********************
		MDC.put(labelAction, "Adm Display Transactions");
		if (!userDAO.existsById(u.getUid())) {
			InvalidException.thrown(String.format("SELECT: User %d does not exist.", u.getUid()),
					new RuntimeException());
			// set body to new ArrayList<>() if this method is called in another function
			return ResponseEntity.status(400).body(null);
		}
		return ResponseEntity.ok().body(tDAO.findAllByUid(u.getUid()));
	}

	public ResponseEntity<List<CartItem>> displayUserTransactionItems(Key k, Transaction t) {
		MDC.put(labelAction, "Adm Display Transaction Items");
		if (!tDAO.existsById(t.getTid())) {
			InvalidException.thrown(String.format("SELECT: Transaction %d does not exist.", t.getTid()), new RuntimeException());
			return ResponseEntity.status(400).body(null);
		}
		List<TuiProto> cips = tuiDAO.findAllByTid(t.getTid());
		List<CartItem> cis = new ArrayList<>();
		for (TuiProto tp : cips) {
			CartItem ci = buildTui(tp);
			if (ci.toString(false) != null || ci.getI().toString(false) != null) {
				cis.add(ci);
			}
		}
		return ResponseEntity.ok().body(cis);
	}

	public ResponseEntity<String> modUserTransactionItem(Key k, TuiProto tp) {
		MDC.put(labelAction, "Adm Modify Transaction Item");
		if (!tDAO.existsById(tp.getTid()) || (tp.getCid() != 0 && !coupDAO.existsById(tp.getCid()))) {
			return InvalidException.thrown(
					String.format("UPDATE: Transaction %d or Coupon %d does not exist.", tp.getTid(), tp.getCid()),
					new RuntimeException());
		}
		if (tp.getQuantity() < 1) {
			return InvalidException.thrown("UPDATE: Transaction item quantity must be greater than 0.",
					new RuntimeException());
		}
		// transaction already confirmed above
		Transaction t = tDAO.findById(tp.getTid()).get();
		List<CartItem> cis = (List<CartItem>) displayUserTransactionItems(k, t).getBody();
		if (cis != null) {
			CartItem ci = null;
			for (CartItem i : cis) {
				if (i.getI().getIid() == tp.getIid() && i.getUtid() == tp.getTid()) {
					ci = i;
				}
			}
			if (ci == null) {
				return InvalidException.thrown("SELECT: Unable to find matching item in user's transaction item list.",
						new RuntimeException());
			}

			if (ci.getI().getQuantity() < tp.getQuantity()) {
				boDAO.save(new BackorderProto(t.getUid(), tp.getIid(), tp.getQuantity(), tp.getCid()));
				log.info("Item {}:{} was put on backorder due to limited stock on hand.", ci.getI().getIid(),
						ci.getI().getUnitname());
			} else {
				if (setNewQuantities(ci.getI(),
						tp.getQuantity() - ci.getCartQuantity()/* newQuantity - oldQuantity */)) {
					tuiDAO.deleteByTidAndIid(tp.getTid(), tp.getIid());
					// TuiProto(ci.getUtid(),ci.getCartQuantity(),ci.getCid(),ci.getI().getIid()));
					tuiDAO.save(tp);
				} else {
					boDAO.save(new BackorderProto(t.getUid(), tp.getIid(), tp.getQuantity(), tp.getCid()));
					log.error("Item {}'s quantities could not be updated, item put on user {}'s backorder.",
							tp.getIid(), t.getUid());
				}
			}

			cis = (List<CartItem>) displayUserTransactionItems(k, t).getBody();
			t.setTotalcost(calculateTotal(cis));
			tDAO.save(t);
			// call third party function to reimburse/bill user for changes
//		buildTui(tp);
			return ResponseEntity.ok().body("Transaction Item Updated");
		} else {
			return InvalidException.thrown(String.format("SELECT: Transaction %d does not exist.", t.getTid()), new RuntimeException());
		}
		
	}
	
	public ResponseEntity<String> delUserTransaction(Key k, Transaction t) { //***********************
		MDC.put(labelAction, "Adm Delete Transaction");
		Optional<Transaction> t2 = tDAO.findById(t.getTid());
		if (t2.isPresent()) {
			if (k.getUid() == t2.get().getUid()) {
				return InvalidException.thrown(
						String.format("DELETE: User %d attempted to delete their own transaction.", k.getUid()),
						new RuntimeException());
			}
		} else {
			return InvalidException.thrown(String.format("DELETE: Transaction %d does not exist.", t.getTid()),
					new RuntimeException());
		}
		tuiDAO.deleteByTid(t.getTid());
		tDAO.deleteById(t.getTid());
		return ResponseEntity.ok().body("User Deleted");
	}

	public ResponseEntity<String> delUserTransactionItem(Key k, TuiProto tp) { //***************
		MDC.put(labelAction, "Adm Delete Transaction Item");
		if (!tDAO.existsById(tp.getTid())) {
			return InvalidException.thrown(
					String.format("DELETE: Transaction %d containing given item does not exist.", tp.getTid()),
					new RuntimeException());
		}
		tuiDAO.delete(tp);
		return ResponseEntity.ok().body("Transaction Item Deleted");
	}

	public List<Coupon> displayCoupons(Key k) { //*********
		MDC.put(labelAction, "Adm Display Coupons");
		return coupDAO.findAll();
	}

	public ResponseEntity<String> addCoupon(Key k, Coupon c) { //*************
		MDC.put(labelAction, "Adm Add Coupon");
		if (c.getCid() != 0) {
			return InvalidException.thrown(String.format("INSERT: Invalid coupon id %d.", c.getCid()),
					new RuntimeException());
		}
		coupDAO.save(c);
		return ResponseEntity.accepted().body("Coupon Added");
	}

	public ResponseEntity<String> modCoupon(Key k, Coupon c) { //************
		MDC.put(labelAction, "Adm Modify Coupon");
		if (!coupDAO.existsById(c.getCid())) {
			return InvalidException.thrown(String.format("UPDATE: Coupon %d does not exist.", c.getCid()),
					new RuntimeException());
		}
		coupDAO.save(c);
		return ResponseEntity.ok().body("Coupon Updated");
	}

	public ResponseEntity<String> delCoupon(Key k, Coupon c) { //********************************
		MDC.put(labelAction, "Adm Delete Coupon");
		if (!coupDAO.existsById(c.getCid())) {
			return InvalidException.thrown(String.format("UPDATE: Coupon %d does not exist.", c.getCid()),
					new RuntimeException());
		}
		if (!cDAO.findAllByCid(c.getCid()).isEmpty() || !boDAO.findAllByCid(c.getCid()).isEmpty()
				|| !tuiDAO.findAllByCid(c.getCid()).isEmpty()) {
			return InvalidException.thrown(
					String.format("DELETE: Coupon %d is present in a cart/backorder/transaction.", c.getCid()),
					new RuntimeException());
		}
		coupDAO.delete(c);
		return ResponseEntity.ok().body("Coupon Deleted");
	}

	public CartItem buildTui(TuiProto tp) { //***********************
		CartItem ci = new CartItem(); // converts to CartItem and adds item as object
		ci.setCid(tp.getCid());
		ci.setUtid(tp.getTid());
		ci.setCartQuantity(tp.getQuantity());
		Optional<Item> i = iDAO.findById(tp.getIid());
		if (i.isPresent()) {
			ci.setI(i.get());
		} else {
//			log.error("SELECT: Item {} does not exist.", cip.getIid());
//			return new CartItem();
			InvalidException.thrown(String.format("SELECT: Item %d does not exist.", tp.getIid()),
					new RuntimeException());
			return new CartItem();
		}
		return ci;
	}

	public double calculateTotal(List<CartItem> cis) { //************
		double total = 0.0;
		double coupon = 00.00;
		double tax = 00.00;
		for (CartItem cs : cis) {
			total += cs.getI().getSellingprice() * cs.getCartQuantity() * (coupon / 100 + 1) * (tax / 100 + 1);
		}
		return total;
	}

	// MUST NOT THROW ERROR, WILL DISRUPT CHECKOUT PROCESS
	// ADMIN MUST HANDLE LOGGED ISSUE VIA BACKORDER TABLE
	public boolean setNewQuantities(Item i, long amountSold) { //***********
		if (iDAO.existsById(i.getIid())) {
			i.setQuantity(i.getQuantity() - amountSold);
			i.setTotalpurchases(i.getTotalpurchases() + amountSold);
			iDAO.save(i);
			return true;
		} else {
			log.error("Unable to update item {}'s quantity", i.getIid());
			return false;
		}
	}
}
