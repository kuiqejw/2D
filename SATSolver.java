package sat;

import immutable.EmptyImList;
import immutable.ImList;
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
        System.out.println("SAT solver starts!!!");
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
    private static Environment solve(ImList<Clause> clauses, Environment env) {
        // TODO: implement this.
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
        Environment result;//this environment is currently undefined
        if (minClause.isUnit()){//if single literal, meaning a ^ (b v c), a is literal chosen
            if (l instanceof PosLiteral){
                result = solve(substitute(clauses, l),env.putTrue(l.getVariable()));//positive literal substitute true value and check
            } else {
                result = solve(substitute(clauses, l),env.putFalse(l.getVariable()));//false of a false is still true. Therefore, subbing in true value
            }
        } else{
            ImList<Clause> temp = substitute(clauses,l);//go into the clause; because more than 2 now. 
            if (l instanceof PosLiteral){
                result = solve(temp, env.putTrue(l.getVariable())); //same thing as above. create a temp clause and check if putting it positive will work
                if (result != null) return result;
                result = solve(temp, env.putFalse(l.getVariable()));
            }
            else{
                result = solve(temp, env.putFalse(l.getVariable()));//not a positive, therefore a negative. reverse of the above. 
                if (result != null)
                    return result;
                result = solve(temp,env.putTrue(l.getVariable()));
            }
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
    private static ImList<Clause> substitute(ImList<Clause> clauses, Literal l) {
        ImList<Clause> result = new EmptyImList<Clause>();
        for (Clause c : clauses) {
            Clause temp = c.reduce(l);//reduce is called here because literals may be empty if null, and return null respectively
            if (temp != null) {
                result = result.add(c.reduce(l));//if still not null, then add the result to what has yet to be reduced
            }
        }
        return result;
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
