package sat;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;

public class VariablesList {
    public HashMap<Integer,Variables> Lister = null; // A master List of all set Variables.
    public HashMap<Integer,Variables> SingleClauses = null;
    //Lists out your clauses that are single. might not be too useful.
    public HashMap<Integer,Variables> FinalisedValues = null; //Lists which variables have finalised values.
    /* A bit of explanation required. Finalised values allows you to compare if a variable within the clause has
    * already got a finalised value. Upon determining it has a finalised value, it will automatically evaluate
    * said variable in the clause's context.
    * IE. if A =1 is determined, in another clause, A appears as {A OR B}, this allows us to instantly find that A has
    * already obtained a determined value. As a result, it will evaluate said clause, in this case, instantly marking the
    * clause as TRUE. This will result in B being ignored if it has already been processed as a "Variable" class, because
     * it would remain as an X case. IE there is no known value that either determines B MUST be 1 or MUST be 0.
    * for processing it because B is still unknown.
    * */
    public HashMap<Integer,Integer> Ignoreus= null; //Lists which clauses can be ignored and no longer processed.
    //might not be useful because of finalised values determination method.

    public Boolean is_unsat = false;
    //If any unsat condition is reached, this will become true. Upon completion of clause processing, instantly causes
    //main SatSolver.java to return a UN-SAT response.
    private Boolean clauseissolved = false;
    /*A marker to ensure that if the clause is solved, I will do 2 things:
    *   ONE: Clear out the entire clause in the original input Formula after it has been completely processed.
    *   This is because there is no longer a need to process ANY of the variables in the clause EVER again.
    *   if they are still unknowns, and are never mentioned in another clause. IT CAN BE ASSIGNED AN ARBITRARY VALUE.
    *   TWO: Remove this clause from consideration of ALL variables involved in this clause.
    *   Yes I want every variable instantiated for record keeping purposes, but if it is involved in a clause that will
    *   evaluate to true, it would never require this clause in its value assignment for consideration.
    *   As a result, remove it.
    */

    private ArrayList<Integer> RemoveFromClause;


    public ArrayList<LinkedList<Integer>> Formula;
    //Save the formula for future referencing within THIS function. NOTE THAT IT IS A REFERENCE AND IS HENCE A SHALLOW COPY.
    //ANY CHANGES MADE TO this.Formula IS REFLECTED IN THE REAL Formula Input! DO NOT USE REMOVE() on OUTER LIST.
    //ONLY USE REMOVE() OR CLEAR() ON OUTER LIST


    VariablesList(ArrayList<LinkedList<Integer>> Formula) {
        this.Lister = new HashMap<Integer, Variables>();
        this.SingleClauses = new HashMap<Integer, Variables>();
        this.FinalisedValues = new HashMap<Integer, Variables>();
        this.Ignoreus = new HashMap<Integer, Integer>();
        this.Formula = Formula;
        this.RemoveFromClause = new ArrayList<Integer>();
    }
    public void Register(LinkedList<Integer> Clause, Integer ClauseNumber){
        for (Integer K : Clause) {
            //Additional check of if it has already obtained a SET value is not yet implemented.


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////    CLAUSE SIZE IS NOT EQUAL TO ONE             ///////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


            if(Clause.size()!=1){
                //Run a check to see if this variable was never initialised in the list.
                if(this.Lister.get(Math.abs(K))==null) {
                    Variableinitnot1.Makevars(this,ClauseNumber,K);
                    //run init for variable that appears in not1 clause.
                    List<Integer> varsandtype = new ArrayList<Integer>();
                    //Create a size = 2 List, First entry being which clause said variable appears in, and Second being variable type.
                    varsandtype.add(Clause.size());
                    varsandtype.add(Negativecheck.check(K));
                    this.Lister.get(Math.abs(K)).is_in.put(ClauseNumber,varsandtype);
                    //This updates the is in for the Variable. DO NOT CHANGE.
                }


                //If the variable has indeed been initialised, update the existing entry
                else{
                    if(this.Lister.get(Math.abs(K)).TrueFinalForm==null){
                        //ie. this number does not have an assigned value.
                        List<Integer> varsandtype = new ArrayList<Integer>();
                        //Again, create a size = 2 List, First entry being which clause said variable appears in, and Second being variable type.
                        varsandtype.add(Clause.size());
                        varsandtype.add(Negativecheck.check(K));
                        this.Lister.get(Math.abs(K)).is_in.put(ClauseNumber,varsandtype);
                        //Taking the reference from within Master Variables hashtable, update variable's is_in hashtable.
                    }
                    else {
                        //This Variable has an assigned Value!
                        if(this.Lister.get(Math.abs(K)).TrueFinalForm.intValue() == SingleClassAssigner.SingleAssign(K).intValue()){
                            //Ie. It satisfies this entire clause instantly
                            this.clauseissolved= true;
                            //Keep the note that the clause is already solved. Continue Processing all variables. Deal
                            //with this later.
                            List<Integer> varsandtype = new ArrayList<Integer>();
                            varsandtype.add(Clause.size());
                            varsandtype.add(Negativecheck.check(K));
                            //Taking the reference from within Master Variables hashtable, update variable's is_in hashtable.
                            this.Lister.get(Math.abs(K)).is_in.put(ClauseNumber, varsandtype);

                        }
                        else{
                            //Because this variable does not solve this clause by default, nothing is done.
                            //DPLL that is implemented later will continue to process with this given value.
                            //For later reference, we will of course add this to the is_in of this variable.
                            //For this, remove this particular item from the clause later, by adding it to a list we keep for this purpose.
                            //We will not update the is_in of this item given that it has no real bearing whether it is in this or not.
                            this.RemoveFromClause.add(K.intValue());

                        }

                        List<Integer> varsandtype = new ArrayList<Integer>();
                        varsandtype.add(Clause.size());
                        varsandtype.add(Negativecheck.check(K));
                        //Taking the reference from within Master Variables hashtable, update variable's is_in hashtable.
                        this.Lister.get(Math.abs(K)).is_in.put(ClauseNumber, varsandtype);
                    }
                }



            }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////    CLAUSE SIZE IS EQUAL TO ONE             ///////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


            //Else occurs when the clause size is 1. So it's a single variable clause.
            else{
                //IF this variable has never been initialised and is appearing as a singular.
                if(this.Lister.get(Math.abs(K))==null) {
                    Variableinit1.Makevarbut1(this, ClauseNumber,K);
                    List<Integer> varsandtype = new ArrayList<Integer>();
                    varsandtype.add(Clause.size());
                    varsandtype.add(Negativecheck.check(K));
                    //Taking the reference from within Master Variables hashtable, update variable's is_in hashtable.
                    this.Lister.get(Math.abs(K)).is_in.put(ClauseNumber, varsandtype);
                }

                //if finalised value was -1.



                else{
                    //The variable has been initialised. Check if it ever had a true assignment.
                    if(this.Lister.get(Math.abs(K)).TrueFinalForm==null){
                        this.Lister.get(Math.abs(K)).TrueFinalForm = SingleClassAssigner.SingleAssign(K);
                        //Since no true assignment, Assign.
                        FinalisedValues.put(Math.abs(K), Lister.get(Math.abs(K)));
                        //Update Master Assigned Hash
                        this.clauseissolved = true;
                        //Clause became true by default..
                        //Now to purge from any other things it appears in.
                        Purger.purge(this,K);

                    }
                    //It has been initialised and had an assignment. Check for unsat condition.
                    else {
                        boolean checkforconflict = false;
                        //First check through current formula
                        for(Integer I: this.Formula.get(ClauseNumber)){
                            //iterate through existing formula, looking for actual statement.
                            if(Math.abs(I) == K ){
                                checkforconflict = (this.Lister.get(Math.abs(K)).TrueFinalForm.intValue() != SingleClassAssigner.SingleAssign(I).intValue());
                            }
                        }
                        //Then check through old formula. Delete clause if evaluates to true, delete variable only if evaluates to untrue.
                        for(Integer L: this.Lister.get(K).is_in.keySet()){
                            //within it's set of all clauses involved, invoke old formulas to check
                            for(Integer ite : this.Formula.get(L)){
                                //iterate through existing formula, looking for actual statement.
                                if(Math.abs(ite) == K ){
                                    checkforconflict = (this.Lister.get(Math.abs(K)).TrueFinalForm.intValue() != SingleClassAssigner.SingleAssign(ite).intValue());
                                }
                            }
                        }

                        if (checkforconflict) {
                            //CONFLICT SETS EQUATION TO IMMEDIATE UN-SAT.
                            this.is_unsat = true;
                            //Upon detection of unsat, because there is no need to care about the correct variables anymore,
                            //the hash of finalised values is not updated.
                        }
                        //Both ways, it must be purged from previous statements.

                        //If nothing was wrong with the current set value, nothing is done. Move to next processing step.
                        else { //Triggers because its previous assignment was correct
                            //Given that this entire statement is now true, set this.clauseissolved=true.
                            this.clauseissolved = true;

                        }
                        Purger.purge(this,K);
                    }
                }

            }

        }
        if(this.RemoveFromClause.size()!=0){
            for(Integer Removeme: this.RemoveFromClause){
                Clause.removeFirstOccurrence(Removeme);
                //By using the provided reference to formula, EDIT base formula to remove element.
            }
            //At this point, ALL variables in the clause have been checked.
            // perform redundant clause removal and possible reduction if the clause was confirmed to be solved.


            //Check if new size of linked list is one. if so, it is now a new integer in Clause
            if(Clause.size() ==1){
                for(Integer NewSingularinClause: Clause){
                    this.Lister.get(NewSingularinClause).TrueFinalForm =  SingleClassAssigner.SingleAssign(NewSingularinClause);
                    //ie. apply the check done to single clause items.
                    this.FinalisedValues.put(NewSingularinClause,this.Lister.get(NewSingularinClause));
                    //now register again to master variables list.
                    //Because this clause is now.. SOLVED... apply steps below.
                    this.clauseissolved=true;

                }
            }
        }

        if(this.clauseissolved){
            this.clauseissolved = false;
            //reset the clause is solved indicator.
            RedundantClauseRemoval.Remove(this,Clause,ClauseNumber);
            //At this point, All variables in the clause have already been initialised.
            //Now Clear the Clause in the Actual formula, reducing it to an empty LinkedList. It would always evaluate to
            //true anyway. Furthermore, an additional catch-all exists in SatSolver.java, which sets any variable without
            //any weighted clauses to an arbitrary value. ie. 1
            this.Formula.get(ClauseNumber).clear();
        }


    }

}

/*
for(Integer PURGE: this.Lister.get(Math.abs(K)).is_in.keySet()){
                            //purge being the clause number it appeared in
                            //FOR ALL CLAUSE NUMBERS IT APPEARS IN.
                            boolean work = false;

                            work = (this.Lister.get(Math.abs(K)).TrueFinalForm.intValue() == SingleClassAssigner.SingleAssign(this.Lister.get(Math.abs(K)).is_in.get(PURGE).get(1)).intValue());
                            //Should we wipe the entire clause because the assigned value satisfies this clause?
                            this.Formula.get(PURGE).removeFirstOccurrence(K);
                            this.Formula.get(PURGE).removeFirstOccurrence(-K);
                            //if the list does not contain the element, it is unchanged.
                            this.Lister.get(Math.abs(K)).is_in.remove(PURGE);
                            //As far as we care, this Integer K is not involved in this particular clause
                            // if it satisfies a clause, delete entire clause using clause removal.
                            //this is because now that clause has no real weightage to anything else either.
                            if(work){
                                //if the single assign check which looks at existing variable's type (negated or not) to determine
                                //what to implement for the clause to be true,
                                //and the assigned value which is assigned is the same part is zero, that clause is already fulfilled.
                                //hence we conclude this clause has no real weightage over our future DPLL considerations.
                                //hence REMOVE ALL TRACES of variables in this clause.

                                for(Integer involvedinclausepurge : this.Formula.get(PURGE)){
                                     this.Lister.get(Math.abs(involvedinclausepurge)).is_in.remove(PURGE);
                                }
                                this.Formula.get(PURGE).clear();
                                //Clause wipe.
                            }


                            //All is in is removed. Now iterate through the new forma and update is_ins.
                            for(Integer Remnants: this.Formula.get(PURGE)){

                                List<Integer> varsandtype = new ArrayList<Integer>();
                                varsandtype.add(this.Formula.get(PURGE).size());

                                //obtain new clause size
                                varsandtype.add(this.Lister.get(Math.abs(Remnants)).is_in.get(PURGE).get(1));
                                //obtain old typing
                                //Updated new is_in values for every variable within.
                                this.Lister.get(Math.abs(Remnants)).is_in.put(PURGE,varsandtype);
                            }

                        }
 */
