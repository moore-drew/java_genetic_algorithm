// Assignment: Genetic Algorithms
// Last modified: 1/14/2020
// Shell of a
// Java program which uses a genetic algorithm to find a solution to
// an equation in conjunctive normal form.

// Program takes as input a  string representing a formula written in 
// conjunctive normal form and prints a string of 1's and 0's meant to be the
// instantiations of the variables as true/false from left to write
// which satisfy the formula.
// 
// Use the Formula class provided to parse a formula read in from the
// command line. 
// Get the number of unique variables in the equation
// Randomly create a population of strings which assign a 0 or 1 to 
// each variable in the list. Example: String 1011
// represents an assignment where a is True, b is False,
// c is True, and d is True. (Since any formula's variables can be 
// mapped into numbers, we won't worry about fancy variable names.)
// While not done do
//     Evaluate fitness of each member of the population
//     (We'll use a fitness function which counts the 
//      number of clauses which evaluate to TRUE.)
//     If a member is "fit enough," 
//        done := true.
//     else 
//        Create a new population by
//           Selecting members to be parents based on fitness.
//           Producing offspring from those parents using crossover and mutation.

import java.io.*;
import java.util.*;

public class cnfga {
   
   // You can experiment with the mutationRate.
   private static final double mutationRate = 0.012;
   private static int verbose = 0;

   public static void main(String [] args) {

       // Set a population size (use an even number)
       // Set the maximum number of generations that
       // you wish to loop through before you give up.
       
       final int pop_size = 6;
       final int max_gens = 10;

       //argument checker
       //TODO: implement flags and arguments for manipulating
       //pop_size and max_gens from command line
       if(args.length == 0 || args.length > 2) {
	   System.out.println("Expecting argument: test formula as singular string.  ex: \"java cnfga '(-c v a) ^ b ^ (d v e) ^ -d ^ (f v c)'\"\nCan increase verbosity with second argument as '1' or '2'.  ex: \"java cnfga '(a v b) ^ (c v d)' 1\"");
	   return;
       }
       
       if(args.length == 2) {
	   if(args[1].equals("1") == true) {
	       verbose = 1;
	   }
	   else if(args[1].equals("2") == true) {
	       verbose = 2;
	   }
	   else {
	       System.out.printf("Unrecognized verbosity argument value, setting to level 1\n");
	       verbose = 1;
	   }
	   
	   System.out.printf("Verbosity set to %d...\n", verbose);
	   System.out.println("Formula input: " + args[0]);
       }
       
       Formula input_formula = new Formula(args[0]);
       
      // Create an initial population of random candidates.
      // (Use the number of unique variables in a formula to create 
      // candidates of appropriate size.)   

       Population current_gen = new Population(pop_size);
       current_gen.seedPop(input_formula.numUniqueVars);
       
      //genNum = 1;            
      //while( the fittest candidate is not fit enough and
      //       genNum < maxGens ){

       int gen_num = 0;
       Candidate fittest = current_gen.getFittest(input_formula);

       if(verbose > 0) {
	   System.out.printf("Interpreted clauses: %s\nUnique vars: %d\nClauses: %d\n", input_formula.clauses, input_formula.numUniqueVars, input_formula.clauses.size());
	   System.out.printf("pop_size: %d\nmax_gens: %d\n", pop_size, max_gens);
	   
	   System.out.println("\nInitial population:");
	   for(int i = 0; i < current_gen.size(); i++) {
	       System.out.printf("\t");
	       current_gen.getCandidate(i).printCandidate();
	       System.out.printf("\n");
	   }
	   
	   System.out.printf("\nFittest is %d, ", fittest.getFitness(input_formula));
	   fittest.printCandidate();
	   System.out.printf("...");
       }
       
       while(gen_num < max_gens && fittest.getFitness(input_formula) != input_formula.clauses.size()) { 
	   System.out.printf("\nNot fit enough (%d > %d)\n\nCreating new generation...\n\n", input_formula.clauses.size(), fittest.getFitness(input_formula));
	   
	   // Create a new population:
      
	   // Since we are creating two new candidates from two
	   //   randomly-selected candidates, we'll only have to
	   //   loop for 
	   //   int halfPop = popSize / 2
	   //   to replace the entire population
	   Population new_gen = new Population(pop_size);
	   Candidate parent1, parent2;
	   
	   // for(int i = 0; i < halfPop; i++){
	   for(int i = 0; i < pop_size / 2; i++) {
	       System.out.printf("\tSelecting parents %d...\n", i);
	       
	       // Select two candidates; remember, the probability of 
	       //   a candidate's selection is weighted by its fitness.
	        if(verbose > 1) {
		   System.out.printf("\tParent1:\n");
	       }
		
	       parent1 = selectParent(current_gen, input_formula);
	       
	       //TODO: should the second parent be selected with
	       //current_gen without the first parent?  if not,
	       //add otherCandidates(candidate) method that
	       //returns an instance of current_gen with
	       //candidate removed from candidates[] and
	       //properly resized.
	        if(verbose > 1) {
		   System.out.printf("\tParent2:\n");
	       }
	       
	       parent2 = selectParent(current_gen, input_formula);

	       while(parent1.equals(parent2) == true) {
		   parent2 = selectParent(current_gen, input_formula);
	       }

	       if(verbose > 0) {
		   System.out.printf("\n\tParents selected:\n\t\t");
		   parent1.printCandidate();
		   System.out.printf("\n\t\t");
		   parent2.printCandidate();
		   System.out.printf("\n");
	       }
	       
	       // Use crossover to create two new candidates
	       Candidate child1 = crossover(parent1, parent2);
	       Candidate child2 = crossover(parent1, parent2);
	       
	       // If a randomly generated number is less than
	       // the mutation rate, mutate the candidate by
	       // flipping a random bit.
	       Random rand = new Random();
	       double mut_roll = rand.nextDouble();
	       if(verbose > 1) {
		   System.out.printf("\n\t\tChild 1 mutation roll: %f", mut_roll);
	       }
	       if(mut_roll < mutationRate) {
		   mutate(child1);
	       }

	       mut_roll = rand.nextDouble();
	       if(verbose > 1) {
		   System.out.printf("\n\t\tChild 2 mutation roll: %f", mut_roll);
	       }
	       if(mut_roll < mutationRate) {
		   mutate(child2);
	       }
	       
	       // Add the new candidates to the new population.
	       if(verbose > 0) {
		   System.out.printf("\n\tChildren:\n\t\t");
		   child1.printCandidate();
		   System.out.printf("\n\t\t");
		   child2.printCandidate();
		   System.out.println("\n");
	       }

	       new_gen.saveCandidate(i * 2, child1);
	       new_gen.saveCandidate(i * 2 + 1, child2);
	   //  }    
	   }
	   current_gen = new_gen;
	   new_gen = null;
	   
	   if(verbose > 0) {
	       System.out.println("\nNew generation:");
	       for(int i = 0; i < current_gen.size(); i++) {
		   System.out.printf("\t");
		   current_gen.getCandidate(i).printCandidate();
		   System.out.printf("\n");
	       }
	   }

	   fittest = current_gen.getFittest(input_formula);
	   
	   if(verbose > 0) {
	       System.out.printf("\nFittest is %d, ", fittest.getFitness(input_formula));
	       fittest.printCandidate();
	       System.out.printf("...");
	   }
	   // Get the new fittest candidate
	   // genNum++;
	   gen_num++;
	   
	   
       // }
       }
      // If you found a fit candidate, print out
      // the solution; else, print "Solution not found."

       if(fittest.getFitness(input_formula) == input_formula.clauses.size()) {
	   System.out.printf("\nFit candidate found after %d generations: ", gen_num++);
	   fittest.printCandidate();
	   System.out.println();
       }
       else {
	   System.out.printf("\n%d generations calculated, fittest possible candidate not found.", max_gens);
       }
       
   }
    
    //NOTE: implemented this method so that the order of candidates does
    //not matter.  having higher fitness candidates at beginning of
    //pop.candidates skews candidate's natural selection if only looking
    //for first 'true' return from selectionWheel() (e.g., if a
    //lowest fitness and highest fitness candidate were both going to
    //roll a 'true' in a given use of this method, the main factor
    //of selection becomes their index in the array, not their fitness).
    //to counteract this, i have written this method to roll all
    //candidates, select those that rolled 'true', and then continuously
    //roll them until only one remains.  this holds true to their fitness
    //-based rolls being the only factor of selection.  however, this may
    //lead to a resulting selection curve that is more biased toward the
    //higher fitness, as the result is a compound curve of how many
    //iterations were held.  perhaps a better method would be to roll the
    //initial candidates n times until only one returns a 'true'?  that
    //could possibly take an immense amount of rolls until that state is
    //met, though.
   private static Candidate selectParent(Population pop, Formula formula) {
        // Returns a random candidate from the population
       boolean pass = false;
       int iter = 0;
       ArrayList<Candidate> potential_parents = new ArrayList<Candidate>();
       ArrayList<Candidate> next_iter = new ArrayList<Candidate>();
       for(Candidate candidate : pop.candidates) {
	   potential_parents.add(candidate);
       }
       //System.out.println(potential_parents.toString());

       while(potential_parents.size() != 1) {
	   if(verbose > 1) {
	       System.out.printf("\n\n\t\tSelection iteration %d...\n\n", iter);
	   }
	   for(Candidate candidate : potential_parents) {
	       //candidate.printCandidate();
	       if(selectionWheel(candidate, formula, formula.clauses.size()) == true) {
		   if(pass == false) {
		       pass = true;
		   }
		   next_iter.add(candidate);
	       }
	   }
	   if(next_iter.size() != 0) {
	       potential_parents = next_iter;
	       next_iter = new ArrayList<Candidate>();
	   }
	   pass = false;
	   iter++;
       }
       return potential_parents.get(0);
   }
   
    
    private static Boolean selectionWheel(Candidate candidate, Formula formula,
         int optimalFitNum){
        // Uses the optimal fitness number, the 
        // fitness of the candidate, and percentages set in the 
        // "odds" array
        // to decide whether to keep a candidate.
        // Return True if candidate is to be kept; false otherwise.
        
        //odds[] represents the categories of the chances 
        //  of a candidate being chosen. A candidate in the first category
        //  has a 5% chance of being selected, in the second -- 10%, etc.
        int[] odds = {5, 10, 13, 32, 40};
	
        // Normally, we expect fitness scores to be larger than 
        // the number of categories given in odds.length,
        // so we need a way to map them into these categories;
        // if the optimal fitness is less than the number of categories
        // in odds, we will simply
        // return true and keep the candidate.     
        // Otherwise,  
        // find groupNum -- the proper index into the odds 
        // array based on fitness; the best fit candidates
        // should have the largest odds of success.

	if(optimalFitNum < odds.length) {
	    return true;
	}

	float rel_fitness = ((float)candidate.getFitness(formula) / optimalFitNum);
	int category = (int) (rel_fitness * odds.length) - 1;
	Random r = new Random();
	int roll = r.nextInt(100); //0-99

	if(verbose > 1) {
	    System.out.printf("\t\tCandidate: ");
	    candidate.printCandidate();
	    System.out.printf("\n\t\t\trel_fitness: %.2f, odds[%d]: %d, roll: %d, ", rel_fitness, category, odds[category], roll);
	}
	if(roll < odds[category]) {
	    if(verbose > 1) {
		System.out.print(" true\n");
	    }
	    return true;
	}
	if(verbose > 1) {
	    System.out.print(" false\n");
	}
	return false;
    }
    
    
    private static Candidate crossover(Candidate parent1, Candidate parent2){
      // Uses standard midpoint crossover to create a new candidate.
      // If the formula has an odd number of variables,
      // the midpoint can be approximated with truncation.
	int length = parent1.getLength();
	Candidate child = new Candidate(length);

	//random to choose which order the split should be done,
	//(e.g. parent1 = '1111', parent2 = '0000', if true,
	//child = '1100', else '0011')
	Random rand = new Random();
	if(rand.nextBoolean() == false) {
	    Candidate temp = parent1;
	    parent1 = parent2;
	    parent2 = temp;
	    temp = null;
	}

	//determining mid_limit based on odd or even length
	int mid_limit = length / 2;
	if(length % 2 == 1) {
	    mid_limit++;
	}

	int i = 0;
	while(i < mid_limit) {
	    child.setValue(i, parent1.getValue(i));
	    i++;
	}

	while(i < length) {
	    child.setValue(i, parent2.getValue(i));
	    i++;
	}

	return child;
    }
    
    
    private static void mutate(Candidate candidate) {
	// Flip a random bit in the candidate.
	Random rand = new Random();
	int bit = rand.nextInt(candidate.getLength());
	if(verbose > 0) {
	    System.out.printf("\n\t\tChild mutated bit %d...\n\t\t", bit);
	    candidate.printCandidate();
	    System.out.printf(" --> ");
	}

	if(candidate.getValue(bit) == true) {
	    candidate.setValue(bit, false);

	    if(verbose > 0) {
		candidate.printCandidate();
		System.out.println();
	    }
	}
	else {
	    candidate.setValue(bit, true);

	    if(verbose > 0) {
		candidate.printCandidate();
		System.out.println();
	    }
	}
    }
    
}
