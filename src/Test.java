import static org.chocosolver.solver.search.strategy.Search.*;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import static org.chocosolver.solver.constraints.nary.cnf.LogOp.*;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.expression.discrete.arithmetic.ArExpression;
import org.chocosolver.solver.constraints.IIntConstraintFactory;


public class Test {
    
    public static void main(String[] args) {
        
        Model model = new Model();
        
        
        IntVar h = model.intVar(0, 2);
        IntVar j = model.intVar(new int[]{0, 7, 10, 11});
        IntVar h2 = model.intVar(0, 100);
        IntVar h2j = model.intVar(0, 100);
        IntVar[] test = model.intVarArray(2, 0, 100);
        test[0] = h;
        test[1] = j;
        
        model.sum(test, "=", 8).post();
        
        int i = 0, k, l, e;
        
        Solver solver = model.getSolver();
        
        solver.setSearch(intVarSearch(test));
        
        while(solver.solve()) {
            System.out.println(
                "h = " + h.getValue() +
                ", j = " + j.getValue() +
                ", h+j = " + (h.getValue() + j.getValue())
            );
            i++;
        }
        System.out.println("Il y a " + i + " solutions");
        
    }
    
}