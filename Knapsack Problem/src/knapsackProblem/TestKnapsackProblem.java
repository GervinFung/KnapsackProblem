package knapsackProblem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public final class TestKnapsackProblem {

	private final static String ITEM_TXT = "Knapsack Problem/items.txt";

	public static void main(final String[] args) {

		final List<Item> itemList = readFile();

		if (itemList.size() != 0) {
			final Scanner scan = new Scanner(System.in);
			while (true) {
				// show the item list in a table (name weight value quantity)
				System.out.printf("\n%-20s%-8s%-8s%s%n", "Name", "Weight", "Value", "Quantity");
				System.out.println("--------------------------------------------------");
				itemList.forEach(item -> System.out.println(item.showProperties()));
				System.out.println("--------------------------------------------------");
				String input;

				do {
					System.out.print("Enter the knapsack maximum capacity : ");
					input = scan.nextLine();
					if (tryParseAsInt(input, Integer.MAX_VALUE)) {break;}
				} while (true);

				final int maxKnapsackCapacity = Integer.parseInt(input);

				Knapsack knapsack = new Knapsack(maxKnapsackCapacity, Collections.unmodifiableList(itemList));

				//find the smallest weight in the item list

				final int smallestWeight = itemList.stream().mapToInt(item -> item.getWeight()).min().orElseThrow(NoSuchElementException::new);

				if (maxKnapsackCapacity < smallestWeight) {
					System.out.println("Weight of smallest weight item : " + smallestWeight);
					System.out.println("knapsack capacity : " + maxKnapsackCapacity);
					System.out.println("no item can be added into the knapsack.");
				}
				else {

					final int totalWeight = knapsack.getAllItems().stream().mapToInt(item -> item.getWeight()).sum();

					if (maxKnapsackCapacity >= totalWeight) {
						System.out.println("total weight of all items : " + totalWeight);
						System.out.println("knapsack capacity : " + maxKnapsackCapacity);
						System.out.println("All items are taken into the knapsack.");
					}
					else {
						// loop the program so user do not have to re-run the program when wanted to try
						// another solution
						System.out.println("\n[ 1 ] - Dynamic Programming (bottom up approach)");
						System.out.println("[ 2 ] - Least Cost Branch and bound");
						System.out.println("[ 3 ] - Memoize (top down approach)");
						System.out.println("[ 4 ] - Brute-force technique");
						System.out.println("[ 5 ] - Genetic Programming");
						
						do {
							System.out.print("Choose option (1-5) >> ");
							input = scan.nextLine();
							if (tryParseAsInt(input, 5)) {break;}
						} while (true);

						final AbstractKnapsackSolution abstractKnapsackSolution = createSolution(knapsack, Integer.parseInt(input));
						knapsack = abstractKnapsackSolution.solveKnapsack();
						//this is used to clear the console screen, might not work for eclipse
						System.out.print("\033[H\033[2J");
						System.out.println("----------" + abstractKnapsackSolution + "----------\n");
						knapsack.showItems();
						System.out.println("Maximum value : " + knapsack.getMaxValue());
						System.out.println("Total occupied weight (kg) : " + knapsack.getAllItemsWeight());
						System.out.println("Maximum knapsack capacity (kg) : " + knapsack.getMaxCapacity());
						System.out.println("Knapsack occupied percentage (%) : " + (float)knapsack.getAllItemsWeight()/knapsack.getMaxCapacity()*100);
					
						System.out.println("\nPress enter to continue OR x/X to exit");
						if ("X".equalsIgnoreCase(scan.nextLine())) {
							scan.close();
							return;
						}
						System.out.print("\033[H\033[2J");
					}
				}
			}
		}
	}

	private static AbstractKnapsackSolution createSolution(final Knapsack knapsack, final int num) {
		switch (num) {
			case 1:
				return new DynamicProgramming(knapsack);
			case 2:
				return new LeastCostBranchAndBound(knapsack);
			case 3:
				return new Memoize(knapsack);
			case 4:
				return new BruteForce(knapsack);
			case 5:
				return new GeneticProgramming(knapsack);
			default:
				throw new IllegalStateException("Should not reach here");
		}
	}

	private static boolean tryParseAsInt(final String data, final int max) {
		try {
			final int temp = Integer.parseInt(data);
			if (temp >= 1 && temp <= max) {
				return true;
			}
		} catch (final NumberFormatException ignored) {}
		return false;
	}

	public static List<Item> readFile() {
		final ArrayList<Item> itemList = new ArrayList<>();
		final String excMessage = "content in \"Knapsack Problem/items.txt\" should be in the following format:\nName of item/Weight/Value/Quantity\n\n";

		try (final BufferedReader reader = new BufferedReader(new FileReader(ITEM_TXT))) {

            String line;
            while ((line = reader.readLine()) != null) {
                final String[] data = line.split("/");
				if (data.length != 4) {
					throw new ArrayIndexOutOfBoundsException(excMessage);
				}
				final String itemName = data[0];
				final String itemWeight = data[1];
				final String itemValue = data[2];
				final String itemQuantity = data[3];
				for (int i = 1; i < 4; i++) {
					if (!tryParseAsInt(data[i], Integer.MAX_VALUE)) {
						throw new NumberFormatException(excMessage);
					}
				}
				itemList.add(new Item(itemName, Integer.parseInt(itemWeight), Integer.parseInt(itemValue), Integer.parseInt(itemQuantity)));
            }
			//sort item according to their density (descending order)
			Collections.sort(itemList);
			return Collections.unmodifiableList(itemList);

		} catch (final IOException e) {
			System.out.println("Error reading " + ITEM_TXT);
		}
		return Collections.emptyList();
	}
}