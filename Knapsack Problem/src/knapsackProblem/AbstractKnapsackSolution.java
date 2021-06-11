package knapsackProblem;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractKnapsackSolution {
	
	protected final Knapsack knapsack;
	
	public AbstractKnapsackSolution(final Knapsack knapsack) {
		this.knapsack = knapsack;
	}
	
	public final Knapsack solveKnapsack() {
		final int maxValue = this.computeMaxValue();
		return new Knapsack(maxValue, this.knapsack.getMaxCapacity(), this.knapsack.getAllItems(), this.finaliseTakenItems(this.takeItems(maxValue)));
	}
	
	//find the maximum value of knapsack
	protected abstract int computeMaxValue();
	
	//identify the items taken into the knapsack
	protected abstract List<Item> takeItems(final int maxValue);

	private Map<Item, Integer> finaliseTakenItems(final List<Item> takeItems) {
		final TreeMap<Item, Integer> mapTakenItems = new TreeMap<>();
		for (final Item item : takeItems) {
			mapTakenItems.put(item, mapTakenItems.containsKey(item) ? mapTakenItems.get(item) + 1 : 1);
		}
		return Collections.unmodifiableMap(mapTakenItems);
	}
}