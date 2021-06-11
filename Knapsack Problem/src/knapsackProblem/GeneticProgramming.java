package knapsackProblem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Map;
import java.util.TreeMap;

public final class GeneticProgramming extends AbstractKnapsackSolution {

	private final int numberOfPopulations, numberOfGenerations, numberOfChromosomes;
	private final double mutationMaxPossiblityRange;
	private final StringBuilder bestOffSpring;
	private int bestValueObtained;

	//this solution might not generate the optimal solutions, as there's a chance of missing out the solution
	//nonetheless it still can provide near optimumr result
	public GeneticProgramming(final Knapsack knapsack) {
		super(knapsack);
		this.numberOfGenerations = 1000;
		this.mutationMaxPossiblityRange = 0.2f;
		this.numberOfChromosomes = this.knapsack.getAllItems().size();
		this.numberOfPopulations = this.knapsack.getAllItems().size() * 2;
		this.bestValueObtained = 0;
		this.bestOffSpring = new StringBuilder();
	}

	//create random populations
	private List <String> createPopulation() {
		//store population
		final List <String> immutablePopulations = new ArrayList<>();
		//stringBuilder to store 1 population
		final StringBuilder population = new StringBuilder(this.numberOfChromosomes);
		for (int i = 0; i < this.numberOfPopulations; i++) {
			for (int j = 0; j < this.numberOfChromosomes; j++) {
				//randomly produce offspring based on 0 OR 1
				population.append(ThreadLocalRandom.current().nextInt(0, 2));
			}
			//add population to list
			immutablePopulations.add(population.toString());
			//renew string builder
			population.setLength(0);
		}
		return Collections.unmodifiableList(immutablePopulations);
	}

	private Map <String, Integer> calculateFitness(final List<String> populations) {
		//store population and their fitness
		final TreeMap <String, Integer> immutableFitnessScore = new TreeMap<>();
		for (final String string : populations) {
			int weight = 0, value = 0;
			for (int i = 0; i < string.length(); i++) {
				//the fitness score is based on number of '1' in a string
				if (string.charAt(i) == '1') {
					weight += this.knapsack.getAllItems().get(i).getWeight();
					value += this.knapsack.getAllItems().get(i).getValue();
				}
			}
			//add into map if and only if the weight is greater than 0 and does not exceed knapsack capacity
			if (weight != 0 && weight <= this.knapsack.getMaxCapacity()) {
				immutableFitnessScore.put(string, value);
			}
		}
		//immutableFitnessScore.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
		return Collections.unmodifiableMap(immutableFitnessScore);
	}

	private List <String> crossOver(final TreeMap<String, Integer> fitnessScore) {
		//if at least 2 parents exists
		if (fitnessScore.size() >= 2) {
			//list to store all offSpring produced by 2 parents
			final List <String> possibleOffSpring = new ArrayList<>();
			//List of possible parents based on selection via fitness score
			final List <String> possibleParents = new ArrayList<>(fitnessScore.keySet());
			final String paternal = possibleParents.get(fitnessScore.size() - 1);
			final String maternal = possibleParents.get(fitnessScore.size() - 2);
			final StringBuilder offSpringBuilder = new StringBuilder(paternal.length());

			for (int i = 0; i < this.numberOfPopulations; i++) {
				final int splitSize = ThreadLocalRandom.current().nextInt(0, paternal.length());
				offSpringBuilder.append(paternal, 0, splitSize);
				offSpringBuilder.append(maternal, splitSize, paternal.length());
				possibleOffSpring.add(mutation(offSpringBuilder.toString()));
				offSpringBuilder.setLength(0);
			}
			return Collections.unmodifiableList(possibleOffSpring);
		}
		//otherwise produce populations until at least 2 parents exists
		return this.createPopulation();
	}

	private String mutation(final String offSpring) {
		//generate mutation possibility
		final double mutatePossibility = ThreadLocalRandom.current().nextDouble(0, 1);
		if (mutatePossibility <= this.mutationMaxPossiblityRange) {
			//builder to store latest mutated offspring
			final StringBuilder latestOffSpring = new StringBuilder(offSpring);
			final int index = ThreadLocalRandom.current().nextInt(0, offSpring.length());
			//began altering the genes
			latestOffSpring.setCharAt(index, offSpring.charAt(index) == '1' ? '0' : '1');
			return latestOffSpring.toString();
		}
		//if out of range just return the original offspring
		return offSpring;
	}

	private int findBestFitnessScore(final Map <String, Integer> fitnessScore) {
		//for all fitness score obtained, get the best one
		for (final String combination : fitnessScore.keySet()) {
			if (fitnessScore.get(combination) > this.bestValueObtained) {
				this.bestValueObtained = fitnessScore.get(combination);
				//get the best offspring gene
				this.bestOffSpring.setLength(0);
				this.bestOffSpring.append(combination);
			}
		}
		return this.bestValueObtained;
	}

	@Override
	protected int computeMaxValue() {
		TreeMap<String, Integer> fitnessScore = new TreeMap<>(this.calculateFitness(this.createPopulation()));
		int max = this.findBestFitnessScore(fitnessScore);
		for (int i = 0; i < this.numberOfGenerations; i++) {
			fitnessScore = new TreeMap<>(this.calculateFitness(this.crossOver(fitnessScore)));
			max = this.findBestFitnessScore(fitnessScore);
		}
		return max;
	}

	@Override
	protected List<Item> takeItems(final int currentMaxValue) {

		final List<Item> takenItems = new ArrayList<>();

		for (int i = 0; i < this.bestOffSpring.length(); i++) {
			
			//if the character at j position is 1, the item that has the same index number with the
			//character is taken into the knapsack, add the item into the takenItem list
			if (this.bestOffSpring.charAt(i) == '1') {
				takenItems.add(this.knapsack.getAllItems().get(i));
			}
		}

		return Collections.unmodifiableList(takenItems);
	}

	@Override
	public String toString() { return "Genetic Programming";}
}