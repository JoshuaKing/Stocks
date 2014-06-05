package Algorithms;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import Common.Exchange;
import Common.KNN;
import Common.Log;
import Common.PricePoint;


public class AlgorithmKnn implements Algorithm {	
	public void run(List<PricePoint> stock, float moneyInvested, float fees) {
		LinkedHashMap<String, Float> profitsEachYear = new LinkedHashMap<String, Float>();
		Exchange exchange = new Exchange();
		
		KNN knn = new KNN(stock.subList(0, stock.size() / 2), 5);
		int ss = 5;
		
		
		int correctIncrease = 0, correctDecrease = 0;
		int incorrectIncrease = 0, incorrectDecrease = 0;
		
		for (int i = stock.size() / 2 + ss; i < stock.size() - 1; i++) {
			PricePoint today = stock.get(i);
			PricePoint tomorrow = stock.get(i + 1);
			List<PricePoint> sample = stock.subList(i - ss, i);
			
			// Get year
			String year = today.date.substring(6);
			int numShares = (int) Math.floor(moneyInvested/today.close);
			
			float expect = knn.score(sample);
			Log.debug("From " + today.close + " Expect: " + expect + " truth: " + tomorrow.open);
			
			if (expect > today.close) {
				if (tomorrow.open > today.close) {
					correctIncrease++;
				} else {
					incorrectIncrease++;
				}
			} else {
				if (tomorrow.open < today.close) {
					correctDecrease++;
				} else {
					incorrectDecrease++;
				}
			}
			
			
			// Check if we will profit even if everything happens correctly
			if (!exchange.test(numShares, today.close, (float) (expect * 0.93), fees)) continue;
			
			float buyCost = exchange.buy(numShares, today.close, fees);
			Log.debug(today.date + ": Bought " + numShares + " at $" + today.close + " (Expect $" + expect + ")");
			
			Float earnings = exchange.sell(tomorrow.open, fees);
			Log.debug(tomorrow.date + ": Sold " + numShares + " at $" + tomorrow.open + " for $" + earnings + " (Net: $" + (earnings - buyCost) + " Off by " + 100*Math.abs(expect - tomorrow.open)/tomorrow.open + ")");
			
			if (profitsEachYear.containsKey(year)) {
				profitsEachYear.put(year, profitsEachYear.get(year) + (earnings - buyCost));
			} else {
				profitsEachYear.put(year, earnings - buyCost);
			}
		}

		Log.alert("\n\n======== KNN Method Comparison ========");
		Log.info("Correct\nIncrease: " + correctIncrease + "\nDecrease: " + correctDecrease + "\n\nIncorrect:\nIncrease: " + incorrectIncrease + "\nDecrease: " + incorrectDecrease);
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
