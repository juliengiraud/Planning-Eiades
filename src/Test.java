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
        IntVar n1 = model.intVar(0, 10);
        IntVar n2 = model.intVar(0, 10);
        IntVar n3 = model.intVar(0, 10);
        IntVar[] test = {n1, n2, n3};
        
        model.count(9, test, model.intVar(4)).post();
        
        int i = 0, k, l, e;
        
        Solver solver = model.getSolver();
        
        solver.setSearch(intVarSearch(test));
        
        while(solver.solve()) {
            System.out.println(
                "n1 = " + n1.getValue() +
                ", n2 = " + n2.getValue() +
                ", n3 = " + n1.getValue()
            );
            i++;
        }
        System.out.println("Il y a " + i + " solutions");
        
    }
    
}