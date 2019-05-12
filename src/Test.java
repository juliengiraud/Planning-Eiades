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
        
        
        IntVar n = model.intVar(0, 3);
        IntVar nb7h = model.intVar(0, 10);
        IntVar nb11h = model.intVar(0, 10);
        
        
        model.arithm(nb7h, "*", nb11h, "=", 0).post();
        
        int i = 0, k, l, e;
        
        Solver solver = model.getSolver();
        
        solver.setSearch(intVarSearch(nb7h, nb11h));
        
        while(solver.solve()) {
            System.out.println(
                "nb7h = " + nb7h.getValue() +
                ", nb11h = " + nb11h.getValue()
            );
            i++;
        }
        System.out.println("Il y a " + i + " solutions");
        
    }
    
}