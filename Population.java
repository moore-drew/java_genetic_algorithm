// Shell for the Population class of the GA assignment.
// Last modified: 1/16/20
public class Population {

    Candidate[] candidates;

    
    // Constructor
    public Population(int populationSize) {
        candidates = new Candidate[populationSize];

    }
    
    public void seedPop(int candidateLength) {
        // Seed a population with candidates of length candidateLength
	for(int i = 0; i < candidates.length; i++) {
	    candidates[i] = new Candidate(candidateLength);
	    candidates[i].seedCandidate();
	}
         
    }      

 
    public Candidate getCandidate(int index) {
        // Return the candidate at a given index
	return candidates[index];
    }

    public Candidate getFittest(Formula formula) {
      // Returns the fittest candidate of the population with 
      // respect to a formula
	Candidate top_candidate = candidates[0];
	int top_fitness = top_candidate.getFitness(formula);
	
	if(top_fitness == formula.clauses.size()) { //assuming can't go over
	    return top_candidate;
	}
	
	int fitness;
	for (int i = 1; i < candidates.length; i++) {
	    fitness = candidates[i].getFitness(formula);
	    if (fitness > top_fitness) {
		top_candidate = candidates[i];
		top_fitness = fitness;
		if(top_fitness == formula.clauses.size()) { //assuming can't go over
		    break;
		}
	    }
	}
	return top_candidate;
    }

  
    public int size() {
       // Returns the population size
	return candidates.length;
    }

    public void saveCandidate(int index, Candidate candidate) {
       // Adds a candidate to the population
	candidates[index] = candidate;
    }
    
    /*
    public static void main(String [] args) {
	Population test = new Population(5);
	test.seedPop(4);
	for(int i = 0; i < test.size(); i++) {
	    test.getCandidate(i).printCandidate();
	}
    }
    */
}
