// Shell for the Candidate class of the GA assignment.
// Last Modified: 1/16/20

import java.io.*;
import java.util.BitSet;
import java.util.Random;

// Since we are trying to focus on the GA aspect of the
// project, we simply associate variable 'a' with index 0
// in the candidate. Subsequent indices represent variables
// 'b', 'c', etc.

public class Candidate {
    
    private BitSet truthVals;
    private int numVals;
    
    public Candidate(int length){
       truthVals = new BitSet(length);
       numVals = length;
    }
    
    public void seedCandidate(){
    // Randomly sets bits in the candidate
	Random r = new Random();
	for(int i = 0; i < numVals; i++) {
	    truthVals.set(i, r.nextBoolean());
	}
    }
    
    public void setValue(int index, boolean val){
    // Sets the bit at the given index to correspond to val.
	truthVals.set(index, val);
    }
    
    public boolean getValue(int index){
      // Returns the value at a given index
	return truthVals.get(index);
    }
    
    public int getLength(){
      // Returns the length of the Candidate
	return numVals;
    }

    /*
    public int getFitness(Formula formula) { //TODO: use the HashSet to your advantage so you don't need the assumed input variable rules
      // Returns the fitness of a candidate; this is the number of clauses
      // in a formula made true by the candidate.
	int fitness = 0;
	//this.printCandidate();
	
	//iterate through clauses; if find true var, fitness++
	//and move to next clause; if no true var, move
	//to next clause.
	for(int i = 0; i < formula.clauses.size(); i++) {
	    // System.out.println(formula.clauses.get(i));
	    for(String var : formula.clauses.get(i)) {
		//System.out.println(var);
		if(var.charAt(0) == '-') {
		    int char_val = var.charAt(1) - 'a';
		    if(truthVals.get(char_val) == false) {
			//System.out.println("true");
			fitness++;
			break;
		    }
		}
		else {
		    int char_val = var.charAt(0) - 'a';
		    if(truthVals.get(char_val) == true) {
			//System.out.println("true");
			fitness++;
			break;
		    }
		}
		//System.out.println("false");
	    }
	}
	return fitness;
    }
    */

    ///*
    public int getFitness(Formula formula) {
	int a_char = 97;
	int fitness = 0;
	String var;

	for(int i = 0; i < formula.clauses.size(); i++) {
	    for(int j = 0; j < numVals; j++) {
		if(getValue(j) == true) {
		    var = new String("" + (char) (a_char + j));
		    //System.out.println(var);
		}
		else {
		    var = new String("-" + (char) (a_char + j) );
		    //System.out.println(var);
		}
		if(formula.clauses.get(i).contains(var) == true) {
		    fitness++;
		    break;
		}
	    }
	}
	return fitness;
    }
    //*/
    
    public void printCandidate(){
      // Prints out the candidate
	
        for(int i = 0; i < numVals; i++) {
	    if(truthVals.get(i) == true) {
	        System.out.print("1");
	    }
	    else {
		System.out.print("0");
	    }
	}
	//System.out.println();
    }
}
