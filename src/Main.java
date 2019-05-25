import static org.chocosolver.solver.search.strategy.Search.*;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.objective.OptimizationPolicy;
import org.chocosolver.solver.variables.IntVar;

public class Main {
    
    private static Model model;
    private static IntVar[][] j, h, cr, t, t2;
    private static IntVar[] s, variateurSemaine, somme2semaines, sommeJoursRepo1erSemaine, joursRepo1erSemaine,
        joursRepo2emSemaine, variateurJoursRepo, somme1;
    private static IntVar zero, toCinqOuSix, _toCinqOuSix;
    
    private static void addConstraintOnSommeHeures1erSemaine(String op, int v) {
        for (int e = 0; e < 16; e++) {
            model.sum(new IntVar[]{
                t2[e][80], t2[e][90], t2[e][100],
                t2[e][81], t2[e][91], t2[e][101],
                t2[e][82], t2[e][92], t2[e][102],
                t2[e][83], t2[e][93], t2[e][103],
                t2[e][84], t2[e][94], t2[e][104]
            }, op, v).post();
        }
    }

    private static void addConstraintOnSommeHeures1erSemaine(String op, IntVar[] v) {
        for (int e = 0; e < 16; e++) {
            model.sum(new IntVar[]{
                t2[e][80], t2[e][90], t2[e][100],
                t2[e][81], t2[e][91], t2[e][101],
                t2[e][82], t2[e][92], t2[e][102],
                t2[e][83], t2[e][93], t2[e][103],
                t2[e][84], t2[e][94], t2[e][104]
            }, op, v[e]).post();
        }
    }
    
    private static void addConstraintOnSommeHeures2emSemaine(String op, int v) {
        for (int e = 0; e < 16; e++) {
            model.sum(new IntVar[]{
                t2[e][85], t2[e][95], t2[e][105],
                t2[e][86], t2[e][96], t2[e][106],
                t2[e][87], t2[e][97], t2[e][107],
                t2[e][88], t2[e][98], t2[e][108],
                t2[e][89], t2[e][99], t2[e][109]
            }, op, v).post();
        }
    }
    
    private static void addConstraintOnSommeHeures1erEt2emSemaine(String op, IntVar[] v) {
        for (int e = 0; e < 16; e++) {
            model.sum(new IntVar[]{
                t2[e][80], t2[e][90], t2[e][100],
                t2[e][81], t2[e][91], t2[e][101],
                t2[e][82], t2[e][92], t2[e][102],
                t2[e][83], t2[e][93], t2[e][103],
                t2[e][84], t2[e][94], t2[e][104],
                t2[e][85], t2[e][95], t2[e][105],
                t2[e][86], t2[e][96], t2[e][106],
                t2[e][87], t2[e][97], t2[e][107],
                t2[e][88], t2[e][98], t2[e][108],
                t2[e][89], t2[e][99], t2[e][109]
            }, op, v[e]).post();
        }
    }
    
    private static void addConstraintOnSommeJoursRepo1erSemaine(String op, IntVar[] v) {
        for (int e = 0; e < 16; e++) {
            model.sum(new IntVar[]{
                t2[e][0], t2[e][1], t2[e][2], t2[e][3], t2[e][4]
            }, op, v[e]).post();
        }
    }
    
    private static void addConstraintOnSommeJoursRepo2emSemaine(String op, IntVar[] v) {
        for (int e = 0; e < 16; e++) {
            model.sum(new IntVar[]{
                t2[e][5], t2[e][6], t2[e][7], t2[e][8], t2[e][9]
            }, op, v[e]).post();
        }
    }
    
    private static void addConstraintOnSommeCommenceA6h1erSemaine(String op, int v) {
        for (int i = 0; i < 5; i++) {
            model.sum(new IntVar[]{
                t2[0][20+i], t2[0][50+i],
                t2[1][20+i], t2[1][50+i],
                t2[2][20+i], t2[2][50+i],
                t2[3][20+i], t2[3][50+i],
                t2[4][20+i], t2[4][50+i],
                t2[5][20+i], t2[5][50+i],
                t2[6][20+i], t2[6][50+i],
                t2[7][20+i], t2[7][50+i],
                t2[8][20+i], t2[8][50+i],
                t2[9][20+i], t2[9][50+i],
                t2[10][20+i], t2[10][50+i],
                t2[11][20+i], t2[11][50+i],
                t2[12][20+i], t2[12][50+i],
                t2[13][20+i], t2[13][50+i],
                t2[14][20+i], t2[14][50+i],
                t2[15][20+i], t2[15][50+i]
            }, op, v).post();
        }
    }
    
    private static void addConstraintOnJour(int v, IntVar n) {
        for (int i = 0; i < 10; i++) {
            model.count(v, new IntVar[]{
                cr[0][i], cr[1][i], cr[2][i], cr[3][i], cr[4][i], cr[5][i], cr[6][i], cr[7][i],
                cr[8][i], cr[9][i], cr[10][i], cr[11][i], cr[12][i], cr[13][i], cr[14][i], cr[15][i]
            }, n).post();
        }
    }
    
    private static void addConstraintOnSommeCommenceA6h2emSemaine(String op, int v) {
        for (int i = 5; i < 10; i++) {
            model.sum(new IntVar[]{
                t2[0][20+i], t2[0][50+i],
                t2[1][20+i], t2[1][50+i],
                t2[2][20+i], t2[2][50+i],
                t2[3][20+i], t2[3][50+i],
                t2[4][20+i], t2[4][50+i],
                t2[5][20+i], t2[5][50+i],
                t2[6][20+i], t2[6][50+i],
                t2[7][20+i], t2[7][50+i],
                t2[8][20+i], t2[8][50+i],
                t2[9][20+i], t2[9][50+i],
                t2[10][20+i], t2[10][50+i],
                t2[11][20+i], t2[11][50+i],
                t2[12][20+i], t2[12][50+i],
                t2[13][20+i], t2[13][50+i],
                t2[14][20+i], t2[14][50+i],
                t2[15][20+i], t2[15][50+i]
            }, op, v).post();
        }
    }
    
    public static void main(String[] args) {
        
        model = new Model();
                
        s = model.intVarArray(16, new int[]{20, 21, 25, 28, 29, 30, 31, 33, 35, 36, 39});
        s[0] = model.intVar(21); // B. MICHELIN, un seul 11h
        s[1] = model.intVar(36); // N. JEANJEAN
        s[2] = model.intVar(20); // MP. CHAUTARD, un seul 11h
        s[3] = model.intVar(30); // C. JAMOIS, un seul 11h
        s[4] = model.intVar(33); // K. REYNAUD
        s[5] = model.intVar(28); // E. KAID, un seul 11h
        s[6] = model.intVar(39); // P. CUIROT
        s[7] = model.intVar(30); // R. BENZAIDE
        s[8] = model.intVar(39); // V. LEGAT, un seul 11h
        s[9] = model.intVar(21); // Y. AUBERT BRUN
        s[10] = model.intVar(25); // V. BANCALARI, un seul 11h
        s[11] = model.intVar(30); // D. SERMET, un seul 11h
        s[12] = model.intVar(30); // F. BOULAY, un seul 11h
        s[13] = model.intVar(35); // V. MARDIROSSIAN
        s[14] = model.intVar(21); // BEA REIX, un seul 11h
        s[15] = model.intVar(29, 31); // MARIE, 3 jours de suite hors mardi-mercredi-jeudi
        
        j = model.intVarMatrix(16, 10, new int[]{0, 7, 10, 11}); // nombre d'heures de travail pour un jour
        h = model.intVarMatrix(16, 10, new int[]{0, 1, 2}); // heure de départ
        cr = model.intVarMatrix(16, 10, new int[]{0, 1, 2, 3, 4, 5}); // crénaux possibles
        
        toCinqOuSix = model.intVar(0, 6);
        _toCinqOuSix = model.intVar(0, 6);
        model.arithm(model.intOffsetView(toCinqOuSix, -6), "-", model.intVar(0,1), "=", _toCinqOuSix).post();
        
        variateurSemaine = model.intVarArray(16, 0, 100);
        variateurJoursRepo = model.intVarArray(16, 0, 1);
        joursRepo1erSemaine = model.intVarArray(16, 0, 10);
        joursRepo2emSemaine = model.intVarArray(16, 0, 10);
        somme2semaines = model.intVarArray(16, 0, 100);
        
        t = model.intVarMatrix(17, 200, 0, 100, false); // variables secondaires
        t2 = model.intVarMatrix(17, 200, 0, 100, false); // variables secondaires
        somme1 = model.intVarArray(16, 0, 100, false); // variables secondaires
        zero = model.intVar(0);
        int i, k, l, e;        
        
        variateurJoursRepo[0] = model.intVar(0);
        variateurJoursRepo[1] = model.intVar(0);
        variateurJoursRepo[2] = model.intVar(0);
        variateurJoursRepo[3] = model.intVar(0);
        variateurJoursRepo[4] = model.intVar(0);
        variateurJoursRepo[5] = model.intVar(0);
        variateurJoursRepo[6] = model.intVar(0);
        variateurJoursRepo[7] = model.intVar(0);
        variateurJoursRepo[8] = model.intVar(0);
        variateurJoursRepo[9] = model.intVar(0);
        variateurJoursRepo[10] = model.intVar(0);
        variateurJoursRepo[11] = model.intVar(0);
        variateurJoursRepo[12] = model.intVar(0);
        variateurJoursRepo[13] = model.intVar(0);
        variateurJoursRepo[14] = model.intVar(0);
        
        for (e = 0; e < 16; e++) {
            
            for (i = 0; i < 10; i++) {
                                
                // Jours de repos (de t2[e][0] à t2[e][9])
                t2[e][i] = model.allEqual(cr[e][i], model.intVar(0)).reify(); // 1 si jour de repo
                
                // Jours de 7h (de t2[e][10] à t2[e][19])
                t2[e][10+i] = model.allEqual(cr[e][i], model.intVar(5)).reify(); // 1 si jour de 7h (12-19h)
                
                
                // Jours de 10h-1/2 (de t2[e][20] à t2[e][29])
                t2[e][20+i] = model.allEqual(cr[e][i], model.intVar(1)).reify(); // 1 si jour de 10h (6h45-16h45)
                
                // Jours de 10h-2/2 (de t2[e][30] à t2[e][39])
                t2[e][30+i] = model.allEqual(cr[e][i], model.intVar(3)).reify(); // 1 si jour de 10h (7h30-17h30)
                
                // Jours de 10h (de t2[e][40] à t2[e][49])
                model.arithm(t2[e][20+i], "+", t2[e][30+i], "=", t2[e][40+i]).post(); // 1 si jour de 10h
                

                // Jours de 11h-1/2 (de t2[e][50] à t2[e][59])
                t2[e][50+i] = model.allEqual(cr[e][i], model.intVar(2)).reify(); // 1 si jour de 11h (6h45-17h45)
                
                // Jours de 11h-2/2 (de t2[e][60] à t2[e][69])
                t2[e][60+i] = model.allEqual(cr[e][i], model.intVar(4)).reify(); // 1 si jour de 11h (7h30-18h30)
                
                // Jours de 11h (de t2[e][70] à t2[e][79])
                model.arithm(t2[e][50+i], "+", t2[e][60+i], "=", t2[e][70+i]).post(); // 1 si jour de 11h
                
                
                // 7 si le jour i fait 7h (de t2[e][80] à t2[e][89])
                model.arithm(t2[e][10+i], "*", model.intVar(7), "=", t2[e][80+i]).post();
                
                // 10 si le jour i fait 10h (de t2[e][90] à t2[e][99])
                model.arithm(t2[e][40+i], "*", model.intVar(10), "=", t2[e][90+i]).post();
                
                // 11 si le jour i fait 11h (de t2[e][100] à t2[e][109])
                model.arithm(t2[e][70+i], "*", model.intVar(11), "=", t2[e][100+i]).post();
                
            }
        }
        
        addConstraintOnJour(1, toCinqOuSix);
        addConstraintOnJour(2, _toCinqOuSix);
        
        for (e = 0; e < 16; e++) {
            
            // Ajout des contraintes de temps sur une semaine
            model.arithm(s[e], "*", model.intVar(2), "=", somme2semaines[e]).post(); // t[e][0] contient les heures à faire en 2 semaines
            
            addConstraintOnSommeHeures1erSemaine("<=", 40); // max 40h / semaine : semaine 1
            addConstraintOnSommeHeures2emSemaine("<=", 40); // max 40h / semaine : semaine 2
            
            // d'une semaine à l'autre on peut varier d'un jour de travail
            // Ajout de la variation : temps de travail de la première semaine = temps voulu +- x
            // nbjourrepo1ersemaine = nbjourrepo2emsemaine +-variateur :
            // nbjourrepo1ersemaine >= nbjourrepo2emsemaine - variateur
            // nbjourrepo1ersemaine <= nbjourrepo2emsemaine + variateur
            addConstraintOnSommeJoursRepo1erSemaine("=", joursRepo1erSemaine); // recup des jours de repo
            addConstraintOnSommeJoursRepo2emSemaine("=", joursRepo2emSemaine); // recup des jours de repo
            model.arithm(joursRepo1erSemaine[e], "+", variateurJoursRepo[e], ">=", joursRepo2emSemaine[e]).post();
            model.arithm(joursRepo1erSemaine[e], "-", variateurJoursRepo[e], "<=", joursRepo2emSemaine[e]).post();
            //addConstraintOnSommeHeures1erEt2emSemaine("=", somme2semaines); // Somme des 2 semaines = 2 * s[e] TMP
            
            addConstraintOnSommeCommenceA6h1erSemaine(">=", 5);
            addConstraintOnSommeCommenceA6h1erSemaine("<=", 6);
            addConstraintOnSommeCommenceA6h2emSemaine(">=", 5);
            addConstraintOnSommeCommenceA6h2emSemaine("<=", 6);
            
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
        /*model.setObjective(Model.MINIMIZE, variateurJoursRepo[0]);
        model.setObjective(Model.MINIMIZE, variateurJoursRepo[1]);
        model.setObjective(Model.MINIMIZE, variateurJoursRepo[2]);
        model.setObjective(Model.MINIMIZE, variateurJoursRepo[3]);
        model.setObjective(Model.MINIMIZE, variateurJoursRepo[4]);
        model.setObjective(Model.MINIMIZE, variateurJoursRepo[5]);
        model.setObjective(Model.MINIMIZE, variateurJoursRepo[6]);
        model.setObjective(Model.MINIMIZE, variateurJoursRepo[7]);
        model.setObjective(Model.MINIMIZE, variateurJoursRepo[8]);
        model.setObjective(Model.MINIMIZE, variateurJoursRepo[9]);
        model.setObjective(Model.MINIMIZE, variateurJoursRepo[10]);
        model.setObjective(Model.MINIMIZE, variateurJoursRepo[11]);
        model.setObjective(Model.MINIMIZE, variateurJoursRepo[12]);
        model.setObjective(Model.MINIMIZE, variateurJoursRepo[13]);
        model.setObjective(Model.MINIMIZE, variateurJoursRepo[14]);
        model.setObjective(Model.MINIMIZE, variateurJoursRepo[15]);*/
        
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
        
        String[] eiades = {
            "B. MICHELIN    ",
            "N. JEANJEAN    ",
            "MP. CHAUTARD   ",
            "C. JAMOIS      ",
            "K. REYNAUD     ",
            "E. KAID        ",
            "P. CUIROT      ",
            "R. BENZAIDE    ",
            "V. LEGAT       ",
            "Y. AUBERT BRUN ",
            "V. BANCALARI   ",
            "D. SERMET      ",
            "F. BOULAY      ",
            "V. MARDIROSSIAN",
            "BEA REIX       ",
            "MARIE          "
        };
        String[] crenaux = {
            "repo      ",
            "6h45-16h45",
            "6h45-17h45",
            "7h30-17h30",
            "7h30-18h30",
            "12-19h    "
        };
        
        l = 0;
        while(solver.solve()) {
            for (e = 0; e < 16; e++) {
                System.out.print(eiades[e] + " : ");
                for (i = 0; i < 10; i++) {
                    if (i != 0) System.out.print(", ");
                    System.out.print("j" + (i + 1) + " = " + crenaux[cr[e][i].getValue()]);
                    //System.out.print(", total s1 = " + t2[e][20+i].getValue());
                    //", nombre de 7h : " + t[e][13].getValue()
                    //", nombre de 11h : " + t[e][15].getValue()
                    //", nombre de jours de repo : " + t[e][49].getValue()
                    if (i == 4) System.out.print(", repo : " + joursRepo1erSemaine[e].getValue());
                    if (i == 9) System.out.print(", repo : " + joursRepo2emSemaine[e].getValue());
                    //if (i == 9) System.out.print(", total : trouvé " + somme1[e].getValue());
                    if (i == 9) System.out.print(", variateur : " + variateurJoursRepo[e].getValue());
                    //if (i == 4 || i == 9) System.out.print(", total s" + i/4 + " : " + somme1[e][i==4?14:30].getValue());
                    if (i == 9) System.out.println(", s = " + s[e].getValue());
                }
            }
            System.out.println();
            l++;
        }
        System.out.println("Il y a " + l + " solutions");
    }
}
