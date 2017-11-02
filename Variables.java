package sat;

import java.util.HashMap;
import java.util.List;

public class Variables {
    public HashMap<Integer,List<Integer>> is_in;
    //is_in registers which clauses this Integer is in, and 2 more things.
    // One: Number of Variables in said clause.
    // Two: Is it existing in the clause as a normal?(1) or as a negated form (-1)
    public Integer represents;
    //There should never be a need to touch represents AFTER the initialisation

    public Integer TrueFinalForm;
    //The value that was SET because it appeared in a single variable clause.


    Variables(Integer Real){
        this.represents=Math.abs(Real);
        is_in = new HashMap<Integer, List<Integer>>();
    }

}
