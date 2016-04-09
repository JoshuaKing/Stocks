package Common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringJoiner;

public class Convert {
    public static void main(String[] args) throws IOException {
        for (String stock : args) convert(stock, "2014");
    }

    private static void convert(String stockId, String dateStop) throws FileNotFoundException {
        Stock stock = CsvLoad.loadStockCsv(stockId);
        File output = new File("resources/ml/" + stockId + ".delta.csv");
        output.getParentFile().mkdirs();
        PrintWriter writer = new PrintWriter(output);

        StringJoiner headings = new StringJoiner(",").add("Date");
        headings.add("RESULT-DELTA-OPEN");
        for (int i = 1; i <= 10; i++) {
            headings.add("DeltaOpen" + i)
                    .add("DeltaPrice" + i)
                    .add("DeltaVolume" + i)
                    .add("Variance" + i);
        }
        //System.out.println(headings.toString());
        writer.println(headings.toString());

        for (int i = 0; i < 20; i++) stock.next();

        while (stock.next()) {
            if (stock.today().date.endsWith(dateStop)) break;

            StringJoiner joiner = new StringJoiner(",")
                    .add(stock.today().date)
                    .add(stock.today().deltaOpen.toString());
            for (int i = 1; i <= 10; i++) {
                joiner.add(detailsOfPoint(stock.daysAgo(i)));
            }
            //System.out.println(joiner.toString());
            writer.println(joiner.toString());
        }
        writer.close();

        System.out.println("Finished stock " + stockId);
    }


    private static String detailsOfPoint(PricePoint p) {
        return new StringJoiner(",")
                .add(p.deltaOpen.toString())
                .add(p.deltaPrice.toString())
                .add(p.deltaVolume.toString())
                .add(p.variance.toString())
                .toString();
    }
}
