import static org.chocosolver.solver.search.strategy.Search.*;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

public class Main {
    
    public static void main(String[] args) {
        
        Model model = new Model();
        
        /*
        Les heures possibles sont :
        6h45 ------------------ 16h45
        6h45 ------------------------------ 17h45
             7h30 ------------------- 17h30
             7h30 ------------------------------- 18h30
                  12h --------------------------------- 19h
        */
        
        IntVar[] s = model.intVarArray(16, new int[]{20, 21, 25, 28, 29, 30, 31, 33, 35, 36, 39});
        model.allEqual(s[0], model.intVar(21)).post(); // B. MICHELIN, un seul 11h
        model.allEqual(s[1], model.intVar(36)).post(); // N. JEANJEAN
        model.allEqual(s[2], model.intVar(20)).post(); // MP. CHAUTARD, un seul 11h
        model.allEqual(s[3], model.intVar(30)).post(); // C. JAMOIS, un seul 11h
        model.allEqual(s[4], model.intVar(33)).post(); // K. REYNAUD
        model.allEqual(s[5], model.intVar(28)).post(); // E. KAID, un seul 11h
        model.allEqual(s[6], model.intVar(39)).post(); // P. CUIROT
        model.allEqual(s[7], model.intVar(30)).post(); // R. BENZAIDE
        model.allEqual(s[8], model.intVar(39)).post(); // V. LEGAT, un seul 11h
        model.allEqual(s[9], model.intVar(21)).post(); // Y. AUBERT BRUN
        model.allEqual(s[10], model.intVar(25)).post(); // V. BANCALARI, un seul 11h
        model.allEqual(s[11], model.intVar(30)).post(); // D. SERMET, un seul 11h
        model.allEqual(s[12], model.intVar(30)).post(); // F. BOULAY, un seul 11h
        model.allEqual(s[13], model.intVar(35)).post(); // V. MARDIROSSIAN
        model.allEqual(s[14], model.intVar(21)).post(); // BEA REIX, un seul 11h
        model.arithm(s[15], ">=", 29).post(); // MARIE, 3 jours de suite hors mardi-mercredi-jeudi
        model.arithm(s[15], "<=", 31).post(); // MARIE, 3 jours de suite hors mardi-mercredi-jeudi
        
        IntVar[][] j = model.intVarMatrix(16, 10, new int[]{0, 7, 10, 11}); // nombre d'heures de travail pour un jour
        IntVar[][] h = model.intVarMatrix(16, 10, new int[]{0, 1, 2}); // heure de départ
        IntVar[][] cr = model.intVarMatrix(16, 10, new int[]{0, 1, 2, 3, 4}); // crénaux possibles
        
        IntVar[][] t = model.intVarMatrix(17, 200, 0, 100, false); // variables secondaires
        int i, k, l, e;
        
                
        for (e = 0; e < 16; e++) {
            
            for (i = 0; i < 10; i++) {
                // Remplissage de j et h
                // j
                model.ifThen(
                    model.allEqual(cr[e][i], model.intVar(0)),
                    model.allEqual(j[e][i], model.intVar(0))
                );
                model.ifThen(
                    model.allEqual(cr[e][i], model.intVar(1)),
                    model.allEqual(j[e][i], model.intVar(10))
                );
                model.ifThen(
                    model.allEqual(cr[e][i], model.intVar(2)),
                    model.allEqual(j[e][i], model.intVar(11))      
                );
                model.ifThen(
                    model.allEqual(cr[e][i], model.intVar(3)),
                    model.allEqual(j[e][i], model.intVar(10))
                );
                model.ifThen(
                    model.allEqual(cr[e][i], model.intVar(4)),
                    model.allEqual(j[e][i], model.intVar(11))
                );
                model.ifThen(
                    model.allEqual(cr[e][i], model.intVar(5)),
                    model.allEqual(j[e][i], model.intVar(7))
                );
                
            }
            
            
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
            
            
            /*// t[e][107] : 10h la première semaine
            t[e][109] = model.allEqual(j[e][0], model.intVar(10)).reify(); // nombre de 10h du 1er jour
            t[e][110] = model.allEqual(j[e][1], model.intVar(10)).reify(); // nombre de 10h du 2em jour
            t[e][111] = model.allEqual(j[e][2], model.intVar(10)).reify(); // nombre de 10h du 3em jour
            t[e][112] = model.allEqual(j[e][3], model.intVar(10)).reify(); // nombre de 10h du 4em jour
            t[e][113] = model.allEqual(j[e][4], model.intVar(10)).reify(); // nombre de 10h du 5em jour
            
            // t[e][108] : 10h la deuxième semaine
            t[e][114] = model.allEqual(j[e][5], model.intVar(10)).reify(); // nombre de 10h du 1er jour
            t[e][115] = model.allEqual(j[e][6], model.intVar(10)).reify(); // nombre de 10h du 2em jour
            t[e][116] = model.allEqual(j[e][7], model.intVar(10)).reify(); // nombre de 10h du 3em jour
            t[e][117] = model.allEqual(j[e][8], model.intVar(10)).reify(); // nombre de 10h du 4em jour
            t[e][118] = model.allEqual(j[e][9], model.intVar(10)).reify(); // nombre de 10h du 5em jour            
            
            
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
            t[e][51] = model.allEqual(j[e][0], model.intVar(0)).reify(); // 1 si c'est un jours de repo, 0 sinon
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
            
            // t[e][73] : nombre de jours de repo sur mardi-mercredi-jeudi de la première semaine
            model.arithm(t[e][52], "+", t[e][53], "=", t[e][75]).post();
            model.arithm(t[e][75], "+", t[e][54], "=", t[e][73]).post();
            
            // t[e][50] : nombre de jour de repo de la deuxième semaine
            t[e][59] = model.allEqual(j[e][5], model.intVar(0)).reify(); // 1 si c'est un jours de repo, 0 sinon
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
            
            // t[e][74] : nombre de jours de repo sur mardi-mercredi-jeudi de la première semaine
            model.arithm(t[e][60], "+", t[e][61], "=", t[e][76]).post();
            model.arithm(t[e][76], "+", t[e][62], "=", t[e][74]).post();*/

            // Contraintes des jours de repo
            // Si 1 jour : soit lundi soit mercredi soit vendredi
            // Si 2 jours : (soit lundi soit mercredi soit vendredi) et (soit mardi soit jeudi)
            // Sinon (si 3 jours) : pas de contraintes
            
            /*// Semaine 1 : (n=t[e][49], lmv=t[e][67], mj=t[e][71])
            model.arithm(t[e][67], "<=", t[e][49]).post();
            model.arithm(t[e][67], "-", t[e][71], "<", 2).post();
            model.arithm(t[e][71], "-", t[e][67], "<", 2).post();
            model.arithm(t[e][67], "*", t[e][49], ">=", t[e][49]).post();
            
            // Semaine 2 : (n=t[e][50], lmv=t[e][68], mj=t[e][72])
            model.arithm(t[e][68], "<=", t[e][50]).post();
            model.arithm(t[e][68], "-", t[e][72], "<", 2).post();
            model.arithm(t[e][72], "-", t[e][68], "<", 2).post();
            model.arithm(t[e][68], "*", t[e][50], ">=", t[e][50]).post();*/
            
            
            /*// Contraintes des 11h : minimum 1 par semaine
            // Première semaine : (nb11h=t[e][15])
            model.arithm(t[e][15], ">", 0).post();

            // Deuxième semaine : (nb11h=t[e][16])
            model.arithm(t[e][16], ">", 0).post();*/
                    
        }
        
        // Contraintes verticales
        
        for (e = 0; e < 16; e ++) { // pour chaque eiade
            for (i = 0; i < 10; i++) { // pour chaque jour
                // si on commence à 12h, on termine à 19h ****A REFAIRE****
                /*
                // t[e][77+i] = 1 si h[e][i]=0 (commence à 6h45 le jour i) (de t[e][77] à t[e][86])
                t[e][77+i] = model.allEqual(h[e][i], model.intVar(0)).reify();
                
                // t[e][88+i] = 1 si h[e][i]=1 (commence à 7h30 le jour i) (de t[e][87] à t[e][96])
                t[e][87+i] = model.allEqual(h[e][i], model.intVar(1)).reify();
                
                // t[e][97+i] = 1 si h[e][i]=2 (commence à 12h le jour i) (de t[e][97] à t[e][106])
                t[e][97+i] = model.allEqual(h[e][i], model.intVar(2)).reify();
                
                // t[e][129+i] = 1 si termine à 16h45 le jour i (de t[e][129] à t[e][138])
                model.arithm(t[e][77+i], "*", t[e][109+i], "=", t[e][129+i]).reify();
                
                // t[e][139+i] = 1 si termine à 17h30 le jour i (de t[e][139] à t[e][148])
                model.arithm(t[e][88+i], "*", t[e][109+i], "=", t[e][139+i]).reify();
                
                // t[e][149+i] = 1 si termine à 17h45 le jour i (de t[e][149] à t[e][158])
                model.arithm(t[e][77+i], "*", t[e][(i<5?33:36)+i], "=", t[e][149+i]).reify();
                
                // t[e][159+i] = 1 si termine à 18h30 le jour i (de t[e][159] à t[e][168])
                model.arithm(t[e][88+i], "*", t[e][(i<5?33:36)+i], "=", t[e][159+i]).reify();
                
                // t[e][97+i] = 1 si h[e][i]=2 (termine à 19h le jour i) (de t[e][97] à t[e][106])*/
            }
        }
        
        /*model.allEqual(s[0], model.intVar(21)).post(); // B. MICHELIN, un seul 11h
        model.allEqual(s[1], model.intVar(36)).post(); // N. JEANJEAN
        model.allEqual(s[2], model.intVar(20)).post(); // MP. CHAUTARD, un seul 11h
        model.allEqual(s[3], model.intVar(30)).post(); // C. JAMOIS, un seul 11h
        model.allEqual(s[4], model.intVar(33)).post(); // K. REYNAUD
        model.allEqual(s[5], model.intVar(28)).post(); // E. KAID, un seul 11h
        model.allEqual(s[6], model.intVar(39)).post(); // P. CUIROT
        model.allEqual(s[7], model.intVar(30)).post(); // R. BENZAIDE
        model.allEqual(s[8], model.intVar(39)).post(); // V. LEGAT, un seul 11h
        model.allEqual(s[9], model.intVar(21)).post(); // Y. AUBERT BRUN
        model.allEqual(s[10], model.intVar(25)).post(); // V. BANCALARI, un seul 11h
        model.allEqual(s[11], model.intVar(30)).post(); // D. SERMET, un seul 11h
        model.allEqual(s[12], model.intVar(30)).post(); // F. BOULAY, un seul 11h
        model.allEqual(s[13], model.intVar(35)).post(); // V. MARDIROSSIAN
        model.allEqual(s[14], model.intVar(21)).post(); // BEA REIX, un seul 11h
        model.arithm(s[15], ">=", 29).post(); // MARIE, 3 jours de suite hors mardi-mercredi-jeudi
        model.arithm(s[15], "<=", 31).post(); // MARIE, 3 jours de suite hors mardi-mercredi-jeudi*/
        
        /*// contraintes personnelles des heures de travail
        model.allEqual(model.intVar(1),
            t[0][15], t[0][16], // B. MICHELIN, un seul 11h
            t[2][15], t[2][16], // MP. CHAUTARD, un seul 11h
            t[3][15], t[3][16], // C. JAMOIS, un seul 11h
            t[5][15], t[5][16], // E. KAID, un seul 11h
            t[8][15], t[8][16], // V. LEGAT, un seul 11h
            t[10][15], t[10][16], // V. BANCALARI, un seul 11h
            t[11][15], t[11][16], // D. SERMET, un seul 11h
            t[12][15], t[12][16], // F. BOULAY, un seul 11h
            t[14][15], t[14][15] // BEA REIX, un seul 11h
        ).post();*/
        
        
        /*model.allEqual(t[15][49], t[15][50], model.intVar(2)).post(); // MARIE, 3 jours de travail chaque semaine
        model.allEqual(t[15][51], t[15][52]).post(); // jours consécutifs : lundi-mardi - première semaine
        model.allEqual(t[15][54], t[15][55]).post(); // jours consécutifs : jeudi-vendredi - première semaine
        model.allEqual(t[15][59], t[15][60]).post(); // jours consécutifs : lundi-mardi - deuxième semaine
        model.allEqual(t[15][62], t[15][63]).post(); // jours consécutifs : jeudi-vendredi - deuxième semaine*/
        

        
        // Contraintes de chaque jour
        for (i = 0; i < 10; i++) {
            /*// t[16][i] = nombre d'eiades qui commencent à 6h45 le jours i (de t[16][0] à t[16][9])
            model.arithm(t[0][77+i], "+", t[1][77+i], "=", t[16][90]).post();
            //for (k = 0; k < 14; k++) {model.arithm(t[16][90+k], "+", t[2+k][77+i], "=", t[16][91+k]).post();}
            model.arithm(t[16][90], "+", t[2][77+i], "=", t[16][91]).post();
            model.arithm(t[16][91], "+", t[3][77+i], "=", t[16][92]).post();
            model.arithm(t[16][92], "+", t[4][77+i], "=", t[16][93]).post();
            model.arithm(t[16][93], "+", t[5][77+i], "=", t[16][94]).post();
            model.arithm(t[16][94], "+", t[6][77+i], "=", t[16][95]).post();
            model.arithm(t[16][95], "+", t[7][77+i], "=", t[16][96]).post();
            model.arithm(t[16][96], "+", t[8][77+i], "=", t[16][97]).post();
            model.arithm(t[16][97], "+", t[9][77+i], "=", t[16][98]).post();
            model.arithm(t[16][98], "+", t[10][77+i], "=", t[16][99]).post();
            model.arithm(t[16][99], "+", t[11][77+i], "=", t[16][100]).post();
            model.arithm(t[16][100], "+", t[12][77+i], "=", t[16][101]).post();
            model.arithm(t[16][101], "+", t[13][77+i], "=", t[16][102]).post();
            model.arithm(t[16][102], "+", t[14][77+i], "=", t[16][103]).post();
            model.arithm(t[16][103], "+", t[15][77+i], "=", t[16][104]).post();
            model.allEqual(t[16][104], t[16][i]).post();
            model.arithm(t[16][i], ">=", 5).post();
            model.arithm(t[16][i], "<=", 6).post();*/
            
            /*// t[16][10+i] = nombre d'eiades qui commencent à 7h30 le jours i (de t[16][10] à t[16][19])
            model.arithm(t[0][87+i], "+", t[1][87+i], "=", t[16][105]).post();
            //for (k = 0; k < 14; k++) {model.arithm(t[16][105+k], "+", t[2+k][87+i], "=", t[16][106+k]).post();}
            model.arithm(t[16][105], "+", t[2][87+i], "=", t[16][106]).post();
            model.arithm(t[16][106], "+", t[3][87+i], "=", t[16][107]).post();
            model.arithm(t[16][107], "+", t[4][87+i], "=", t[16][108]).post();
            model.arithm(t[16][108], "+", t[5][87+i], "=", t[16][109]).post();
            model.arithm(t[16][109], "+", t[6][87+i], "=", t[16][110]).post();
            model.arithm(t[16][110], "+", t[7][87+i], "=", t[16][111]).post();
            model.arithm(t[16][111], "+", t[8][87+i], "=", t[16][112]).post();
            model.arithm(t[16][112], "+", t[9][87+i], "=", t[16][113]).post();
            model.arithm(t[16][113], "+", t[10][87+i], "=", t[16][114]).post();
            model.arithm(t[16][114], "+", t[11][87+i], "=", t[16][115]).post();
            model.arithm(t[16][115], "+", t[12][87+i], "=", t[16][116]).post();
            model.arithm(t[16][116], "+", t[13][87+i], "=", t[16][117]).post();
            model.arithm(t[16][117], "+", t[14][87+i], "=", t[16][118]).post();
            model.arithm(t[16][118], "+", t[15][87+i], "=", t[16][119]).post();
            model.allEqual(t[16][119], t[16][10+i]).post();*/
            

            /*// t[16][20+i] = nombre d'eiades qui commencent à 12h le jours i (de t[16][20] à t[16][29])
            model.arithm(t[0][97+i], "+", t[1][97+i], "=", t[16][120]).post();
            //for (k = 0; k < 14; k++) {model.arithm(t[16][120+k], "+", t[2+k][97+i], "=", t[16][121+k]).post();}
            model.arithm(t[16][120], "+", t[2][97+i], "=", t[16][121]).post();
            model.arithm(t[16][121], "+", t[3][97+i], "=", t[16][122]).post();
            model.arithm(t[16][122], "+", t[4][97+i], "=", t[16][123]).post();
            model.arithm(t[16][123], "+", t[5][97+i], "=", t[16][124]).post();
            model.arithm(t[16][124], "+", t[6][97+i], "=", t[16][125]).post();
            model.arithm(t[16][125], "+", t[7][97+i], "=", t[16][126]).post();
            model.arithm(t[16][126], "+", t[8][97+i], "=", t[16][127]).post();
            model.arithm(t[16][127], "+", t[9][97+i], "=", t[16][128]).post();
            model.arithm(t[16][128], "+", t[10][97+i], "=", t[16][129]).post();
            model.arithm(t[16][129], "+", t[11][97+i], "=", t[16][130]).post();
            model.arithm(t[16][130], "+", t[12][97+i], "=", t[16][131]).post();
            model.arithm(t[16][131], "+", t[13][97+i], "=", t[16][132]).post();
            model.arithm(t[16][132], "+", t[14][97+i], "=", t[16][133]).post();
            model.arithm(t[16][133], "+", t[15][97+i], "=", t[16][134]).post();
            model.allEqual(t[16][134], t[16][20+i]).post();*/
            
            
            // t[16][30+i] = nombre d'eiades qui terminent à 13h45 le jours i (de t[16][30] à t[16][39])
            
            
            // t[16][40+i] = nombre d'eiades qui terminent à 16h45 le jours i (de t[16][40] à t[16][49])
            
            
            // t[16][50+i] = nombre d'eiades qui terminent à 17h30 le jours i (de t[16][50] à t[16][59])
            
            
            // t[16][60+i] = nombre d'eiades qui terminent à 17h45 le jours i (de t[16][60] à t[16][69])
            
            
            // t[16][70+i] = nombre d'eiades qui terminent à 18h30 le jours i (de t[16][70] à t[16][79])
            
            
            // t[16][80+i] = nombre d'eiades qui terminent à 19h le jours i (de t[16][80] à t[16][89])
            
            
        }

        Solver solver = model.getSolver();
        
        solver.setSearch(intVarSearch(s[15], // pour les heures de Marie
                
            cr[0][0], cr[0][1], cr[0][2], cr[0][3], cr[0][4], cr[0][5], cr[0][6], cr[0][7], cr[0][8], cr[0][9],
            cr[1][0], cr[1][1], cr[1][2], cr[1][3], cr[1][4], cr[1][5], cr[1][6], cr[1][7], cr[1][8], cr[1][9],
            cr[2][0], cr[2][1], cr[2][2], cr[2][3], cr[2][4], cr[2][5], cr[2][6], cr[2][7], cr[2][8], cr[2][9],
            cr[3][0], cr[3][1], cr[3][2], cr[3][3], cr[3][4], cr[3][5], cr[3][6], cr[3][7], cr[3][8], cr[3][9],
            cr[4][0], cr[4][1], cr[4][2], cr[4][3], cr[4][4], cr[4][5], cr[4][6], cr[4][7], cr[4][8], cr[4][9],
            cr[5][0], cr[5][1], cr[5][2], cr[5][3], cr[5][4], cr[5][5], cr[5][6], cr[5][7], cr[5][8], cr[5][9],
            cr[6][0], cr[6][1], cr[6][2], cr[6][3], cr[6][4], cr[6][5], cr[6][6], cr[6][7], cr[6][8], cr[6][9],
            cr[7][0], cr[7][1], cr[7][2], cr[7][3], cr[7][4], cr[7][5], cr[7][6], cr[7][7], cr[7][8], cr[7][9],
            cr[8][0], cr[8][1], cr[8][2], cr[8][3], cr[8][4], cr[8][5], cr[8][6], cr[8][7], cr[8][8], cr[8][9],
            cr[9][0], cr[9][1], cr[9][2], cr[9][3], cr[9][4], cr[9][5], cr[9][6], cr[9][7], cr[9][8], cr[9][9],
            cr[10][0], cr[10][1], cr[10][2], cr[10][3], cr[10][4], cr[10][5], cr[10][6], cr[10][7], cr[10][8], cr[10][9],
            cr[11][0], cr[11][1], cr[11][2], cr[11][3], cr[11][4], cr[11][5], cr[11][6], cr[11][7], cr[11][8], cr[11][9],
            cr[12][0], cr[12][1], cr[12][2], cr[12][3], cr[12][4], cr[12][5], cr[12][6], cr[12][7], cr[12][8], cr[12][9],
            cr[13][0], cr[13][1], cr[13][2], cr[13][3], cr[13][4], cr[13][5], cr[13][6], cr[13][7], cr[13][8], cr[13][9],
            cr[14][0], cr[14][1], cr[14][2], cr[14][3], cr[14][4], cr[14][5], cr[14][6], cr[14][7], cr[14][8], cr[14][9],
            cr[15][0], cr[15][1], cr[15][2], cr[15][3], cr[15][4], cr[15][5], cr[15][6], cr[15][7], cr[15][8], cr[15][9]
            
        ));
        
        i = 0;
        String[] eiades = {
            "B. MICHELIN", "N. JEANJEAN", "MP. CHAUTARD", "C. JAMOIS",
            "K. REYNAUD", "E. KAID", "P. CUIROT", "R. BENZAIDE",
            "V. LEGAT", "Y. AUBERT BRUN", "V. BANCALARI", "D. SERMET",
            "F. BOULAY","V. MARDIROSSIAN","BEA REIX","MARIE"
        };
        String[] horaires = {
            "6h45", "7h30", "12h"
        };
        String[] crenaux = {"repo", "6h45-16h45", "6h45-17h45", "7h30-17h30", "7h30-18h30", "12-19h"};
        
        while(solver.solve()) {
            System.out.println("nb=" + t[16][0].getValue());
            for (k = 0; k < 16; k++) {
                System.out.println(
                    eiades[k] +
                    //", j1 = " + j[k][0].getValue() + " : " + horaires[h[k][0].getValue()] +
                    ", j1 = " + crenaux[cr[k][0].getValue()] + 
                    //", j2 = " + j[k][1].getValue() + " : " + horaires[h[k][1].getValue()] +
                    ", j2 = " + crenaux[cr[k][1].getValue()] +
                    //", j3 = " + j[k][2].getValue() + " : " + horaires[h[k][2].getValue()] +
                    ", j3 = " + crenaux[cr[k][2].getValue()] +
                    //", j4 = " + j[k][3].getValue() + " : " + horaires[h[k][3].getValue()] +
                    ", j4 = " + crenaux[cr[k][3].getValue()] +
                    //", j5 = " + j[k][4].getValue() + " : " + horaires[h[k][4].getValue()] +
                    ", j5 = " + crenaux[cr[k][4].getValue()] +
                    //", total s1 : " + t[k][5].getValue() +
                    //", nombre de 7h : " + t[k][13].getValue() +
                    //", nombre de 11h : " + t[k][15].getValue() +
                    //", nombre de jours de repo : " + t[k][49].getValue() +
                    //", j6 = " + j[k][5].getValue() + " : " + horaires[h[k][5].getValue()] +
                    ", j6 = " + crenaux[cr[k][5].getValue()] +
                    //", j7 = " + j[k][6].getValue() + " : " + horaires[h[k][6].getValue()] +
                    ", j7 = " + crenaux[cr[k][6].getValue()] +
                    //", j8 = " + j[k][7].getValue() + " : " + horaires[h[k][7].getValue()] +
                    ", j8 = " + crenaux[cr[k][7].getValue()] +
                    //", j9 = " + j[k][8].getValue() + " : " + horaires[h[k][8].getValue()] +
                    ", j9 = " + crenaux[cr[k][8].getValue()] +
                    //", j10 = " + j[k][9].getValue() + " : " + horaires[h[k][9].getValue()] +
                    ", j10 = " + crenaux[cr[k][9].getValue()] +
                    //", total s2 : " + t[k][11].getValue() +
                    //", total prévu : " + t[k][0].getValue() +
                    //", total : trouvé " + t[k][10].getValue() +
                    //", nombre de 7h : " + t[k][14].getValue() +
                    //", nombre de 11h : " + t[k][16].getValue() +
                    //", nombre de jours de repo : " + t[k][50].getValue() +
                    ", s = " + s[k].getValue());
            }
            System.out.println();
            i++;
        }
        System.out.println("Il y a " + i + " solutions");
    }
}
