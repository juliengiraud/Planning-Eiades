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
        model.allEqual(s[1], model.intVar(36)).post(); // N. JEANJEAN, nb max de 11h : 10 (rien à faire)
        model.allEqual(s[2], model.intVar(20)).post(); // MP. CHAUTARD, nb max de 11h : 0
        model.allEqual(s[3], model.intVar(30)).post(); // C. JAMOIS, nb max de 11h : 1
        model.allEqual(s[4], model.intVar(33)).post(); // K. REYNAUD, nb max de 11h : 10 (rien à faire)
        model.allEqual(s[5], model.intVar(28)).post(); // E. KAID, nb max de 11h : 0
        model.allEqual(s[6], model.intVar(39)).post(); // P. CUIROT, nb max de 11h : 10 (rien à faire)
        model.allEqual(s[7], model.intVar(30)).post(); // R. BENZAIDE, nb max de 11h : 10 (rien à faire)
        model.allEqual(s[8], model.intVar(39)).post(); // V. LEGAT, nb max de 11h : 0
        model.allEqual(s[9], model.intVar(21)).post(); // Y. AUBERT BRUN, nb max de 11h : 10 (rien à faire)
        model.allEqual(s[10], model.intVar(25)).post(); // V. BANCALARI, nb max de 11h : 0
        model.allEqual(s[11], model.intVar(30)).post(); // D. SERMET, nb max de 11h : 0
        model.allEqual(s[12], model.intVar(30)).post(); // F. BOULAY, nb max de 11h : 0
        model.allEqual(s[13], model.intVar(35)).post(); // V. MARDIROSSIAN, nb max de 11h : 10 (rien à faire)
        model.allEqual(s[14], model.intVar(21)).post(); // BEA REIX, nb max de 11h : 1
        model.allEqual(s[15], model.intVar(29)).post(); // MARIE, 7 + 2*11*/
        
        IntVar[][] j = model.intVarMatrix(16, 10, new int[]{0, 7, 10, 11}); // nombre d'heures de travail pour un jour
        IntVar[][] t = model.intVarMatrix(16, 13, 0, 100, false); // variables secondaires
        BoolVar[][] b = model.boolVarMatrix(16, 10);
        BoolVar[][] _b = model.boolVarMatrix(16, 10);
        int i, k, l, e;
                
        for (e = 0; e < 2; e++) {
            // Ajout de la contrainte de temps sur une semaine
            model.arithm(s[e], "*", model.intVar(2), "=", t[e][0]).post(); // t[e][0] contient les heures à faire en 2 semaines
            model.allEqual(t[e][1], j[e][0]).post(); // t[e][1] = j1
            for (i = 1; i < 10; i++) {
                model.arithm(t[e][i], "+", j[e][i], "=", t[e][i+1]).post();
                // t[e][10]="total des 2 semaines", t[e][5]="total 1er semaine"
            }
            model.arithm(t[e][10], "-", t[e][5], "=", t[e][11]).post(); // t[e][11]="total 2em semaine"

            model.arithm(t[e][5], "<=", model.intVar(40)).post(); // max 40h / semaine : semaine 1
            model.arithm(t[e][11], "<=", model.intVar(40)).post(); // max 40h / semaine : semaine 2
            model.arithm(t[e][12], ">=", model.intOffsetView(s[e], -6)).post(); // d'une semaine à l'autre on peut
            model.arithm(t[e][12], "<=", model.intOffsetView(s[e], +6)).post(); // varier d'un jour de travail (- de 14h)
            model.allEqual(t[e][5], t[e][12]).post(); // temps de travail de la première semaine = temps voulu +- x
            model.allEqual(t[e][10], t[e][0]).post(); // j1+j2+j3+j4+j5+j6+j7+j8+j9+j10 = 2*s

            // Contraintes des jours de repo
            // (lundi=0 xor mercredi=0 xor vendredi=0) and mardi!=0 and jeudi!=0)
            // or (lundi=0 xor mercredi=0 xor vendredi=0) and (mardi=0 xor jeudi=0)
            // Version optimisée : ( !a + !c) * ( !a + !e) * (a + c + e) * ( !b + !d) * ( !c + !e) OU 20h ou 21h
            for (i = 0; i < 10; i++) {
                b[e][i] = model.allEqual(j[e][i], model.intVar(0)).reify(); // j = 0 pour ce jour
                _b[e][i] = b[e][i].not(); // j != 0 pour ce jour
            }
            for (i = 0; i < 2; i++) { // pour chaque semaine
                model.addClauses(or(
                    model.allEqual(s[e], model.intVar(20)).reify(),
                    model.allEqual(s[e], model.intVar(21)).reify(),
                    and(
                        or(_b[e][0 + 5*i], _b[e][2 + 5*i]),
                        or(_b[e][0 + 5*i], _b[e][4 + 5*i]),
                        or(b[e][0 + 5*i], b[e][2 + 5*i], b[e][4 + 5*i]),
                        or(_b[e][1 + 5*i], _b[e][3 + 5*i]),
                        or(_b[e][2 + 5*i], _b[e][4 + 5*i]))
                    )
                );
            }

            // Contraintes des 7h et 11h : si on fait un 11h, on ne fait pas de 7h
            for (l = 0; l < 2; l++) { // pour chaque semaine
                for (i = 5*l; i < 5+5*l; i++) {
                    for (k = 5*l; k < 5+5*l; k++) {
                        if (i != k) {
                            model.ifThen(
                                model.allEqual(j[e][i], model.intVar(11)),
                                model.allDifferent(j[e][k], model.intVar(7))
                            );
                        }
                    }
                }
            }
        }
        
        // Contraintes de chaque eiade
        for (i = 0; i < 10; i++) { // pour toutes les heures de travail
            model.arithm(j[0][i], "<", model.intVar(8)).post(); // B. MICHELIN, 3*7
            // N. JEANJEAN, nb max de 11h : 10 (rien à faire)
            model.arithm(j[2][i], "<", model.intVar(11)).post(); // MP. CHAUTARD, nb max de 11h : 0
            /*model.arithm(j[3][i], "", model.intVar()).post(); // C. JAMOIS, nb max de 11h : 1*/
            // K. REYNAUD, nb max de 11h : 10 (rien à faire)
            model.arithm(j[5][i], "<", model.intVar(11)).post(); // E. KAID, nb max de 11h : 0
            // P. CUIROT, nb max de 11h : 10 (rien à faire)
            // R. BENZAIDE, nb max de 11h : 10 (rien à faire)
            model.arithm(j[8][i], "<", model.intVar(11)).post(); // V. LEGAT, nb max de 11h : 0
            // Y. AUBERT BRUN, nb max de 11h : 10 (rien à faire)
            model.arithm(j[10][i], "<", model.intVar(11)).post(); // V. BANCALARI, nb max de 11h : 0
            model.arithm(j[11][i], "<", model.intVar(11)).post(); // D. SERMET, nb max de 11h : 0
            model.arithm(j[12][i], "<", model.intVar(11)).post(); // F. BOULAY, nb max de 11h : 0
            // V. MARDIROSSIAN, nb max de 11h : 10 (rien à faire)
            /*model.arithm(j[14][i], "", model.intVar()).post(); // BEA REIX, nb max de 11h : 1*/
            /*model.arithm(j[15][i], "", model.intVar()).post(); // MARIE, 7 + 2*11*/
        }
        

        Solver solver = model.getSolver();
        
        solver.setSearch(intVarSearch(
            j[0][0], j[0][1], j[0][2], j[0][3], j[0][4], j[0][5], j[0][6], j[0][7], j[0][8], j[0][9],
            j[1][0], j[1][1], j[1][2], j[1][3], j[1][4], j[1][5], j[1][6], j[1][7], j[1][8], j[1][9]
        ));
        
        
        i = 0;
        while(solver.solve()) {
            System.out.println(
                "B. MICHELIN, " +
                "j1 = " + j[0][0].getValue() +
                ", j2 = " + j[0][1].getValue() +
                ", j3 = " + j[0][2].getValue() +
                ", j4 = " + j[0][3].getValue() +
                ", j5 = " + j[0][4].getValue() +
                //", total s1 : " + t[0][5].getValue() +
                ", j6 = " + j[0][5].getValue() +
                ", j7 = " + j[0][6].getValue() +
                ", j8 = " + j[0][7].getValue() +
                ", j9 = " + j[0][8].getValue() +
                ", j10 = " + j[0][9].getValue() +
                //", total s2 : " + t[0][11].getValue() +
                //", total prévu : " + t[0][0].getValue() +
                //", total : trouvé " + t[0][10].getValue() +
                ", s = " + s[0].getValue() + "\n" +
                
                "N. JEANJEAN, " +
                "j1 = " + j[1][0].getValue() +
                ", j2 = " + j[1][1].getValue() +
                ", j3 = " + j[1][2].getValue() +
                ", j4 = " + j[1][3].getValue() +
                ", j5 = " + j[1][4].getValue() +
                ", j6 = " + j[1][5].getValue() +
                ", j7 = " + j[1][6].getValue() +
                ", j8 = " + j[1][7].getValue() +
                ", j9 = " + j[1][8].getValue() +
                ", j10 = " + j[1][9].getValue() +
                ", s = " + s[1].getValue() + "\n");
            i++;
        }
        System.out.println("Il y a " + i + " solutions");
    }
}
