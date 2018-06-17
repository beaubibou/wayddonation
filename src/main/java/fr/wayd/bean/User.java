package fr.wayd.bean;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public final static int ADMIN_WAYD=2;
    private String nom;
    private String mail;
    private int nbrclik=0;
    private int profil;
    private boolean cgu;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)



    }

    public int getNbrclik() {
        return nbrclik;
    }

    public void setNbrclik(int nbrclik) {
        this.nbrclik = nbrclik;
    }

    public boolean isCgu() {
        return cgu;
    }

    public void setCgu(boolean cgu) {
        this.cgu = cgu;
    }

    public User(String nom, String mail, int profil, boolean cgu) {
        this.nom = nom;
        this.mail =mail;
        this.profil=profil;
      //  this.nbrclick=0;

    }

    public int getProfil() {
        return profil;
    }

    public void setProfil(int profil) {
        this.profil = profil;
    }

    public String getNom() {
        return nom;
    }

    public String getMail() {
        return mail;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public String toString() {
        return "User{" +
                "nom='" + nom + '\'' +
                ", mail='" + mail + '\'' +
                ", nbrclick=" +nbrclik+
                '}';
    }

    public void addClick() {
      nbrclik++;
    }
}
