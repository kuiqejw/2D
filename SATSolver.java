package sat;

import immutable.EmptyImList;
import immutable.ImList;
import java.util.Iterator;
import sat.env.Environment;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
import sat.formula.NegLiteral;
import sat.formula.PosLiteral;
/**
 * A simple DPLL SAT solver. See http://en.wikipedia.org/wiki/DPLL_algorithm
 */
public class SATSolver {
    /**
     * Solve the problem using a simple version of DPLL with backtracking and
     * unit propagation. The returned environment binds literals of class
     * bool.Variable rather than the special literals used in clausification of
     * class clausal.Literal, so that clients can more readily use it.
     *
     * @return an environment for which the problem evaluates to Bool.TRUE, or
     *         null if no such environment exists.
     */
    public static Environment solve(Formula formula) {
        // TODO: implement this.

        return solve(formula.getClauses(), new Environment()); //creates new immutable list using clauses from formula
    }

    /**
     * Takes a partial assignment of variables to values, and recursively
     * searches for a complete satisfying assignment.
     *
     * @param clauses
     *            formula in conjunctive normal form
     * @param env
     *            assignment of some or all variables in clauses to true or
     *            false values.
     * @return an environment for which all the clauses evaluate to Bool.TRUE,
     *         or null if no such environment exists.
     */
    //ORDER IS BOTTOMS UP DUE TO SHAUN'S INTEFERENCE
    //MINCLAUSE TAKES SMALLEST BOTTOMS UP SO IT WILL GENERALLY END 2-3
    private static Environment solve(ImList<Clause> clauses, Environment env) {
        if (clauses.isEmpty()){
            return env; //if empty clause, clause list is unsolvable. Backtrack.
        }
        
        
        int minSize = Integer.MAX_VALUE; //this is a constant. of 2^31-1
        Clause minClause = new Clause();//creates a new clause
        for (Clause c : clauses){ //for enumerated list of clauses
            if (c.size() < minSize){
                minSize = c.size();
                minClause = c;//this will keep looking for the smallest clause
            }
            if (c.size()==0)
                return null; //return false value
        }
        //now we're going into the clause! Endless Recursion!
        Literal l = minClause.chooseLiteral();//random picking of literal from within list
        /*System.out.print("CLAUSE:   ");
        System.out.print(minClause.toString());
        System.out.print("     ");
        System.out.print("Variable: ");
        System.out.print(l.getVariable().getName());
        System.out.print("    CHOSE: ");
        */
        Environment result;//this environment is currently undefined
        if (minClause.isUnit()){//if single literal, meaning a ^ (b v c), a is literal chosen
            if (l instanceof PosLiteral){
                //System.out.println("SINGLE PLUS");
                result = solve(substitute(clauses, l),env.putTrue(l.getVariable()));//positive literal substitute true value and check

            } else {
                //System.out.println("SINGLE MINUS");
                result = solve(substitute(clauses, l),env.putFalse(l.getVariable()));//false of a false is still true. Therefore, subbing in true value

            }
        } else{
            env = env.putTrue(l);
            ImList<Clause> temp = substitute(clauses,l);//go into the clause; because more than 2 now.
            
            Environment potential = solve(temp, env);
            if (potential == null){
                env = env.putFalse(l);
                return solve(substitute(clauses,l.getNegation()),env);
            }
            else{
                return potential;
            }/*
            if (l instanceof PosLiteral){
                System.out.println("MORE PLUS");
                result = solve(temp, env.putTrue(l.getVariable())); //same thing as above. create a temp clause and check if putting it positive will work
                if (result != null) return result;
                result = solve(temp, env.putFalse(l.getVariable()));
            }
            else{
                System.out.println("MORE MINUS");
                result = solve(temp, env.putFalse(l.getVariable()));//not a positive, therefore a negative. reverse of the above.
                if (result != null)
                    return result;
                result = solve(temp,env.putTrue(l.getVariable()));
            }*/
        }
        return result;            //the associated variable that we have picked
    }

    /**
     * given a clause list and literal, produce a new list resulting from
     * setting that literal to true
     *
     * @param clauses
     *            , a list of clauses
     * @param l
     *            , a literal to set to true
     * @return a new list of clauses resulting from setting l to true
     */
    private static ImList<Clause> substitute(ImList<Clause> clauses,
			Literal l) {
		ImList<Clause> output = new EmptyImList<Clause>();
		Iterator<Clause> iterator = clauses.iterator();
		while (iterator.hasNext()){
			Clause clause = iterator.next();
			if (clause.contains(l)||clause.contains(l.getNegation()))
					clause = clause.reduce(l);
			if (clause != null) output = output.add(clause);
		}
		return output;
}   
    /*
    ImList<Clause> sub = new EmptyImList<>();
    while(clauses.iterator().hasNext()){//checking to see if there is a notehr member of the clause
        Clause c = clause.iterator().next().redulce(l);
        if (c!= null) subb.add(c);
    }
    return sub;
    }
****/
}
