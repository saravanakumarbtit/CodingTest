import java.util.HashMap;
import java.util.Map;

/**
 * DistributedRandomNumberGenerator is used to generate Random elements based on
 * their probabilities
 * 
 * @author Saravanan
 * @version 1.0
 */
public class DistributedRandomNumberGenerator {

    private Map<String, Double> distribution;
    private double distSum;

    public DistributedRandomNumberGenerator() {
        distribution = new HashMap<>();
    }

    public void addNumber(String value, double distribution) {
        if (this.distribution.get(value) != null) {
			// If addNumber(int value, ...) is called multiple times with the same value
			// this line ensures that the sum distSum holds the correct value.
            distSum -= this.distribution.get(value);
        }
        this.distribution.put(value, distribution);
        distSum += distribution;
		// distSum value will be mostly 1 because after adding the probability of all
		// the elements it should be equal to 1
    }

    public String getDistributedRandomNumber() {
		double rand = Math.random(); 
		// Returns a double value with a positive sign, greaterthan or 
		// equal to 0.0 and less than 1.0
        double ratio = 1.0f / distSum;
        double tempDist = 0;
        for (String i : distribution.keySet()) {
            tempDist += distribution.get(i);
            if (rand / ratio <= tempDist) {
                return i;
            }
        }
        return null;
    }

}