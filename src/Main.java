import static org.chocosolver.solver.search.strategy.Search.*;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

public class Main {
    
    private static Model model;
    private static IntVar[][] j, h;
    private static IntVar[] s, variateurSemaine, somme2semaines, sommeJoursRepo1erSemaine, joursRepo1erSemaine,
        joursRepo2emSemaine, variateurJoursRepo, somme1;
    private static IntVar zero, un, deux, trois, deuxOuTrois, cinqOuSix, huit, onze;
    
    private static int nb_eiades, i, k, l, e;
    
    private static void addConstraintOnSommeHeures1erSemaine(String op, int v) {
        for (e = 0; e < nb_eiades; e++) {
            model.sum(new IntVar[]{
                h[e][0], h[e][1], h[e][2], h[e][3], h[e][4]
            }, op, v).post();
        }
    }
    private static void addConstraintOnSommeHeures1erSemaine(String op, IntVar[] v) {
        for (e = 0; e < nb_eiades; e++) {
            model.sum(new IntVar[]{
                h[e][0], h[e][1], h[e][2], h[e][3], h[e][4]
            }, op, v[e]).post();
        }
    }
    
    private static void addConstraintOnSommeHeures2emSemaine(String op, int v) {
        for (e = 0; e < nb_eiades; e++) {
            model.sum(new IntVar[]{
                h[e][5], h[e][6], h[e][7], h[e][8], h[e][9]
            }, op, v).post();
        }
    }
    
    private static void addConstraintOnSommeHeures1erEt2emSemaine(String op, IntVar[] v) {
        for (e = 0; e < nb_eiades; e++) {
            model.sum(new IntVar[]{
                j[e][0], j[e][1], j[e][2], j[e][3], j[e][4], j[e][5], j[e][6], j[e][7], j[e][8], j[e][9]
            }, op, v[e]).post();
        }
    }
    
    private static void addConstraintOnSommeJoursRepo1erSemaine(String op, IntVar[] v) {
        for (e = 0; e < nb_eiades; e++) {
            model.sum(new IntVar[]{
            }, op, v[e]).post();
        }
    }
    
    private static void addConstraintOnSommeJoursRepo2emSemaine(String op, IntVar[] v) {
        for (e = 0; e < nb_eiades; e++) {
            model.sum(new IntVar[]{
            }, op, v[e]).post();
        }
    }
    
    public static void main(String[] args) {
        
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
            "MARIE          ",
            "Intérimaire    "
        };
        String[] horaires = {
            "repos   ", "6h45-", "7h30-", "  8h-"
        };
        String[] duree = {
            "", "", "", "", "", "", "", "", "", "", "10h", "11h"
        };
        
        nb_eiades = eiades.length;
        
        model = new Model();
                
        s = model.intVarArray(nb_eiades, 20, 41);
        s[0] = model.intVar(20); // B. MICHELIN, 2x10h, pas de 11h
        s[1] = model.intVar(36); // N. JEANJEAN
        s[2] = model.intVar(21); // MP. CHAUTARD, un seul 11h
        s[3] = model.intVar(31); // C. JAMOIS, un seul 11h
        s[4] = model.intVar(33); // K. REYNAUD 3x11h
        s[5] = model.intVar(31); // E. KAID, un seul 11h
        s[6] = model.intVar(41); // P. CUIROT
        s[7] = model.intVar(31); // R. BENZAIDE
        s[8] = model.intVar(41); // V. LEGAT, un seul 11h
        s[9] = model.intVar(21); // Y. AUBERT BRUN
        s[10] = model.intVar(26); // V. BANCALARI, un seul 11h
        s[11] = model.intVar(31); // D. SERMET, un seul 11h
        s[12] = model.intVar(31); // F. BOULAY, un seul 11h
        s[13] = model.intVar(36); // V. MARDIROSSIAN
        s[14] = model.intVar(21); // BEA REIX, un seul 11h
        s[15] = model.intVar(29, 31); // MARIE, 3 jours de suite hors mardi-mercredi-jeudi possible 31.5h ?
        s[16] = model.intVar(41); // intérimaire
        
        j = model.intVarMatrix(nb_eiades, 10, new int[]{0, 10, 11}); // nombre d'heures de travail pour un jour
        h = model.intVarMatrix(nb_eiades, 10, 0, 3); // heure de départ (0 -> repos, 1 -> 6h45, 2 -> 7h30, 3 -> 8h)
        
        variateurSemaine = model.intVarArray(nb_eiades, 0, 100);
        variateurJoursRepo = model.intVarArray(nb_eiades, 0, 1);
        joursRepo1erSemaine = model.intVarArray(nb_eiades, 0, 10);
        joursRepo2emSemaine = model.intVarArray(nb_eiades, 0, 10);
        somme2semaines = model.intVarArray(nb_eiades, 0, 100);
        
        zero = model.intVar(0);
        un = model.intVar(1);
        deux = model.intVar(2);
        trois = model.intVar(3);
        deuxOuTrois = model.intVar(2, 3); // huitMoinsCinqOuSix
        cinqOuSix = model.intVar(5, 6);
        huit = model.intVar(8);
        onze = model.intVar(11);
        
        model.arithm(huit, "-", cinqOuSix, "=", deuxOuTrois).post(); // huitMoinsCinqOuSix
        
        
        variateurJoursRepo[0] = zero;
        variateurJoursRepo[1] = zero;
        variateurJoursRepo[2] = zero;
        variateurJoursRepo[3] = zero;
        variateurJoursRepo[4] = zero;
        variateurJoursRepo[5] = zero;
        variateurJoursRepo[6] = zero;
        variateurJoursRepo[7] = zero;
        variateurJoursRepo[8] = zero;
        variateurJoursRepo[9] = zero;
        variateurJoursRepo[10] = zero;
        variateurJoursRepo[11] = zero;
        variateurJoursRepo[12] = zero;
        variateurJoursRepo[13] = zero;
        variateurJoursRepo[14] = zero;
        variateurJoursRepo[15] = zero;
        variateurJoursRepo[16] = zero;
        
        // Contraintes du nombre d'eiades par créneaux horaires pour chaque jour
        addCountOnCommenceA6h45(cinqOuSix);
        addCountOnCommenceA7h30(deuxOuTrois);
        addCountOnCommenceA8h(deux);
        
        for (e = 0; e < nb_eiades; e++) {
            for (i = 0; i < 10; i++) {
                /* Les seules horaires possibles sont :
                j=0  et h=0 -> repos
                j=10 et h=1 -> 6h45---------16h45
                j=10 et h=2   ->    7h30----------17h30
                j=11 et h=1 -> 6h45---------------------17h45
                j=11 et h=2   ->    7h30----------------------18h30
                j=11 et h=3     ->       8h-------------------------19h*/
                model.sum(new IntVar[]{h[e][i], j[e][i]}, "=", model.intVar(new int[]{0, 11, 12, 13, 14})).post();
                model.count(model.intVar(new int[]{3, 10}), new IntVar[]{h[e][i], j[e][i]}, model.intVar(0)).post();
                model.count(model.intVar(new int[]{0, 11}), new IntVar[]{h[e][i], j[e][i]}, model.intVar(0)).post();
            }
        }
        
        for (e = 0; e < nb_eiades; e++) {
            // Ajout des contraintes de temps sur une semaine
            model.arithm(s[e], "*", deux, "=", somme2semaines[e]).post(); // t[e][0] contient les heures à faire en 2 semaines
        }
        
        addConstraintOnSommeHeures1erSemaine("<=", 41); // max 40h / semaine : semaine 1
        addConstraintOnSommeHeures2emSemaine("<=", 41); // max 40h / semaine : semaine 2
            
        // d'une semaine à l'autre on peut varier d'un jour de travail
        // Ajout de la variation : temps de travail de la première semaine = temps voulu +- x
        // nbjourrepos1ersemaine = nbjourrepos2emsemaine +-variateur :
        // nbjourrepos1ersemaine >= nbjourrepos2emsemaine - variateur
        // nbjourrepos1ersemaine <= nbjourrepos2emsemaine + variateur
        addConstraintOnSommeJoursRepo1erSemaine("=", joursRepo1erSemaine); // recup des jours de repos
        addConstraintOnSommeJoursRepo2emSemaine("=", joursRepo2emSemaine); // recup des jours de repos
        for (e = 0; e < nb_eiades; e++) {
            model.arithm(joursRepo1erSemaine[e], "+", variateurJoursRepo[e], ">=", joursRepo2emSemaine[e]).post();
            model.arithm(joursRepo1erSemaine[e], "-", variateurJoursRepo[e], "<=", joursRepo2emSemaine[e]).post();
        }
        addConstraintOnSommeHeures1erEt2emSemaine("=", somme2semaines); // Somme des 2 semaines = 2 * s[e]
            
            
        // Contraintes des jours de repos
        // Si 1 jour : soit lundi soit mercredi soit vendredi
        // Si 2 jours : (soit lundi soit mercredi soit vendredi) et (soit mardi soit jeudi)
        // Sinon (si 3 jours) : pas de contraintes
        
        
        
        /*model.allEqual(t[15][49], t[15][50], model.intVar(2)).post(); // MARIE, 3 jours de travail chaque semaine
        model.allEqual(t[15][51], t[15][52]).post(); // jours consécutifs : lundi-mardi - première semaine
        model.allEqual(t[15][54], t[15][55]).post(); // jours consécutifs : jeudi-vendredi - première semaine
        model.allEqual(t[15][59], t[15][60]).post(); // jours consécutifs : lundi-mardi - deuxième semaine
        model.allEqual(t[15][62], t[15][63]).post(); // jours consécutifs : jeudi-vendredi - deuxième semaine*/
        


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
            h[16][0], h[16][1], h[16][2], h[16][3], h[16][4], h[16][5], h[16][6], h[16][7], h[16][8], h[16][9],
            
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
            j[15][0], j[15][1], j[15][2], j[15][3], j[15][4], j[15][5], j[15][6], j[15][7], j[15][8], j[15][9],
            j[16][0], j[16][1], j[16][2], j[16][3], j[16][4], j[16][5], j[16][6], j[16][7], j[16][8], j[16][9]

            
        ));
        
        l = 0;
        while(solver.solve()) {
            for (e = 0; e < nb_eiades; e++) {
                System.out.print(eiades[e] + " : ");
                for (i = 0; i < 10; i++) {
                    if (i != 0) System.out.print(", ");
                    System.out.print("j" + (i + 1) + " = " + horaires[h[e][i].getValue()] + duree[j[e][i].getValue()]);
                    //if (i == 9) System.out.print(", voulu  : " + somme2semaines[e].getValue());
                    //if (i == 9) System.out.print(", repos : " + joursRepo2emSemaine[e].getValue());
                    //if (i == 9) System.out.print(", total : trouvé " + somme1[e].getValue());
                    //if (i == 9) System.out.print(", variateur : " + variateurJoursRepo[e].getValue());
                    if (i == 9) System.out.println(", s = " + s[e].getValue());
                }
            }
            System.out.println();
            l++;
        }
        System.out.println("Il y a " + l + " solutions");
    }
    
    // Fonctions de contraintes
    
    private static void addCountOnCommenceA6h45(IntVar c) {
        if (17 != nb_eiades) System.out.print(0/0); // Problème avec le nombre d'eiades
        for (i = 0; i < 10; i++) {
            model.count(1, new IntVar[]{
                h[ 0][i], h[ 1][i], h[ 2][i], h[ 3][i], h[ 4][i], h[5][i], 
                h[ 6][i], h[ 7][i], h[ 8][i], h[ 9][i], h[10][i], h[11][i],
                h[12][i], h[13][i], h[14][i], h[15][i], h[16][i]
            }, c).post();
        }
    }
    
    private static void addCountOnCommenceA7h30(IntVar c) {
        if (17 != nb_eiades) System.out.print(0/0); // Problème avec le nombre d'eiades
        for (i = 0; i < 10; i++) {
            model.count(2, new IntVar[]{
                h[ 0][i], h[ 1][i], h[ 2][i], h[ 3][i], h[ 4][i], h[5][i], 
                h[ 6][i], h[ 7][i], h[ 8][i], h[ 9][i], h[10][i],
                h[11][i], h[12][i], h[13][i], h[14][i], h[15][i]
            }, c).post();
        }
    }
    
    private static void addCountOnCommenceA8h(IntVar c) {
        if (17 != nb_eiades) System.out.print(0/0); // Problème avec le nombre d'eiades
        for (i = 0; i < 10; i++) {
            model.count(3, new IntVar[]{
                h[ 0][i], h[ 1][i], h[ 2][i], h[ 3][i], h[ 4][i], h[5][i], 
                h[ 6][i], h[ 7][i], h[ 8][i], h[ 9][i], h[10][i],
                h[11][i], h[12][i], h[13][i], h[14][i], h[15][i]
            }, c).post();
        }
    }
    
}

/* notes résolution
j  h s j+h j-h
0  0 1 0   0   (repos)
0  1 0 1   -1
0  2 0 2   -2
0  3 0 3   -3
10 0 0 10  10
10 1 1 11  9   (6h45-16h45)
10 2 1 12  8   (7h30-17h30)
10 3 0 13  7
11 0 0 11  11
11 1 1 12  10  (6h45-17h45)
11 2 1 13  9   (7h30-18h30)
11 3 1 14  8   (8h-19h)
*/