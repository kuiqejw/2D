package sat;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class SatSolvercheat{
    public VariablesList Varlist;
    //Note that we are particular on the EXACT type of list.
    //Using Linked list on the outer portion is disastrous.
    // Adding or deleting elements in a linked list however, is better then that of an arraylist. Hence the particularness.
    public Integer Solve(ArrayList<LinkedList<Integer>> Formula){
        this.Varlist = new VariablesList(Formula);
        //CREATE A NEW VARIABLES LIST
        for(int Clausecounter =0; Clausecounter<Formula.size(); Clausecounter++){
            this.Varlist.Register(Formula.get(Clausecounter),Clausecounter);
            //PROCESS CLAUSES TO OBTAIN LIST OF VARIABLES, AND ALSO SINGLE CLAUSES THAT EXIST FOR QUICK-MOVE
            //From which you can deduce that VariablesList class is actually a misnomer, and actually more like clause and variable list.
            if(this.Varlist.is_unsat){
                System.out.println("not satisfiable");
                return null;
                //ie. break the processing.
            }
        }
        Set<Integer> AllVariables = this.Varlist.Lister.keySet();
        for(Integer O: AllVariables){
            //Check Each Variable's HashTable for is_in.
            //Must have unassigned value and empty
            if(this.Varlist.Lister.get(O).is_in.isEmpty() && this.Varlist.Lister.get(O).TrueFinalForm==null){
                //If they are empty, then they hold no effective bearing and can be assigned 1.
                this.Varlist.Lister.get(O).TrueFinalForm=1; //Arbitrary Value
                this.Varlist.FinalisedValues.put(O,this.Varlist.Lister.get(O));
                //update master hash table.
            }
            //else if has only one.
            else {
                if(this.Varlist.Lister.get(O).is_in.size()==1){
                    //Said member only belongs to one Clause.
                    //Said member is the only member in said clause due to reduction
                    Set<Integer> SETTER=this.Varlist.Lister.get(O).is_in.keySet();
                    Integer Lookupin = 0;
                    for (Integer SSS : SETTER){ Lookupin = SSS;} //EXTRACT CLAUSE NUMBER
                    //Check if the Linkedlist, which specifies that clause in formula is only holding that variable.
                    //if(this.Varlist.Lister.get(O).is_in.get(Lookupin).get(1)==1){
                    if(this.Varlist.Formula.get(Lookupin).size() ==1){
                        //Since this is true, check its expression in clause if is negation.
                        if(this.Varlist.Lister.get(O).is_in.get(Lookupin).get(1)==1){
                            //since it is expressed as one, truefinalform = 1 update master hash table.
                            this.Varlist.Lister.get(O).TrueFinalForm=1; //Arbitrary Value
                            this.Varlist.FinalisedValues.put(O,this.Varlist.Lister.get(O));
                        }
                        else{
                            this.Varlist.Lister.get(O).TrueFinalForm=-1; //Arbitrary Value
                            this.Varlist.FinalisedValues.put(O,this.Varlist.Lister.get(O));
                        }
                    }
                    Purger.purge(this.Varlist,O);
                }
                else{

                    //if it occurs as a multiclause, with a single variable clause among.
                    Set<Integer> Checkforsingleclausetype = this.Varlist.Lister.get(O).is_in.keySet();
                    for (Integer A: Checkforsingleclausetype){
                        // A is the clause number in which this variable O partakes in
                        if(this.Varlist.Lister.get(O).is_in.get(A).get(0)==1){
                            /*System.out.print("Variable: ");
                            System.out.println(O);
                            System.out.print("ClauseNumber: ");
                            System.out.println(A);
                            System.out.print("is in ");
                            System.out.println(this.Varlist.Lister.get(O).is_in.get(A));
                            System.out.print("Forma ");
                            System.out.println(Formula.get(A));*/
                            //ie. is a single clause variable type, choose the correct assignment.
                            this.Varlist.Lister.get(O).TrueFinalForm =SingleClassAssigner.SingleAssign(this.Varlist.Lister.get(O).is_in.get(A).get(1));
                            //Assigned True Value accordingly, update Master Hash table
                            this.Varlist.FinalisedValues.put(O,this.Varlist.Lister.get(O));
                            //Check for conflict for each clause.

                            for(Integer allclauses: this.Varlist.Lister.get(Math.abs(O)).is_in.keySet()){
                                int summer =0;
                                boolean unidentified=false;
                                for (Integer Variables: this.Varlist.Formula.get(allclauses)){
                                    //test variables in cases. If there is an unset one, it automatically passes
                                    //if not, the sum of all their values must be =>1
                                    if(this.Varlist.Lister.get(Math.abs(Variables)).TrueFinalForm==null){unidentified = true;}
                                    else{summer +=this.Varlist.Lister.get(Math.abs(Variables)).TrueFinalForm;}
                                }
                                if(unidentified){}
                                else{

                                    if(summer==0){
                                        for(Integer KOL: this.Varlist.Lister.get(5998).is_in.keySet()){
                                            System.out.print("5998 isin: " );
                                            System.out.println(this.Varlist.Lister.get(5998).is_in.get(KOL));
                                        }
                                        System.out.println("TRIGGER");
                                        System.out.println("not satisfiable");
                                        return null;
                                    }
                                }
                            }

                            for(Integer PURGE: this.Varlist.Lister.get(Math.abs(O)).is_in.keySet()) {
                                //purge being the clause number it appeared in
                                //FOR ALL CLAUSE NUMBERS IT APPEARS IN.
                                this.Varlist.Formula.get(PURGE).removeFirstOccurrence(O);
                                this.Varlist.Formula.get(PURGE).removeFirstOccurrence(-O);
                                for (Integer Remnants : this.Varlist.Formula.get(PURGE)) {

                                    List<Integer> varsandtype = new ArrayList<Integer>();
                                    varsandtype.add(this.Varlist.Formula.get(PURGE).size());

                                    //obtain new clause size
                                    varsandtype.add(this.Varlist.Lister.get(Math.abs(Remnants)).is_in.get(PURGE).get(1));
                                    //obtain old typing
                                    //Updated new is_in values for every variable within.
                                    this.Varlist.Lister.get(Math.abs(Remnants)).is_in.put(PURGE, varsandtype);
                                }
                            }
                        }
                    }


                }
            }
        }
        //now that you have ALL possible values that could have been inferred, and one class of reducible clauses reduced to nothing
        // either due to defaulting to true, you're ready to start DPLL.
        //HASHTABLE used for lookup and manipulation.




        return null;
    }
}
