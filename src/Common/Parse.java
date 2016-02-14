package Common;
import Algorithms.Algorithm;
import Algorithms.AlgorithmBuyLow;
import Algorithms.AlgorithmHold;

import java.text.NumberFormat;
import java.util.List;


public class Parse {
	private static final Float fees = 0.0011F;
	private static final Float MONEY_INVESTED = 1000000F;
	private static final int START_FROM_YEAR = 2005;
	private static final int END_AT_YEAR = 2015;
	private static final int STOCKS_PER_ITERATION = 40;
	private static final int TOTAL_STOCKS = 100;
	private static NumberFormat nf = NumberFormat.getCurrencyInstance();

	public static void main(String[] args) {
		
		try {
			float moneyA = 0, moneyB = 0;
			int num = 0;
			nf.setParseIntegerOnly(true);
			nf.setMaximumFractionDigits(0);

			for (int i = 0; i < TOTAL_STOCKS; i+= STOCKS_PER_ITERATION) {
				List<Stock> stocks = CsvLoad.loadStockListCsv(args[0], false, i, i + STOCKS_PER_ITERATION - 1);
				num += 1;
				moneyA += runAlgorithm("BuyLow", new AlgorithmBuyLow(0.00F), stocks, START_FROM_YEAR, END_AT_YEAR);
				moneyB += runAlgorithm("Hold", new AlgorithmHold(), stocks, START_FROM_YEAR, END_AT_YEAR);
			}
			Log.alert("");
			Log.alert("=======================================================");
			Log.alert("Algorithm Buy Low ROI since " + START_FROM_YEAR + ": " + moneyA/(MONEY_INVESTED * num * STOCKS_PER_ITERATION));
			Log.alert("Algorithm Hold ROI since " + START_FROM_YEAR + ": " + moneyB/(MONEY_INVESTED * num * STOCKS_PER_ITERATION));
			Log.alert("Algorithm Buy Low TOTAL " + nf.format(moneyA));
			Log.alert("Algorithm Hold TOTAL " +  nf.format(moneyB));
			Log.alert("=======================================================");
		} catch (Exception e) {
			Log.error(e);
		}
	}
	
	private static float runAlgorithm(String name, Algorithm algorithm, List<Stock> stocks, int from, int to) {
		float money = 0;
		float totalfees = 0;
		float profit2014 = 0;
		for (Stock stock : stocks) {
			Exchange exchange = algorithm.run(stock, MONEY_INVESTED, fees, from, to);
			
			money += algorithm.revenue();
			totalfees += exchange.fees();
			profit2014 += exchange.yearlyProfits().get("2009") == null ? 0 : exchange.yearlyProfits().get("2009");
			
			Log.info("Stock " + stock.name() + ": " + exchange.profit() + " ROI " + (exchange.profit() + MONEY_INVESTED)/ MONEY_INVESTED + " (Fees: " + exchange.fees() + ")");
		}
		float roi = money / (MONEY_INVESTED * stocks.size());
		Log.alert("Algorithm " + name + ": End value " + money + " ROI " + roi + " (Fees: " + totalfees + ")");
		Log.alert("Algorithm " + name + " Profit 2014: " + profit2014 + "\n");
		return money;
	}
}
