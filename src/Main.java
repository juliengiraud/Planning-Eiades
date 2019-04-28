import static org.chocosolver.solver.search.strategy.Search.*;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import static org.chocosolver.solver.constraints.nary.cnf.LogOp.*;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

public class Main {
    
    public static void main(String[] args) {
        
        Model model = new Model();
        
        IntVar[] s = model.intVarArray(16, new int[]{20, 21, 25, 29, 28, 30, 33, 35, 36, 39});
        model.allEqual(s[0], model.intVar(21)).post(); // B. MICHELIN, 3*7
        model.allEqual(s[1], model.intVar(36)).post(); // N. JEANJEAN, nb max de 11h : 10
        model.allEqual(s[2], model.intVar(20)).post(); // MP. CHAUTARD, nb max de 11h : 0
        model.allEqual(s[3], model.intVar(30)).post(); // C. JAMOIS, nb max de 11h : 1
        model.allEqual(s[4], model.intVar(33)).post(); // K. REYNAUD, nb max de 11h : 10
        model.allEqual(s[5], model.intVar(28)).post(); // E. KAID, nb max de 11h : 0
        model.allEqual(s[6], model.intVar(39)).post(); // P. CUIROT, nb max de 11h : 10
        model.allEqual(s[7], model.intVar(30)).post(); // R. BENZAIDE, nb max de 11h : 10
        model.allEqual(s[8], model.intVar(39)).post(); // V. LEGAT, nb max de 11h : 0
        model.allEqual(s[9], model.intVar(21)).post(); // Y. AUBERT BRUN, nb max de 11h : 10
        model.allEqual(s[10], model.intVar(25)).post(); // V. BANCALARI, nb max de 11h : 0
        model.allEqual(s[11], model.intVar(30)).post(); // D. SERMET, nb max de 11h : 0
        model.allEqual(s[12], model.intVar(30)).post(); // F. BOULAY, nb max de 11h : 0
        model.allEqual(s[13], model.intVar(35)).post(); // V. MARDIROSSIAN, nb max de 11h : 10
        model.allEqual(s[14], model.intVar(21)).post(); // BEA REIX, nb max de 11h : 1
        model.allEqual(s[15], model.intVar(29)).post(); // MARIE, 7 + 2*11*/
        
        IntVar[][] j = model.intVarMatrix(16, 10, new int[]{0, 7, 10, 11}); // nombre d'heures de travail pour un jour
        IntVar[][] t = model.intVarMatrix(16, 13, 0, 100, false); // variables secondaires
        
        for (int i = 0; i < 10; i++) {
            model.arithm(j[0][i], "<", model.intVar(8)).post(); // B. MICHELIN, 3*7
        }
        
        // Ajout de la contrainte de temps sur une semaine
        model.arithm(s[0], "*", model.intVar(2), "=", t[0][0]); // t[0] contient les heures à faire en 2 semaines
        model.allEqual(t[0][1], j[0][0]).post(); // t[1] = j1
        for (int i = 1; i < 10; i++) {
            model.arithm(t[0][i], "+", j[0][i], "=", t[0][i+1]).post(); // t[10]="total des 2 semaines", t[5]="total 1er semaine"
        }
        model.arithm(t[0][10], "-", t[0][5], "=", t[0][11]).post(); // t[11]="total 2em semaine"
        
        model.arithm(t[0][5], "<=", model.intVar(40)).post(); // max 40h / semaine : semaine 1
        model.arithm(t[0][11], "<=", model.intVar(40)).post(); // max 40h / semaine : semaine 2
        model.arithm(t[0][12], ">=", model.intOffsetView(s[0], -1)).post(); // de combien de temps
        model.arithm(t[0][12], "<=", model.intOffsetView(s[0], +1)).post(); // on peut varier d'une semaine a l'autre ?
        model.allEqual(t[0][5], t[0][12]).post(); // temps de travail de la première semaine = temps voulu +- x
        model.allEqual(t[0][10], t[0][0]).post(); // j1+j2+j3+j4+j5+j6+j7+j8+j9+j10 = 2*s
        
        // Contraintes des jours de repo
        // (lundi=0 xor mercredi=0 xor vendredi=0) and mardi!=0 and jeudi!=0)
        // or (lundi=0 xor mercredi=0 xor vendredi=0) and (mardi=0 xor jeudi=0)
        // Version optimisée : ( !a + !c) * ( !a + !e) * (a + c + e) * ( !b + !d) * ( !c + !e) OU 20h ou 21h
        BoolVar[] b = model.boolVarArray(10);
        BoolVar[] _b = model.boolVarArray(10);
        for (int i = 0; i < 10; i++) {
            b[i] = model.allEqual(j[0][i], model.intVar(0)).reify(); // j = 0 pour ce jour
            _b[i] = b[i].not(); // j != 0 pour ce jour
        }
        for (int i = 0; i < 2; i++) { // pour chaque semaine
            model.addClauses(or(
                model.allEqual(s[0], model.intVar(20)).reify(),
                model.allEqual(s[0], model.intVar(21)).reify(),
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
                            model.allEqual(j[0][i], model.intVar(11)),
                            model.allDifferent(j[0][k], model.intVar(7))
                        );
                    }
                }
            }
        }
        

        Solver solver = model.getSolver();
        
        solver.setSearch(intVarSearch(j[0][0], j[0][1], j[0][2], j[0][3], j[0][4], j[0][5],
            j[0][6], j[0][7], j[0][8], j[0][9]));
        
        
        int i = 0;
        while(solver.solve()) {
            System.out.println(
                "j1 = " + j[0][0].getValue() +
                ", j2 = " + j[0][1].getValue() +
                ", j3 = " + j[0][2].getValue() +
                ", j4 = " + j[0][3].getValue() +
                ", j5 = " + j[0][4].getValue() +
                ", j6 = " + j[0][5].getValue() +
                ", j7 = " + j[0][6].getValue() +
                ", j8 = " + j[0][7].getValue() +
                ", j9 = " + j[0][8].getValue() +
                ", j10 = " + j[0][9].getValue() +
                ", s = " + s[0].getValue());
            i++;
        }
        System.out.println("Il y a " + i + " solutions");
    }
}
