package main

import (
	"math/rand"
	"time"
)

type Individual struct {
	selectedItems []bool
	fitness       int
}


const GENE_SIZE = 1000
const WEIGHT_LIMIT = 300
var VALUES [GENE_SIZE]int
var WEIGHTS [GENE_SIZE]int


func init() {
	r := rand.New(rand.NewSource(time.Now().UnixNano()))
	for i := 0; i < GENE_SIZE; i++ {
		VALUES[i] = r.Intn(100)
		WEIGHTS[i] = r.Intn(100)
	}
}


func CreateRandom(r *rand.Rand) *Individual {
	ind := &Individual{
		selectedItems: make([]bool, GENE_SIZE),
	}
	for i := 0; i < GENE_SIZE; i++ {
		ind.selectedItems[i] = r.Intn(2) == 1
	}
	return ind
}


func (ind *Individual) MeasureFitness() {
	totalWeight := 0
	totalValue := 0
	for i := 0; i < GENE_SIZE; i++ {
		if ind.selectedItems[i] {
			totalValue += VALUES[i]
			totalWeight += WEIGHTS[i]
		}
	}
	if totalWeight > WEIGHT_LIMIT {
		ind.fitness = -(totalWeight - WEIGHT_LIMIT)
	} else {
		ind.fitness = totalValue
	}
}

func (ind *Individual) CrossoverWith(mate *Individual, r *rand.Rand) *Individual {
	child := &Individual{
		selectedItems: make([]bool, GENE_SIZE),
	}
	crossoverPoint := r.Intn(GENE_SIZE)
	for i := 0; i < GENE_SIZE; i++ {
		if i < crossoverPoint {
			child.selectedItems[i] = ind.selectedItems[i]
		} else {
			child.selectedItems[i] = mate.selectedItems[i]
		}
	}
	return child
}


func (ind *Individual) Mutate(r *rand.Rand) {
	mutationPoint := r.Intn(GENE_SIZE)
	ind.selectedItems[mutationPoint] = !ind.selectedItems[mutationPoint]
}
