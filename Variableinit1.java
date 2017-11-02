package sat;

import java.util.ArrayList;
import java.util.List;

public class Variableinit1 {
    private Variableinit1(){}
    public static void Makevarbut1(VariablesList passedin, Integer ClauseNumber, Integer K) {

        //Creation of entry with Variable
        passedin.Lister.put(Math.abs(K), new Variables(ClauseNumber));
        //Again, create a size = 2 List, First entry being which clause said variable appears in, and Second being variable type.
        //Use that to update the clauses it is in.
        List<Integer> varsandtype = new ArrayList<Integer>();
        varsandtype.add(ClauseNumber);
        varsandtype.add(Negativecheck.check(K));
        passedin.Lister.get(Math.abs(K)).is_in.put(ClauseNumber, varsandtype);

        //Assign final value, due to it never having been assigned a value
        passedin.Lister.get(Math.abs(K)).TrueFinalForm = SingleClassAssigner.SingleAssign(K);
        passedin.FinalisedValues.put(Math.abs(K), passedin.Lister.get(Math.abs(K)));
        //Store this existing variable's reference within the Hashtable of finalised values.
        //This is the initialisation. It has never been referenced. No further checks required, end init.
    }


}
