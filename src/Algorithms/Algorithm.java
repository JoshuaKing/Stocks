package Algorithms;
import Common.Exchange;
import Common.Stock;


public interface Algorithm {
	public Exchange run(Stock stock, float moneyInvested, float fees, int from, int to);
	public String name();
	public float revenue();
}
