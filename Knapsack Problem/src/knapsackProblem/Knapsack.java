package knapsackProblem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class Knapsack {

	private final int maxCapacity, maxValue;
	
	//all the available items
	private final List<Item> allItems;

	private final Map<Item, Integer> mapTakenItems;

	public Knapsack(final int maxValue, final int maxCapacity, final List<Item> allItems, final Map<Item, Integer> mapTakenItems) {
		this.maxCapacity = maxCapacity;
		this.maxValue = maxValue;
		this.allItems = ungroupItems(allItems);
		this.mapTakenItems = mapTakenItems;
	}

	public Knapsack(final int maxCapacity, final List<Item> allItems) {
		this(0, maxCapacity, allItems, new TreeMap<>());
	}

	//getter
	public int getMaxCapacity() { return this.maxCapacity; }

	public int getMaxValue() { return this.maxValue;}

	public List<Item> getAllItems() { return Collections.unmodifiableList(this.allItems); }

	public Map<Item, Integer> getTakenItems() { return Collections.unmodifiableMap(this.mapTakenItems); }

	public void showItems() {
		System.out.println("-------------Item(s) added into knapsack-------------");
		System.out.printf("%-20s%-8s%-8s%s%n", "Name", "Weight", "Value", "Taken Quantity");
		System.out.println("--------------------------------------------------");
		this.getTakenItems().keySet().forEach(item -> System.out.println(item.toString() + this.getTakenItems().get(item)));
		System.out.println("--------------------------------------------------");
	}
	
	public int getAllItemsWeight() {
		int sumOfWeight = 0;

		for (final Item item : this.getTakenItems().keySet()) {
			sumOfWeight += (item.getWeight() * this.getTakenItems().get(item));
		}
		
		return sumOfWeight;
	}
	
	public List<Item> ungroupItems(final List<Item> itemList) {

		final List<Item> allItems = new ArrayList<>();

		for (final Item item : itemList) {

			// itemQuantity --> quantity of a type of item,
			// determine how many times that item will be added into allItems list
			// add the individual items into allItems list
			for (int j = 0;j < item.getQuantity();j++) {
				allItems.add(item);
			}
		}

		return Collections.unmodifiableList(allItems);
	}
}