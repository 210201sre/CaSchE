package com.revature.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revature.models.Coupon;

public interface CouponDAO extends JpaRepository<Coupon, Long> {

}
