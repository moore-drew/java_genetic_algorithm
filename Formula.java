// Assignment: Genetic Algorithms
// Last Modified: 1/16/20
// Use this class to create a data structure from the input which
// allows for quick access and evaluation of a formula.
// Input must be a string representing a formula in conjunctive normal form.
// There is no error checking; correct input is expected.
// Variable names are limited to a single, lowercase letter and
// cannot include 'v'. 
// - represents negation, v represents disjunction, ^ represents conjunction
// 
// Examples:
//   input: "(a v b) ^ (b v c) ^ d"
//     formula.clauses == [[b, a], [b, c], [d]]
//     formula.numUniqueVars == 4
//   input: "a ^ (b v c v -d)"
//     formula.clauses == [[a], [-d, b, c]]
//   input: "a"
//     formula.clauses == [[a]]
//   input: "(-b v c) ^ a"
//     formula.clauses == [[-b, c], [a]]
//   Note that the "outer" list is an array list, and the "inner" lists are 
//   HashSets. Thus, the order of the variables in the HashSets is not set. 
//   Each of the elements in the HashSet is a string; e.g., b in the first 
//   formula is really "b", while -b in the last formula is "-b".

import java.io.*;
import java.util.*;

public class Formula {

  public ArrayList<HashSet<String>> clauses = new ArrayList<HashSet<String>>();
      // Each clause is represented by a hash set of literals, Strings of 
      // form "t" or "-t", where t is the variable name.
      
  public int numUniqueVars = 0; // Number of unique variables in the formula.
  
  public Formula(String input){
     // Creates an ArrayList of HashSets which represents a
     // formula by a collection of clauses. Each clause is
     // a HashSet of strings which are either the variable
     // names or '-' followed by the variable name.
     // Thus, a formula can be read as the conjunction of
     // the clauses which reprent a disjunction of literals.
    boolean negative = false;
    HashSet<String> clause = new HashSet<String>();
    int clauseNum = 0;
    
    // Create a HashSet to count the number of distinct variables
    // in the input formula
    HashSet<Character> uniqueVars = new HashSet<Character>(); 
      
    for(int i = 0; i < input.length(); i++){        
      // add elements to the hash set    
      char variable = input.charAt(i);
      String strVar = Character.toString(variable);
      String negVar = " ";
      if( variable == '^' ){
        // add the created clause to formula
        clauses.add(clause);
        clauseNum++;
        // create new clause to use for the clause after the ^
        clause = new HashSet<String>();
      }  
      else if( (variable != '(') & (variable != ')') & 
               (variable != ' ') & (variable != 'v')) {
        if( variable == '-' ){
          negative = true;
        }
        else {
          if( negative ){
            negVar = "-" + strVar;
            clause.add(negVar);
          }
          else {
            clause.add(strVar);
          }
          negative = false;
          uniqueVars.add(variable);
        }
      }
        
    }
    clauses.add(clause);
    numUniqueVars = uniqueVars.size();
  }
    /*
  public static void main(String[] args) {
    Formula test = new Formula("a ^ b");
    System.out.println(test.clauses);
    test = new Formula("(a v b) ^ (b v c)");
    System.out.println(test.clauses);
  }
    */
}


