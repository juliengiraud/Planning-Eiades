import Objets.Planning;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
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
        */
        //int i, j;
        Model model = new Model("Test");
        
        // Model objective function 3X + 4Y
        IntVar OBJ = model.intVar("objective", 0, 999);
        
        //model.scalar(new IntVar[2], new int[]{3,4}, "l", OBJ).post();
        // Specify objective
        model.setObjective(Model.MAXIMIZE, OBJ);
        // Compute optimum
        model.getSolver().solve();
        
    }
}
