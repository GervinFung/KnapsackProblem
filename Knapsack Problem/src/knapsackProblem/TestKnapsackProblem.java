package knapsackProblem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

public final class TestKnapsackProblem {
	public static void main(String[] args) {

		final ArrayList<Item> itemList = new ArrayList<>();

		final boolean fileNoProblem = readFile(itemList);
		//sort item according to their density (descending order)
		Collections.sort(itemList);

		if (fileNoProblem) {
			Knapsack knapsack;
			final Scanner scan = new Scanner(System.in);
			while (true) {
				// show the item list in a table (name weight value quantity)
				System.out.printf("\n%-20s%-8s%-8s%s%n", "Name", "Weight", "Value", "Quantity");
				System.out.println("--------------------------------------------------");
				for (final Item item : itemList) {
					System.out.println(item.showProperties());
				}
				System.out.println("--------------------------------------------------");
				String input;
				// knapsackCapcity --> allow user to determine the max capacity of the knapsack
				// solutionOption --> allow user to choose which solution they want to see
				int maxKnapsackCapacity, solutionOption;
				boolean invalidInput; // used to loop prompt input if invalid input is entered
				// this loop prompt for knapsack capacity until user enter a non negative
				// integer
				do {
					System.out.print("Enter the knapsack maximum capacity : ");
					input = scan.nextLine();
					invalidInput = !tryParseAsInt(input, Integer.MAX_VALUE);
				} while (invalidInput);

				maxKnapsackCapacity = Integer.parseInt(input);

				knapsack = new Knapsack(maxKnapsackCapacity, Collections.unmodifiableList(itemList));

				//find the smallest weight in the item list
				int smallestWeight = itemList.get(0).getWeight();
				for (final Item item : itemList) {
					if(item.getWeight() < smallestWeight) {
						smallestWeight = item.getWeight();
					}
				}
				if (maxKnapsackCapacity < smallestWeight) {
					System.out.println("Weight of smallest weight item : " + smallestWeight);
					System.out.println("knapsack capacity : " + maxKnapsackCapacity);
					System.out.println("no item can be added into the knapsack.");
				}
				else {
					int totalWeight = 0;
					for (final Item item : knapsack.getAllItems()) {
						totalWeight += item.getWeight();
					}
					if (maxKnapsackCapacity >= totalWeight) {
						System.out.println("total weight of all items : " + totalWeight);
						System.out.println("knapsack capacity : " + maxKnapsackCapacity);
						System.out.println("All items are taken into the knapsack.");
					}
					else {
						String solution;
						// loop the program so user do not have to re-run the program when wanted to try
						// another solution
						System.out.println("\n[ 1 ] - Dynamic Programming (bottom up approach)");
						System.out.println("[ 2 ] - Least Cost Branch and bound");
						System.out.println("[ 3 ] - Memoize (top down approach)");
						System.out.println("[ 4 ] - Brute-force technique");
						System.out.println("[ 5 ] - Genetic Programming");
						// this loop prompt for solution option until user enter 1, 2, 3, 4, 5
						
						do {
							System.out.print("Choose option (1-5) >> ");
							input = scan.nextLine();
							invalidInput = !tryParseAsInt(input, 5);
						} while (invalidInput);
						solutionOption = Integer.parseInt(input);
						// dynamic programming
						if (solutionOption == 1) {
							final DynamicProgramming solutionDP = new DynamicProgramming(knapsack);
							solution = solutionDP.toString();
							knapsack = solutionDP.solveKnapsack();
						}
						
						// branch and bound
						else if (solutionOption == 2) {
							final LeastCostBranchAndBound solutionBNB = new LeastCostBranchAndBound(knapsack);
							solution = solutionBNB.toString();
							knapsack = solutionBNB.solveKnapsack();
						}

						else if (solutionOption == 3) {
							final Memoize solutionMemoize = new Memoize(knapsack);
							solution = solutionMemoize.toString();
							knapsack = solutionMemoize.solveKnapsack();
						}
						
						// brute force technique
						else if (solutionOption == 4) {
							final BruteForce solutionBF = new BruteForce(knapsack);
							solution = solutionBF.toString();
							knapsack = solutionBF.solveKnapsack();
						}

						else {
							final GeneticProgramming solutionGP = new GeneticProgramming(knapsack);
							solution = solutionGP.toString();
							knapsack = solutionGP.solveKnapsack();
						}
						//this is used to clear the console screen, might not work for eclipse
						System.out.print("\033[H\033[2J");
						System.out.println("----------" + solution + "----------\n");
						knapsack.showItems();
						System.out.println("Maximum value : " + knapsack.getMaxValue());
						System.out.println("Total occupied weight (kg) : " + knapsack.getAllItemsWeight());
						System.out.println("Maximum knapsack capacity (kg) : " + knapsack.getMaxCapacity());
						System.out.println("Knapsack occupied percentage (%) : " + (float)knapsack.getAllItemsWeight()/knapsack.getMaxCapacity()*100);
					
						System.out.println("\nPress enter to continue...");
						scan.nextLine();
						System.out.print("\033[H\033[2J");
					}
				}
			}
		}
	}

	// check if the 2nd to 4th data in items.txt text file
	// can be parsed as integer
	private static boolean tryParseAsInt(final String data, final int max) {
		try {
			final int temp = Integer.parseInt(data);
			if (temp >= 1 && temp <= max) {
				return true;
			}
		} catch (final Exception ignored) {}
		return false;
	}

	// this method reads data from file to create item object and add to itemList
	// return true if file is read properly, return false if file is not found or content is
	// not valid
	public static boolean readFile(final ArrayList<Item> itemList) {
		final String absoluteFilePath = new File("items.txt").getAbsolutePath();
		final String excMessage = "content in \"items.txt\" should be in the following format:\nName of item/Weight/Value/Quantity\n\n";
		try {
			final Scanner scanFile = new Scanner(new File(absoluteFilePath));	
			while (scanFile.hasNext()) {
				final String[] data = scanFile.nextLine().split("/");
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
			scanFile.close();
		} catch (final FileNotFoundException exc) {
			System.out.println("File named "+ absoluteFilePath + " not found");
			return false;
		} catch (final NumberFormatException | ArrayIndexOutOfBoundsException exc) {
			System.out.println(exc.getMessage());
			return false;
		}
		return true;
	}
}
