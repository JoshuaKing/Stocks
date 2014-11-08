package Common;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class CsvLoad {
	public static List<Stock> loadStockListCsv(String path, Boolean forceRefresh, Integer from, Integer to) {
		// Read in file, add new stock price points for each line in the CSV file
		List<Stock> stockList = new ArrayList<Stock>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			br.readLine();	// Remove header row
			String line = "";
			int i = 0;
			while ((line = br.readLine()) != null) {
				if ((from != null && i < from) || (to != null && i > to)) {
					i++;
					continue;
				}
				String stock = line.split(",", 2)[0].toLowerCase();
				if (!forceRefresh && new File("lib/" + stock + ".csv").exists()) {
					Log.debug("Already have " + stock.toUpperCase());
					stockList.add(loadStockCsv(stock));
				} else {
					String url = "http://real-chart.finance.yahoo.com/table.csv?s=" + stock.toUpperCase() + "&a=00&b=1&c=1970&d=11&e=31&f=2015&g=d&ignore=.csv";
					downloadFromUrl(new URL(url), "lib/" + stock + ".csv");
					Log.debug("Downloaded " + stock.toUpperCase());
					stockList.add(loadStockCsv(stock));
				}
				i++;
				if (i % 10 == 0) {
					//Log.alert("Loaded " + (i - from));
				}
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		return stockList;
	}
	
	public static Stock loadStockCsv(String stockId) {
		// Read in file, add new stock price points for each line in the CSV file
		List<PricePoint> stock = new ArrayList<PricePoint>();
		try {
			BufferedReader br = new BufferedReader(new FileReader("lib/" + stockId.toLowerCase() + ".csv"));
			br.readLine();	// Remove header row
			String line = "", nextLine = "";
			while ((nextLine = br.readLine()) != null) {
				if (line == "") {
					line = nextLine;
					continue;
				}
				stock.add(0, PricePoint.generate(line.split(","), nextLine.split(",")));
				line = nextLine;
			}
			br.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		return new Stock(stockId.toUpperCase(), stock);
	}
	
	private static File downloadFromUrl(URL url, String localFilename) throws IOException {
	    try {
	        InputStream is = url.openConnection().getInputStream();       //get connection inputstream
	        FileOutputStream fos = new FileOutputStream(localFilename);   //open outputstream to local file

	        byte[] buffer = new byte[4096];              //declare 4KB buffer
	        int len;

	        //while we have availble data, continue downloading and storing to local file
	        while ((len = is.read(buffer)) > 0) {  
	            fos.write(buffer, 0, len);
	        }
	        
	        is.close();
	        fos.close();
	        
	        return new File(localFilename);
	    } catch (Exception e) {
	    	Log.alert("Error downloading " + url + " to file " + localFilename);
	    	return null;
	    }
	}
}
