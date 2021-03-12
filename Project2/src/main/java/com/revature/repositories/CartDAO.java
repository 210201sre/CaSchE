package com.revature.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revature.models.CartItemProto;

public interface CartDAO extends JpaRepository<CartItemProto, Long>  {

	
	//@Query(value = "SELECT * FROM projectzero.cart WHERE uid = :uid")
	public List<CartItemProto> findAllByUid(long uid);
	
	//@Modifying
	//@Query(value = "DELETE FROM projectzero.cart WHERE uid = :uid")
	public void deleteByUid(long uid);
	
	// TODO this method may not return properly
	public Optional<CartItemProto> findByUidAndIid(long uid, long iid);
	
	//@Query(value = "SELECT COUNT(iid) FROM projectzero.cart WHERE uid = :uid")
	public long countByUid(/*@Param("cid")*/ long uid);

	// find out what the query is that returns a boolean
	public boolean existsByIid(long iid);
	
	//@Query(value = "SELECT * FROM projectzero.cart WHERE cid = :cid")
	public List<CartItemProto> findAllByCid(long cid);
	
}
