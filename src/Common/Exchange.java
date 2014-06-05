package Common;

public class Exchange {
	public float profit = 0;
	public float allFees = 0;
	public int sharesOwned = 0;
	public float priceBought = 0;
	
	public float buy(int num, float price, float fees) {
		float cost = num * price + (num * price * fees);
		allFees += (num * price * fees);
		
		profit -= cost;
		sharesOwned += num;
		priceBought = price;
		
		return cost;
	}
	
	public float sell(float price, float fees) {
		float earnings = sharesOwned * price - (sharesOwned * price * fees);
		allFees += (sharesOwned * price * fees);
		
		profit += earnings;
		sharesOwned = 0;
		priceBought = 0;
		
		return earnings;
	}

	public boolean test(int num, Float buy, Float sell, Float fees) {
		float cost = num * buy + (num * buy * fees);
		float earnings = num * sell - (num * sell * fees);
		
		return cost < earnings;
	}
}
