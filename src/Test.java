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
        
        
        IntVar n = model.intVar(new int[]{0, 7, 10, 11});
        IntVar[] x = model.intVarArray(5, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        IntVar[] r = model.intVarArray(4, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        model.arithm(n, "/", model.intVar(7), "=", x[0]).post();
        model.mod(n, model.intVar(7), r[0]).post();
        
        model.arithm(r[0], "/", model.intVar(3), "=", x[1]).post();
        model.mod(r[0], model.intVar(3), r[1]).post();
        
        model.arithm(x[1], "+", r[1], "=", x[2]).post();
        
        model.arithm(r[0], "-", x[2], "=", x[3]).post();
        
        model.arithm(x[3], "/", model.intVar(2), "=", x[4]).post();
        model.mod(x[3], model.intVar(2), r[2]).post();
        
        model.arithm(x[0], "-", x[4], "=", r[3]).post();
        IntVar b, d;
        
        r[0] = model.intVar(new int[]{0, 1, 2, 3});
        r[1] = model.intVar(new int[]{1, 2, 4});
        r[2] = model.intVar(0);
        r[3] = model.intVar(0);
        
        b = model.allEqual(n, model.intVar(7)).reify();
        model.sum(r, "+", model.intVar(5)).post();
        
        
        int i, k, l, e;
        
        Solver solver = model.getSolver();
        
        solver.setSearch(intVarSearch(r));
        
        while(solver.solve()) {
            System.out.println(
                "r1 = " + r[0].getValue() +
                ", r2 = " + r[1].getValue()
            );
        }
        
    }
    
}