package Common;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;


public class Knn {
	Stock training;
	int knn = 3;
	
	public int neighbours() {
		return knn;
	}
	
	public Knn(Stock trainData, int knn) {
		training = trainData;
		this.knn = knn;
	}
	
	public float score(Stock test) {
		TreeMap<Float, PricePoint> neighbours = new TreeMap<Float, PricePoint>();
		
		for (int i = 0; i <= training.size() - test.size(); i++) {
			Stock sample = training.subset(i, i + test.size());
			Float score = compare(sample, test);
			
			neighbours.put(score, training.exact(i + 1));
			
			if (neighbours.size() > knn) {
				neighbours.remove(neighbours.lastKey());
			}
		}
		
		float score = 0;
		while (neighbours.size() > 0) {
			PricePoint p = neighbours.remove(neighbours.firstKey());
			score += p.deltaOpen;
		}
		
		return score / knn;
	}
	
	private float compare(Stock s1, Stock s2) {
		float score = 0;
		
		for (int i = 0; i < s1.size(); i++) {
			PricePoint a = s1.exact(i);
			PricePoint b = s2.exact(i);
			
			//score += Math.abs(a.open - b.open) + Math.abs(a.close - b.close) + Math.abs(a.max - b.max) + Math.abs(a.min - b.min);
			score += Math.abs(a.deltaOpen - b.deltaOpen);// + Math.abs(a.deltaVolume - b.deltaVolume);
		}
		
		return score;
	}
}
