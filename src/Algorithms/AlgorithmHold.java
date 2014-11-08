package Algorithms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import Common.Exchange;
import Common.Log;
import Common.PricePoint;
import Common.Stock;

public class AlgorithmHold implements Algorithm {
	float spent = 0;

	@Override
	public Exchange run(Stock stock, float moneyInvested, float fees, int since) {
		String currentYear = "";
		Exchange exchange = new Exchange();
		
		while(stock.next()) {
			PricePoint today = stock.today();
			
			// Get year
			String year = today.getYear();
			
			if (Integer.valueOf(today.getYear()) < since) continue;
			if (!year.equals(currentYear)) {
				spent += exchange.sell(stock.yesterday().open, fees, stock.yesterday());
				currentYear = year;
				int numShares = (int) Math.floor(moneyInvested/today.open);
				spent += exchange.buy(numShares, today.open, fees, today);
			}
		}
		if (exchange.hasShares()) {
			spent += exchange.sell(stock.last().close, fees, stock.last());
		}
		stock.reset();
		//exchange.summarise("Algorithm Hold");
		return exchange;
	}

	@Override
	public float spent() {
		return spent;
	}

	@Override
	public String name() {
		return "Algorithm Hold";
	}

}
