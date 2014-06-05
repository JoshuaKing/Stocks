package Algorithms;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import Common.Exchange;
import Common.Log;
import Common.PricePoint;

public class AlgorithmHold implements Algorithm {

	@Override
	public void run(List<PricePoint> stock, float moneyInvested, float fees) {
		LinkedHashMap<String, Float> profitsEachYear = new LinkedHashMap<String, Float>();
		Exchange exchange = new Exchange();
		float buyCost = 0;
		
		for (int i = 0; i < stock.size(); i++) {
			PricePoint today = stock.get(i);
			
			// Get year
			String year = today.date.substring(6);
			
			if (!profitsEachYear.containsKey(year)) {
				profitsEachYear.put(year, (float) 0);
				if (i > 0) {
					String lastYear = stock.get(i - 1).date.substring(6);
					int numShares = exchange.sharesOwned;
					Float earnings = exchange.sell(stock.get(i - 1).close, fees);
					Log.debug(stock.get(i - 1).date + ": Sold " + numShares + " at $" + stock.get(i - 1).close + " for $" + earnings + " (Net: $" + (earnings - buyCost) + ")");
					profitsEachYear.put(lastYear, earnings - buyCost);
				}

				int numShares = (int) Math.floor(moneyInvested/today.open);
				buyCost = exchange.buy(numShares, today.open, fees);
				Log.debug(today.date + ": Bought " + numShares + " at $" + today.open);
			}
			
			if (i == stock.size() - 1) {
				int numShares = exchange.sharesOwned;
				Float earnings = exchange.sell(stock.get(i - 1).close, fees);
				Log.debug(stock.get(i - 1).date + ": Sold " + numShares + " at $" + stock.get(i - 1).close + " for $" + earnings + " (Net: $" + (earnings - buyCost) + ")");
				profitsEachYear.put(year, earnings - buyCost);
			}
		}
		
		Log.alert("\n\n======== Hold Method Comparison ========");
		Log.alert("Total Profit/Loss: $" + exchange.profit + "   (Inc. $" + exchange.allFees + " fees)");
		Log.alert("Avg Profit per Year: $" + (exchange.profit / profitsEachYear.size()));
		
		Log.info("\nProfit Breakdown Each Year: ");
		Iterator<Entry<String, Float>> it = profitsEachYear.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Float> entry = it.next();
			Log.info(entry.getKey() + ": $" + entry.getValue());
		}
	}

}
