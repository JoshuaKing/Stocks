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

	@Override
	public void run(Stock stock, float moneyInvested, float fees) {
		String currentYear = "";
		Exchange exchange = new Exchange();
		
		while(stock.next()) {
			PricePoint today = stock.today();
			
			// Get year
			String year = today.date.substring(6);
			
			if (!year.equals(currentYear)) {
				exchange.sell(stock.yesterday().open, fees, currentYear);
				currentYear = year;
				int numShares = (int) Math.floor(moneyInvested/today.open);
				exchange.buy(numShares, today.open, fees, year);
			}
		}
		if (exchange.hasShares()) {
			exchange.sell(stock.last().close, fees, currentYear);
		}
		stock.reset();
		exchange.summarise("Algorithm Hold");
	}

}
