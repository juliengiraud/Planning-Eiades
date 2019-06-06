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
        
        
        IntVar nbCommenceA6h45 = model.intVar(5, 6);
        IntVar nbTermineA16h45 = model.intVar(1, 6); // car > 0
        IntVar nbTermineA17h45 = model.intVar(1, 6); // car > 0
        IntVar nbCommenceA7h30 = model.intVar(2, 3);
        IntVar nbTermineA17h30 = model.intVar(1, 2); // car > 0 et < 3
        IntVar nbTermineA18h30 = model.intVar(1);
        IntVar nbCommenceA8h = model.intVar(2);
        
        model.arithm(nbCommenceA6h45, "+", nbCommenceA7h30, "=", 8).post();
        model.arithm(nbTermineA16h45, "+", nbTermineA17h45, "=", nbCommenceA6h45).post();
        model.arithm(nbTermineA17h30, "+", nbTermineA18h30, "=", nbCommenceA7h30).post();
        model.arithm(nbTermineA16h45, "<", nbCommenceA6h45).post();
        model.arithm(nbTermineA17h45, "<", nbCommenceA6h45).post();

        
        int i, k, l, e;
        
        Solver solver = model.getSolver();
        
        solver.setSearch(intVarSearch(nbTermineA16h45, nbTermineA17h45, nbTermineA17h30));
        
        i = 0;
        while(solver.solve()) {
            System.out.println(
                "   " + nbCommenceA6h45.getValue() +
                "    " + nbCommenceA7h30.getValue() +
                "    " + nbCommenceA8h.getValue() +
                "   " + nbTermineA16h45.getValue() +
                "     " + nbTermineA17h45.getValue() +
                "     " + nbTermineA17h30.getValue() +
                "     " + nbTermineA18h30.getValue() +
                "     " + nbCommenceA8h.getValue()
            );
            System.out.println(nbTermineA16h45.getValue() + " 6h45         16h45");
            System.out.println(nbTermineA17h45.getValue() + " 6h45                     17h45");
            System.out.println(nbTermineA17h30.getValue() + "      7h30          17h30");
            System.out.println(nbTermineA18h30.getValue() + "      7h30                      18h30");
            System.out.println(nbCommenceA8h.getValue()   + "           8h                         19h\n\n");
            i++;
        }
        System.out.println("Il y a " + i + " solutions");
        
    }
    
}