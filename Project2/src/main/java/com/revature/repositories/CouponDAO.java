package com.revature.repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.models.Coupon;

@Repository
public interface CouponDAO extends JpaRepository<Coupon, Long> {

	ArrayList<Coupon> findAllByCid(long cid);

}
