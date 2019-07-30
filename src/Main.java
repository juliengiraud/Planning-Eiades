import static org.chocosolver.solver.search.strategy.Search.*;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

public class Main {
    
    private static Model model;
    private static IntVar[][] cr;
    private static IntVar[] s, variateurSemaine, somme2semaines, joursRepo1erSemaine,
        joursRepo2emSemaine, joursRepo3emSemaine, joursRepo4emSemaine, variateurJoursRepo,
        somme1, nbJoursReposS1, nbJoursReposS2;
    private static IntVar zero, un, deux, trois, cinq, huit, onze, deuxOuTrois, cinqOuSix, huitMoinsCinqOuSix;
    
    private static int nb_eiades, i, k, l, e;
    
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
            "Julien         "
        };
        String[] horaires = {
            "repos     ", // j=0 et h=0, index=0
            "6h45-16h45", // j=10 et h=1, index=1
            "7h30-17h30", // j=10 et h=2, index=2
            "6h45-17h45", // j=11 et h=1, index=3
            "7h30-18h30", // j=11 et h=2, index=4
            "8h-19h    "  // j=11 et h=3, index=5
        };
        
        nb_eiades = eiades.length;
        
        model = new Model();
                
        s = model.intVarArray(nb_eiades, 20, 41);
        s[ 0] = model.intVar(20); // B. MICHELIN, 2x10h, pas de 11h, 1 semaine sur 4 un 9h-19h
        s[ 1] = model.intVar(36); // N. JEANJEAN, un seul 11h, 1 semaine sur 2 un 7h30-18h30
        s[ 2] = model.intVar(21); // MP. CHAUTARD, pas de 11h, 1 semaine sur 4 : 9h-19h
        s[ 3] = model.intVar(30); // C. JAMOIS, pas de 11h, 1 semaine sur 2 : 9h-19h
        s[ 4] = model.intVar(33); // K. REYNAUD 3x11h, une fois par semaine : 7h30-18h30
        s[ 5] = model.intVar(30); // E. KAID, pas de 11h, une semaine sur 2 : 9h-19h le lundi
        s[ 6] = model.intVar(41); // P. CUIROT, un seul 11h, une fois par semaine 7h30-18h30 (le mercredi)
        s[ 7] = model.intVar(31); // R. BENZAIDE, un seul 11h, une fois par semaine un 7h30-18h30 le vendredi
        s[ 8] = model.intVar(40); // V. LEGAT, que des 10h, 1 semaine sur 2 : 9h-19h
        s[ 9] = model.intVar(21); // Y. AUBERT BRUN, un seul 11h, une semaine sur 4 : 7h30-18h30 le lundi
        s[10] = model.intVar(25); // V. BANCALARI, pas de 11h, une semaine sur 3 : 9h-19h
        s[11] = model.intVar(25); // D. SERMET, pas de 11h, une semaine sur 3 : 9h-19h le lundi
        s[12] = model.intVar(31); // F. BOULAY, un seul 11h, une semaine sur 2 : 9h-19h
        s[13] = model.intVar(30); // V. MARDIROSSIAN, pas de 11h, une  semaine sur deux : 9h-19h
        s[14] = model.intVar(21); // BEA REIX, un seul 11h, 1 semaine sur 4 : 7h30-18h30
        s[15] = model.intVar(31); // MARIE, 3 jours de suite hors mardi-mercredi-jeudi, 1 semaine sur 2 : 7h30-18h30
        s[16] = model.intVar(41); // Julien sur 4 jours, 1 fois par semaine : 7h30-18h30
        
        cr = model.intVarMatrix(nb_eiades, 20, 0, horaires.length-1); // index des horaires
        
        variateurSemaine = model.intVarArray(nb_eiades, 0, 100);
        variateurJoursRepo = model.intVarArray(nb_eiades, 0, 1);
        joursRepo1erSemaine = model.intVarArray(nb_eiades, 0, 10);
        joursRepo2emSemaine = model.intVarArray(nb_eiades, 0, 10);
        joursRepo3emSemaine = model.intVarArray(nb_eiades, 0, 10);
        joursRepo4emSemaine = model.intVarArray(nb_eiades, 0, 10);
        somme2semaines = model.intVarArray(nb_eiades, 0, 100);
        
        zero = model.intVar(0);
        un = model.intVar(1);
        deux = model.intVar(2);
        trois = model.intVar(3);
        cinq = model.intVar(5);
        huit = model.intVar(8);
        onze = model.intVar(11);
        
        
        //deuxOuTrois = model.intVar(2, 3);
        //cinqOuSix = model.intVar(5, 6);
        //huitMoinsCinqOuSix = model.intVar(2, 3);
        //model.arithm(huit, "-", cinqOuSix, "=", huitMoinsCinqOuSix).post();
        // Contraintes du nombre d'eiades par créneaux horaires pour chaque jour
        /*addCountOnCommenceA6h45(cinqOuSix);
        addCountOnCommenceA7h30(huitMoinsCinqOuSix);
        addCountOnCommenceA8h(deux); // donc les 2 terminent à 19h
        addCountOnTermineEntre17h30Et17h45(deuxOuTrois);
        addCountOnTermineA18h30(un);*/
        
        
        //addConstraintOnSommeHeures1erSemaine("<=", 41); // max 41h / semaine : semaine 1 A REFAIRE
        //addConstraintOnSommeHeures2emSemaine("<=", 41); // max 41h / semaine : semaine 2
        //addConstraintOnSommeHeures1erEt2emSemaine("=", somme2semaines); // Somme des 2 semaines = 2 * s[e]
        for (e = 0; e < nb_eiades; e++) {
            //System.out.println((s[e].getValue()*2)/10-(s[e].getValue()*2)%10 + " " + (s[e].getValue()*2)%10);
            addCountOn10hDes2Semaines(model.intVar((s[e].getValue()*2)/10-(s[e].getValue()*2)%10), e);
            addCountOn11hDes2Semaines(model.intVar((s[e].getValue()*2)%10), e);
        }
        
        nbJoursReposS1 = model.intVarArray(nb_eiades, 1, 3);
        nbJoursReposS2 = model.intVarArray(nb_eiades, 1, 3);
        for (e = 0; e < nb_eiades; e++) {
            //model.arithm(nbJoursReposS1[e], "*", nbJoursReposS2[e], "!=", 3).post(); A remettre si besoin
            model.allEqual(nbJoursReposS1[e], nbJoursReposS2[e]);
        }
        // d'une semaine à l'autre on peut varier d'un jour de travail
        addCountOnJoursReposXemSemaine(nbJoursReposS1, 1); //  1,    2   ou  3 jour(s) de repos la 1er semaine
        addCountOnJoursReposXemSemaine(nbJoursReposS2, 2); // 1|2, 1|2|3 ou 2|3 jour(s) de repos la 2em semaine

        
            
            
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
        model.setObjective(Model.MINIMIZE, variateurJoursRepo[15]);
        model.setObjective(Model.MINIMIZE, variateurJoursRepo[16]);*/
        
        solver.setSearch(intVarSearch(s[15], // pour les heures de Marie
                
            cr[0][ 0], cr[0][ 1], cr[0][ 2], cr[0][ 3], cr[0][ 4], cr[0][ 5], cr[0][ 6], cr[0][ 7], cr[0][ 8], cr[0][ 9], cr[ 0][10], cr[ 0][11], cr[ 0][12], cr[ 0][13], cr[ 0][14], cr[ 0][15], cr[ 0][16], cr[ 0][17], cr[ 0][18], cr[ 0][19],
            cr[1][ 0], cr[1][ 1], cr[1][ 2], cr[1][ 3], cr[1][ 4], cr[1][ 5], cr[1][ 6], cr[1][ 7], cr[1][ 8], cr[1][ 9], cr[ 1][10], cr[ 1][11], cr[ 1][12], cr[ 1][13], cr[ 1][14], cr[ 1][15], cr[ 1][16], cr[ 1][17], cr[ 1][18], cr[ 1][19],
            cr[2][ 0], cr[2][ 1], cr[2][ 2], cr[2][ 3], cr[2][ 4], cr[2][ 5], cr[2][ 6], cr[2][ 7], cr[2][ 8], cr[2][ 9], cr[ 2][10], cr[ 2][11], cr[ 2][12], cr[ 2][13], cr[ 2][14], cr[ 2][15], cr[ 2][16], cr[ 2][17], cr[ 2][18], cr[ 2][19],
            cr[3][ 0], cr[3][ 1], cr[3][ 2], cr[3][ 3], cr[3][ 4], cr[3][ 5], cr[3][ 6], cr[3][ 7], cr[3][ 8], cr[3][ 9], cr[ 3][10], cr[ 3][11], cr[ 3][12], cr[ 3][13], cr[ 3][14], cr[ 3][15], cr[ 3][16], cr[ 3][17], cr[ 3][18], cr[ 3][19],
            cr[4][ 0], cr[4][ 1], cr[4][ 2], cr[4][ 3], cr[4][ 4], cr[4][ 5], cr[4][ 6], cr[4][ 7], cr[4][ 8], cr[4][ 9], cr[ 4][10], cr[ 4][11], cr[ 4][12], cr[ 4][13], cr[ 4][14], cr[ 4][15], cr[ 4][16], cr[ 4][17], cr[ 4][18], cr[ 4][19],
            cr[5][ 0], cr[5][ 1], cr[5][ 2], cr[5][ 3], cr[5][ 4], cr[5][ 5], cr[5][ 6], cr[5][ 7], cr[5][ 8], cr[5][ 9], cr[ 5][10], cr[ 5][11], cr[ 5][12], cr[ 5][13], cr[ 5][14], cr[ 5][15], cr[ 5][16], cr[ 5][17], cr[ 5][18], cr[ 5][19],
            cr[6][ 0], cr[6][ 1], cr[6][ 2], cr[6][ 3], cr[6][ 4], cr[6][ 5], cr[6][ 6], cr[6][ 7], cr[6][ 8], cr[6][ 9], cr[ 6][10], cr[ 6][11], cr[ 6][12], cr[ 6][13], cr[ 6][14], cr[ 6][15], cr[ 6][16], cr[ 6][17], cr[ 6][18], cr[ 6][19],
            cr[7][ 0], cr[7][ 1], cr[7][ 2], cr[7][ 3], cr[7][ 4], cr[7][ 5], cr[7][ 6], cr[7][ 7], cr[7][ 8], cr[7][ 9], cr[ 7][10], cr[ 7][11], cr[ 7][12], cr[ 7][13], cr[ 7][14], cr[ 7][15], cr[ 7][16], cr[ 7][17], cr[ 7][18], cr[ 7][19],
            cr[8][ 0], cr[8][ 1], cr[8][ 2], cr[8][ 3], cr[8][ 4], cr[8][ 5], cr[8][ 6], cr[8][ 7], cr[8][ 8], cr[8][ 9], cr[ 8][10], cr[ 8][11], cr[ 8][12], cr[ 8][13], cr[ 8][14], cr[ 8][15], cr[ 8][16], cr[ 8][17], cr[ 8][18], cr[ 8][19],
            cr[9][ 0], cr[9][ 1], cr[9][ 2], cr[9][ 3], cr[9][ 4], cr[9][ 5], cr[9][ 6], cr[9][ 7], cr[9][ 8], cr[9][ 9], cr[ 9][10], cr[ 9][11], cr[ 9][12], cr[ 9][13], cr[ 9][14], cr[ 9][15], cr[ 9][16], cr[ 9][17], cr[ 9][18], cr[ 9][19],
            cr[10][0], cr[10][1], cr[10][2], cr[10][3], cr[10][4], cr[10][5], cr[10][6], cr[10][7], cr[10][8], cr[10][9], cr[10][10], cr[10][11], cr[10][12], cr[10][13], cr[10][14], cr[10][15], cr[10][16], cr[10][17], cr[10][18], cr[10][19],
            cr[11][0], cr[11][1], cr[11][2], cr[11][3], cr[11][4], cr[11][5], cr[11][6], cr[11][7], cr[11][8], cr[11][9], cr[11][10], cr[11][11], cr[11][12], cr[11][13], cr[11][14], cr[11][15], cr[11][16], cr[11][17], cr[11][18], cr[11][19],
            cr[12][0], cr[12][1], cr[12][2], cr[12][3], cr[12][4], cr[12][5], cr[12][6], cr[12][7], cr[12][8], cr[12][9], cr[12][10], cr[12][11], cr[12][12], cr[12][13], cr[12][14], cr[12][15], cr[12][16], cr[12][17], cr[12][18], cr[12][19],
            cr[13][0], cr[13][1], cr[13][2], cr[13][3], cr[13][4], cr[13][5], cr[13][6], cr[13][7], cr[13][8], cr[13][9], cr[13][10], cr[13][11], cr[13][12], cr[13][13], cr[13][14], cr[13][15], cr[13][16], cr[13][17], cr[13][18], cr[13][19],
            cr[14][0], cr[14][1], cr[14][2], cr[14][3], cr[14][4], cr[14][5], cr[14][6], cr[14][7], cr[14][8], cr[14][9], cr[14][10], cr[14][11], cr[14][12], cr[14][13], cr[14][14], cr[14][15], cr[14][16], cr[14][17], cr[14][18], cr[14][19],
            cr[15][0], cr[15][1], cr[15][2], cr[15][3], cr[15][4], cr[15][5], cr[15][6], cr[15][7], cr[15][8], cr[15][9], cr[15][10], cr[15][11], cr[15][12], cr[15][13], cr[15][14], cr[15][15], cr[15][16], cr[15][17], cr[15][18], cr[15][19],
            cr[16][0], cr[16][1], cr[16][2], cr[16][3], cr[16][4], cr[16][5], cr[16][6], cr[16][7], cr[16][8], cr[16][9], cr[16][10], cr[16][11], cr[16][12], cr[16][13], cr[16][14], cr[16][15], cr[16][16], cr[16][17], cr[16][18], cr[16][19]
            
        ));
        
        l = 0;
        while(solver.solve()) {
            for (e = 0; e < nb_eiades; e++) {
                System.out.print(eiades[e] + " : ");
                for (i = 0; i < 20; i++) {
                    if (i != 0) System.out.print(", ");
                    System.out.print("j" + (i+1) + "=" + horaires[cr[e][i].getValue()]);
                    //if (i == 9) System.out.print(", voulu  : " + somme2semaines[e].getValue());
                    //if (i == 9) System.out.print(", repos : " + joursRepo2emSemaine[e].getValue());
                    //if (i == 9) System.out.print(", total : trouvé " + somme1[e].getValue());
                    //if (i == 9) System.out.print(", variateur : " + variateurJoursRepo[e].getValue());
                    if ((i+1)%5 == 0) System.out.print(", Fin semaine " + (i/5+1) + " ");
                    if (i == 19) System.out.println(", s = " + s[e].getValue());
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
        for (i = 0; i < 20; i++) {
            model.count(model.intVar(new int[]{1, 3}), new IntVar[]{
                cr[ 0][i], cr[ 1][i], cr[ 2][i], cr[ 3][i], cr[ 4][i], cr[5][i], 
                cr[ 6][i], cr[ 7][i], cr[ 8][i], cr[ 9][i], cr[10][i], cr[11][i],
                cr[12][i], cr[13][i], cr[14][i], cr[15][i], cr[16][i]
            }, c).post();
        }
    }
    
    private static void addCountOnCommenceA7h30(IntVar c) {
        if (17 != nb_eiades) System.out.print(0/0); // Problème avec le nombre d'eiades
        for (i = 0; i < 20; i++) {
            model.count(model.intVar(new int[]{2, 4}), new IntVar[]{
                cr[ 0][i], cr[ 1][i], cr[ 2][i], cr[ 3][i], cr[ 4][i], cr[5][i], 
                cr[ 6][i], cr[ 7][i], cr[ 8][i], cr[ 9][i], cr[10][i], cr[11][i],
                cr[12][i], cr[13][i], cr[14][i], cr[15][i], cr[16][i]
            }, c).post();
        }
    }
    
    private static void addCountOnCommenceA8h(IntVar c) {
        if (17 != nb_eiades) System.out.print(0/0); // Problème avec le nombre d'eiades
        for (i = 0; i < 20; i++) {
            model.count(5, new IntVar[]{
                cr[ 0][i], cr[ 1][i], cr[ 2][i], cr[ 3][i], cr[ 4][i], cr[5][i], 
                cr[ 6][i], cr[ 7][i], cr[ 8][i], cr[ 9][i], cr[10][i], cr[11][i],
                cr[12][i], cr[13][i], cr[14][i], cr[15][i], cr[16][i]
            }, c).post();
        }
    }
    
    
    private static void addCountOnTermineA16h45(IntVar c) {
        if (17 != nb_eiades) System.out.print(0/0); // Problème avec le nombre d'eiades
        for (i = 0; i < 20; i++) {
            model.count(1, new IntVar[]{
                cr[ 0][i], cr[ 1][i], cr[ 2][i], cr[ 3][i], cr[ 4][i], cr[5][i], 
                cr[ 6][i], cr[ 7][i], cr[ 8][i], cr[ 9][i], cr[10][i], cr[11][i],
                cr[12][i], cr[13][i], cr[14][i], cr[15][i], cr[16][i]
            }, c).post();
        }
    }
    
    private static void addCountOnTermineA17h30(IntVar c) {
        if (17 != nb_eiades) System.out.print(0/0); // Problème avec le nombre d'eiades
        for (i = 0; i < 20; i++) {
            model.count(2, new IntVar[]{
                cr[ 0][i], cr[ 1][i], cr[ 2][i], cr[ 3][i], cr[ 4][i], cr[5][i], 
                cr[ 6][i], cr[ 7][i], cr[ 8][i], cr[ 9][i], cr[10][i], cr[11][i],
                cr[12][i], cr[13][i], cr[14][i], cr[15][i], cr[16][i]
            }, c).post();
        }
    }
    
    private static void addCountOnTermineA17h45(IntVar c) {
        if (17 != nb_eiades) System.out.print(0/0); // Problème avec le nombre d'eiades
        for (i = 0; i < 20; i++) {
            model.count(3, new IntVar[]{
                cr[ 0][i], cr[ 1][i], cr[ 2][i], cr[ 3][i], cr[ 4][i], cr[5][i], 
                cr[ 6][i], cr[ 7][i], cr[ 8][i], cr[ 9][i], cr[10][i], cr[11][i],
                cr[12][i], cr[13][i], cr[14][i], cr[15][i], cr[16][i]
            }, c).post();
        }
    }
    
    private static void addCountOnTermineA18h30(IntVar c) {
        if (17 != nb_eiades) System.out.print(0/0); // Problème avec le nombre d'eiades
        for (i = 0; i < 20; i++) {
            model.count(4, new IntVar[]{
                cr[ 0][i], cr[ 1][i], cr[ 2][i], cr[ 3][i], cr[ 4][i], cr[5][i], 
                cr[ 6][i], cr[ 7][i], cr[ 8][i], cr[ 9][i], cr[10][i], cr[11][i],
                cr[12][i], cr[13][i], cr[14][i], cr[15][i], cr[16][i]
            }, c).post();
        }
    }
    
    private static void addCountOnTermineEntre17h30Et17h45(IntVar c) {
        if (17 != nb_eiades) System.out.print(0/0); // Problème avec le nombre d'eiades
        for (i = 0; i < 20; i++) {
            model.count(model.intVar(new int[]{2, 3}), new IntVar[]{
                cr[ 0][i], cr[ 1][i], cr[ 2][i], cr[ 3][i], cr[ 4][i], cr[5][i], 
                cr[ 6][i], cr[ 7][i], cr[ 8][i], cr[ 9][i], cr[10][i], cr[11][i],
                cr[12][i], cr[13][i], cr[14][i], cr[15][i], cr[16][i]
            }, c).post();
        }
    }
    
    private static void addCountOnJoursReposXemSemaine(IntVar[] v, int s) {
        for (e = 0; e < nb_eiades; e++) {
            model.count(0, new IntVar[]{
                cr[e][0 + 5*(s-1)], cr[e][1 + 5*(s-1)], cr[e][2 + 5*(s-1)], cr[e][3 + 5*(s-1)], cr[e][4 + 5*(s-1)]
            }, v[e]).post();
        }
    }
    
    private static void addCountOn10hDes2Semaines(IntVar v, int e) {
        model.count(model.intVar(1,2), new IntVar[]{
            cr[e][0], cr[e][1], cr[e][2], cr[e][3], cr[e][4],
            cr[e][5], cr[e][6], cr[e][7], cr[e][8], cr[e][9]
        }, v).post();
    }
    
    private static void addCountOn11hDes2Semaines(IntVar v, int e) {
        model.count(5, new IntVar[]{
            cr[e][0], cr[e][1], cr[e][2], cr[e][3], cr[e][4],
            cr[e][5], cr[e][6], cr[e][7], cr[e][8], cr[e][9]
        }, v).post();
    }
    
    
}
