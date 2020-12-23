package knapsackProblem;

public class Node {

	private final float upperBound, lowerBound, cumulativeValue, cumulativeWeight;

	private final int level;

	private final boolean isSelected;
	
	public Node() {
		this.cumulativeValue = 0;
		this.cumulativeWeight = 0;
		this.upperBound = 0;
		this.lowerBound = 0;
		this.level = 0;
		this.isSelected = false;
	}

	public Node(final float upperBound, final float lowerBound, final float cumulativeValue, final float cumulativeWeight, final int level, final boolean isSelected) {
		this.cumulativeValue = cumulativeValue;
		this.cumulativeWeight = cumulativeWeight;
		this.upperBound = upperBound;
		this.lowerBound = lowerBound;
		this.level = level;
		this.isSelected = isSelected;
	}

	public Node(final Node anotherNode, final float bound) {
		this.cumulativeValue = anotherNode.cumulativeValue;
		this.cumulativeWeight = anotherNode.cumulativeWeight;
		this.upperBound = bound;
		this.lowerBound = bound;
		this.level = anotherNode.level;
		this.isSelected = anotherNode.isSelected;
	}

	public Node(final Node anotherNode) {
		this.cumulativeValue = anotherNode.cumulativeValue;
		this.cumulativeWeight = anotherNode.cumulativeWeight;
		this.upperBound = anotherNode.upperBound;
		this.lowerBound = anotherNode.lowerBound;
		this.level = anotherNode.level;
		this.isSelected = anotherNode.isSelected;
	}

	public float getUpperBound() { return this.upperBound; }
	public float getLowerBound() { return this.lowerBound; }
	public float getCumulativeValue() { return this.cumulativeValue; }
	public float getCumulativeWeight() { return this.cumulativeWeight; }
	public int getNodeLevel() { return this.level; }
	public boolean getNodeIsSelected() { return this.isSelected; }
}