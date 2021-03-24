package com.revature.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.revature.models.Transaction;
@Repository
public interface TransactionDAO extends JpaRepository<Transaction, Long> {
	
	//@Query(value = "SELECT * FROM projectzero.transactions WHERE uid = :uid")
	public List<Transaction> findAllByUid(long uid);

	public void deleteByUid(long uid);
	
	@Modifying
	@Query(value="INSERT INTO projectzero.transactions (uid, totalcost) values (:uid, CAST(:totalcost AS money)", nativeQuery = true)
	public Transaction save(@Param("uid") long uid, @Param("totalcost") double totalcost);

	public Object findAllByTid(long tid);
	
}
