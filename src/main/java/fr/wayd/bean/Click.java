package fr.wayd.bean;

public class Click {
    int nbr;
    public Click(){

    }

    public Click(int nbr) {
   this.nbr=nbr;
    }

    public int getNbr() {
        return nbr;
    }

    public void setNbr(int nbr) {
        this.nbr = nbr;
    }

    public void ajouteUn() {
  nbr++;
    }
}
