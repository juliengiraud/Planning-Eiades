package Objets;

public class Eiade {
    
    private String nom;
    private int nbMaxDeOnzeHeures;

    public Eiade(String no, int nb) {
        this.nom = no;
        this.nbMaxDeOnzeHeures = nb;
    }
    
    public String getNom() {
        return this.nom;
    }
    
    public int getNbMaxDeOnzeHeures() {
        return this.nbMaxDeOnzeHeures;
    }
    
}
