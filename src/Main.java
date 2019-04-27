import static org.chocosolver.solver.search.strategy.Search.*;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import static org.chocosolver.solver.constraints.nary.cnf.LogOp.*;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

public class Main {
    
    public static void main(String[] args) {
        
        Model model = new Model();
        
        // s=j1+j2+j3+j4+j5
        
        IntVar s = model.intVar("s", new int[]{20, 21, 25, 29, 30, 33, 36, 39});
        //IntVar s = model.intVar("s", 39);
        IntVar[] j = model.intVarArray(10, new int[]{0, 7, 10, 11});
        IntVar[] t = model.intVarArray(11, 0, 100, false);
        
        // Ajout de la contrainte de temps sur une semaine
        model.arithm(s, "*", model.intVar(2), "=", t[0]); // t[0] contient les heures à faire en 2 semaines
        model.arithm(j[0], "+", j[1], "=", t[1]).post(); // t[1] = j1+j2
        model.arithm(t[1], "+", j[2], "=", t[2]).post(); // t[2] = j1+j2+j3
        model.arithm(t[2], "+", j[3], "=", t[3]).post(); // t[3] = j1+j2+j3+j4
        model.arithm(t[3], "+", j[4], "=", t[4]).post(); // t[4] = j1+j2+j3+j4+j5
        model.arithm(t[4], "+", j[5], "=", t[5]).post(); // t[5] = j1+j2+j3+j4+j5+j6
        model.arithm(t[5], "+", j[6], "=", t[6]).post(); // t[6] = j1+j2+j3+j4+j5+j6+j7
        model.arithm(t[6], "+", j[7], "=", t[7]).post(); // t[7] = j1+j2+j3+j4+j5+j6+j7+j8
        model.arithm(t[7], "+", j[8], "=", t[8]).post(); // t[8] = j1+j2+j3+j4+j5+j6+j7+j8+j9
        model.arithm(t[8], "+", j[9], "=", t[9]).post(); // t[9] = j1+j2+j3+j4+j5+j6+j7+j8+j9+j10
        
        // t[10] = [s-1, s+1]
        model.arithm(t[10], ">=", model.intOffsetView(s, -1)).post();
        model.arithm(t[10], "<=", model.intOffsetView(s, +1)).post();
        model.allEqual(t[4], t[10]).post(); // temps de travail de la première semaine
        model.allEqual(t[9], t[0]).post(); // j1+j2+j3+j4+j5+j6+j7+j8+j9+j10 = 2*s
        
        // Contraintes des jours de repo
        // (lundi=0 xor mercredi=0 xor vendredi=0) and mardi!=0 and jeudi!=0)
        // or (lundi=0 xor mercredi=0 xor vendredi=0) and (mardi=0 xor jeudi=0)
        BoolVar[] b = model.boolVarArray(10);
        BoolVar[] _b = model.boolVarArray(10);
        // Première semaine
        b[0] = model.allEqual(j[0], model.intVar(0)).reify(); // b[0] <=> (lundi == 0) <=> a
        _b[0] = b[0].not();
        b[1] = model.allEqual(j[1], model.intVar(0)).reify(); // b[1] <=> (mardi == 0) <=> b
        _b[1] = b[1].not();
        b[2] = model.allEqual(j[2], model.intVar(0)).reify(); // b[2] <=> (mercredi == 0) <=> c
        _b[2] = b[2].not();
        b[3] = model.allEqual(j[3], model.intVar(0)).reify(); // b[3] <=> (jeudi == 0) <=> d
        _b[3] = b[3].not();
        b[4] = model.allEqual(j[4], model.intVar(0)).reify(); // b[4] <=> (vendredi == 0) <=> e
        _b[4] = b[4].not();
        // Deuxième semaine
        b[5] = model.allEqual(j[5], model.intVar(0)).reify();
        _b[5] = b[5].not();
        b[6] = model.allEqual(j[6], model.intVar(0)).reify();
        _b[6] = b[6].not();
        b[7] = model.allEqual(j[7], model.intVar(0)).reify();
        _b[7] = b[7].not();
        b[8] = model.allEqual(j[8], model.intVar(0)).reify();
        _b[8] = b[8].not();
        b[9] = model.allEqual(j[9], model.intVar(0)).reify();
        _b[9] = b[9].not();
        
        
        // Version optimisée : ( !a + !c) * ( !a + !e) * (a + c + e) * ( !b + !d) * ( !c + !e) OU 20h ou 21h
        // Première semaine
        model.addClauses(or(
            model.allEqual(s, model.intVar(20)).reify(),
            model.allEqual(s, model.intVar(21)).reify(),
            and(
                or(_b[0], _b[2]),
                or(_b[0], _b[4]),
                or(b[0], b[2], b[4]),
                or(_b[1], _b[3]),
                or(_b[2], _b[4]))
            )
        );
        // Deuxième semaine
        model.addClauses(or(
            model.allEqual(s, model.intVar(20)).reify(),
            model.allEqual(s, model.intVar(21)).reify(),
            and(
                or(_b[5], _b[7]),
                or(_b[5], _b[9]),
                or(b[5], b[7], b[9]),
                or(_b[6], _b[8]),
                or(_b[7], _b[9]))
            )
        );

        
        // Contraintes des 7h et 11h : si on fait un 11h, on ne fait pas de 7h
        model.ifThen(
            model.arithm(j[0], "=", 11),
            model.arithm(j[1], ">", 7)
        );
        model.ifThen(
            model.arithm(j[0], "=", 11),
            model.arithm(j[2], ">", 7)
        );
        model.ifThen(
            model.arithm(j[0], "=", 11),
            model.arithm(j[3], ">", 7)
        );
        model.ifThen(
            model.arithm(j[0], "=", 11),
            model.arithm(j[4], ">", 7)
        );
        
        model.ifThen(
            model.arithm(j[1], "=", 11),
            model.arithm(j[0], ">", 7)
        );
        model.ifThen(
            model.arithm(j[1], "=", 11),
            model.arithm(j[2], ">", 7)
        );
        model.ifThen(
            model.arithm(j[1], "=", 11),
            model.arithm(j[3], ">", 7)
        );
        model.ifThen(
            model.arithm(j[1], "=", 11),
            model.arithm(j[4], ">", 7)
        );
        
        model.ifThen(
            model.arithm(j[2], "=", 11),
            model.arithm(j[0], ">", 7)
        );
        model.ifThen(
            model.arithm(j[2], "=", 11),
            model.arithm(j[1], ">", 7)
        );
        model.ifThen(
            model.arithm(j[2], "=", 11),
            model.arithm(j[3], ">", 7)
        );
        model.ifThen(
            model.arithm(j[2], "=", 11),
            model.arithm(j[4], ">", 7)
        );
        
        model.ifThen(
            model.arithm(j[3], "=", 11),
            model.arithm(j[0], ">", 7)
        );
        model.ifThen(
            model.arithm(j[3], "=", 11),
            model.arithm(j[1], ">", 7)
        );
        model.ifThen(
            model.arithm(j[3], "=", 11),
            model.arithm(j[2], ">", 7)
        );
        model.ifThen(
            model.arithm(j[3], "=", 11),
            model.arithm(j[4], ">", 7)
        );
        
        model.ifThen(
            model.arithm(j[4], "=", 11),
            model.arithm(j[0], ">", 7)
        );
        model.ifThen(
            model.arithm(j[4], "=", 11),
            model.arithm(j[1], ">", 7)
        );
        model.ifThen(
            model.arithm(j[4], "=", 11),
            model.arithm(j[2], ">", 7)
        );
        model.ifThen(
            model.arithm(j[4], "=", 11),
            model.arithm(j[3], ">", 7)
        );
        
        // Première semaine
        for (int i = 0; i < 5; i++) {
            for (int k = 0; k < 5; k++) {
                if (i != k) {
                    model.ifThen(
                        model.allEqual(j[i], model.intVar(11)),
                        model.allDifferent(j[k], model.intVar(7))
                    );
                }
            }
        }
        // Deuxième semaine
        for (int i = 5; i < 10; i++) {
            for (int k = 5; k < 10; k++) {
                if (i != k) {
                    model.ifThen(
                        model.allEqual(j[i], model.intVar(11)),
                        model.allDifferent(j[k], model.intVar(7))
                    );
                }
            }
        }
        

        Solver solver = model.getSolver();
        
        solver.setSearch(intVarSearch(j[0], j[1], j[2], j[3], j[4], j[5], j[6], j[7], j[8], j[9]));
        
        
        int i = 0;
        while(solver.solve()) {
            System.out.println("j1 = " + j[0].getValue() +
                ", j2 = " + j[1].getValue() +
                ", j3 = " + j[2].getValue() +
                ", j4 = " + j[3].getValue() +
                ", j5 = " + j[4].getValue() +
                ", j6 = " + j[5].getValue() +
                ", j7 = " + j[6].getValue() +
                ", j8 = " + j[7].getValue() +
                ", j9 = " + j[8].getValue() +
                ", j10 = " + j[9].getValue() +
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
