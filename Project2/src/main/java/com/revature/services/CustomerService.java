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

	public User login(String username, String password) {
		MDC.put("Action", "Login");
		HttpSession s = req.getSession(false);
		if (s != null && s.getAttribute(key) != null) {
			Key k = (Key) s.getAttribute(key);
			if (userDAO.existsById(k.getUid())) {
				throw new InvalidException(String.format("LOGIN: User %d already logged in on this device.", k.getUid()));
			}
		}
		User u = userDAO.findByUname(username).orElseThrow(() -> new UserNotFoundException(
				String.format(String.format("LOGIN: No User with username:%s password:%s", username, password))));
		if (u.getPswrd().equals(password)) {
			HttpSession session = req.getSession();
			Random r = new Random();
			long sid = r.nextLong();
			List<Long> limit = new ArrayList<>();
			while (userDAO.findBySid(sid).isPresent() && limit.size() < Long.MAX_VALUE - 1) {
				if (!limit.contains(sid)) {
					limit.add(sid);
				}
				sid = r.nextLong();
			}
			if ((limit.size() >= Long.MAX_VALUE - 1)) {
				throw new InvalidException("LOGIN: DATABASE LIMIT: Unable to generate a secure connection.");
			}
			u.setSid(sid);
			Key k = new Key(u.getSid(), u.getUid());
			session.setAttribute(key, k);
			userDAO.save(u);

			return u;
		} else {
			throw new UserNotFoundException(String.format("No User with username:%s password:%s", username, password));
		}
	}

	public void logout(Key k) {
		MDC.put("Action", "Logout");
		HttpSession session = req.getSession(false);
		if (session == null) {
			throw new InvalidException("User not logged in.");
		} else if (!userDAO.findBySid(((Key) session.getAttribute(key)).getSid()).isPresent()) {
			session.invalidate();
			throw new InvalidException("Invalid session.");
		}
		User u = userDAO.findById(k.getUid()).get();
		u.setSid(null);
		userDAO.save(u);
		session.invalidate();
		
	}

	public User addUsr(User u) {
		MDC.put("Action", "New User");
		if (userDAO.findByUname(u.getUname()).isPresent()) {
			throw new InvalidException(String.format("INSERT: Username %s already exists.", u.getUname()));
		}		
		if (u.getUid() != null || u.getSid() != null) {
			throw new InvalidException(
					String.format("INSERT: Invalid ID(s) (%d:%d) passed during insertion.", u.getUid(), u.getSid()));
		}
		if (!u.getAccesslevel().equals("Customer")) {
			throw new InvalidException(String.format("INSERT: Invalid access. User: %s",u.toString()));
		}
		return userDAO.save(u);
	}

	// Handle session verification in Controller if possible, and pass to methods.
	// This will save on lines of duplicate code. Otherwise create a seperate method
	// in this service file.
	public User getMyInfo(Key k) {
		MDC.put("Action", "User Info");
		return userDAO.findById(k.getUid()).orElseThrow(
				() -> new UserNotFoundException(String.format("SELECT: User %d no longer exists.", k.getUid())));
		// technically, if validation passed, then user is guaranteed to exist. Unless
		// deleted in between validation and info gathering
	}

//	public int modUser(User u, Key k) {
	public User modUser(User u, Key k) {
		MDC.put("Action", "Modify User");
		if (u.getUid() == k.getUid()) {
			//TODO
			 User u2 = userDAO.findById(u.getUid()).get();
			 System.out.println(u);
			 if (u.getFname() != u2.getFname() && u.getFname() != "") {
				 u2.setFname(u.getFname());
			 }
			 if (u.getLname() != u2.getLname() && u.getLname() != "") {
				 u2.setLname(u.getLname());
			 }
			 if (u.getEmail() != u2.getEmail() && u.getEmail() != null) {
				 u2.setEmail(u.getEmail());
			 }
			 if (u.getPhonenum() != u2.getPhonenum() && u.getPhonenum() != "") {
				 u2.setPhonenum(u.getPhonenum());
			 }
			 if (u.getAddress() != u2.getAddress() && u.getAddress() != "") {
				 u2.setAddress(u.getAddress());
			 }
			 if (u.getCity() != u2.getCity() && u.getCity() != null) {
				 u2.setCity(u.getCity());
			 }
			 if (u.getState() != u2.getState() && u.getState() != null) {
				 u2.setState(u.getState());
			 }
			 if (u.getZip() != u2.getZip() && u.getZip() != null) {
				 u2.setZip(u.getZip());
			 }
			 
			//    if field != u2.field && field != null/ 0?
			//        u2.field = u.field //using userDAO.save
			//    else
			//        u1.field = u2.field //using userDAO.update
			
			// return userDAO.save(u2);
			// return userDAO.update(u1.get..., ...);
//			return userDAO.update(k.getUid(), u.getFname(), u.getLname(), u.getEmail(), u.getPhonenum(), u.getAddress(), u.getCity(), u.getState(), u.getZip());
			return userDAO.save(u2);
		} else {
			throw new InvalidException(
					String.format("UPDATE: Invalid User (%d!%d) modification.", u.getUid(), k.getUid()));
		}
	}

	public User resetUnPw(String uname, String pswrd, Key k){
		MDC.put("Action", "Reset Username Password");
		userDAO.findByUname(uname).orElseThrow(
				() -> new UserNotFoundException(String.format("SELECT: username: %s is taken exists.", uname)));
		User u = userDAO.findBySid(k.getSid()).orElseThrow(
				() -> new UserNotFoundException(String.format("SELECT: User %d no longer exists.", k.getUid())));
		u.setUname(uname);
		u.setPswrd(pswrd);
		return userDAO.save(u);
	}
	
	// Handle userDAO.existsById(k.getUid()) in controller if possible
	public boolean delUser(Key k) {
		MDC.put("Action", "Delete User");
		// if (u.getUid() == k.getUid() && userDAO.existsById(k.getUid())) {
//		if (u.getUid() == k.getUid()) {
			userDAO.deleteById(k.getUid());
			return true;
//		} else {
//			log.error("DELETE: User {} does not exist.", k.getUid());
//			return false;
//			throw new InvalidException(String.format("DELETE: User %d does not exist.", k.getUid()));
//		}
	}

	// CART SERVICE METHODS
	public CartItem addToMyCart(CartItemProto cip, Key k) {
		MDC.put("Action", "Add to Cart");
		cip.setUid(k.getUid());
		if (cip.getUid() < 1 || cip.getCid() < 0 || k.getSid() == null) {
			throw new InvalidException(String.format("UPDATE: Invalid ID(s) (%d:%d:%d) passed during cart update.",
					cip.getUid(), cip.getCid(), k.getSid()));
		} else {
			if (cip.getUid() == k.getUid()) {
				if (iDAO.existsById(cip.getIid())) {
					return buildCartItem(cDAO.save(cip));
				} else {
//					log.error("SELECT: Item {} does not exist.", cip.getIid());
//					return new CartItem();
					throw new InvalidException(String.format("SELECT: Item %d does not exist.", cip.getIid()));
				}
			} else {
//				log.error("INSERT: User {} mismatch {}", cip.getUid(), k.getUid());
//				return new CartItem();
				throw new InvalidException(String.format("INSERT: User %d mismatch %d", cip.getUid(), k.getUid()));
			}
		}
	}

	public CartItem modMyCart(CartItemProto cip, Key k) {
		MDC.put("Action", "Modify Cart Item");
		if (cip.getUid() < 1 || (!coupDAO.existsById(cip.getCid()) && cip.getCid()!= 0 ) || k.getSid() == null) {
			throw new InvalidException(String.format("UPDATE: Invalid ID(s) (%d:%d:%d) passed during cart update.",
					cip.getUid(), cip.getCid(), k.getSid()));
		} else {
			if (cip.getUid() == k.getUid()) {
				if (cDAO.findByUidAndIid(cip.getUid(), cip.getIid()).isPresent()) {
					return buildCartItem(cDAO.save(cip));
				} else {
//					log.error("SELECT: Item {} does not exist.", cip.getIid());
//					return new CartItem();
					throw new InvalidException(String.format("SELECT: Item %d does not exist in cart.", cip.getIid()));
				}
			} else {
//				log.error("UPDATE: User {} mismatch {}", cip.getUid(), k.getUid());
//				return new CartItem();
				throw new InvalidException(String.format("UPDATE: User %d mismatch %d", cip.getUid(), k.getUid()));
			}
		}
	}

	public boolean delMyCartItem(CartItemProto cip, Key k) {
		MDC.put("Action", "Delete Cart Item");
		if (cip.getUid() == k.getUid()) {
			cDAO.delete(cip);
			return true;
		} else {
			throw new InvalidException(String.format("UPDATE: User %d mismatch %d", cip.getUid(), k.getUid()));
		}
	}

	public boolean emptyCart(Key k) {
		MDC.put("Action", "Empty Cart");
		if (cDAO.countByUid(k.getUid()) > 0) {
			//log.info("count was {}",cDAO.countByUid(k.getUid()));
			List<CartItemProto> cps = cDAO.findAllByUid(k.getUid());
			for (CartItemProto p : cps) {
				cDAO.delete(p);
			}
			//cDAO.deleteByUid(k.getUid());
			return true;
		} else {
			throw new InvalidException(String.format("DELETE: User %d cart is empty.", k.getUid()));
		}
	}

	public List<CartItem> displayCart(Key k) {
		MDC.put("Action", "Display Cart");
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

	public boolean checkout(Key k) {
		String start=""; if (MDC.get("Start")!=null){ start = MDC.get("Start");}
		
		
		// the below line may not work properly
		//if (cDAO.countByUid(k.getUid()) > 0) {
		MDC.put("TopAction", "Checkout");
		if(cDAO.findAllByUid(k.getUid()).size() > 0) {
			List<CartItem> cis = displayCart(k);
			MDC.put("TopAction", "Checkout"); if(!start.equals("")){MDC.put("Start",start);}
			Transaction t = new Transaction();
			t.setUid(k.getUid());
			t.setTotalcost(0.01);
			//t = tDAO.save(t.getUid(), t.getTotalcost());
			t = tDAO.save(t);
			log.info("Transaction id {}", t);
			if (t.getTid() > 0) {
				for (CartItem ci : cis) {
					if (ci.getI().getQuantity() < ci.getCartQuantity()) {
						boDAO.save(new BackorderProto(k.getUid(), ci.getI().getIid(), ci.getCartQuantity(), ci.getCid()));
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
				MDC.put("TopAction", "Checkout"); if(!start.equals("")){MDC.put("Start",start);}
				return true;
			} else {
				throw new InvalidException(
						String.format("CHECKOUT: Failed to get transaction ID for user %d.", k.getUid()));
			}
		} else {
			throw new InvalidException(String.format("CHECKOUT: User %d cart is empty.", k.getUid()));
		}
	}

	// TRANSACTION SERVICE METHODS
	public List<Transaction> displayTransactions(Key k) {
		MDC.put("Action", "Display Transactions");
		return tDAO.findAllByUid(k.getUid());
	}

	public List<CartItem> displayTransactionItems(Transaction t, Key k) {
		MDC.put("Action", "Display Transaction Items");
		if (t.getTid() > 0 && k.getUid() == t.getUid()) {
			throw new InvalidException(String.format("SELECT: User %d does not match requested Transaction %d", k.getUid(),t.getUid()));
		}
		List<TuiProto> cips = tuiDAO.findAllByTid(t.getTid());
		List<CartItem> cis = new ArrayList<>();
		for (TuiProto tp : cips) {
			CartItem ci = buildTui(tp);
			if (ci.toString(false) != null || ci.getI().toString(false) != null) {
				cis.add(ci);
			}
		}
		return cis;
	}

	public List<BackorderProto> displayBackorders(Key k) {
		MDC.put("Action", "Display Backorders");
		return boDAO.findAllByUid(k.getUid());
	}
	// STAND ALONE METHODS
	protected CartItem buildCartItem(CartItemProto cip) {
		CartItem ci = new CartItem();
		ci.setCid(cip.getCid());
		ci.setUtid(cip.getUid());
		ci.setCartQuantity(cip.getQuantity());
		Optional<Item> i = iDAO.findById(cip.getIid());
		if (i.isPresent()) {
			ci.setI(i.get());
		} else {
//			log.error("SELECT: Item {} does not exist.", cip.getIid());
//			return new CartItem();
			throw new InvalidException(String.format("SELECT: Item %d does not exist.", cip.getIid()));
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
//			log.error("SELECT: Item {} does not exist.", cip.getIid());
//			return new CartItem();
			throw new InvalidException(String.format("SELECT: Item %d does not exist.", tp.getIid()));
		}
		return ci;
	}

	private double calculateTotal(List<CartItem> cis) {
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
	private boolean setNewQuantities(Item i, long amountSold) {
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
