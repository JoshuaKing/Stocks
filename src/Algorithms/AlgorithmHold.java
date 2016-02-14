package Algorithms;

import Common.Exchange;
import Common.PricePoint;
import Common.Stock;

public class AlgorithmHold implements Algorithm {
	private static final double PERCENTAGE_INVESTED = 1.0;
	float revenue = 0;

	@Override
	public Exchange run(Stock stock, float moneyInvested, float fees, int from, int to) {
		String currentYear = "";
		Exchange exchange = new Exchange(moneyInvested);
		revenue = moneyInvested;
		
		while(stock.next()) {
			PricePoint today = stock.today();
			
			// Get year
			String year = today.getYear();
			
			if (Integer.valueOf(today.getYear()) < from || Integer.valueOf(today.getYear()) > to) continue;
			if (!year.equals(currentYear)) {
				//spent += exchange.sell(stock.yesterday().open, fees, stock.yesterday());
				currentYear = year;
				int numShares = (int) Math.floor((revenue * PERCENTAGE_INVESTED)/today.open);
				if (!exchange.hasShares()) revenue -= exchange.buy(numShares, today.open, fees, today);
			}
		}
		if (exchange.hasShares()) {
			revenue += exchange.sell(stock.last().close, fees, stock.last());
		}
		stock.reset();
		return exchange;
	}

	@Override
	public float revenue() {
		return revenue;
	}

	@Override
	public String name() {
		return "Algorithm Hold";
	}

}
