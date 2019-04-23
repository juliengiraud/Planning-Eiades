package Objets;

import Objets.Eiade;

public class Case {
    
    private Eiade eiade;
    private double heureDebut;
    private double heureFin;
    
    public Case(Eiade e, double hd, double hf) {
        this.eiade = e;
        this.heureDebut = hd;
        this.heureFin = hf;
    }
    
    public Eiade getEiade() {
        return this.eiade;
    }
    
    public double getHeureDebut() {
        return this.heureDebut;
    }
    
    public void setHeureDebut(double h) {
        this.heureDebut = h;
    }
    
    public double getHeureFin() {
        return this.heureFin;
    }
    
    public void setHeureFin(double h) {
        this.heureFin = h;
    }
    
    public String getHoraires() {
        return this.heureDebut + " - " + this.heureFin;
    }
    
}
