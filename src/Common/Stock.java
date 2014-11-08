package Common;

import java.util.List;

public class Stock {
	private List<PricePoint> stock;
	private int index = -1;
	private String name;
	
	public Stock(String name, List<PricePoint> stock) {
		this.stock = stock;
		this.name = name;
	}
	
	public String name() {
		return name;
	}
	
	public int size() {
		return stock.size();
	}
	
	public PricePoint today() {
		return get(0);
	}
	
	public PricePoint yesterday() {
		return get(1);
	}
	
	public PricePoint lastYear() {
		return get(365);
	}

	public PricePoint sixMonthsAgo() {
		return get(182);
	}
	
	public Boolean next() {
		index++;
		return index < stock.size();
	}
	
	public void reset() {
		index = -1;
	}
	
	public int index() {
		return index;
	}
	
	public PricePoint exact(int index) {
		return stock.get(index);
	}
	
	public PricePoint daysAgo(int days) {
		return get(days);
	}
	
	public PricePoint last() {
		return stock.get(stock.size() - 1);
	}
	
	public Stock subset(int from, int to) {
		return new Stock(name + "(" + from + "-" + to + ")", stock.subList(from, to));
	}
	
	private PricePoint get(int i) {
		int get = index - i < 0 ? 0 : index - i;
		get = get >= stock.size() ? stock.size() - 1  : get;
		return stock.get(get);
	}

	public Stock subset(int from) {
		return subset(from, stock.size() - 1);
	}
}