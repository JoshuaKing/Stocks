package Common;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public class CsvLoad {
	public static List<PricePoint> loadNormalCsv(String path) {
		// Read in file, add new stock price points for each line in the CSV file
		List<PricePoint> stock = new ArrayList<PricePoint>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
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
		
		return stock;
	}
}
