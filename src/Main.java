import static org.chocosolver.solver.search.strategy.Search.*;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import static org.chocosolver.solver.constraints.nary.cnf.LogOp.*;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

public class Main {
    
    public static void main(String[] args) {
        
        Model model = new Model();
        
        /*
        Les heures possibles sont :
        6h45 -------- 13h45
        6h45 ------------------ 16h45
        6h45 ------------------------ 17h45
             7h30 --------- 14h
             7h30 ------------------------- 17h30
             7h30 ------------------------------- 18h30
                  12h --------------------------------- 19h
        */
        
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
        IntVar[][] t = model.intVarMatrix(16, 100, 0, 100, false); // variables secondaires
        BoolVar[][] b = model.boolVarMatrix(16, 10);
        BoolVar[][] _b = model.boolVarMatrix(16, 10);
        int i, k, l, e;
                
        for (e = 0; e < 16; e++) {
            // Ajout de la contrainte de temps sur une semaine
            model.arithm(s[e], "*", model.intVar(2), "=", t[e][0]).post(); // t[e][0] contient les heures à faire en 2 semaines
            model.allEqual(t[e][1], j[e][0]).post(); // t[e][1] = j1
            for (i = 1; i < 10; i++) {
                model.arithm(t[e][i], "+", j[e][i], "=", t[e][i+1]).post();
                // t[e][10]="total des 2 semaines", t[e][5]="total 1er semaine"
            }
            model.arithm(t[e][10], "-", t[e][5], "=", t[e][11]).post(); // t[e][11]="total 2em semaine"

            model.arithm(t[e][5], "<=", 40).post(); // max 40h / semaine : semaine 1
            model.arithm(t[e][11], "<=", 40).post(); // max 40h / semaine : semaine 2
            model.arithm(t[e][12], ">=", model.intOffsetView(s[e], -6)).post(); // d'une semaine à l'autre on peut
            model.arithm(t[e][12], "<=", model.intOffsetView(s[e], +6)).post(); // varier d'un jour de travail (- de 14h)
            model.allEqual(t[e][5], t[e][12]).post(); // temps de travail de la première semaine = temps voulu +- x
            model.allEqual(t[e][10], t[e][0]).post(); // j1+j2+j3+j4+j5+j6+j7+j8+j9+j10 = 2*s

            // t[e][13] : nombre de 7h la première semaine
            t[e][17] = model.allEqual(j[e][0], model.intVar(7)).reify(); // nombre de 7h du 1er jour
            t[e][18] = model.allEqual(j[e][1], model.intVar(7)).reify(); // nombre de 7h du 2em jour
            t[e][19] = model.allEqual(j[e][2], model.intVar(7)).reify(); // nombre de 7h du 3em jour
            t[e][20] = model.allEqual(j[e][3], model.intVar(7)).reify(); // nombre de 7h du 4em jour
            t[e][21] = model.allEqual(j[e][4], model.intVar(7)).reify(); // nombre de 7h du 5em jour
            model.arithm(t[e][17], "+", t[e][18], "=", t[e][22]).post();
            model.arithm(t[e][22], "+", t[e][19], "=", t[e][23]).post();
            model.arithm(t[e][23], "+", t[e][20], "=", t[e][24]).post();
            model.arithm(t[e][24], "+", t[e][21], "=", t[e][13]).post();
            
            // t[e][14] : nombre de 7h la deuxième semaine
            t[e][25] = model.allEqual(j[e][5], model.intVar(7)).reify(); // nombre de 7h du 1er jour
            t[e][26] = model.allEqual(j[e][6], model.intVar(7)).reify(); // nombre de 7h du 2em jour
            t[e][27] = model.allEqual(j[e][7], model.intVar(7)).reify(); // nombre de 7h du 3em jour
            t[e][28] = model.allEqual(j[e][8], model.intVar(7)).reify();// nombre de 7h du 4em jour
            t[e][29] = model.allEqual(j[e][9], model.intVar(7)).reify(); // nombre de 7h du 5em jour
            model.arithm(t[e][25], "+", t[e][26], "=", t[e][30]).post();
            model.arithm(t[e][30], "+", t[e][27], "=", t[e][31]).post();
            model.arithm(t[e][31], "+", t[e][28], "=", t[e][32]).post();
            model.arithm(t[e][32], "+", t[e][29], "=", t[e][14]).post();
            
            
            // t[e][15] : nombre de 11h la première semaine
            t[e][33] = model.allEqual(j[e][0], model.intVar(11)).reify(); // nombre de 11h du 1er jour
            t[e][34] = model.allEqual(j[e][1], model.intVar(11)).reify(); // nombre de 11h du 2em jour
            t[e][35] = model.allEqual(j[e][2], model.intVar(11)).reify(); // nombre de 11h du 3em jour
            t[e][36] = model.allEqual(j[e][3], model.intVar(11)).reify(); // nombre de 11h du 4em jour
            t[e][37] = model.allEqual(j[e][4], model.intVar(11)).reify(); // nombre de 11h du 5em jour
            model.arithm(t[e][33], "+", t[e][34], "=", t[e][38]).post();
            model.arithm(t[e][38], "+", t[e][35], "=", t[e][39]).post();
            model.arithm(t[e][39], "+", t[e][33], "=", t[e][40]).post();
            model.arithm(t[e][40], "+", t[e][37], "=", t[e][15]).post();
            
            // t[e][16] : nombre de 11h la deuxième semaine
            t[e][41] = model.allEqual(j[e][5], model.intVar(11)).reify(); // nombre de 11h du 1er jour
            t[e][42] = model.allEqual(j[e][6], model.intVar(11)).reify(); // nombre de 11h du 2em jour
            t[e][43] = model.allEqual(j[e][7], model.intVar(11)).reify(); // nombre de 11h du 3em jour
            t[e][44] = model.allEqual(j[e][8], model.intVar(11)).reify(); // nombre de 11h du 4em jour
            t[e][45] = model.allEqual(j[e][9], model.intVar(11)).reify(); // nombre de 11h du 5em jour
            model.arithm(t[e][41], "+", t[e][42], "=", t[e][46]).post();
            model.arithm(t[e][46], "+", t[e][43], "=", t[e][47]).post();
            model.arithm(t[e][47], "+", t[e][44], "=", t[e][48]).post();
            model.arithm(t[e][48], "+", t[e][45], "=", t[e][16]).post();
            
            
            // t[e][49] : nombre de jours de repo de la première semaine
            t[e][51] = model.allEqual(j[e][0], model.intVar(0)).reify();
            t[e][52] = model.allEqual(j[e][1], model.intVar(0)).reify();
            t[e][53] = model.allEqual(j[e][2], model.intVar(0)).reify();
            t[e][54] = model.allEqual(j[e][3], model.intVar(0)).reify();
            t[e][55] = model.allEqual(j[e][4], model.intVar(0)).reify();
            model.arithm(t[e][51], "+", t[e][52], "=", t[e][56]).post();
            model.arithm(t[e][56], "+", t[e][53], "=", t[e][57]).post();
            model.arithm(t[e][57], "+", t[e][54], "=", t[e][58]).post();
            model.arithm(t[e][58], "+", t[e][55], "=", t[e][49]).post();
            
            // t[e][67] : nombre de jours de repo sur lundi-mercredi-vendredi de la première semaine
            model.arithm(t[e][51], "+", t[e][53], "=", t[e][69]).post();
            model.arithm(t[e][69], "+", t[e][55], "=", t[e][67]).post();
            
            // t[e][71] : nombre de jours de repo sur mardi-jeudi de la première semaine
            model.arithm(t[e][52], "+", t[e][54], "=", t[e][71]).post();
            
            // t[e][50] : nombre de jour de repo de la deuxième semaine
            t[e][59] = model.allEqual(j[e][5], model.intVar(0)).reify();
            t[e][60] = model.allEqual(j[e][6], model.intVar(0)).reify();
            t[e][61] = model.allEqual(j[e][7], model.intVar(0)).reify();
            t[e][62] = model.allEqual(j[e][8], model.intVar(0)).reify();
            t[e][63] = model.allEqual(j[e][9], model.intVar(0)).reify();
            model.arithm(t[e][59], "+", t[e][60], "=", t[e][64]).post();
            model.arithm(t[e][64], "+", t[e][61], "=", t[e][65]).post();
            model.arithm(t[e][65], "+", t[e][62], "=", t[e][66]).post();
            model.arithm(t[e][66], "+", t[e][63], "=", t[e][50]).post();
            
            // t[e][68] nombre de jours de repo sur lundi-mercredi-vendredi de la deuxième semaine
            model.arithm(t[e][59], "+", t[e][61], "=", t[e][70]).post();
            model.arithm(t[e][70], "+", t[e][63], "=", t[e][68]).post();
            
            // t[e][72] nombre de jours de repo sur mardi-jeudi de la deuxième semaine
            model.arithm(t[e][60], "+", t[e][62], "=", t[e][72]).post();

            // Contraintes des jours de repo
            // Si 1 jour : soit lundi soit mercredi soit vendredi
            // Si 2 jours : (soit lundi soit mercredi soit vendredi) et (soit mardi soit jeudi)
            // Sinon pas de contrainte ? (pas mardi et jeudi en meme temps ?)
            
            // Si 1 jour, semaine 1 :
            model.ifThen(
                model.arithm(t[e][49], "=", 1),
                model.arithm(t[e][67], "=", 1) // lundi-mercredi-vendredi
            );
            // Si 1 jour, semaine 2 :
            model.ifThen(
                model.allEqual(t[e][50], model.intVar(1)),
                model.allEqual(t[e][68], model.intVar(1)) // lundi-mercredi-vendredi
            );
            // Si 2 jours, semaine 1 :
            model.ifThen(
                model.allEqual(t[e][49], model.intVar(2)),
                model.allEqual(t[e][67], model.intVar(1)) // lundi-mercredi-vendredi
            );
            model.ifThen(
                model.allEqual(t[e][49], model.intVar(2)),
                model.allEqual(t[e][71], model.intVar(1)) // mardi-jeudi
            );
            // Si 2 jours, semaine 2 :
            model.ifThen(
                model.allEqual(t[e][50], model.intVar(2)),
                model.allEqual(t[e][68], model.intVar(1)) // lundi-mercredi-vendredi
            );
            model.ifThen(
                model.allEqual(t[e][50], model.intVar(2)),
                model.allEqual(t[e][72], model.intVar(1)) // mardi-jeudi
            );
            
            
            // Contraintes des 7h et 11h : si on fait un 11h, on ne fait pas de 7h
            model.ifThen( // première semaine
                model.arithm(t[e][15], ">", 0), // s'il y a des 11h
                model.allEqual(t[e][13], model.intVar(0)) // alors il n'y a pas de 7h
            );
            model.ifThen( // deuxième semaine
                model.arithm(t[e][16], ">", 0), // s'il y a des 11h
                model.allEqual(t[e][14], model.intVar(0)) // alors il n'y a pas de 7h
            );
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
            j[1][0], j[1][1], j[1][2], j[1][3], j[1][4], j[1][5], j[1][6], j[1][7], j[1][8], j[1][9],
            j[2][0], j[2][1], j[2][2], j[2][3], j[2][4], j[2][5], j[2][6], j[2][7], j[2][8], j[2][9],
            j[3][0], j[3][1], j[3][2], j[3][3], j[3][4], j[3][5], j[3][6], j[3][7], j[3][8], j[3][9],
            j[4][0], j[4][1], j[4][2], j[4][3], j[4][4], j[4][5], j[4][6], j[4][7], j[4][8], j[4][9],
            j[5][0], j[5][1], j[5][2], j[5][3], j[5][4], j[5][5], j[5][6], j[5][7], j[5][8], j[5][9],
            j[6][0], j[6][1], j[6][2], j[6][3], j[6][4], j[6][5], j[6][6], j[6][7], j[6][8], j[6][9],
            j[7][0], j[7][1], j[7][2], j[7][3], j[7][4], j[7][5], j[7][6], j[7][7], j[7][8], j[7][9],
            j[8][0], j[8][1], j[8][2], j[8][3], j[8][4], j[8][5], j[8][6], j[8][7], j[8][8], j[8][9],
            j[9][0], j[9][1], j[9][2], j[9][3], j[9][4], j[9][5], j[9][6], j[9][7], j[9][8], j[9][9],
            j[10][0], j[10][1], j[10][2], j[10][3], j[10][4], j[10][5], j[10][6], j[10][7], j[10][8], j[10][9],
            j[11][0], j[11][1], j[11][2], j[11][3], j[11][4], j[11][5], j[11][6], j[11][7], j[11][8], j[11][9],
            j[12][0], j[12][1], j[12][2], j[12][3], j[12][4], j[12][5], j[12][6], j[12][7], j[12][8], j[12][9],
            j[13][0], j[13][1], j[13][2]//, j[13][3], j[13][4], j[13][5], j[13][6], j[13][7], j[13][8], j[13][9],
            //j[14][0], j[14][1], j[14][2], j[14][3], j[14][4], j[14][5], j[14][6], j[14][7], j[14][8], j[14][9],
            //j[15][0], j[15][1], j[15][2], j[15][3], j[15][4], j[15][5], j[15][6], j[15][7], j[15][8], j[15][9]
        ));
        
        i = 0;
        String[] eiades = {
            "B. MICHELIN", "N. JEANJEAN", "MP. CHAUTARD", "C. JAMOIS",
            "K. REYNAUD", "E. KAID", "P. CUIROT", "R. BENZAIDE",
            "V. LEGAT", "Y. AUBERT BRUN", "V. BANCALARI", "D. SERMET",
            "F. BOULAY","V. MARDIROSSIAN","BEA REIX","MARIE"
        };
        while(solver.solve()) {
            for (k = 0; k < 16; k++) {
                System.out.println(
                    eiades[k] +
                    ", j1 = " + j[k][0].getValue() +
                    ", j2 = " + j[k][1].getValue() +
                    ", j3 = " + j[k][2].getValue() +
                    ", j4 = " + j[k][3].getValue() +
                    ", j5 = " + j[k][4].getValue() +
                    //", total s1 : " + t[k][5].getValue() +
                    //", nombre de 7h : " + t[k][13].getValue() +
                    //", nombre de 11h : " + t[k][15].getValue() +
                    ", nombre de jours de repo : " + t[k][49].getValue() +
                    ", j6 = " + j[k][5].getValue() +
                    ", j7 = " + j[k][6].getValue() +
                    ", j8 = " + j[k][7].getValue() +
                    ", j9 = " + j[k][8].getValue() +
                    ", j10 = " + j[k][9].getValue() +
                    //", total s2 : " + t[k][11].getValue() +
                    //", total prévu : " + t[k][0].getValue() +
                    //", total : trouvé " + t[k][10].getValue() +
                    //", nombre de 7h : " + t[k][14].getValue() +
                    //", nombre de 11h : " + t[k][16].getValue() +
                    ", nombre de jours de repo : " + t[k][50].getValue() +
                    ", s = " + s[k].getValue());
            }
            System.out.println();
            i++;
        }
        System.out.println("Il y a " + i + " solutions");
    }
}
