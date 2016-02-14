package Common;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class Exchange {
	private float profit = 0;
	private float totalFees = 0;
	private int sharesOwned = 0;
	private int sharesShort = 0;
	private float totalSpent = 0;
	private LinkedHashMap<String, Float> profitEachYear = new LinkedHashMap<String, Float>();
	
	public LinkedHashMap<String, Float> yearlyProfits() {
		return profitEachYear;
	}
	
	public float buy(int num, float price, float fees, PricePoint p) {
		float cost = num * price + (num * price * fees);
		totalFees += (num * price * fees);
		
		profit -= cost;
		sharesOwned += num;
		Log.debug(p.date + ": Bought " + num + " shares for $" + cost + " @" + price + "ea. (total shares: " + sharesOwned + ")");
		
		totalSpent += cost;
		String year = p.getYear();
		if (profitEachYear.containsKey(year)) {
			profitEachYear.put(year, profitEachYear.get(year) - cost);
		} else if (year != null && !year.isEmpty()) {
			profitEachYear.put(year, -cost);
		}
		return cost;
	}
	
	public float sell(float price, float fees, PricePoint p) {
		if (!hasShares()) return 0;
		float fee = (sharesOwned * price * fees);
		float earnings = sharesOwned * price - fee;
		totalFees += fee;
		totalSpent += fee;

		profit += earnings;
		Log.debug(p.date + ": Sold " + sharesOwned + " shares for $" + earnings + " @" + price + "ea. (profit so far: " + profit + ")");
		sharesOwned = 0;

		String year = p.getYear();
		if (profitEachYear.containsKey(year)) {
			profitEachYear.put(year, profitEachYear.get(year) + earnings);
		} else {
			profitEachYear.put(year, earnings);
		}
		return earnings;
	}
	
	public float shortSell(int num, float price, float fees, PricePoint p) {
		sharesShort += num;
		float fee = (sharesShort * price * fees);
		float earnings = sharesShort * price - fee;
		totalFees += fee;
		totalSpent += fee;
		
		profit += earnings;
		Log.debug(p.date + ": Shorted " + num + " shares for $" + earnings + " @" + price + "ea. (total short shares: " + sharesShort + ")");

		String year = p.getYear();
		if (profitEachYear.containsKey(year)) {
			profitEachYear.put(year, profitEachYear.get(year) + earnings);
		} else {
			profitEachYear.put(year, earnings);
		}
		return fee;
	}
	
	public float coverShort(float price, float fees, PricePoint p) {
		if (sharesShort == 0) return 0;
		
		float cost = sharesShort * price + (sharesShort * price * fees);
		totalFees += (sharesShort * price * fees);
		totalSpent += cost;
		
		profit -= cost;
		totalSpent += cost;
		Log.debug(p.date + ": Bought back " + sharesShort + " shares for $" + cost + " @" + price + "ea. (total profit so far: " + profit + ")");
		sharesShort = 0;
		
		String year = p.getYear();
		if (profitEachYear.containsKey(year)) {
			profitEachYear.put(year, profitEachYear.get(year) - cost);
		} else if (year != null && !year.isEmpty()) {
			profitEachYear.put(year, -cost);
		}
		return cost;
	}

	public boolean test(int num, Float buy, Float sell, Float fees) {
		float cost = num * buy + (num * buy * fees);
		float earnings = num * sell - (num * sell * fees);
		
		return cost < earnings;
	}
	
	public float testBuy(int num, Float buy, Float sell, Float fees) {
		float cost = num * buy + (num * buy * fees);
		float earnings = num * sell - (num * sell * fees);
		
		return earnings - cost;
	}
	
	public boolean hasShares() {
		return sharesOwned > 0;
	}
	
	public boolean hasShortedShares() {
		return sharesShort > 0;
	}
	
	public void summarise(String method) {
		Log.alert("\n==========[ " + method + " ]==========");
		Log.alert("Total Profit/Loss: $" + profit + " (Inc. $" + totalFees + " fees) Spent: " + totalSpent);
		Log.alert("Avg Return: " + 100*(profit/totalSpent) + "%");
		Log.alert("Equivalent Compound Rate: " + Math.pow((50000+profit)/50000,1.0/profitEachYear.size()) +"\n");
		
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
	
	public Float fees() {
		return totalFees;
	}
	
	public int sharesOwned() {
		return sharesOwned;
	}

	public float spent() {
		return totalSpent;
	}
}
