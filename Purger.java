package sat;

import java.util.ArrayList;
import java.util.List;

public class Purger {

    public static void purge(VariablesList a,Integer o){
        for(Integer PURGE: a.Lister.get(Math.abs(o)).is_in.keySet()){
            //purge being the clause number it appeared in
            //FOR ALL CLAUSE NUMBERS TO BE PURGED APPEARS IN.
            boolean work = false;
            //iterate through formula till you hit the original variable.
            for(Integer I : a.Formula.get(PURGE)){
                if (Math.abs(I)== Math.abs(o)){
                    work = (a.Lister.get(Math.abs(o)).TrueFinalForm.intValue() == SingleClassAssigner.SingleAssign(a.Lister.get(Math.abs(o)).is_in.get(PURGE).get(1)).intValue());
                    //Should we wipe the entire clause because the assigned value satisfies this clause?
                }
            }

            a.Formula.get(PURGE).removeFirstOccurrence(o);
            a.Formula.get(PURGE).removeFirstOccurrence(-o);
            //if the list does not contain the opposing element, it is unchanged.
            a.Lister.get(Math.abs(o)).is_in.remove(PURGE);
            //As far as we care, this Integer K is not involved in this particular clause
            // if it satisfies a clause, delete entire clause using clause removal.
            //this is because now that clause has no real weightage to anything else either.
            if(work){
                //if the single assign check which looks at existing variable's type (negated or not) to determine
                //what to implement for the clause to be true,
                //and the assigned value which is assigned is the same part is zero, that clause is already fulfilled.
                //hence we conclude this clause has no real weightage over our future DPLL considerations.
                //hence REMOVE ALL TRACES of variables in this clause.

                for(Integer involvedinclausepurge : a.Formula.get(PURGE)){
                    a.Lister.get(Math.abs(involvedinclausepurge)).is_in.remove(PURGE);
                }
                a.Formula.get(PURGE).clear();
                //Clause wipe.
            }


            //All is in is removed. Now iterate through the new forma and update is_ins.
            for(Integer Remnants: a.Formula.get(PURGE)){

                List<Integer> varsandtype = new ArrayList<Integer>();
                varsandtype.add(a.Formula.get(PURGE).size());

                //obtain new clause size
                varsandtype.add(a.Lister.get(Math.abs(Remnants)).is_in.get(PURGE).get(1));
                //obtain old typing
                //Updated new is_in values for every variable within.
                a.Lister.get(Math.abs(Remnants)).is_in.put(PURGE,varsandtype);
            }

        }
    }
}
