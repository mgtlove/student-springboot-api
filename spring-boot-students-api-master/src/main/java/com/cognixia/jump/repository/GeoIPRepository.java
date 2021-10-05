package com.cognixia.jump.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cognixia.jump.model.GeoIP;

public interface GeoIPRepository extends JpaRepository<GeoIP, Long> {

}
