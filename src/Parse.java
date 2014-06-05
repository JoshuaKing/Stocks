import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;


public class Parse {
	static Float fees = (float) 0.0011;
	static Float moneyInvested = (float) 50000;

	public static void main(String[] args) {
		
		try {
			List<PricePoint> stock = CsvLoad.loadNormalCsv(args[0]);
			algorithm(stock);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private static float average(List<PricePoint> l) {
		float avg = 0;
		for (PricePoint p : l) {
			avg += p.avgPrice / l.size();
		}
		return avg;
	}
	
	private static PricePoint max(List<PricePoint> l) {
		PricePoint m = null;
		for (PricePoint p : l) {
			if (m == null || p.max > m.max) {
				m = p;
			}
		}
		return m;
	}
	
	public static void algorithm(List<PricePoint> stock) {
		LinkedHashMap<String, Float> profitsEachYear = new LinkedHashMap<String, Float>();
		Exchange exchange = new Exchange();
		LinkedList<PricePoint> runningAvg = new LinkedList<PricePoint>();
		
		for (int i = 0; i < 5; i++) {
			runningAvg.add(stock.get(stock.size() - 1 - i));
		}
		
		for (int i = stock.size() - 1 - 5; i > 6; i--) {
			PricePoint today = stock.get(i);
			
			// Get year
			String year = today.date.substring(6);
			int numShares = (int) Math.floor(moneyInvested/today.open);
			
			// Stats for stock
			Float avg = average(runningAvg);
			runningAvg.removeFirst();
			runningAvg.add(today);
			PricePoint weekMax = max(stock.subList(i - 5, i));
			PricePoint nextWeek = stock.get(i - 6);

			// Check if we will profit even if everything happens correctly
			if (!exchange.test(numShares, today.open, avg, fees)) continue;
			
			float buyCost = exchange.buy(numShares, today.open, fees);
			System.out.println(today.date + ": Bought " + numShares + " at $" + today.open + " (Sell Order at $" + avg + ")");
			
			Float earnings = (float) 0;
			// Find how much they sell for now
			if (weekMax.max > avg) {
				earnings = exchange.sell(avg, fees);
				System.out.println(weekMax.date + ": Sold " + numShares + " at $" + avg + " for $" + earnings + " (Net: $" + (earnings - buyCost) + ")");
			} else {
				earnings = exchange.sell(nextWeek.open, fees);
				System.out.println(nextWeek.date + ": Could not sell at average. Sold " + numShares + " at $" + nextWeek.open + " for $" + earnings + " (Net: $" + (earnings - buyCost) + ")");
			}
			
			if (profitsEachYear.containsKey(year)) {
				profitsEachYear.put(year, profitsEachYear.get(year) + (earnings - buyCost));
			} else {
				profitsEachYear.put(year, earnings - buyCost);
			}
		}
		
		System.out.println("Done\n\n\n");
		System.out.println("Total Profit/Loss: $" + exchange.profit + "   (Inc. $" + exchange.allFees + " fees)");
		System.out.println("Avg Profit per Year: $" + (exchange.profit / profitsEachYear.size()));
		
		System.out.println("\nProfit Breakdown Each Year: ");
		Iterator<Entry<String, Float>> it = profitsEachYear.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Float> entry = it.next();
			System.out.println(entry.getKey() + ": $" + entry.getValue());
		}
		
	}

	

}
