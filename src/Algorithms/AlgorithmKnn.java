package Algorithms;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import Common.Exchange;
import Common.Knn;
import Common.Log;
import Common.PricePoint;
import Common.Stock;


public class AlgorithmKnn implements Algorithm {	
	Boolean appendTestData = false;
	int length;
	Knn knn;
	
	public AlgorithmKnn(Stock training, int neighbours, int length, Boolean append) {
		appendTestData = append;
		knn = new Knn(training, neighbours);
		this.length = length;
	}
	
	public void run(Stock stock, float moneyInvested, float fees) {
		Exchange exchange = new Exchange();
		int correctIncrease = 0, correctDecrease = 0;
		int incorrectIncrease = 0, incorrectDecrease = 0;
		
		
		for (int i = 0; i < stock.size() - length - 1; i++) {
			stock.next();
			PricePoint today = stock.today();
			PricePoint tomorrow = stock.exact(stock.index() + 1);
			Stock sample = stock.subset(i, i + length);
			
			if (i % 300 == 0 && i > 0) {
				knn = new Knn(stock.subset(0, i), knn.neighbours());
			}
			
			// Get year
			String year = today.date.substring(6);
			int numShares = (int) Math.floor(moneyInvested/today.close);
			
			float expect = today.open * (1 + knn.score(sample));
			//Log.debug("From " + today.close + " Expect: " + expect + " truth: " + tomorrow.open);
			
			if (expect > today.close) {
				if (tomorrow.open > today.close) {
					correctIncrease++;
				} else {
					//Log.alert("Bought @" +today.close + " Expected " + expect + " Got " + tomorrow.open);
					/*if (exchange.test(numShares, today.close, (float) (expect), fees))
						Log.alert("Bought @" +today.close + " Expected " + expect + " Got " + tomorrow.open);
					*/incorrectIncrease++;
				}
			} else {
				if (tomorrow.open < today.close) {
					correctDecrease++;
				} else {
					incorrectDecrease++;
				}
			}
			
			exchange.sell(today.open, fees, year);
			
			// Check if we will profit even if everything happens correctly
			if (!exchange.test(numShares, today.close, (float) (expect), fees)) continue;
			
			exchange.buy(numShares, today.close, fees, year);
		}
		
		exchange.sell(stock.last().open, fees, stock.last().getYear());
		
		stock.reset();
		exchange.summarise("Algorithm KNN");
		Log.alert("Increase: " + correctIncrease + " | " + incorrectIncrease + "  " + (float)correctIncrease/(correctIncrease + incorrectIncrease)*100.0 + "%");
		Log.alert("Decrease: " + correctDecrease + " | " + incorrectDecrease + "  " + (float)correctDecrease/(correctDecrease + incorrectDecrease)*100.0 + "%");
	}
}
