package com.revature.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.revature.exceptions.InvalidException;
import com.revature.exceptions.UserNotFoundException;
import com.revature.models.BackorderProto;
import com.revature.models.CartItem;
import com.revature.models.CartItemProto;
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

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@Service
public class CustomerService {

	private static final Logger log = LoggerFactory.getLogger(CustomerService.class);
	
	@Autowired
	private HttpServletRequest req;

	@Autowired
	private UserDAO userDAO;
	@Autowired
	private CartDAO cDAO;
	@Autowired
	private ItemDAO iDAO;
	@Autowired
	private TransactionDAO tDAO;
	@Autowired
	private BackorderDAO boDAO;
	@Autowired
	private TuiDAO tuiDAO;
	@Autowired
	private CouponDAO coupDAO;

	private String key = "projectzero";
	private String labelAction = "Action";
	private String labelTopAction = "TopAction";
	private String labelCheckout = "Checkout";
	private String labelStart = "Start";
	private String noUser = "User does not exist.";
	private String noItem = "SELECT: Item %d does not exist.";
	private Random r = new Random();

	//// EVERY DAO METHOD CALL MUST BE WITHIN A TRY - CATCH (PSQLException e) BLOCK ////
	
	private Counter successLoginCounter;
	private Counter failLoginCounter;
	
	private Counter backorderCheckoutCounter;
	private Counter completedCheckoutCounter;
	
	private MeterRegistry meterRegistry;
	
	//@Autowired
	public CustomerService() {
		super();
	}

	@Autowired
	public CustomerService(MeterRegistry meterRegistry) {
		this.meterRegistry = meterRegistry;
		initLoginCounters();
		initCheckoutCounters();
	}
	
	private void initLoginCounters() {
		successLoginCounter = Counter.builder("login.sucesses").tag("type", "success")
				.description("The number of successful login attempts").register(meterRegistry);
		failLoginCounter = Counter.builder("login.failures").tag("type", "fail")
				.description("The number of failed login attempts").register(meterRegistry);
	}
	
	private void initCheckoutCounters() {
		backorderCheckoutCounter = Counter.builder("checkout.backorder").tag("type", "backorder")
				.description("The number of items sent to backorder").register(meterRegistry);
		completedCheckoutCounter = Counter.builder("checkout.complete").tag("type", "complete")
				.description("The number of completed checkouts").register(meterRegistry);
	}
	
	public ResponseEntity<String> login(String username, String password) {
		MDC.put(labelAction, "Login");
		HttpSession s = req.getSession(false);
		if (s != null && s.getAttribute(key) != null) {
			Key k = (Key) s.getAttribute(key);
			if (userDAO.existsById(k.getUid())) {
				return InvalidException.thrown(String.format("LOGIN: User %d already logged in on this device.", k.getUid()), new RuntimeException());
			}
		}
		Optional<User> u2 = userDAO.findByUname(username);
		if (!u2.isPresent()) {
			failLoginCounter.increment();
			return InvalidException.thrown(String.format("LOGIN: No User with username:%s password:%s", username, password), new UserNotFoundException());
		}
		User u = u2.get();
		if (u.getPswrd().equals(password)) {
			HttpSession session = req.getSession();
			long sid = r.nextLong();
			List<Long> limit = new ArrayList<>();
			while (userDAO.findBySid(sid).isPresent() && limit.size() < Long.MAX_VALUE - 1) {
				if (!limit.contains(sid)) {
					limit.add(sid);
				}
				sid = r.nextLong();
			}
			if ((limit.size() >= Long.MAX_VALUE - 1)) {
				return InvalidException.thrown("LOGIN: DATABASE LIMIT: Unable to generate a secure connection.", new RuntimeException());
			}
			u.setSid(sid);
			Key k = new Key(u.getSid(), u.getUid());
			session.setAttribute(key, k);
			userDAO.save(u);

			successLoginCounter.increment();
			return ResponseEntity.ok().body("Logged In");
		} else {
			failLoginCounter.increment();
			return InvalidException.thrown(String.format("No User with username:%s password:%s", username, password), new RuntimeException());
		}
	}

	public ResponseEntity<String> logout(Key k) {
		MDC.put(labelAction, "Logout");
		HttpSession session = req.getSession(false);
		if (session == null) {
			return InvalidException.thrown("User not logged in.", new RuntimeException());
		} else if (!userDAO.findBySid(((Key) session.getAttribute(key)).getSid()).isPresent()) {
			session.invalidate();
			return InvalidException.thrown("Invalid session.", new RuntimeException());
		} else if (!userDAO.findById(k.getUid()).isPresent()) {
			session.invalidate();
			return InvalidException.thrown(noUser, new RuntimeException());
		}
		User u = userDAO.findById(k.getUid()).get();
		u.setSid(null);
		userDAO.save(u);
		session.invalidate();
		return ResponseEntity.ok().body("Logged Out");
	}

	public ResponseEntity<String> addUsr(User u) { //************************************
		MDC.put(labelAction, "New User");
		if (userDAO.findByUname(u.getUname()).isPresent()) {
			return InvalidException.thrown(String.format("INSERT: Username %s already exists.", u.getUname()), new RuntimeException());
		}		
		if (u.getUid() != null || u.getSid() != null) {
			return InvalidException.thrown(String.format("INSERT: Invalid ID(s) (%d:%d) passed during insertion.", u.getUid(), u.getSid()), new RuntimeException());
		}
		if (!u.getAccesslevel().equals("Customer")) {
			return InvalidException.thrown(String.format("INSERT: Invalid access. User: %s",u.toString()), new RuntimeException());
		}
		userDAO.save(u);
		return ResponseEntity.accepted().body("User Account Created");
	}

	// Handle session verification in Controller if possible, and pass to methods.
	// This will save on lines of duplicate code. Otherwise create a separate method
	// in this service file.
	public ResponseEntity<User> getMyInfo(Key k) { //*****************************
		MDC.put(labelAction, "User Info");
		Optional<User> u2 = userDAO.findById(k.getUid());
		if(u2.isPresent()) {
			return ResponseEntity.ok().body(u2.get());
		}
		else {
			Optional.empty();
			InvalidException.thrown(noUser, new RuntimeException());
			return ResponseEntity.status(400).body(null);
		}
//		return userDAO.findById(k.getUid()).orElseThrow(
//				() -> new UserNotFoundException(String.format("SELECT: User %d no longer exists.", k.getUid())));
		// technically, if validation passed, then user is guaranteed to exist. Unless
		// deleted in between validation and info gathering
	}

	public ResponseEntity<String> modUser(User u, Key k) {
		MDC.put(labelAction, "Modify User");
		if (u.getUid() == k.getUid()) {
			//Recommended that user's current data is pulled and
			//  if inputed data is new and not empty
			//    new data replaces pulled data.
			//User u2 = userDAO.findById(u.getUid()).get();
			//userDAO.save(u2);
			userDAO.save(u);
			return ResponseEntity.ok().body("User Updated");
		} else {
			return InvalidException.thrown(String.format("UPDATE: Invalid User (%d!%d) modification.", u.getUid(), k.getUid()), new RuntimeException());
		}
	}

	public ResponseEntity<String> resetUnPw(String uname, String pswrd, Key k){ //*************************************
		MDC.put(labelAction, "Reset Username Password");
		if(!userDAO.findByUname(uname).isPresent()) {			
			return InvalidException.thrown(String.format("SELECT: username: %s is taken.", uname), new RuntimeException());
		}
		Optional<User> u2 = userDAO.findBySid(k.getSid());
		if(!u2.isPresent()) {
			return InvalidException.thrown(String.format("SELECT: User %d no longer exists.", k.getUid()), new RuntimeException());
		}
		User u = u2.get();
		u.setUname(uname);
		u.setPswrd(pswrd);
		userDAO.save(u);
		return ResponseEntity.ok().body("User credentials successfully changed.");
	}
	
	// Handle userDAO.existsById(k.getUid()) in controller if possible
	public ResponseEntity<String> delUser(Key k) { //******************
		MDC.put(labelAction, "Delete User");
		Optional<User> u2 = userDAO.findById(k.getUid());
		if (!u2.isPresent()) {
			return InvalidException.thrown(noUser, new RuntimeException());
		}
		User u = u2.get();
		if (u.getSid() != k.getSid()) {
			return InvalidException.thrown("Invalid Session", new RuntimeException());
		}
		userDAO.deleteById(u.getUid());
		return ResponseEntity.ok().body("User account has been deleted.");

	}

	// CART SERVICE METHODS
	public ResponseEntity<String> addToMyCart(CartItemProto cip, Key k) { //*************************
		MDC.put(labelAction, "Add to Cart");
		cip.setUid(k.getUid());
		if (cip.getUid() < 1 || cip.getCid() < 0 || k.getSid() == null) {
			return InvalidException.thrown(String.format("UPDATE: Invalid ID(s) (%d:%d:%d) passed during cart update.",
					cip.getUid(), cip.getCid(), k.getSid()), new RuntimeException());
		} else {
			if (cip.getUid() == k.getUid()) {
				if (iDAO.existsById(cip.getIid())) {
					cDAO.save(cip);
					return ResponseEntity.accepted().body("Item added to cart.");
				} else {
//					return new CartItem();
					return InvalidException.thrown(String.format(noItem, cip.getIid()), new RuntimeException());
				}
			} else {
//				return new CartItem();
				return InvalidException.thrown(String.format("INSERT: User %d mismatch %d", cip.getUid(), k.getUid()), new RuntimeException());
			}
		}
	}

	public ResponseEntity<String> modMyCart(CartItemProto cip, Key k) {
		MDC.put(labelAction, "Modify Cart Item");
		if (cip.getUid() < 1 || (!coupDAO.existsById(cip.getCid()) && cip.getCid()!= 0 ) || k.getSid() == null) {
			return InvalidException.thrown(String.format("UPDATE: Invalid ID(s) (%d:%d:%d) passed during cart update.",
					cip.getUid(), cip.getCid(), k.getSid()), new RuntimeException());
		} else {
			if (cip.getUid() == k.getUid()) {
				if (cDAO.findByUidAndIid(cip.getUid(), cip.getIid()).isPresent()) {
					cDAO.save(cip);
					return ResponseEntity.ok().body("Cart item modified");
				} else {
//					return new CartItem();
					return InvalidException.thrown(String.format("SELECT: Item %d does not exist in cart.", cip.getIid()), new RuntimeException());
				}
			} else {
//				return new CartItem();
				return InvalidException.thrown(String.format("UPDATE: User %d mismatch %d", cip.getUid(), k.getUid()), new RuntimeException());
			}
		}
	}

	public ResponseEntity<String> delMyCartItem(CartItemProto cip, Key k) { //**********************
		MDC.put(labelAction, "Delete Cart Item");
		if (cip.getUid() == k.getUid()) {
			cDAO.delete(cip);
			return ResponseEntity.ok().body("Cart item deleted.");
		} else {
			return InvalidException.thrown(String.format("UPDATE: User %d mismatch %d", cip.getUid(), k.getUid()), new RuntimeException());
		}
	}

	public ResponseEntity<String> emptyCart(Key k) { //*****************
		MDC.put(labelAction, "Empty Cart");
		if (cDAO.countByUid(k.getUid()) > 0) {
			List<CartItemProto> cps = cDAO.findAllByUid(k.getUid());
			for (CartItemProto p : cps) {
				cDAO.delete(p);
			}
			return ResponseEntity.ok().body("Cart has been emptied.");
		} else {
			return InvalidException.thrown(String.format("DELETE: User %d cart is empty.", k.getUid()), new RuntimeException());
		}
	}

	public List<CartItem> displayCart(Key k) {
		MDC.put(labelAction, "Display Cart");
		List<CartItemProto> cips = cDAO.findAllByUid(k.getUid());
		List<CartItem> cis = new ArrayList<>();
		for (CartItemProto cip : cips) {
			CartItem ci = buildCartItem(cip);
			if (ci.toString(false) != null || ci.getI().toString(false) != null) {
				cis.add(ci);
			}
		}
		return cis;
	}

	public ResponseEntity<String> checkout(Key k) {
		String start=""; if (MDC.get(labelStart)!=null){ start = MDC.get(labelStart);}
		
		MDC.put(labelTopAction, labelCheckout);
		if(cDAO.findAllByUid(k.getUid()).size() > 0) {
			List<CartItem> cis = displayCart(k);
			MDC.put(labelTopAction, labelCheckout); if(!start.equals("")){MDC.put(labelStart,start);}
			Transaction t = new Transaction();
			t.setUid(k.getUid());
			t.setTotalcost(0.01);
			t = tDAO.save(t);
			log.info("Transaction id {}", t);
			if (t.getTid() > 0) {
				for (CartItem ci : cis) {
					if (ci.getI().getQuantity() < ci.getCartQuantity()) {
						boDAO.save(new BackorderProto(k.getUid(), ci.getI().getIid(), ci.getCartQuantity(), ci.getCid()));
						backorderCheckoutCounter.increment();
						log.info("Item {}:{} was put on backorder due to limited stock on hand.", ci.getI().getIid(),
								ci.getI().getUnitname());
					} else {
						if (setNewQuantities(ci.getI(), ci.getCartQuantity())) {
							tuiDAO.save(new TuiProto(t.getTid(), ci.getI().getIid(), ci.getCartQuantity(), ci.getCid()));
						} else {
							boDAO.save(new BackorderProto(k.getUid(), ci.getI().getIid(), ci.getCartQuantity(), ci.getCid()));
							log.error("Item {}'s quantities could not be updated, item put on user {}'s backorder.",
									ci.getI().getIid(), k.getUid());
						}
					}
				}
				if(!tuiDAO.findAllByTid(t.getTid()).isEmpty()) {
					t.setTotalcost(calculateTotal(cis));
					tDAO.save(t);
				} else {
					tDAO.delete(t);
				}
				emptyCart(k);
				MDC.put(labelTopAction, labelCheckout); if(!start.equals("")){MDC.put(labelStart,start);}
				completedCheckoutCounter.increment();
				return ResponseEntity.ok().body("Checkout Complete");
			} else {
				return InvalidException.thrown(String.format("CHECKOUT: Failed to get transaction ID for user %d.", k.getUid()), new RuntimeException());
			}
		} else {
			return InvalidException.thrown(String.format("CHECKOUT: User %d cart is empty.", k.getUid()), new RuntimeException());
		}
	}

	// TRANSACTION SERVICE METHODS
	public List<Transaction> displayTransactions(Key k) { //*******************
		MDC.put(labelAction, "Display Transactions");
		return tDAO.findAllByUid(k.getUid());
	}

	public ResponseEntity<List<CartItem>> displayTransactionItems(Transaction t, Key k) {
		MDC.put(labelAction, "Display Transaction Items");
		if (t.getTid() > 0 && k.getUid() == t.getUid()) {
			InvalidException.thrown(String.format("SELECT: User %d does not match requested Transaction %d", k.getUid(),t.getUid()), new RuntimeException());
			//send body as new ArrayList<>() if this method gets called in another function
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

	public List<BackorderProto> displayBackorders(Key k) { //******************
		MDC.put(labelAction, "Display Backorders");
		return boDAO.findAllByUid(k.getUid());
	}
	// STAND ALONE METHODS
	public CartItem buildCartItem(CartItemProto cip) { //************
		CartItem ci = new CartItem();
		ci.setCid(cip.getCid());
		ci.setUtid(cip.getUid());
		ci.setCartQuantity(cip.getQuantity());
		Optional<Item> i = iDAO.findById(cip.getIid());
		if (i.isPresent()) {
			ci.setI(i.get());
		} else {
//			return new CartItem();
			InvalidException.thrown(String.format(noItem, cip.getIid()), new RuntimeException());
			return new CartItem();
		}
		return ci;
	}
	
	private CartItem buildTui(TuiProto tp) {//converts to CartItem and adds item as object
		CartItem ci = new CartItem();
		ci.setCartQuantity(tp.getCid());
		ci.setUtid(tp.getTid());
		ci.setCartQuantity(tp.getQuantity());
		Optional<Item> i = iDAO.findById(tp.getIid());
		if (i.isPresent()) {
			ci.setI(i.get());
		} else {
			InvalidException.thrown(String.format(noItem, tp.getIid()), new RuntimeException());
			return new CartItem();
		}
		return ci;
	}

	private double calculateTotal(List<CartItem> cis) { //**********
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
	public boolean setNewQuantities(Item i, long amountSold) { //*****************
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
