package Algorithms;
import java.util.List;

import Common.PricePoint;


public interface Algorithm {
	public void run(List<PricePoint> stock, float moneyInvested, float fees);
}
