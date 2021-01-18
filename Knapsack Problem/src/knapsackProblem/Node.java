package knapsackProblem;

public class Node {

	private final float upperBound, lowerBound, cumulativeValue, cumulativeWeight;

	private final int level;

	private final boolean isSelected;

	private Node(final float upperBound, final float lowerBound, final float cumulativeValue, final float cumulativeWeight, final int level, final boolean isSelected) {
		this.cumulativeValue = cumulativeValue;
		this.cumulativeWeight = cumulativeWeight;
		this.upperBound = upperBound;
		this.lowerBound = lowerBound;
		this.level = level;
		this.isSelected = isSelected;
	}

	public float getUpperBound() { return this.upperBound; }
	public float getLowerBound() { return this.lowerBound; }
	public float getCumulativeValue() { return this.cumulativeValue; }
	public float getCumulativeWeight() { return this.cumulativeWeight; }
	public int getNodeLevel() { return this.level; }
	public boolean getNodeIsSelected() { return this.isSelected; }

	public final static class NodeBuilder {

		private float upperBound, lowerBound, cumulativeValue, cumulativeWeight;

		private int level;
	
		private boolean isSelected;

		public NodeBuilder () {
			this.cumulativeValue = 0;
			this.cumulativeWeight = 0;
			this.upperBound = 0;
			this.lowerBound = 0;
			this.level = 0;
			this.isSelected = false;
		}

		public NodeBuilder (final Node anotherNode) {
			this.cumulativeValue = anotherNode.cumulativeValue;
			this.cumulativeWeight = anotherNode.cumulativeWeight;
			this.upperBound = anotherNode.upperBound;
			this.lowerBound = anotherNode.lowerBound;
			this.level = anotherNode.level;
			this.isSelected = anotherNode.isSelected;
		}

		public NodeBuilder setUpperBound(final float upperBound) {
			this.upperBound = upperBound;
			return this;
		}

		public NodeBuilder setLowerBound(final float lowerBound) {
			this.lowerBound = lowerBound;
			return this;
		}

		public NodeBuilder setCumulativeValue(final float cumulativeValue) {
			this.cumulativeValue = cumulativeValue;
			return this;
		}

		public NodeBuilder setCumulativeWeight(final float cumulativeWeight) {
			this.cumulativeWeight = cumulativeWeight;
			return this;
		}

		public NodeBuilder setLevel(final int level) {
			this.level = level;
			return this;
		}

		public NodeBuilder setIsSelected(final boolean isSelected) {
			this.isSelected = isSelected;
			return this;
		}

		public Node build() {
			return new Node(this.upperBound, this.lowerBound, this.cumulativeValue, this.cumulativeWeight, this.level, this.isSelected);
		}
	}
}