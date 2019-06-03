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
        
        
        IntVar h = model.intVar(1, 3);
        IntVar j = model.intVar(new int[]{1, 2, 3});
        
        model.arithm(h, "*", j, "!=", 3).post();
        
        int i, k, l, e;
        l = 37*2;
        
        System.out.println(l + " = " + (l/10-l%10) + "*10h + " + l%10 + "*11h");
        
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