package sat;

import java.util.ArrayList;
import java.util.List;

public class Variableinitnot1 {
    private Variableinitnot1(){}
    public static void Makevars(VariablesList passedin, Integer ClauseNumber, Integer K){
        //IE. for each clause processed, IF CLAUSE IS NOT A SINGLE VARIABLE CLAUSE.
        //K refers to the variable number.

        //If the variable has never been initialised, Create a new entry

        passedin.Lister.put(Math.abs(K), new Variables(ClauseNumber));
        // Initialise said Variable's true number representation (We just need to know what variable it is)


        //Create a size = 2 List, First entry being which clause said variable appears in, and Second being variable type.
        List<Integer> varsandtype = new ArrayList<Integer>();
        varsandtype.add(ClauseNumber);
        varsandtype.add(Negativecheck.check(K));

        //Taking the reference from within Master Variables hashtable, update variable's is_in hashtable.
        passedin.Lister.get(Math.abs(K)).is_in.put(ClauseNumber,varsandtype);
        //Because passed in is a reference, I can do this, and it effects the VariablesList class itself.
    }
}
