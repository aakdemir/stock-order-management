package com.brokage.stock_order.repository;

import com.brokage.stock_order.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    Optional<Asset> findByCustomerId(Long customerId);
}
