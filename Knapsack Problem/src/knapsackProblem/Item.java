package knapsackProblem;

public final class Item implements Comparable<Item> {

	private final String name;

	private final int weight, value, quantity;

	public Item(final String name, final int weight, final int value, final int quantity) {
		this.name = name;
		this.weight = weight;
		this.value = value;
		this.quantity = quantity;
	}

	public int getWeight() { return this.weight;}

	public int getValue() { return this.value;}

	public int getQuantity() { return this.quantity;}

	public float getDensity() { return (float) this.value / this.weight;}

	//will be used For sorting items according to density in descending order
	//where priority is given to density, if density is the same, then item with 
	//smaller value will be prioritised, to maintain ascending order of value for item with equal density
	@Override
	public int compareTo(final Item AnotherItem) {
		if (this.getDensity() > AnotherItem.getDensity()) {
			return -1;
		}
		else if (this.getDensity() == AnotherItem.getDensity()) {
			return Integer.compare(this.getValue(), AnotherItem.getValue());
		}
		return 1;
	}

	public String showProperties() {return String.format("%-20s%-8s%-8s%s", name, weight, value, quantity);}

	@Override
	public String toString() {return String.format("%-20s%-8s%-8s", name, weight, value);}
}