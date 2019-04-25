
import static org.chocosolver.solver.search.strategy.Search.*;
import Objets.Planning;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.objective.ParetoOptimizer;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.IntVar;

public class Main {
    
    public static void main(String[] args) {
        
        /*Planning planning = new Planning();
        
        // 1. Modelling part

        // 1.a declare the variables
        //IntVar heuresTravailJour = model.intVar("heuresTravailJour", new int[]{7, 10, 11});
        // 21 = a*7+b*10+c*11 -> 
        // a*7 -> v1
        // b*10 -> v2
        // c*11 -> v3
        // v1 + v2 -> v1
        // v1 + v3 -> v1
        IntVar a, b, c;
        a = model.intVar("a", new int[]{0,1,2,3});
        b = model.intVar("b", new int[]{0,1,2});
        c = model.intVar("c", new int[]{0,1,2});
        IntVar v1 = model.intScaleView(a, 7);
        IntVar v2 = model.intScaleView(b, 10);
        IntVar v3 = model.intScaleView(c, 11);
        
        IntVar michel = model.intVar("michel", 21);
        

        // 1.b post the constraints
        //model.arithm(model.arithm(v1, "+", "v2"), "=", michel).post();
        
        // 2. Solving part
        Solver solver = model.getSolver();
        
        while(solver.solve()){
            // do something, e.g. print out variable values
            
        }

        // 2.a define a search strategy
        *//*
        //int i, j;
        Model model = new Model("Test");
        
        // Model objective function 3X + 4Y
        IntVar OBJ = model.intVar("objective", 0, 999);
        
        //model.scalar(new IntVar[2], new int[]{3,4}, "l", OBJ).post();
        // Specify objective
        model.setObjective(Model.MAXIMIZE, OBJ);
        // Compute optimum
        model.getSolver().solve();*/
        // simple model
        Model model = new Model();
        /*IntVar a = model.intVar("a", new int[]{0, 1, 2, 3, 10});
        IntVar b = model.intVar("b", new int[]{0, 1, 2, 3, 10});
        IntVar c = model.intVar("c", new int[]{0, 1, 2, 3});
        IntVar s = model.intVar("s", 20);
        
        model.arithm(a, "+", b, "=", s).post();*/

        
        IntVar a = model.intVar("a", new int[]{0, 1, 2, 3, 4});
        IntVar a2 = model.intVar("a2", 0, 100, false);
        model.arithm(a, "*", model.intVar("a3", 7), "=", a2).post();
        
        IntVar b = model.intVar("b", new int[]{0, 1, 2, 3, 4});
        IntVar b2 = model.intVar("b2", 0, 100, false);
        model.arithm(b, "*", model.intVar("b3", 10), "=", b2).post();
        
        IntVar c = model.intVar("c", new int[]{0, 1, 2, 3, 4});
        IntVar c2 = model.intVar("c2", 0, 100, false);
        model.arithm(c, "*", model.intVar("c3", 11), "=", c2).post();
        
        IntVar s = model.intVar("s", 0, 100, false);
        IntVar s2 = model.intVar("s2", 0, 100, false);
        model.arithm(a2, "+", b2, "=", s2).post();
        
        //IntVar s3 = model.intVar("s3", new int[]{20, 21, 25, 29, 30, 33, 36, 39});
        IntVar s3 = model.intVar("s3", 20);
        model.arithm(s2, "+", c2, "=", s3).post();
        
        

        Solver solver = model.getSolver();
        
        solver.setSearch(intVarSearch(a, b));

        while(solver.solve()) {
            System.out.println("7" + " * " + a.getValue() + " + " + "10" + " * " +
            b.getValue() + " + " + "11" + " * " + c.getValue() + " = " + s3.getValue());
            /*System.out.println("a=" + a.getValue() + " b=" + b.getValue() +
                    " c=" + c.getValue() + " s=" + s.getValue());*/
        }
        
    }
}
