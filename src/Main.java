import static org.chocosolver.solver.search.strategy.Search.*;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import static org.chocosolver.solver.constraints.nary.cnf.LogOp.*;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

public class Main {
    
    public static void main(String[] args) {
        
        Model model = new Model();
        
        /*IntVar[] a = model.intVarArray(5, new int[]{0, 1, 2, 3, 4});
        IntVar[] b = model.intVarArray(5, 0, 100, false);
        IntVar s = model.intVar("s", new int[]{20, 21, 25, 29, 30, 33, 36, 39});
        
        model.arithm(a[0], "*", model.intVar(7), "=", b[0]).post(); // b[0] = 7*a
        model.arithm(a[1], "*", model.intVar(10), "=", b[1]).post(); // b[1] = 10*b
        model.arithm(a[2], "*", model.intVar(11), "=", b[2]).post(); // b[2] = 11*c
        model.arithm(b[0], "+", b[1], "=", b[3]).post(); // b[3] = 7*a+10*b
        model.arithm(b[3], "+", b[2], "=", s).post(); // s = 7*a+10*b+11*c*/
        
        // s=21, s=j1+j2+j3+j4+j5
        
        IntVar s = model.intVar("s", new int[]{20, 21, 25, 29, 30, 33, 36, 39});
        IntVar[] j = model.intVarArray(5, new int[]{0, 7, 10, 11});
        IntVar[] t = model.intVarArray(4, 0, 100, false);
        
        model.arithm(j[0], "+", j[1], "=", t[0]).post();
        model.arithm(t[0], "+", j[2], "=", t[1]).post();
        model.arithm(t[1], "+", j[3], "=", t[2]).post();
        model.arithm(t[2], "+", j[4], "=", t[3]).post();
        
        model.allEqual(t[3], s).post();
        
        // Contraintes des jours de repo
        // (lundi=0 xor mercredi=0 xor vendredi=0) and mardi!=0 and jeudi!=0)
        // or (lundi=0 xor mercredi=0 xor vendredi=0) and (mardi=0 xor jeudi=0)
        BoolVar[] b = model.boolVarArray(5);
        b[0] = model.allEqual(j[0], model.intVar(0)).reify(); // b[0] <=> (lundi == 0)
        b[1] = model.allEqual(j[1], model.intVar(0)).reify(); // b[1] <=> (mardi == 0)
        b[2] = model.allEqual(j[2], model.intVar(0)).reify(); // b[2] <=> (mercredi == 0)
        b[3] = model.allEqual(j[3], model.intVar(0)).reify(); // b[3] <=> (jeudi == 0)
        b[4] = model.allEqual(j[4], model.intVar(0)).reify(); // b[4] <=> (vendredi == 0)
        
        /*model.addClauses(
            or(
                and(b[0],       b[1].not(), b[2].not(), b[3].not(), b[4].not()),
                and(b[0],       b[1],       b[2].not(), b[3].not(), b[4].not()),
                //and(b[0],       b[1].not(), b[2].not(), b[3],       b[4].not()),
                
                and(b[0].not(), b[1].not(), b[2],       b[3].not(), b[4].not()),
                //and(b[0].not(), b[1],       b[2],       b[3].not(), b[4].not()),
                and(b[0].not(), b[1].not(), b[2],       b[3],       b[4].not()),
                
                //and(b[0].not(), b[1].not(), b[2].not(), b[3].not(), b[4]),
                and(b[0].not(), b[1],       b[2].not(), b[3].not(), b[4]),
                and(b[0].not(), b[1].not(), b[2].not(), b[3],       b[4])
            )
        );*/
        
        // Version optimisée
        model.addClauses(
            and(
                or(
                    b[0].not(),
                    b[2].not()
                ),
                or(
                    b[0].not(),
                    b[4].not()
                ),
                or(
                    b[0],
                    b[2],
                    b[4]
                ),
                or(
                    b[1].not(),
                    b[3].not()
                ),
                or(
                    b[2].not(),
                    b[4].not()
                )
            )
        );
        
        // Contraintes des 7h et 11h

        Solver solver = model.getSolver();
        
        solver.setSearch(intVarSearch(j[0], j[1], j[2], j[3], j[4]));
        
        
        int i = 0;
        while(solver.solve()) {
            System.out.println("j1 = " + j[0].getValue() +
                ", j2 = " + j[1].getValue() +
                ", j3 = " + j[2].getValue() +
                ", j4 = " + j[3].getValue() +
                ", j5 = " + j[4].getValue() +
                ", s = " + s.getValue()/* +
                ", t[0] = " + t[0].getValue() +
                ", t[1] = " + t[1].getValue() +
                ", t[2] = " + t[2].getValue() +
                ", t[3] = " + t[3].getValue()*/);
            /*System.out.println("a = " + a[0].getValue() +
                ", b = " + a[1].getValue() +
                ", c = " + a[2].getValue() +
                ", s = " + s.getValue());*/
            i++;
        }
        System.out.println("Il y a " + i + " solutions");
    }
}
