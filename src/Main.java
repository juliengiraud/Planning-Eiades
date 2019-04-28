import static org.chocosolver.solver.search.strategy.Search.*;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import static org.chocosolver.solver.constraints.nary.cnf.LogOp.*;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

public class Main {
    
    public static void main(String[] args) {
        
        Model model = new Model();
        
        //IntVar s = model.intVar("s", new int[]{20, 21, 25, 29, 30, 33, 36, 39});
        IntVar s = model.intVar("s", 39);
        IntVar[] j = model.intVarArray(10, new int[]{0, 7, 10, 11}); // nombre d'heures de travail pour un jour
        IntVar[] t = model.intVarArray(12, 0, 100, false); // variables secondaires
        
        // Ajout de la contrainte de temps sur une semaine
        model.arithm(s, "*", model.intVar(2), "=", t[0]); // t[0] contient les heures à faire en 2 semaines
        model.allEqual(t[1], j[0]).post(); // t[1] = j1
        for (int i = 1; i < 10; i++) {
            model.arithm(t[i], "+", j[i], "=", t[i+1]).post(); // somme des jours (résultat dans t[10])
        }
        
        // t[11] = [s-1, s+1]
        model.arithm(t[11], ">=", model.intOffsetView(s, -1)).post(); // de combien de temps
        model.arithm(t[11], "<=", model.intOffsetView(s, +1)).post(); // on peut varier d'une semaine a l'autre ?
        model.allEqual(t[5], t[11]).post(); // temps de travail de la première semaine = temps voulu +- x
        model.allEqual(t[10], t[0]).post(); // j1+j2+j3+j4+j5+j6+j7+j8+j9+j10 = 2*s
        
        // Contraintes des jours de repo
        // (lundi=0 xor mercredi=0 xor vendredi=0) and mardi!=0 and jeudi!=0)
        // or (lundi=0 xor mercredi=0 xor vendredi=0) and (mardi=0 xor jeudi=0)
        // Version optimisée : ( !a + !c) * ( !a + !e) * (a + c + e) * ( !b + !d) * ( !c + !e) OU 20h ou 21h
        BoolVar[] b = model.boolVarArray(10);
        BoolVar[] _b = model.boolVarArray(10);
        for (int i = 0; i < 10; i++) {
            b[i] = model.allEqual(j[i], model.intVar(0)).reify(); // j = 0 pour ce jour
            _b[i] = b[i].not(); // j != 0 pour ce jour
        }
        for (int i = 0; i < 2; i++) { // pour chaque semaine
            model.addClauses(or(
                model.allEqual(s, model.intVar(20)).reify(),
                model.allEqual(s, model.intVar(21)).reify(),
                and(
                    or(_b[0 + 5*i], _b[2 + 5*i]),
                    or(_b[0 + 5*i], _b[4 + 5*i]),
                    or(b[0 + 5*i], b[2 + 5*i], b[4 + 5*i]),
                    or(_b[1 + 5*i], _b[3 + 5*i]),
                    or(_b[2 + 5*i], _b[4 + 5*i]))
                )
            );
        }
        
        // Contraintes des 7h et 11h : si on fait un 11h, on ne fait pas de 7h
        for (int l = 0; l < 2; l++) { // pour chaque semaine
            for (int i = 5*l; i < 5+5*l; i++) {
                for (int k = 5*l; k < 5+5*l; k++) {
                    if (i != k) {
                        model.ifThen(
                            model.allEqual(j[i], model.intVar(11)),
                            model.allDifferent(j[k], model.intVar(7))
                        );
                    }
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
                ", s = " + s.getValue());
            i++;
        }
        System.out.println("Il y a " + i + " solutions");
    }
}
