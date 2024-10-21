package com.brokage.stock_order;

import com.brokage.stock_order.entity.Asset;
import com.brokage.stock_order.repository.AssetRepository;
import com.brokage.stock_order.service.AssetServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class AssetServiceTest {
    @InjectMocks
    AssetServiceImpl assetServiceImpl;

    @Mock
    AssetRepository assetRepository;

    Asset asset;

    @BeforeEach
    public void setUp () {
        MockitoAnnotations.openMocks(this);
        asset = new Asset(1L, 1L, "TRY", BigDecimal.valueOf(100), BigDecimal.valueOf(100));
    }

    @Test
    public void testListAssetsByCustomerId_EmptyList(){
        Long customerId = 1L;
        when(assetRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());


        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> assetServiceImpl.listAssetsByCustomerId(customerId));

        assertEquals("Asset not found with customerId: 1", exception.getMessage());

    }

    @Test
    public void testListAssetsByCustomerId_FullList(){
        Long customerId = 1L;
        when(assetRepository.findByCustomerId(customerId)).thenReturn(Optional.of(asset));
        Asset serviceAsset = assetServiceImpl.listAssetsByCustomerId(customerId);
        assertNotNull(serviceAsset);
        assertEquals(BigDecimal.valueOf(100), serviceAsset.getUsableSize());
    }
}
