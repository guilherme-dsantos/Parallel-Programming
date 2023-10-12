package knapsack;

import java.util.Random;

public class ParallelKnapsackGA {
	private static final int N_GENERATIONS = 500;
	private static final int POP_SIZE = 100000;
	private static final double PROB_MUTATION = 0.5;
	private static final int TOURNAMENT_SIZE = 3;
	int nCores = 2;
	int chunkSize = POP_SIZE / nCores;
	Thread[] threads = new Thread[nCores];

	private Random r = new Random();

	private Individual[] population = new Individual[POP_SIZE];

	public ParallelKnapsackGA() {
		populateInitialPopulationRandomly();
	}

	private void populateInitialPopulationRandomly() {

		/* Creates a new population, made of random individuals */
		for (int i = 0; i < POP_SIZE; i++) {
			population[i] = Individual.createRandom(r);
		}

	}

	public void run() {
		long startTime = System.nanoTime();
		for (int generation = 0; generation < N_GENERATIONS; generation++) {

			// Step1 - Calculate Fitness
			for (int t = 0; t < nCores; t++) {
				int start = chunkSize * t;
				int end = (t < nCores - 1) ? chunkSize * (t + 1) : POP_SIZE;
				threads[t] = new Thread(() -> {
					for (int i = start; i < end; i++) {
						population[i].measureFitness();
					}

				});
				threads[t].start();
			}

			for (int i = 0; i < nCores; i++) {
				try {
					threads[i].join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			// Step2 - Print the best individual so far.
			Individual best = bestOfPopulation();
			System.out.println("Best at generation " + generation + " is " + best + " with "
					+ best.fitness);

			// Step3 - Find parents to mate (cross-over)
			Individual[] newPopulation = new Individual[POP_SIZE];
			newPopulation[0] = best; // The best individual remains

			for (int t = 0; t < nCores; t++) {
				int start = (chunkSize * t == 0) ? 1 : chunkSize * t;
				int end = (t < nCores - 1) ? chunkSize * (t + 1) : POP_SIZE;
				threads[t] = new Thread(() -> {
					for (int i = start; i < end; i++) {
						// We select two parents, using a tournament.
						Individual parent1 = tournament(TOURNAMENT_SIZE, r);
						Individual parent2 = tournament(TOURNAMENT_SIZE, r);

						newPopulation[i] = parent1.crossoverWith(parent2, r);
					}
				});
				threads[t].start();
			}

			for (int i = 0; i < nCores; i++) {
				try {
					threads[i].join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			// Step4 - Mutate

			for (int t = 0; t < nCores; t++) {
				int start = (chunkSize * t == 0) ? 1 : chunkSize * t;
				int end = (t < nCores - 1) ? chunkSize * (t + 1) : POP_SIZE;
				threads[t] = new Thread(() -> {
					for (int i = start; i < end; i++) {
						if (r.nextDouble() < PROB_MUTATION) {
							newPopulation[i].mutate(r);
						}
					}
				});
				threads[t].start();
			}

			for (int i = 0; i < nCores; i++) {
				try {
					threads[i].join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			population = newPopulation;
		}
		long estimatedTime = System.nanoTime() - startTime;
		System.out.println("[Parallel] Finished in " + (double) estimatedTime /
				1_000_000_000 + " seconds");
	}

	private Individual tournament(int tournamentSize, Random r) {
		/*
		 * In each tournament, we select tournamentSize individuals at random, and we
		 * keep the best of those.
		 */
		Individual best = population[r.nextInt(POP_SIZE)];
		for (int i = 0; i < tournamentSize; i++) {
			Individual other = population[r.nextInt(POP_SIZE)];
			if (other.fitness > best.fitness) {
				best = other;
			}
		}

		return best;
	}

	private Individual bestOfPopulation() {
		/*
		 * Returns the best individual of the population.
		 */
		Individual best = population[0];
		for (Individual other : population) {
			if (other.fitness > best.fitness) {
				best = other;
			}
		}
		return best;
	}

}
