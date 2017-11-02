package sat;


import java.util.LinkedList;

public class RedundantClauseRemoval {
    private RedundantClauseRemoval(){}
    //Clause in this case refers to the clause that contains the variable that caused entire clause to become true.
    public static void Remove(VariablesList pass, LinkedList<Integer> Clause, Integer Clausenumber){
        //For every variable within this clause, remove the reference to THIS clause. in their is_in. This is because
        //this clause has no weight on their final assignment.
        for(Integer VariableinClause : Clause){

            pass.Lister.get(Math.abs(VariableinClause)).is_in.remove(Clausenumber);
        }

    }
}
