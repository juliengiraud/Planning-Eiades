package Objets;

import Objets.Eiade;
import java.util.ArrayList;
import java.util.List;

public class Planning {

    private List<List<Case>> planning;
    private List<Eiade> eiades;
    
    public Planning() {
        this.planning = new ArrayList<List<Case>>();
        
        this.eiades = new ArrayList<Eiade>();
        
        this.eiades.add(new Eiade("B. MICHELIN", 0));
        this.eiades.add(new Eiade("N. JEANJEAN", 10));
        this.eiades.add(new Eiade("MP. CHAUTARD", 0));
        this.eiades.add(new Eiade("C. JAMOIS", 1));
        this.eiades.add(new Eiade("K. REYNAUD", 10));
        this.eiades.add(new Eiade("E. KAID", 0));
        this.eiades.add(new Eiade("P. CUIROT", 10));
        this.eiades.add(new Eiade("R. BENZAIDE", 10));
        this.eiades.add(new Eiade("V. LEGAT", 0));
        this.eiades.add(new Eiade("Y. AUBERT BRUN", 10));
        this.eiades.add(new Eiade("V. BANCALARI", 0));
        this.eiades.add(new Eiade("D. SERMET", 0));
        this.eiades.add(new Eiade("F. BOULAY", 0));
        this.eiades.add(new Eiade("V. MARDIROSSIAN", 10));
        this.eiades.add(new Eiade("BEA REIX", 1));
        this.eiades.add(new Eiade("MARIE", 10));
        
        int i, j;
        for (i = 0; i < eiades.size(); i++) {
            planning.add(new ArrayList<Case>());
            for (j = 0; j < 10; j++) {
                planning.get(i).add(new Case(eiades.get(i), 6.75, 19));
            }
        }
    }
    
}
