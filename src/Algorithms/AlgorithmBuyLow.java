package Algorithms;

import Common.Exchange;
import Common.PricePoint;
import Common.Stock;

public class AlgorithmBuyLow implements Algorithm {
	private static float avgAnnualIncrease = 0.0F;
	float spent = 0;

	public AlgorithmBuyLow(float avgIncrease) {
		avgAnnualIncrease = avgIncrease;
	}

	@Override
	public Exchange run(Stock stock, float moneyInvested, float fees, int since) {
		Exchange exchange = new Exchange();
		
		while (stock.next()) {
			PricePoint today = stock.today();
			float longTermPrice = stock.sixMonthsAgo().open * (1 + avgAnnualIncrease);
			
			int numShares = (int) Math.floor(moneyInvested/today.open);
			
			if (Integer.valueOf(today.getYear()) >= since) continue;
			if (today.open < longTermPrice) {
				if (!exchange.hasShares() && !exchange.hasShortedShares() && exchange.test(numShares, today.open, longTermPrice, fees)) spent += exchange.buy(numShares, today.open, fees, today);
			} else {
				if (today.open > longTermPrice * 1.5) spent += exchange.sell(today.open, fees, today);
			}
		}
		
		if (exchange.hasShares()) {
			spent += exchange.sell(stock.last().close, fees, stock.last());
		}
		
		stock.reset();		
		return exchange;
	}

	@Override
	public float spent() {
		return spent;
	}

	@Override
	public String name() {
		return "Algorithm Buy Low";
	}

}
