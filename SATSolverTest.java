package sat;
import com.sun.org.apache.xpath.internal.SourceTree;

import sat.SATSolver;
import sat.env.Environment;
import sat.formula.Clause;
import sat.formula.NegLiteral;
import sat.formula.PosLiteral;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Set;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import sat.env.*;
import sat.formula.*;
import java.nio.file.*;
import immutable.EmptyImList;
import immutable.ImList;

public class SATSolverTest {
    Variable va = new Variable("a");
    Variable vb = new Variable("b");
    Variable vc = new Variable ("c");

    Literal a = PosLiteral.make(va);
    Literal b = PosLiteral.make(vb);
    Literal c = PosLiteral.make(vc);
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();
    public static void main(String[] args) {
        String[] Splitup;
        ArrayList<LinkedList<Integer>> MasterForma= new ArrayList<>();
        LinkedList<Integer> Holder = new LinkedList<>();
        Integer NumberofVariables=0;
        Integer NumberofClauses=0;
        try (BufferedReader reader = new BufferedReader(new FileReader("C:/Users/User/Desktop/HOASE/LargeSat.cnf" ))) {
            //Requires buffered reader for readline function. Then read through substrings
            String View;
            while ((View =reader.readLine())!= null){
                if(View.trim().isEmpty()){continue;}
                if (View.substring(0,1).equals("p")) {
                    //Assign Splitup's Value
                    Splitup = View.split("\\s+");
                    NumberofVariables=Integer.parseInt(Splitup[2]);
                    NumberofClauses=Integer.parseInt(Splitup[3]);
                }
                //STRICT. will ignore all badly formatted lines that do not start with p or c. ONLY p or c. P and C will NOT be processed.
                else if(   !(View.substring(0,1).equals("c")) ) {
                    //Assign Splitup's Value
                    Splitup = View.split("\\s+");
                    for (String Expression: Splitup) {
                        //If it is not a denotation of end of line.
                        if (!Expression.equals("0")) {
                            //Add to current holder.
                            Holder.add(Integer.parseInt(Expression));
                        }
                        //it is the end of the line. cut and submit to MasterForma. Clear holder.
                        else{
                            MasterForma.add(Holder);
                            Holder= new LinkedList<>();
                        }
                    }
                }
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (false){ //(NumberofClauses-1)!=MasterForma.size()){
            //System.out.println("ERROR IN INPUT FILE. CLAUSE NUMBER AND CLAUSES FOUND DID NOT MATCH");
        }
        else {
            System.out.println("SAT solver starts!!!");
            SatSolvercheat A = new SatSolvercheat();
            long started = System.nanoTime();
            A.Solve(MasterForma);
            if(A.Varlist.is_unsat==true){
                System.out.println("not satisfiable");
                long ended = System.nanoTime();
            }
            Set<Integer> Answers = A.Varlist.FinalisedValues.keySet();
            /*for(Integer I: Answers){
                System.out.print(I+ ":");
                System.out.println(A.Varlist.FinalisedValues.get(I).TrueFinalForm);
            }*/
            //finalised values = A.varlist.FinalisedValues appears as a hashtable.
            //new formula = A.Varlist.Formula //Appears as a ArrayList<LinkedList<Integer>>
            Boolean Stillhas = false;
            /*for (LinkedList<Integer> AA: A.Varlist.Formula){
                System.out.println(AA);
                if(AA.size()!=0){Stillhas=true;}
            }*/
            if(Stillhas) {
                Formula formula = new Formula();
                for (LinkedList<Integer> CLAUSES : A.Varlist.Formula) {
                    Clause clause = new Clause();
                    for (Integer VARSINSIDE : CLAUSES) {
                        if (VARSINSIDE < 0) {
                            clause = clause.add(NegLiteral.make(Integer.toString(Math.abs(VARSINSIDE))));
                        } else {
                            clause = clause.add(PosLiteral.make(Integer.toString(Math.abs(VARSINSIDE))));
                        }
                    }
                    formula = formula.addClause(clause);
                }
                Environment env;
                //System.out.println(formula.toString());
                env = SATSolver.solve(formula);
                if (env == null) {
                    System.out.println("not satisfiable");
                } else {
                    System.out.println("satisfiable");
                    try {
                    PrintWriter out = new PrintWriter(new FileWriter("C:/Users/User/Desktop/HOASE/answer.txt"));
                    Hashtable<Integer, Bool> Answer = new Hashtable<>();
                    for (int varnum = 1; varnum <= NumberofVariables; varnum++) {
                        Bool bool = env.get(new Variable(Integer.toString(varnum)));
                        if (bool == Bool.TRUE) {
                            //out.println(Integer.toString(varnum) + ":TRUE");
                            Answer.put(varnum, Bool.TRUE);
                        } else if (bool == Bool.FALSE) {
                            //out.println(Integer.toString(varnum) + ":FALSE");
                            Answer.put(varnum, Bool.FALSE);
                        } else {
                            //was already defined in Cheatcode. Do nothing.
                        }
                    }

                    for (Integer I : A.Varlist.FinalisedValues.keySet()) {
                        Bool holder;
                        if (A.Varlist.FinalisedValues.get(I).TrueFinalForm == 1) {
                            holder =  Bool.TRUE;
                        } else {
                            holder = Bool.FALSE;
                        }
                        //assign accordingly.
                        Answer.put(I, holder);
                    }
                    for (Integer Answerkey : Answer.keySet()) {
                        if (Answer.get(Answerkey) == Bool.FALSE) {
                            out.println(Integer.toString(Answerkey) + ":FALSE");
                        } else {
                            out.println(Integer.toString(Answerkey) + ":TRUE");
                        }

                    }
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            }
            else{
                System.out.println("satisfiable");
                try {
                    PrintWriter out = new PrintWriter(new FileWriter("C:/Users/User/Desktop/HOASE/answer.txt"));
                    Hashtable<Integer, Boolean> Answer = new Hashtable<>();
                    Boolean holder;
                    for (Integer I : A.Varlist.FinalisedValues.keySet()) {
                        if (A.Varlist.FinalisedValues.get(I).TrueFinalForm == 1) {
                            holder = true;
                        } else {
                            holder = false;
                        }
                        //assign accordingly.
                        Answer.put(I, holder);
                    }
                    for (Integer Answerkey : Answer.keySet()) {
                        //System.out.println(Answerkey);
                        if (Answer.get(Answerkey) == false) {
                            out.println(Integer.toString(Answerkey) + ":FALSE");
                        } else {
                            out.println(Integer.toString(Answerkey) + ":TRUE");
                        }

                    }
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            long time = System.nanoTime();
            long timeTaken = time - started;
            System.out.println("Time:" + timeTaken/1000000.0 + "ms");
        }

    }
}
  /*  public void testSATSolver1(){
        // (a v b)
        Environment e = SATSolver.solve(makeFm(makeCl(a,b)) ); }


    public void testSATSolver2(){
        // (~a)
        Environment e = SATSolver.solve(makeFm(makeCl(na))); }

    private static Formula makeFm(Clause... e) {
        Formula f = new Formula();
        for (Clause c : e) {
            f = f.addClause(c);
        }
        return f;
    }

    private static Clause makeCl(Literal... e) {
        Clause c = new Clause();
        for (Literal l : e) {
            c = c.add(l);
        }
        return c;
    }
}


/*
for(LinkedList<Integer> a : A.Varlist.Formula){
                System.out.print(a);
            }
 */





