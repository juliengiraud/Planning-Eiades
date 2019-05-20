import static org.chocosolver.solver.search.strategy.Search.*;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

public class Main {
    
    public static void main(String[] args) {
        
        Model model = new Model();
        
        /*
        Les heures possibles sont :
        6h45 -------- 13h45
        6h45 ------------------ 16h45
        6h45 ------------------------------ 17h45
             7h30 ------------------- 17h30
             7h30 ------------------------------- 18h30
                  12h --------------------------------- 19h
        */
        
        IntVar[] s = model.intVarArray(16, new int[]{20, 21, 25, 28, 29, 30, 31, 33, 35, 36, 39});
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
        model.arithm(s[15], ">=", 29).post(); // MARIE, 3 jours de suite hors mardi-mercredi-jeudi
        model.arithm(s[15], "<=", 31).post(); // MARIE, 3 jours de suite hors mardi-mercredi-jeudi
        
        IntVar[][] j = model.intVarMatrix(16, 10, new int[]{0, 7, 10, 11}); // nombre d'heures de travail pour un jour
        IntVar[][] h = model.intVarMatrix(16, 10, new int[]{0, 1, 2}); // heure de départ
        IntVar[][] t = model.intVarMatrix(17, 200, 0, 100, false); // variables secondaires
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
            t[e][28] = model.allEqual(j[e][8], model.intVar(7)).reify(); // nombre de 7h du 4em jour
            t[e][29] = model.allEqual(j[e][9], model.intVar(7)).reify(); // nombre de 7h du 5em jour
            model.arithm(t[e][25], "+", t[e][26], "=", t[e][30]).post();
            model.arithm(t[e][30], "+", t[e][27], "=", t[e][31]).post();
            model.arithm(t[e][31], "+", t[e][28], "=", t[e][32]).post();
            model.arithm(t[e][32], "+", t[e][29], "=", t[e][14]).post();
            
            
            // t[e][107] : 10h la première semaine
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
            model.arithm(t[e][76], "+", t[e][62], "=", t[e][74]).post();

            // Contraintes des jours de repo
            // Si 1 jour : soit lundi soit mercredi soit vendredi
            // Si 2 jours : (soit lundi soit mercredi soit vendredi) et (soit mardi soit jeudi)
            // Sinon (si 3 jours) : pas de contraintes
            
            // Semaine 1 : (n=t[e][49], lmv=t[e][67], mj=t[e][71])
            model.arithm(t[e][67], "<=", t[e][49]).post();
            model.arithm(t[e][67], "-", t[e][71], "<", 2).post();
            model.arithm(t[e][71], "-", t[e][67], "<", 2).post();
            model.arithm(t[e][67], "*", t[e][49], ">=", t[e][49]).post();
            
            // Semaine 2 : (n=t[e][50], lmv=t[e][68], mj=t[e][72])
            model.arithm(t[e][68], "<=", t[e][50]).post();
            model.arithm(t[e][68], "-", t[e][72], "<", 2).post();
            model.arithm(t[e][72], "-", t[e][68], "<", 2).post();
            model.arithm(t[e][68], "*", t[e][50], ">=", t[e][50]).post();
            
            
            // Contraintes des 7h et 11h : si on fait un 11h, on ne fait pas de 7h SAUF POUR MARIE (e=15)
            if (e != 15) {
                // Première semaine : (nb7h=t[e][13], nb11h=t[e][15])
                model.arithm(t[e][13], "*", t[e][15], "=", 0).post();

                // Deuxième semaine : (nb7h=t[e][14], nb11h=t[e][16])
                model.arithm(t[e][14], "*", t[e][16], "=", 0).post();
            }
            
        }
        
        // Contraintes verticales
        
        for (e = 0; e < 16; e ++) { // pour chaque eiade
            for (i = 0; i < 10; i++) { // pour chaque jour
                // si on commence à 12h, on termine à 19h
                // Si on commence à 7h30 on ne fait pas de 7h
                IntVar h2 = model.intVar(0, 50);
                IntVar h2j = model.intVar(0, 50);
                model.arithm(h[e][i], "*", model.intVar(2), "=", h2).post();
                model.arithm(h2, "+", j[e][i], "=", h2j).post();
                model.allDifferent(
                    h2j,
                    model.intVar(2),
                    model.intVar(4),
                    model.intVar(9),
                    model.intVar(14),
                    model.intVar(15)
                ).post();
                /*model.ifThen( // h[e][i]=2 -> j[e][i]=7
                    model.allEqual(h[e][i], model.intVar(2)),
                    model.allEqual(j[e][i], model.intVar(7))
                );
                model.ifThen( // h[e][i]=1 -> j[e][i]>7
                    model.allEqual(h[e][i], model.intVar(1)),
                    model.arithm(j[e][i], ">", 7)
                );*/
                
                // t[e][77+i] = 1 si h[e][i]=0 (commence à 6h45 le jour i) (de t[e][77] à t[e][86])
                t[e][77+i] = model.allEqual(h[e][i], model.intVar(0)).reify();
                
                // t[e][88+i] = 1 si h[e][i]=1 (commence à 7h30 le jour i) (de t[e][87] à t[e][96])
                t[e][87+i] = model.allEqual(h[e][i], model.intVar(1)).reify();
                
                // t[e][97+i] = 1 si h[e][i]=2 (commence à 12h le jour i) (de t[e][97] à t[e][106])
                t[e][97+i] = model.allEqual(h[e][i], model.intVar(2)).reify();
                
                // t[e][119+i] = 1 si termine à 13h45 le jour i (de t[e][119] à t[e][128])
                model.arithm(t[e][77+i], "*", t[e][(i<5?17:20)+i], "=", t[e][119+i]).reify();
                
                // t[e][129+i] = 1 si termine à 16h45 le jour i (de t[e][129] à t[e][138])
                model.arithm(t[e][77+i], "*", t[e][109+i], "=", t[e][129+i]).reify();
                
                // t[e][139+i] = 1 si termine à 17h30 le jour i (de t[e][139] à t[e][148])
                model.arithm(t[e][88+i], "*", t[e][109+i], "=", t[e][139+i]).reify();
                
                // t[e][149+i] = 1 si termine à 17h45 le jour i (de t[e][149] à t[e][158])
                model.arithm(t[e][77+i], "*", t[e][(i<5?33:36)+i], "=", t[e][149+i]).reify();
                
                // t[e][159+i] = 1 si termine à 18h30 le jour i (de t[e][159] à t[e][168])
                model.arithm(t[e][88+i], "*", t[e][(i<5?33:36)+i], "=", t[e][159+i]).reify();
                
                // t[e][97+i] = 1 si h[e][i]=2 (termine à 19h le jour i) (de t[e][97] à t[e][106])
            }
        }
        
        
        // Contraintes de chaque jour
        for (i = 0; i < 10; i++) {
            // contraintes personnelles des heures de travail
            model.arithm(j[0][i], "<", model.intVar(8)).post(); // B. MICHELIN, 3*7
            model.arithm(j[2][i], "<", model.intVar(11)).post(); // MP. CHAUTARD, nb max de 11h : 0
            model.arithm(j[5][i], "<", model.intVar(11)).post(); // E. KAID, nb max de 11h : 0
            model.arithm(j[8][i], "<", model.intVar(11)).post(); // V. LEGAT, nb max de 11h : 0
            model.arithm(j[10][i], "<", model.intVar(11)).post(); // V. BANCALARI, nb max de 11h : 0
            model.arithm(j[11][i], "<", model.intVar(11)).post(); // D. SERMET, nb max de 11h : 0
            model.arithm(j[12][i], "<", model.intVar(11)).post(); // F. BOULAY, nb max de 11h : 0
            
            // t[16][i] = nombre d'eiades qui commencent à 6h45 le jours i (de t[16][0] à t[16][9])
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
            model.arithm(t[16][i], "<=", 6).post();
            
            // t[16][10+i] = nombre d'eiades qui commencent à 7h30 le jours i (de t[16][10] à t[16][19])
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
            model.allEqual(t[16][119], t[16][10+i]).post();
            

            // t[16][20+i] = nombre d'eiades qui commencent à 12h le jours i (de t[16][20] à t[16][29])
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
            model.allEqual(t[16][134], t[16][20+i]).post();
            
            
            // t[16][30+i] = nombre d'eiades qui terminent à 13h45 le jours i (de t[16][30] à t[16][39])
            
            
            // t[16][40+i] = nombre d'eiades qui terminent à 16h45 le jours i (de t[16][40] à t[16][49])
            
            
            // t[16][50+i] = nombre d'eiades qui terminent à 17h30 le jours i (de t[16][50] à t[16][59])
            
            
            // t[16][60+i] = nombre d'eiades qui terminent à 17h45 le jours i (de t[16][60] à t[16][69])
            
            
            // t[16][70+i] = nombre d'eiades qui terminent à 18h30 le jours i (de t[16][70] à t[16][79])
            
            
            // t[16][80+i] = nombre d'eiades qui terminent à 19h le jours i (de t[16][80] à t[16][89])
            
            
        }
        model.arithm(t[3][15], "<=", 1).post(); // C. JAMOIS, nb max de 11h : 1 - Première semaine
        model.arithm(t[3][16], "<=", 1).post(); // C. JAMOIS, nb max de 11h : 1 - Deuxième semaine
        model.arithm(t[14][15], "<=", 1).post(); // BEA REIX, nb max de 11h : 1 - Première semaine
        model.arithm(t[14][16], "<=", 1).post(); // BEA REIX, nb max de 11h : 1 - Deuxième semaine
        model.allEqual(t[15][49], t[15][50], model.intVar(2)).post(); // MARIE, 3 jours de travail chaque semaine
        model.allEqual(t[15][51], t[15][52]).post(); // jours consécutifs : lundi-mardi - première semaine
        model.allEqual(t[15][54], t[15][55]).post();// jours consécutifs : jeudi-vendredi - première semaine
        model.allEqual(t[15][59], t[15][60]).post(); // jours consécutifs : lundi-mardi - deuxième semaine
        model.allEqual(t[15][62], t[15][63]).post();// jours consécutifs : jeudi-vendredi - deuxième semaine
        

        Solver solver = model.getSolver();
        
        solver.setSearch(intVarSearch(s[15],
                
            h[0][0], h[0][1], h[0][2], h[0][3], h[0][4], h[0][5], h[0][6], h[0][7], h[0][8], h[0][9],
            h[1][0], h[1][1], h[1][2], h[1][3], h[1][4], h[1][5], h[1][6], h[1][7], h[1][8], h[1][9],
            h[2][0], h[2][1], h[2][2], h[2][3], h[2][4], h[2][5], h[2][6], h[2][7], h[2][8], h[2][9],
            h[3][0], h[3][1], h[3][2], h[3][3], h[3][4], h[3][5], h[3][6], h[3][7], h[3][8], h[3][9],
            h[4][0], h[4][1], h[4][2], h[4][3], h[4][4], h[4][5], h[4][6], h[4][7], h[4][8], h[4][9],
            h[5][0], h[5][1], h[5][2], h[5][3], h[5][4], h[5][5], h[5][6], h[5][7], h[5][8], h[5][9],
            h[6][0], h[6][1], h[6][2], h[6][3], h[6][4], h[6][5], h[6][6], h[6][7], h[6][8], h[6][9],
            h[7][0], h[7][1], h[7][2], h[7][3], h[7][4], h[7][5], h[7][6], h[7][7], h[7][8], h[7][9],
            h[8][0], h[8][1], h[8][2], h[8][3], h[8][4], h[8][5], h[8][6], h[8][7], h[8][8], h[8][9],
            h[9][0], h[9][1], h[9][2], h[9][3], h[9][4], h[9][5], h[9][6], h[9][7], h[9][8], h[9][9],
            h[10][0], h[10][1], h[10][2], h[10][3], h[10][4], h[10][5], h[10][6], h[10][7], h[10][8], h[10][9],
            h[11][0], h[11][1], h[11][2], h[11][3], h[11][4], h[11][5], h[11][6], h[11][7], h[11][8], h[11][9],
            h[12][0], h[12][1], h[12][2], h[12][3], h[12][4], h[12][5], h[12][6], h[12][7], h[12][8], h[12][9],
            h[13][0], h[13][1], h[13][2], h[13][3], h[13][4], h[13][5], h[13][6], h[13][7], h[13][8], h[13][9],
            h[14][0], h[14][1], h[14][2], h[14][3], h[14][4], h[14][5], h[14][6], h[14][7], h[14][8], h[14][9],
            h[15][0], h[15][1], h[15][2], h[15][3], h[15][4], h[15][5], h[15][6], h[15][7], h[15][8], h[15][9],
            
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
            j[13][0], j[13][1], j[13][2], j[13][3], j[13][4], j[13][5], j[13][6], j[13][7], j[13][8], j[13][9],
            j[14][0], j[14][1], j[14][2], j[14][3], j[14][4], j[14][5], j[14][6], j[14][7], j[14][8], j[14][9],
            j[15][0], j[15][1], j[15][2], j[15][3], j[15][4], j[15][5], j[15][6], j[15][7], j[15][8], j[15][9]
            
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
        while(solver.solve()) {
            System.out.println("nb=" + t[16][0].getValue());
            for (k = 0; k < 16; k++) {
                System.out.println(
                    eiades[k] +
                    ", j1 = " + j[k][0].getValue() + " : " + horaires[h[k][0].getValue()] +
                    ", j2 = " + j[k][1].getValue() + " : " + horaires[h[k][1].getValue()] +
                    ", j3 = " + j[k][2].getValue() + " : " + horaires[h[k][2].getValue()] +
                    ", j4 = " + j[k][3].getValue() + " : " + horaires[h[k][3].getValue()] +
                    ", j5 = " + j[k][4].getValue() + " : " + horaires[h[k][4].getValue()] +
                    //", total s1 : " + t[k][5].getValue() +
                    //", nombre de 7h : " + t[k][13].getValue() +
                    //", nombre de 11h : " + t[k][15].getValue() +
                    //", nombre de jours de repo : " + t[k][49].getValue() +
                    ", j6 = " + j[k][5].getValue() + " : " + horaires[h[k][5].getValue()] +
                    ", j7 = " + j[k][6].getValue() + " : " + horaires[h[k][6].getValue()] +
                    ", j8 = " + j[k][7].getValue() + " : " + horaires[h[k][7].getValue()] +
                    ", j9 = " + j[k][8].getValue() + " : " + horaires[h[k][8].getValue()] +
                    ", j10 = " + j[k][9].getValue() + " : " + horaires[h[k][9].getValue()] +
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
