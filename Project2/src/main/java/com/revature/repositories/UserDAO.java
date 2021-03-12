package com.revature.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.revature.models.User;

public interface UserDAO extends JpaRepository<User, Long> {
										//Second value is Id data type (wrapper class)
	
//	@Transactional // if you want this to be part of a transaction
//	@Modifying // if you intend to manipulate data with INSERT, UPDATE, 
//	@Query(value = "FROM User WHERE email LIKE %:substr%")
//	public List<User> FindByEmailContains(String substr); 
	
	//@Query(value = "FROM users WHERE sid = :sid")
//	public Optional<User> findBySid(long sid);
	
	public Optional<User> findByUname(String uname);
	
	public Optional<User> findBySid(long sid);
	
	public Optional<List<User>> findAllByAccesslevel(String accesslevel);
	
	public Optional<List<User>> findAllByUname(String uname);
	
//	@Modifying
//	@Query(value="UPDATE projectzero.users SET fname=:fname, lname=:lname, email=:email, phonenum=:phonenum, address=:address, city=:city, state=:state, zip=:zip WHERE uid = :uid", nativeQuery = true)
//	@Transactional
//	public int update(@Param("uid") long uid, @Param("fname") String fname,
//			@Param("lname") String lname, @Param("email") String email, 
//			@Param("phonenum") String phonenum, @Param("address") String address, 
//			@Param("city") String city, @Param("state") String state, @Param("zip") String zip);
}
