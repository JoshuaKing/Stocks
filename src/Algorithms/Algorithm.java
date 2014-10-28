package Algorithms;
import java.util.List;

import Common.PricePoint;
import Common.Stock;


public interface Algorithm {
	public void run(Stock stock, float moneyInvested, float fees);
}
