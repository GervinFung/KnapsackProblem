package knapsackProblem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;
import java.util.List;

public final class Memoize extends AbstractKnapsackSolution {

	private final TreeMap<Integer, Integer> topDownMap;

	public Memoize(final Knapsack knapsack) {
		super(knapsack);
		this.topDownMap = new TreeMap<>();
	}

	@Override
	protected int computeMaxValue() {

		//values --> the maximum values in each cells in the table
		final int[][] memoizeTable = new int[this.knapsack.getAllItems().size() + 1][this.knapsack.getMaxCapacity() + 1];

		//return the value in the final cell
		return memoize(this.topDownMap, this.knapsack, memoizeTable, this.knapsack.getAllItems().size(), this.knapsack.getMaxCapacity());
	}

	private int memoize(final TreeMap<Integer, Integer> recursiveMap, final Knapsack knapsack, final int[][] memoize, final int n, final int capacity) {
		//if value is stored before
		//just return the value stored, reduce time needed to calculate
		if (memoize[n][capacity] != 0) {
			return memoize[n][capacity];
		}
		//if n is not positive integer anymore
		if (n <= 0) {
			return 0;
		}
		else if (knapsack.getAllItems().get(n - 1).getWeight() <= capacity){
			//find the value of not taking n - 1th item
			final int notTakeNthItem = memoize(recursiveMap, knapsack, memoize, n - 1, capacity);
			//find the value of taking n-1th item
			final int takeNthItem = knapsack.getAllItems().get(n - 1).getValue() + memoize(recursiveMap, knapsack, memoize, n - 1, capacity - knapsack.getAllItems().get(n - 1).getWeight());
			memoize[n][capacity] = Math.max(notTakeNthItem, takeNthItem);
			//each time a new maximum value is generated, store it to a TreeMap,
			//key: maximum value
			//value: the index of the item that contributes to that maximum value
			recursiveMap.putIfAbsent(memoize[n][capacity], n - 1);
			return memoize[n][capacity];
		}
		else {
			memoize[n][capacity] = memoize(recursiveMap, knapsack, memoize, n - 1, capacity);
			//each time a new maximum value is generated, store it to a TreeMap,
			//key: maximum value
			//value: the index of the item that contributes to that maximum value
			recursiveMap.putIfAbsent(memoize[n][capacity], n - 1);
			return memoize[n][capacity];
		}
	}

	@Override
	protected List<Item> takeItems(int currentMaxValue) {

		final List<Item> takenItems = new ArrayList<>();

		while (currentMaxValue > 0) {
			
			//get and add item that contribute to the current maximum value
			//item that contributes to the current maximum value
			final Item currentItem = this.knapsack.getAllItems().get(Collections.unmodifiableMap(this.topDownMap).get(currentMaxValue));
			takenItems.add(currentItem);
			
			//decrement current maximum value by the current item's value
			//to find the next item that contribute to the maximum value
			currentMaxValue -= currentItem.getValue();
		}

		return Collections.unmodifiableList(takenItems);
	}
	@Override
	public String toString() { return "Memoize(Top-Down Approach)"; }
}
