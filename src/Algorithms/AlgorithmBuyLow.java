package Algorithms;

import Common.Exchange;
import Common.Log;
import Common.PricePoint;
import Common.Stock;

public class AlgorithmBuyLow implements Algorithm {
	private static final int CASH_SPEND = 25000;
	private static final int CASH_BUFFER = 30000;
	private static float avgAnnualIncrease = 0.0F;
	private static float margin;
	float revenue = 0;

	public AlgorithmBuyLow(float avgIncrease, float margin) {
		avgAnnualIncrease = avgIncrease;
		this.margin = margin;
	}

	@Override
	public Exchange run(Stock stock, float moneyInvested, float fees, int from, int to) {
		Exchange exchange = new Exchange(moneyInvested);
		revenue = moneyInvested;
		float sellAt = 0;
		
		while (stock.next()) {
			PricePoint today = stock.today();
			float longTermPrice = stock.sixMonthsAgo().open * (1 + avgAnnualIncrease);
			
			int numShares = revenue > CASH_BUFFER ? (int) Math.floor(CASH_SPEND/today.open) : 0;
			
			if (Integer.valueOf(today.getYear()) < from || Integer.valueOf(today.getYear()) > to) continue;
			if (today.open < longTermPrice * (1.0F - margin)) {
				if (exchange.test(numShares, today.open, longTermPrice, fees)) {
					revenue -= exchange.buy(numShares, today.open, fees, today);
					sellAt = longTermPrice * (1.0F + margin);
					if (revenue < 0) {
						Log.error("Cannot have < 0 revenue: " + revenue);
					}
				}
			} else if (today.open >= sellAt) {
				revenue += exchange.sell(today.open, fees, today);
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
		return "Algorithm Buy Low";
	}

}
