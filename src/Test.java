import static org.chocosolver.solver.search.strategy.Search.*;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import static org.chocosolver.solver.constraints.nary.cnf.LogOp.*;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.expression.discrete.arithmetic.ArExpression;
import org.chocosolver.solver.constraints.IIntConstraintFactory;


public class Test {
    
    private static int get10hAFaire(int h) {
        return h%10 == 0 ? h/10 : get10hAFaire(h-11*(h%10));
    }
    
    private static int get11hAFaire(int h) {
        return h%10;
    }
    
    public static void main(String[] args) {
        
        int k, l, e, test, i;
        /*int tab[] = new int[] {20, 21, 25, 30, 31, 33, 36, 40, 41};
        for (i = 0; i < tab.length; i++) {
            test = tab[i]*4;
            System.out.println(tab[i] + " " + test + " " + test%10 + " "+ test/10 + " "+ test%11 + " " + test/11);
            System.out.println(get10hAFaire(test) + "*10h + " + get11hAFaire(test) + "*11h\n");
        }*/
        
        Model model = new Model();
        
        
        IntVar nbCommenceA6h45 = model.intVar(5, 6); /* b=5 ou 6 */
        IntVar nbTermineA16h45 = model.intVar(4, 5); /* b1>=4 */
        IntVar nbTermineA17h45 = model.intVar(0, 2); /* b2>=0 */
        IntVar nbCommenceA7h30 = model.intVar(3, 4); /* c=3 ou 4 */
        IntVar nbTermineA17h30 = model.intVar(2, 3); /* c1>=0 et c1+c2=c*/
        IntVar nbTermineA18h30 = model.intVar(1); /* c2=1 */
        IntVar nbCommenceA9h = model.intVar(1); /* d=1 */
        
        model.arithm(nbCommenceA6h45, "+", nbCommenceA7h30, "=", 9).post(); /* b+c=9 */
        model.arithm(nbTermineA16h45, "+", nbTermineA17h45, "=", nbCommenceA6h45).post(); /* b1+b2=b */
        model.arithm(nbTermineA17h30, "+", nbTermineA18h30, "=", nbCommenceA7h30).post(); /* c1+c2=c */
        
        
        Solver solver = model.getSolver();
        
        solver.setSearch(intVarSearch(nbTermineA16h45, nbTermineA17h45, nbTermineA17h30));
        
        i = 0;
        while(solver.solve()) {
            System.out.println(
                "   " + nbCommenceA6h45.getValue() +
                "    " + nbCommenceA7h30.getValue() +
                "    " + nbCommenceA9h.getValue() +
                "   " + nbTermineA16h45.getValue() +
                "     " + nbTermineA17h30.getValue() +
                "     " + nbTermineA17h45.getValue() +
                "     " + nbTermineA18h30.getValue() +
                "     " + nbCommenceA9h.getValue()
            );
            System.out.println(nbTermineA16h45.getValue() + " 6h45         16h45");
            System.out.println(nbTermineA17h45.getValue() + " 6h45                     17h45");
            System.out.println(nbTermineA17h30.getValue() + "      7h30          17h30");
            System.out.println(nbTermineA18h30.getValue() + "      7h30                      18h30");
            System.out.println(nbCommenceA9h.getValue()   + "           9h                         19h\n\n");
            i++;
        }
        System.out.println("Il y a " + i + " solutions");
        
    }
    
}