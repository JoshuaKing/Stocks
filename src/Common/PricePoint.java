package Common;

public class PricePoint {
		public String date;
		public Float avgPrice, deltaPrice, deltaVolume, deltaOpen, variance;
		public Float open, close, min, max;

		public static PricePoint generate(String[] csv, String[] nextCsv) {
			String date = csv[0].substring(8) + "/" + csv[0].substring(5, 7) + "/" + csv[0].substring(0, 4);
			Float avgYesterday = (Float.valueOf(nextCsv[1]) + Float.valueOf(nextCsv[2]) + Float.valueOf(nextCsv[3]) + Float.valueOf(nextCsv[4])) / 4;
			Float volToday = Float.valueOf(csv[5]);
			Float volYesterday = Float.valueOf(nextCsv[5]);
			
			PricePoint p = new PricePoint();
			p.date = date;
			
			p.open = Float.valueOf(csv[1]);
			p.max = Float.valueOf(csv[2]);
			p.min = Float.valueOf(csv[3]);
			p.close = Float.valueOf(csv[4]);

			p.variance = p.max - p.min;

			p.deltaVolume = (volToday - volYesterday) / volYesterday;
			
			p.avgPrice = (p.open + p.max + p.min + p.close) / 4;
			p.deltaPrice = (p.avgPrice - avgYesterday) / avgYesterday;
			p.deltaOpen = (p.open - Float.valueOf(nextCsv[1])) / Float.valueOf(nextCsv[1]);
			return p;
		}
		
		public String getYear() {
			return date.substring(6);
		}
	}
