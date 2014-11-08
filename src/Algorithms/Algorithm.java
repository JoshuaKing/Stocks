package Algorithms;
import java.util.List;

import Common.Exchange;
import Common.PricePoint;
import Common.Stock;


public interface Algorithm {
	public Exchange run(Stock stock, float moneyInvested, float fees, int since);
	public float spent();
	public String name();
}
