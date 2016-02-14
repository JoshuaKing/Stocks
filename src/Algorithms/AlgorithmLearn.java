package Algorithms;

import Common.Exchange;
import Common.Log;
import Common.PricePoint;
import Common.Stock;

public class AlgorithmLearn implements Algorithm {
	private static final int CASH_SPEND = 20000;
	float revenue = 0;

	@Override
	public Exchange run(Stock stock, float moneyInvested, float fees, int from, int to) {
		Exchange exchange = new Exchange(moneyInvested);
		revenue = moneyInvested;

		while (stock.next()) {
			PricePoint today = stock.today();
			exchange.handle(today.open, today.getYear());
			float avg = stock.average(182);
			
			int numShares = (int) Math.floor(CASH_SPEND/today.open);
			
			if (Integer.valueOf(today.getYear()) < from || Integer.valueOf(today.getYear()) > to) continue;
			if (today.open < avg) {
				float min = stock.min(182);
				float percent = 1/ ((today.open - min) / (avg - min));
				float price = today.open * (0.5F + percent * 0.5F);
				if (exchange.test(numShares, today.open, price, fees)) {
					Log.debug("From min " + min + " avg " + avg + " : Expecting price to rise to " + price + " from " + today.open);
					revenue -= exchange.buy(numShares, today.open, fees, today);
					exchange.sellOrder(price, numShares, fees);
				}
			}
		}
		
		if (exchange.hasShares()) {
			//revenue += exchange.sell(stock.last().close, fees, stock.last());
			exchange.handleLast(stock.last().getYear());
		}
		
		stock.reset();
		revenue = exchange.getCash();
		return exchange;
	}

	@Override
	public float revenue() {
		return revenue;
	}

	@Override
	public String name() {
		return "Algorithm Learn";
	}

}
