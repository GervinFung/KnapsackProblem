package knapsackProblem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

public final class DynamicProgramming extends AbstractKnapsackSolution{

	private final TreeMap<Integer, Integer> bottomUpMap;

	public DynamicProgramming(final Knapsack knapsack) {
		super(knapsack);
		this.bottomUpMap = new TreeMap<>();
	}

	//construct a bottom up table that has j (maxCapacity + 1) columns and i (number of item) rows
	//each cells in the bottom up table store the maximum value when the maxCapapcity is j after
	//deciding to take or not to take the i-th item, the final cell of the table is the maximum value
	@Override
	protected int computeMaxValue() {

		//values --> the maximum values in each cells in the table
		final int[] values = new int[this.knapsack.getMaxCapacity() + 1]; 

		int i = 0;
		//from maxCapacity to the weight of the respective item (last column to the column that the i-th item can be taken)
		for(int w = this.knapsack.getMaxCapacity(); w >= this.knapsack.getAllItems().get(i).getWeight(); w--) {

			//value1 --> value of knapsack if i-th item is not taken (values[j] of previous item)
			//value2 --> value of knapsack if i-th item is taken (current item's value + 
			//maximum value when the maxCapacity - current item's weight)
			values[w] = Math.max(values[w] , this.knapsack.getAllItems().get(i).getValue() + values[w - this.knapsack.getAllItems().get(i).getWeight()]);

			//each time a new maximum value is generated, store it to a TreeMap,
			//key: maximum value
			//value: the index of the item that contributes to that maximum value
			this.bottomUpMap.putIfAbsent(values[w], i);

			if (i < this.knapsack.getAllItems().size() - 1 && w == this.knapsack.getAllItems().get(i).getWeight()) {
				//from first item to last item (first row to last row)
				i++;
				w = this.knapsack.getMaxCapacity() + 1;
			}
		}

		//return the value in the final cell (maximum value when j = maximum capacity 
		//of the knapsack and all item has been considered to be taken or not to be taken) 
		return values[this.knapsack.getMaxCapacity()];
	}
	
	@Override //determine which items that contribute to the maximum value
	protected List<Item> takeItems(int currentMaxValue) {

		final List<Item> takenItems = new ArrayList<>();

		while (currentMaxValue > 0) {

			//item that contributes to the current maximum value
			//get and add item that contribute to the current maximum value
			final Item currentItem = this.knapsack.getAllItems().get(Collections.unmodifiableMap(this.bottomUpMap).get(currentMaxValue));
			takenItems.add(currentItem);

			//decrement current maximum value by the current item's value
			//to find the next item that contribute to the maximum value
			currentMaxValue -= currentItem.getValue();
		}

		return Collections.unmodifiableList(takenItems);
	}
	@Override
	public String toString() { return "Dynamic Programming(Bottom-Up Approach)";}
}