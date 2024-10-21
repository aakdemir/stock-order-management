package com.brokage.stock_order.service;

import com.brokage.stock_order.entity.Asset;

public interface AssetService {
    void saveAsset(Asset asset);
    Asset listAssetsByCustomerId(Long customerId);
}
