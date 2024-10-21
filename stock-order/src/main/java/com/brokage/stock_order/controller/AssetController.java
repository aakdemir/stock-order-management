package com.brokage.stock_order.controller;

import com.brokage.stock_order.entity.Asset;
import com.brokage.stock_order.service.AssetService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assets")
public class AssetController {

    @Autowired
    AssetService assetService;

    @GetMapping("/listAssetsByCustomerId/{customerId}")
    public ResponseEntity<Asset> listAssetByCustomerId(@Valid @PathVariable @NotNull(message = "customerId cant be null") Long customerId) {
        return new ResponseEntity<>(assetService.listAssetsByCustomerId(customerId), HttpStatus.OK);
    }
}
