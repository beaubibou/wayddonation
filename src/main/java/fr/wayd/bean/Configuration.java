package fr.wayd.bean;

public class Configuration {
    int nbrclicksjourmax=4;
    int versionstore;
    String fournisseur;

    public String getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(String fournisseur) {
        this.fournisseur = fournisseur;
    }

    public int getVersionstore() {
        return versionstore;
    }

    public void setVersionstore(int versionstore) {
        this.versionstore = versionstore;
    }

    public int getNbrclicksjourmax() {
        return nbrclicksjourmax;
    }

    public void setNbrclicksjourmax(int nbrclicksjourmax) {
        this.nbrclicksjourmax = nbrclicksjourmax;
    }

    public Configuration(int nbrclicksjourmax) {
        this.nbrclicksjourmax = nbrclicksjourmax;
    }

    public Configuration() {
    }


}
