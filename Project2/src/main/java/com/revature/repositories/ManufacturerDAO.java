package com.revature.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revature.models.Manufacturer;

public interface ManufacturerDAO extends JpaRepository<Manufacturer, Long> {

	public Manufacturer findById(long mid);
	
	
}
