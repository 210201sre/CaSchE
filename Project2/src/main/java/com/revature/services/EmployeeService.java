package com.revature.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.exceptions.UserNotFoundException;
import com.revature.models.CartItem;
import com.revature.models.CartItemProto;
import com.revature.models.Key;
import com.revature.models.Transaction;
import com.revature.models.TuiProto;
import com.revature.models.User;
import com.revature.repositories.UserDAO;

@Service
public class EmployeeService /*extends CustomerService*/ {

	@Autowired
	private UserDAO userDAO;
	
	public List<User> displayInternalDirectory(Key k) {
		MDC.put("Action", "Emp Display Directory");
		List<User> dir1 = userDAO.findAllByAccesslevel("Employee").orElseThrow(() -> new UserNotFoundException("SELECT: No Employees found."));
		List<User> dir2 = userDAO.findAllByAccesslevel("Admin").orElseThrow(() -> new UserNotFoundException("SELECT: No Admins found."));
		dir1 = Stream.concat(dir1.stream(), dir2.stream()).collect(Collectors.toList());
		if (dir1.isEmpty()) {
			throw new UserNotFoundException("SELECT: Internal directory is empty.");
		}
		dir2 = new ArrayList<>();
		for (User u : dir1) {
			u.setPswrd(null);
			u.setUname(null);
			dir2.add(u);
		}
		return dir2;
	}

	// TODO
	public List<User> displayFilteredDirectory(Key k, String... params){
		return null;
	}

	// TODO
	public User resetUserLogin(Key k, User u, String uname, String pswrd) {
		// DUPLICATE CustomerService.resetUnPw();
		return null;
	}

	// TODO
	public CartItemProto modCustomerTransationItem(Key k, User u, TuiProto tp) {
		// take tid, quantity, iid from cip
		
		// u.getAccessLevel == "Customer" && k.getUid() != u.getUid()
		// check if cip exists in Transactions table
		
		// run calculateTotal(List<CartItem>) and setNewQuantities(Item, newQuantity - oldQuantity)
		// return tDAO.save(CartItem)
		return null;
	}

	// TODO
	public void delCustomerTransactionItem(Key k, User u, TuiProto tp) {
		// verify u.getUid == tDAO.findById(cip.getTid()).orElseThrow(...).getUid()
		// verify cip exists in Transaction where matching tid AND cip else thow exception
		
		return;
	}

	// TODO
	public List<Transaction> displayCustomerTransactions(Key k, User u) {
		// duplicate customerService.displayTransactions()
		
		return null;
	}

	// TODO
	public List<CartItem> dispalyCustomerTransactionItems(Key k, Transaction t) {
		// duplicate customerService.displayTransactionItems
		// to be used in conjunction with displayCustomerTransactions on front end

		return null;
	}
	
	// TODO
	public List<User> displayFilteredCustomers(Key k, String... params) {
		//implement custom DAO method
		return null;
	}
}
