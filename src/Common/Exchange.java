package Common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

public class Exchange {
	private float profit = 0;
	private float totalFees = 0;
	private int sharesOwned = 0;
	private float totalSpent = 0;
	private LinkedHashMap<String, Float> profitEachYear = new LinkedHashMap<String, Float>();
	
	public float buy(int num, float price, float fees, String year) {
		float cost = num * price + (num * price * fees);
		totalFees += (num * price * fees);
		
		profit -= cost;
		sharesOwned += num;
		Log.debug("Bought " + num + " shares for $" + cost + " @" + price + "ea. (total shares: " + sharesOwned + ")");
		
		totalSpent += cost;
		if (profitEachYear.containsKey(year)) {
			profitEachYear.put(year, profitEachYear.get(year) - cost);
		} else if (year != null && !year.isEmpty()) {
			profitEachYear.put(year, -cost);
		}
		return cost;
	}
	
	public float sell(float price, float fees, String year) {
		if (!hasShares()) return 0;
		float earnings = sharesOwned * price - (sharesOwned * price * fees);
		totalFees += (sharesOwned * price * fees);

		profit += earnings;
		Log.debug("Sold " + sharesOwned + " shares for $" + earnings + " @" + price + "ea. (profit so far: " + profit + ")");
		sharesOwned = 0;
		
		if (profitEachYear.containsKey(year)) {
			profitEachYear.put(year, profitEachYear.get(year) + earnings);
		} else {
			profitEachYear.put(year, earnings);
		}
		return earnings;
	}

	public boolean test(int num, Float buy, Float sell, Float fees) {
		float cost = num * buy + (num * buy * fees);
		float earnings = num * sell - (num * sell * fees);
		
		return cost < earnings;
	}
	
	public boolean hasShares() {
		return sharesOwned > 0;
	}
	
	public void summarise(String method) {
		Log.alert("\n==========[ " + method + " ]==========");
		Log.alert("Total Profit/Loss: $" + profit + " (Inc. $" + totalFees + " fees) Spent: " + totalSpent);
		Log.alert("Avg Return: " + 100*(profit/totalSpent) + "%");
		Log.alert("Equivalent Compound Rate: " + Math.pow(profit/50000,1.0/profitEachYear.size()) +"\n");
		
		Log.info("+------+-----------+");
		Log.info("| Year | Profit    |");
		Log.info("+------+-----------+");
		
		Iterator<Entry<String, Float>> it = profitEachYear.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Float> profit = it.next();
			Log.info("| " + profit.getKey() + " | " + profit.getValue() + " |");
		}

		Log.info("+------+-----------+");
	}
	
	public Float profit() {
		return profit;
	}
	
	public int sharesOwned() {
		return sharesOwned;
	}
}
