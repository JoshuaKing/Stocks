package Common;
import java.util.List;

import Algorithms.Algorithm;
import Algorithms.AlgorithmBuyLow;
import Algorithms.AlgorithmHold;


public class Parse {
	static Float fees = 0.0011F;
	static Float moneyInvested = (float) 20000;
	static int startFromYear = 2005;

	public static void main(String[] args) {
		
		try {
			float roiA = 0, roiB = 0;
			int num = 0;
			
			for (int i = 0; i < 200; i+=40) {
				List<Stock> stocks = CsvLoad.loadStockListCsv(args[0], false, i, i+40);
				num += 1;
				roiA += runAlgorithm("BuyLow", new AlgorithmBuyLow((float) 0.05), stocks, startFromYear);
				roiB += runAlgorithm("Hold", new AlgorithmHold(), stocks, startFromYear);
			}
			Log.alert("\n=======================================================");
			Log.alert("Algorithm Buy Low ROI since " + startFromYear + ": " + roiA/num);
			Log.alert("Algorithm Hold ROI since " + startFromYear + ": " + roiB/num);
			Log.alert("=======================================================");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private static float runAlgorithm(String name, Algorithm algorithm, List<Stock> stocks, int startFrom) {
		float profit = 0;
		float totalfees = 0;
		float profit2014 = 0;
		for (Stock stock : stocks) {
			Exchange exchange = algorithm.run(stock, moneyInvested, fees, startFrom);
			profit += exchange.profit();
			totalfees += exchange.fees();
			Log.info("Stock " + stock.name() + ": " + exchange.profit() + " (Fees: " + exchange.fees() + ")");
			profit2014 += exchange.yearlyProfits().get("2014") == null ? 0 : exchange.yearlyProfits().get("2014");
		}
		float roi = (algorithm.spent() + profit)/algorithm.spent();
		Log.alert("Algorithm " + name + ": Cost " + algorithm.spent() + " Profit " + profit + " ROI " + roi + " (Fees: " + totalfees + ")");
		Log.alert("Algorithm " + name + " Profit 2014: " + profit2014 + "\n");
		return roi;
	}
}
