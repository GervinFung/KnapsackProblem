package knapsackProblem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class BruteForce extends AbstractKnapsackSolution {
	
	// combinations of taking and not taking the items into the knapsack that gives the maximum value
	// while weight is lesser than knapsack maximum capacity (in the form of 1s and 0s)
	// e.g. 100110 means, only first item, forth item and fifth item is taken
	private final StringBuilder optimalCombination;
	
	public BruteForce(final Knapsack knapsack) {
		super(knapsack);
		this.optimalCombination = new StringBuilder();
	}
	
	@Override
	protected int computeMaxValue() {
		
		// number of possible combinations is 2^n where n is number of all items
		final int numberOfCombinations = (int)Math.pow(2.0, this.knapsack.getAllItems().size());
		
		
		//sumOfValue --> total value of the taken items in a combination
		//sumOfWeight --> total weight of the taken items in a combination
		//maxValue --> largest total value where total weight is lesser than knapsack maximum capacity
		int sumOfValue, sumOfWeight, maxValue = 0;
		final StringBuilder combination = new StringBuilder();
		
		// convert 0 until numberOfCombinations-1 to binary form and check if the combination gives the maximum value
		for (int i = 0; i < numberOfCombinations; i++) {

			combination.append(Integer.toBinaryString(i));
			sumOfValue = 0;
			sumOfWeight = 0;

			for (int j = 0; j < combination.toString().length(); j++) {
				
				//if the character at j position is 1, the item that has the same index number with the character
				//is taken into the knapsack, add the item's value to sumOfValue and weight to sumOfWeight
				if (combination.toString().charAt(j) == '1') {
					sumOfValue += this.knapsack.getAllItems().get(j).getValue();
					sumOfWeight += this.knapsack.getAllItems().get(j).getWeight();
				}
			}
			
			if (sumOfValue > maxValue && sumOfWeight <= this.knapsack.getMaxCapacity()) {
				maxValue = sumOfValue;
				this.optimalCombination.setLength(0);
				this.optimalCombination.append(combination.toString());
			}
			combination.setLength(0);
		}
		
		return maxValue;
	}
	
	@Override
	protected List<Item> takeItems(final int currentMaxValue) {
		final List<Item> takenItems = new ArrayList<>();

		
		for (int i = 0; i < this.optimalCombination.length(); i++) {
			
			//if the character at j position is 1, the item that has the same index number with the
			//character is taken into the knapsack, add the item into the takenItem list
			if (this.optimalCombination.charAt(i) == '1') {
				takenItems.add(this.knapsack.getAllItems().get(i));
			}
		}

		return Collections.unmodifiableList(takenItems);
	}

	@Override
	public String toString() { return "Brute-Force"; }
}