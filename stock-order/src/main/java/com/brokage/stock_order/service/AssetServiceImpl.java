package com.brokage.stock_order.service;

import com.brokage.stock_order.entity.Asset;
import com.brokage.stock_order.repository.AssetRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AssetServiceImpl implements AssetService{
    @Autowired
    AssetRepository assetRepository;
    /**
     * save asset 1 by 1
     */
    @Override
    public void saveAsset(Asset asset) {

        assetRepository.save(asset);
    }

    /**
     * @param customerId  // getting according to customerId with TRY asset.
     *                    TRY isn't a parameter because there is only TRY assets in the table.s
     * @return
     */
    @Override
    public Asset listAssetsByCustomerId(Long customerId) {
        Optional<Asset> optionalAsset = assetRepository.findByCustomerId(customerId);
        if(optionalAsset.isPresent()) {
            return optionalAsset.get();
        } else {
            throw new EntityNotFoundException(("Asset not found with customerId: " + customerId));
        }
    }
}
