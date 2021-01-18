package knapsackProblem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class LeastCostBranchAndBound extends AbstractKnapsackSolution {

	//boolean array to store at every index if the element is included or not 
	private final boolean finalPathOfItemsTaken[];
	private final List<Item> copyOfItems;

	public LeastCostBranchAndBound(final Knapsack knapsack) {
		super(knapsack);
		this.finalPathOfItemsTaken = new boolean[this.knapsack.getAllItems().size()];
		//make a copy to avoid affecting the real items
		this.copyOfItems = new ArrayList<>(this.knapsack.getAllItems());
		//need to override sort due to arrangement of items with same density
		//which has differences on the way algorithms work, as Branch and Bound sort according to density of items, if item has same density
		//item with higher value must be at the top, this sorting does that
		Collections.sort(this.copyOfItems, new Comparator<Item>(){
			@Override
			public int compare(final Item item1, final Item item2) {
				if (item1.getDensity() > item2.getDensity()) {
					return -1;
				}
				else if (item1.getDensity() == item2.getDensity()) {
					return -Integer.compare(item1.getValue(), item2.getValue());
				}
				else {
					return 1;
				}
			}
		});
	}

	private float computeBound(final Node current, final boolean isSelected, final boolean isUpperBound) {
		float value, weight;
		if (isSelected) {
			value = current.getCumulativeValue() - this.copyOfItems.get(current.getNodeLevel()).getValue();
			weight = current.getCumulativeWeight() + this.copyOfItems.get(current.getNodeLevel()).getWeight();
		}
		else {
			value = current.getCumulativeValue();
			weight = current.getCumulativeWeight();
		}

		for (int i = current.getNodeLevel() + 1; i < this.copyOfItems.size(); i++) {
			final Item item = this.copyOfItems.get(i);
			if (weight + item.getWeight() <= this.knapsack.getMaxCapacity()) {
				weight += item.getWeight();
				value -= item.getValue();
			}
			else {
				if (isUpperBound) {
					value -= (float)(this.knapsack.getMaxCapacity() - weight) / item.getWeight() * item.getValue();
				}
				break;
			}
		}
		return value;
	}

	private Node updateNode(final Node current, final boolean isSelected) {
		if (isSelected) {
			final float cumulativeWeight = current.getCumulativeWeight() + this.copyOfItems.get(current.getNodeLevel()).getWeight();
			final float cumulativeValue = current.getCumulativeValue() - this.copyOfItems.get(current.getNodeLevel()).getValue();
			return new Node.NodeBuilder().setUpperBound(computeBound(current, isSelected, true))
										.setLowerBound(computeBound(current, isSelected, false))
										.setCumulativeValue(cumulativeValue)
										.setCumulativeWeight(cumulativeWeight)
										.setLevel(current.getNodeLevel() + 1)
										.setIsSelected(isSelected)
										.build();
		}
		return new Node.NodeBuilder().setUpperBound(computeBound(current, isSelected, true))
									.setLowerBound(computeBound(current, isSelected, false))
									.setCumulativeValue(current.getCumulativeValue())
									.setCumulativeWeight(current.getCumulativeWeight())
									.setLevel(current.getNodeLevel() + 1)
									.setIsSelected(isSelected)
									.build();
	}

	@Override
	protected int computeMaxValue() {

        Node takeItemNode = new Node.NodeBuilder().build(), noTakeItemNode = new Node.NodeBuilder().build();
		float minLowerBound = 0, finalLowerBound = Integer.MAX_VALUE;

		//boolean array to store at every index if the element is included or not 
        final boolean pathOfItemTaken[] = new boolean[this.copyOfItems.size()];
		
		//Priority queue to store elements based on lower bounds 
        final PriorityQueue<Node> nodesPriorityQueue = new PriorityQueue<Node>(new Comparator<Node>() {
			@Override
			public int compare(final Node nodeA, final Node nodeB) {
				return nodeA.getLowerBound() > nodeB.getLowerBound() ? 1 : -1;
			}
		});
	
		// Insert a dummy Node 
		nodesPriorityQueue.add(new Node.NodeBuilder().build());
	
		while (!nodesPriorityQueue.isEmpty()) {
			//Extract the peek element from the priority queue and assign it to the current node
			final Node current = nodesPriorityQueue.poll();

			//If the upper bound of the current node is less than minLowerBound
			//which is the minimum lower bound of all the nodes explored, then there is no point of exploration, hence continue with extract element
			if (current.getUpperBound() > minLowerBound || current.getUpperBound() >= finalLowerBound) { continue;}

			if (current.getNodeLevel() != 0) {
				pathOfItemTaken[current.getNodeLevel() - 1] = current.getNodeIsSelected();
			}

			if (current.getNodeLevel() == this.copyOfItems.size()) {
				if (current.getLowerBound() < finalLowerBound) {
					// Reached last level 
					for (int i = 0; i < this.copyOfItems.size(); i++) {
						this.finalPathOfItemsTaken[i] = pathOfItemTaken[i];
					}
					finalLowerBound = current.getLowerBound();
				}
				continue;
			}
			// noTakeItemNode -> Exludes current item 
			noTakeItemNode = updateNode(current, false);

			if (current.getCumulativeWeight() + this.copyOfItems.get(current.getNodeLevel()).getWeight() <= this.knapsack.getMaxCapacity()) {
				// takeItemNode -> includes current item
				takeItemNode = updateNode(current, true);
			}

			else {
				// Stop the takeItemNode from getting added to the priority queue
				takeItemNode = new Node.NodeBuilder(takeItemNode).setUpperBound(1).setLowerBound(1).build();
			}
			// Update minLowerBound
			minLowerBound = Math.min(minLowerBound, takeItemNode.getLowerBound());
			minLowerBound = Math.min(minLowerBound, noTakeItemNode.getLowerBound());
	
			if (minLowerBound >= takeItemNode.getUpperBound()) {
                nodesPriorityQueue.add(new Node.NodeBuilder(takeItemNode).build());
            }
			if (minLowerBound >= noTakeItemNode.getUpperBound()) {
                nodesPriorityQueue.add(new Node.NodeBuilder(noTakeItemNode).build());
            }
		}
		return (int)-finalLowerBound;
	}

	@Override
	protected List<Item> takeItems(final int maxValue) {
        final List<Item> itemsTaken = new ArrayList<>();
        for (int i = 0; i < this.finalPathOfItemsTaken.length; i++) {
			//if the boolean at position i is true, the item that has the same index number with the
			//boolean value is taken into the knapsack, add the item into the takenItem list
			if (this.finalPathOfItemsTaken[i]) {
				itemsTaken.add(this.copyOfItems.get(i));
			}
		}
		return Collections.unmodifiableList(itemsTaken);
	}

	@Override
	public String toString() {return "Least Cost Branch & Bound";}
}