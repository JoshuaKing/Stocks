package Common;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;


public class KNN {
	List<PricePoint> training;
	int knn = 3;
	
	public KNN(List<PricePoint> trainData, int knn) {
		training = trainData;
		this.knn = knn;
	}
	
	public float score(List<PricePoint> test) {
		TreeMap<Float, PricePoint> neighbours = new TreeMap<Float, PricePoint>();
		
		for (int i = test.size(); i < training.size() - 1; i++) {
			List<PricePoint> sample = training.subList(i - test.size(), i);
			Float score = compare(sample, test);
			
			neighbours.put(score, training.get(i + 1));
			
			if (neighbours.size() > knn) {
				neighbours.remove(neighbours.lastKey());
			}
		}
		
		float score = 0;
		while (neighbours.size() > 0) {
			PricePoint p = neighbours.remove(neighbours.firstKey());
			score += p.open / knn;
		}
		
		return score;
	}
	
	private float compare(List<PricePoint> s1, List<PricePoint> s2) {
		float score = 0;
		
		for (int i = 0; i < s1.size(); i++) {
			PricePoint a = s1.get(i);
			PricePoint b = s2.get(i);
			
			score += Math.abs(a.open - b.open) + Math.abs(a.close - b.close) + Math.abs(a.max - b.max) + Math.abs(a.min - b.min);
			score += Math.abs(a.deltaPrice - b.deltaPrice) + Math.abs(a.deltaVolume - b.deltaVolume);
		}
		
		return score;
	}
}
