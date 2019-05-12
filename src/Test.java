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
        IntVar lmv = model.intVar(0, 10);
        IntVar mj = model.intVar(0, 10);
        
        model.arithm(lmv, "+", mj, "=", n).post();
        
        // lmj <= n
        model.arithm(lmv, "<=", n).post();
        
        // lmv - mj < 2
        model.arithm(lmv, "-", mj, "<", 2).post();
        
        // mj - lmv < 2
        model.arithm(mj, "-", lmv, "<", 2).post();
        
        // mlv * n >= n
        model.arithm(lmv, "*", n, ">=", n).post();
        
        int i, k, l, e;
        
        Solver solver = model.getSolver();
        
        solver.setSearch(intVarSearch(lmv, mj));
        
        while(solver.solve()) {
            System.out.println(
                "lmv = " + lmv.getValue() +
                ", mj = " + mj.getValue() +
                ", n = " + n.getValue()
            );
        }
        
    }
    
}