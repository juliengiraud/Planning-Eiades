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
        
        
        IntVar h = model.intVar(0, 3);
        IntVar j = model.intVar(new int[]{0, 10, 11});
        IntVar test = model.intVar(0, 1);
        
        int i, k, l, e;
        
        test = model.allEqual(h, j).reify();
        
        Solver solver = model.getSolver();
        
        solver.setSearch(intVarSearch(j, h));
        
        i = 0;
        while(solver.solve()) {
            System.out.println(
                "j" + " = " + j.getValue() +
                ", h" + " = " + h.getValue()
            );
            i++;
        }
        System.out.println("Il y a " + i + " solutions");
        
    }
    
}