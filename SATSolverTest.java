package sat;



/*
import static org.junit.Assert.*;

import org.junit.Test;
*/

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
import java.util.stream.Stream;
import sat.SATSolver;


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
/*
    Clause canb = make (a, nb);
    Clause canb = make (a, b);
    Clause cnbc = make(nb, c);
  */  

	
	// TODO: add the main method that reads the .cnf file and calls SATSolver.solve to determine the satisfiability
    
   /* 
    public static void main(String[] args){
        Formula formula = new Formula();
        int variables = 0;
            String line;
            while ((line = br.readLine())! = null){
            if (!(line.substring(0,1).equals("p")) && !(line.substring(0,1).equals("c"))){//not a problem not a comment statement
                Clause clause = new Clause();
                for (String linesplit : line.split ("\\s+")){
                    if (!linesplit.equals("0")){//not an empty clause
                         if (linesplit.substring(0, 1).equals("-"))//turn negative or positive
                                clause = clause.add(NegLiteral.make(linesplit.substring(1)));
                            else
                                clause = clause.add(PosLiteral.make(linesplit));
                    }
                }
                formula = formula.addClause(clause);
            }
            else if (line.substring(0,1).equals("p")){
                String[] linesplit = line.split("\\s+");
                variables = Integer.parseInt(linesplit[2]);
            }
            
        }
    }   catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(formula.toString());
        
        Environment env;
        env = SATSolver.solve(formula);
        if (env == null) {
            System.out.println("not satisfiable");
        } else{
            System.out.println("satisfiable");
        }
        try{
            PrintWriter out = new PrintWriter(new FileWriter("C:/Main/SUTD/50.001/Project-2D/Project-2D-starting/sampleCNF/BoolAssignment.txt"));
                for (int varnum = 1; varnum <= variables; varnum++) {
                    Bool bool = env.get(new Variable(Integer.toString(varnum)));
                    if (bool == Bool.TRUE) {
                        out.println(Integer.toString(varnum) + ":TRUE");
                    }
                    else {
                        out.println(Integer.toString(varnum) + ":FALSE");
                    }
                }
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }    
*/public static void main(String[] args) {
        Formula formula = new Formula();
        int variables = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("C:/Users/ongajong/Documents/project-2d-starting/sampleCNF/largeUnSat.cnf" ))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!(line.substring(0,1).equals("p")) && !(line.substring(0,1).equals("c"))) {
                    Clause clause = new Clause();
                    for (String linesplit : line.split("\\s+")) {
                        if (!linesplit.equals("0")) {
                            if (linesplit.substring(0, 1).equals("-"))
                                clause = clause.add(NegLiteral.make(linesplit.substring(1)));
                            else
                                clause = clause.add(PosLiteral.make(linesplit));
                        }
                    }
                    formula = formula.addClause(clause);
                }
                else if (line.substring(0,1).equals("p")) {
                    String[] linesplit = line.split("\\s+");
                    variables = Integer.parseInt(linesplit[2]);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(formula.toString());
        
        long started = System.nanoTime();
        Environment env;
        env = SATSolver.solve(formula);
        if (env == null) {
            System.out.println("not satisfiable");
        }
        else {
            System.out.println("satisfiable");
            try {
                PrintWriter out = new PrintWriter(new FileWriter("C:/Users/ongajong/Documents/project-2d-starting/sampleCNF/boolean1.txt"));
                for (int varnum = 1; varnum <= variables; varnum++) {
                    Bool bool = env.get(new Variable(Integer.toString(varnum)));
                    if (bool == Bool.TRUE) {
                        out.println(Integer.toString(varnum) + ":TRUE");
                    }
                    else {
                        out.println(Integer.toString(varnum) + ":FALSE");
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
    public void testSATSolver1(){
    	// (a v b)
    	Environment e = SATSolver.solve(makeFm(makeCl(a,b))	);
/*
    	assertTrue( "one of the literals should be set to true",
    			Bool.TRUE == e.get(a.getVariable())  
    			|| Bool.TRUE == e.get(b.getVariable())	);
    	
*/    	
    }
    
    
    public void testSATSolver2(){
    	// (~a)
    	Environment e = SATSolver.solve(makeFm(makeCl(na)));
/*
    	assertEquals( Bool.FALSE, e.get(na.getVariable()));
*/    	
    }
    
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