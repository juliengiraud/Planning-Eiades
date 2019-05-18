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
        IntVar[] test = {h, j};
        
        model.arithm(h, "*", model.intVar(2), "=", h2).post();
        model.arithm(h2, "+", j, "=", h2j).post();
        model.allDifferent(
            h2j,
            model.intVar(2),
            model.intVar(4),
            model.intVar(9),
            model.intVar(14),
            model.intVar(15)
        ).post();
        
        int i = 0, k, l, e;
        
        Solver solver = model.getSolver();
        
        solver.setSearch(intVarSearch(test));
        
        while(solver.solve()) {
            System.out.println(
                "h = " + h.getValue() +
                ", j = " + j.getValue()
            );
            i++;
        }
        System.out.println("Il y a " + i + " solutions");
        
    }
    
}