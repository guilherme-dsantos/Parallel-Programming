package main

import (
	"fmt"
	"math/rand"
	"time"
)

const POP_SIZE = 100000
const PROB_MUTATION = 0.5
const TOURNAMENT_SIZE = 3
const N_GENERATIONS = 500

func main() {
	r := rand.New(rand.NewSource(time.Now().UnixNano()))

	// Create the initial population
	population := make([]*Individual, POP_SIZE)
	for i := 0; i < POP_SIZE; i++ {
		population[i] = CreateRandom(r)
	}

	startTime := time.Now()
	for generation := 0; generation < N_GENERATIONS; generation++ {

		// Step 1 - Calculate Fitness
		fitnessResults := make(chan *Individual, POP_SIZE)

		for i := 0; i < POP_SIZE; i++ {
			go func(individual *Individual) {
				individual.MeasureFitness()
				fitnessResults <- individual
			}(population[i])
		}

		for i := 0; i < POP_SIZE; i++ {
       		population[i] = <-fitnessResults
   		}

		close(fitnessResults)

		// Step 2 - Print the best individual so far.
		best := bestOfPopulation(population)
		fmt.Printf("Best at generation %d with %d\n", generation, best.fitness)

		// Step 3 - Find parents to mate (cross-over)
		newPopulation := make([]*Individual, POP_SIZE)
		newPopulation[0] = best 

		crossoverResults := make(chan *Individual, POP_SIZE)

		for i := 1; i < POP_SIZE; i++ {
			go func(index int) {
				localRand := rand.New(rand.NewSource(time.Now().UnixNano() + int64(index))) //This is for more randomness, to avoid the same parents
				parent1 := tournament(population, TOURNAMENT_SIZE, localRand)
				parent2 := tournament(population, TOURNAMENT_SIZE, localRand)
				child := parent1.CrossoverWith(parent2, localRand)
				crossoverResults <- child
			}(i)
		}
		
		for i := 1; i < POP_SIZE; i++ {
			newPopulation[i] = <-crossoverResults
		}

		close(crossoverResults)

		// Step 4 - Mutate
		mutationResults := make(chan *Individual, POP_SIZE)

   		for i := 1; i < POP_SIZE; i++ {
			index := i;
      	 	go func(individual *Individual) {
				r := rand.New(rand.NewSource(time.Now().UnixNano() + int64(index))) //This is for more randomness, to avoid the same mutation
				if r.Float64() < PROB_MUTATION {
					individual.Mutate(r)
				}
				mutationResults <- individual
      		}(newPopulation[i])
   		}

		for i := 1; i < POP_SIZE; i++ {
			newPopulation[i] = <-mutationResults
		}

		close(mutationResults)
		population = newPopulation
	}

	elapsedTime := time.Since(startTime)
	fmt.Printf("[Go-routines] Finished in %.3f seconds\n", elapsedTime.Seconds())
}

func tournament(population []*Individual, tournamentSize int, r *rand.Rand) *Individual {
	
	best := population[r.Intn(POP_SIZE)]
	for i := 0; i < tournamentSize; i++ {
		other := population[r.Intn(POP_SIZE)]
		if other.fitness > best.fitness {
			best = other
		}
	}
	return best
}

func bestOfPopulation(population []*Individual) *Individual {
	
	best := population[0]
	for _, other := range population {
		if other.fitness > best.fitness {
			best = other
		}
	}
	return best
}
