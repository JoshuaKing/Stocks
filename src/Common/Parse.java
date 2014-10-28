package Common;
import java.util.List;

import Algorithms.AlgorithmBuyLow;
import Algorithms.AlgorithmHold;
import Algorithms.AlgorithmKnn;


public class Parse {
	static Float fees = (float) 0.0011;
	static Float moneyInvested = (float) 50000;

	public static void main(String[] args) {
		
		try {
			Stock stock = new Stock(CsvLoad.loadNormalCsv(args[0]));
			new AlgorithmKnn(stock.subset(0, 300), 3, 5, false).run(stock.subset(300), moneyInvested, fees);
			new AlgorithmBuyLow((float) 0.05).run(stock, moneyInvested, fees);
			/*new AlgorithmBuyLow((float) 0.05).run(stock, moneyInvested, fees);
			new AlgorithmBuyLow((float) 0.08).run(stock, moneyInvested, fees);
			new AlgorithmBuyLow((float) 0.1).run(stock, moneyInvested, fees);
			new AlgorithmBuyLow((float) 0.1, 90).run(stock, moneyInvested, fees);
			*///new AlgorithmTest().run(stock, moneyInvested, fees);
			new AlgorithmHold().run(stock, moneyInvested, fees);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	static float average(List<PricePoint> l) {
		float avg = 0;
		for (PricePoint p : l) {
			avg += p.avgPrice / l.size();
		}
		return avg;
	}
	
	static PricePoint max(List<PricePoint> l) {
		PricePoint m = null;
		for (PricePoint p : l) {
			if (m == null || p.max > m.max) {
				m = p;
			}
		}
		return m;
	}
}
