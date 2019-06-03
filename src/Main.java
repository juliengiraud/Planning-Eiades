import static org.chocosolver.solver.search.strategy.Search.*;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

public class Main {
    
    private static Model model;
    private static IntVar[][] cr;
    private static IntVar[] s, variateurSemaine, somme2semaines, sommeJoursRepo1erSemaine, joursRepo1erSemaine,
        joursRepo2emSemaine, variateurJoursRepo, somme1;
    private static IntVar zero, un, deux, trois, cinq, huit, onze, deuxOuTrois, cinqOuSix, huitMoinsCinqOuSix;
    
    private static int nb_eiades, i, k, l, e;
    
    private static void addConstraintOnSommeHeures1erSemaine(String op, int v) {
        for (e = 0; e < nb_eiades; e++) {}
    }
    private static void addConstraintOnSommeHeures1erSemaine(String op, IntVar[] v) {
        for (e = 0; e < nb_eiades; e++) {}
    }
    
    private static void addConstraintOnSommeHeures2emSemaine(String op, int v) {
        for (e = 0; e < nb_eiades; e++) {}
    }
    
    private static void addConstraintOnSommeHeures1erEt2emSemaine(String op, IntVar[] v) {
        for (e = 0; e < nb_eiades; e++) {}
    }
    
    private static void addConstraintOnSommeJoursRepo1erSemaine(String op, IntVar[] v) {
        for (e = 0; e < nb_eiades; e++) {}
    }
    
    private static void addConstraintOnSommeJoursRepo2emSemaine(String op, IntVar[] v) {
        for (e = 0; e < nb_eiades; e++) {}
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
            "repos     ", // j=0 et h=0, index=0
            "6h45-16h45", // j=10 et h=1, index=1
            "7h30-17h30", // j=10 et h=2, index=2
            "6h45-17h45", // j=11 et h=1, index=3
            "7h30-18h30", // j=11 et h=2, index=4
            "8h-19h    " // j=11 et h=3, index=5
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
        
        cr = model.intVarMatrix(nb_eiades, 10, 0, horaires.length-1); // index des horaires
        
        variateurSemaine = model.intVarArray(nb_eiades, 0, 100);
        variateurJoursRepo = model.intVarArray(nb_eiades, 0, 1);
        joursRepo1erSemaine = model.intVarArray(nb_eiades, 0, 10);
        joursRepo2emSemaine = model.intVarArray(nb_eiades, 0, 10);
        somme2semaines = model.intVarArray(nb_eiades, 0, 100);
        
        zero = model.intVar(0);
        un = model.intVar(1);
        deux = model.intVar(2);
        trois = model.intVar(3);
        cinq = model.intVar(5);
        huit = model.intVar(8);
        onze = model.intVar(11);
        
        deuxOuTrois = model.intVar(2, 3);
        cinqOuSix = model.intVar(5, 6);
        
        huitMoinsCinqOuSix = model.intVar(2, 3);
        model.arithm(huit, "-", cinqOuSix, "=", huitMoinsCinqOuSix).post();
        
        
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
        /*addCountOnCommenceA6h45(cinqOuSix);
        addCountOnCommenceA7h30(huitMoinsCinqOuSix);
        addCountOnCommenceA8h(deux); // donc les 2 terminent à 19h
        addCountOnTermineEntre17h30Et17h45(deuxOuTrois);
        addCountOnTermineA18h30(un);*/
        
        for (e = 0; e < nb_eiades; e++) {
            // Ajout des contraintes de temps sur une semaine
            model.arithm(s[e], "*", deux, "=", somme2semaines[e]).post(); // _[e] = les heures à faire en 2 semaines
        }
        
        addConstraintOnSommeHeures1erSemaine("<=", 41); // max 41h / semaine : semaine 1
        addConstraintOnSommeHeures2emSemaine("<=", 41); // max 41h / semaine : semaine 2
            
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
            cr[15][0], cr[15][1], cr[15][2], cr[15][3], cr[15][4], cr[15][5], cr[15][6], cr[15][7], cr[15][8], cr[15][9],
            cr[16][0], cr[16][1], cr[16][2], cr[16][3], cr[16][4], cr[16][5], cr[16][6], cr[16][7], cr[16][8], cr[16][9]
            
        ));
        
        l = 0;
        while(solver.solve()) {
            for (e = 0; e < nb_eiades; e++) {
                System.out.print(eiades[e] + " : ");
                for (i = 0; i < 10; i++) {
                    if (i != 0) System.out.print(", ");
                    System.out.print("j" + (i+1) + "=" + horaires[cr[e][i].getValue()]);
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
            model.count(model.intVar(new int[]{1, 3}), new IntVar[]{
                cr[ 0][i], cr[ 1][i], cr[ 2][i], cr[ 3][i], cr[ 4][i], cr[5][i], 
                cr[ 6][i], cr[ 7][i], cr[ 8][i], cr[ 9][i], cr[10][i], cr[11][i],
                cr[12][i], cr[13][i], cr[14][i], cr[15][i], cr[16][i]
            }, c).post();
        }
    }
    
    private static void addCountOnCommenceA7h30(IntVar c) {
        if (17 != nb_eiades) System.out.print(0/0); // Problème avec le nombre d'eiades
        for (i = 0; i < 10; i++) {
            model.count(model.intVar(new int[]{2, 4}), new IntVar[]{
                cr[ 0][i], cr[ 1][i], cr[ 2][i], cr[ 3][i], cr[ 4][i], cr[5][i], 
                cr[ 6][i], cr[ 7][i], cr[ 8][i], cr[ 9][i], cr[10][i], cr[11][i],
                cr[12][i], cr[13][i], cr[14][i], cr[15][i], cr[16][i]
            }, c).post();
        }
    }
    
    private static void addCountOnCommenceA8h(IntVar c) {
        if (17 != nb_eiades) System.out.print(0/0); // Problème avec le nombre d'eiades
        for (i = 0; i < 10; i++) {
            model.count(5, new IntVar[]{
                cr[ 0][i], cr[ 1][i], cr[ 2][i], cr[ 3][i], cr[ 4][i], cr[5][i], 
                cr[ 6][i], cr[ 7][i], cr[ 8][i], cr[ 9][i], cr[10][i], cr[11][i],
                cr[12][i], cr[13][i], cr[14][i], cr[15][i], cr[16][i]
            }, c).post();
        }
    }
    
    
    private static void addCountOnTermineA16h45(IntVar c) {
        if (17 != nb_eiades) System.out.print(0/0); // Problème avec le nombre d'eiades
        for (i = 0; i < 10; i++) {
            model.count(1, new IntVar[]{
                cr[ 0][i], cr[ 1][i], cr[ 2][i], cr[ 3][i], cr[ 4][i], cr[5][i], 
                cr[ 6][i], cr[ 7][i], cr[ 8][i], cr[ 9][i], cr[10][i], cr[11][i],
                cr[12][i], cr[13][i], cr[14][i], cr[15][i], cr[16][i]
            }, c).post();
        }
    }
    
    private static void addCountOnTermineA17h30(IntVar c) {
        if (17 != nb_eiades) System.out.print(0/0); // Problème avec le nombre d'eiades
        for (i = 0; i < 10; i++) {
            model.count(2, new IntVar[]{
                cr[ 0][i], cr[ 1][i], cr[ 2][i], cr[ 3][i], cr[ 4][i], cr[5][i], 
                cr[ 6][i], cr[ 7][i], cr[ 8][i], cr[ 9][i], cr[10][i], cr[11][i],
                cr[12][i], cr[13][i], cr[14][i], cr[15][i], cr[16][i]
            }, c).post();
        }
    }
    
    private static void addCountOnTermineA17h45(IntVar c) {
        if (17 != nb_eiades) System.out.print(0/0); // Problème avec le nombre d'eiades
        for (i = 0; i < 10; i++) {
            model.count(3, new IntVar[]{
                cr[ 0][i], cr[ 1][i], cr[ 2][i], cr[ 3][i], cr[ 4][i], cr[5][i], 
                cr[ 6][i], cr[ 7][i], cr[ 8][i], cr[ 9][i], cr[10][i], cr[11][i],
                cr[12][i], cr[13][i], cr[14][i], cr[15][i], cr[16][i]
            }, c).post();
        }
    }
    
    private static void addCountOnTermineA18h30(IntVar c) {
        if (17 != nb_eiades) System.out.print(0/0); // Problème avec le nombre d'eiades
        for (i = 0; i < 10; i++) {
            model.count(4, new IntVar[]{
                cr[ 0][i], cr[ 1][i], cr[ 2][i], cr[ 3][i], cr[ 4][i], cr[5][i], 
                cr[ 6][i], cr[ 7][i], cr[ 8][i], cr[ 9][i], cr[10][i], cr[11][i],
                cr[12][i], cr[13][i], cr[14][i], cr[15][i], cr[16][i]
            }, c).post();
        }
    }
    
    private static void addCountOnTermineEntre17h30Et17h45(IntVar c) {
        if (17 != nb_eiades) System.out.print(0/0); // Problème avec le nombre d'eiades
        for (i = 0; i < 10; i++) {
            model.count(model.intVar(new int[]{2, 3}), new IntVar[]{
                cr[ 0][i], cr[ 1][i], cr[ 2][i], cr[ 3][i], cr[ 4][i], cr[5][i], 
                cr[ 6][i], cr[ 7][i], cr[ 8][i], cr[ 9][i], cr[10][i], cr[11][i],
                cr[12][i], cr[13][i], cr[14][i], cr[15][i], cr[16][i]
            }, c).post();
        }
    }
    
    
}
