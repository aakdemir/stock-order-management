package com.brokage.stock_order;

import com.brokage.stock_order.entity.Asset;
import com.brokage.stock_order.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class StockOrderApplication implements CommandLineRunner {

	@Autowired
	AssetService assetService;

	public static void main(String[] args) {
		SpringApplication.run(StockOrderApplication.class, args);
	}


	/**
	 * Runs before app to create static table data
	 * @param args
	 * @throws Exception
	 */
	@Override
	public void run(String... args) throws Exception {
		Asset [] assets = new Asset [] {
				new Asset(1L,1L,"TRY", new BigDecimal(100), new BigDecimal(100)),
				new Asset(2L,2L,"TRY", new BigDecimal(200), new BigDecimal(200)),
				new Asset(3L,3L,"TRY", new BigDecimal(300), new BigDecimal(300)),
				new Asset(4L,4L,"TRY", new BigDecimal(400), new BigDecimal(400))
		};

		Arrays.stream(assets).forEach(asset -> assetService.saveAsset(asset));
	}
}
