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
        
        int i = 0, k, l, e;
        
        /* version minimum lignes -> les tests montrent que c'est le plus rapide*/
        model.sum(new IntVar[]{h, j}, "=", model.intVar(new int[]{0, 11, 12, 13, 14})).post();
        model.count(model.intVar(new int[]{3, 10}), new IntVar[]{h, j}, model.intVar(0)).post();
        model.count(model.intVar(new int[]{0, 11}), new IntVar[]{h, j}, model.intVar(0)).post();
        
        /*version simple -> les tests montrent que c'est plus lent
        model.count(model.intVar(new int[]{0, 1}), new IntVar[]{h, j}, model.intVar(0)).post();
        model.count(model.intVar(new int[]{0, 2}), new IntVar[]{h, j}, model.intVar(0)).post();
        model.count(model.intVar(new int[]{0, 3}), new IntVar[]{h, j}, model.intVar(0)).post();
        model.count(model.intVar(new int[]{0, 10}), new IntVar[]{h, j}, model.intVar(0)).post();
        model.count(model.intVar(new int[]{0, 11}), new IntVar[]{h, j}, model.intVar(0)).post();
        model.count(model.intVar(new int[]{10, 3}), new IntVar[]{h, j}, model.intVar(0)).post();*/
        
        Solver solver = model.getSolver();
        
        solver.setSearch(intVarSearch(j, h));
        
        while(solver.solve()) {
            System.out.println(
                "j = " + j.getValue() +
                ", h = " + h.getValue()
            );
            i++;
        }
        System.out.println("Il y a " + i + " solutions");
        
    }
    
}