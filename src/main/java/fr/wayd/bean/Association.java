package fr.wayd.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Association implements Parcelable {

    private String nom;
    private String mail;
    private int nbrclik = 0;
    private String detail;
    private String lienFacebook;
    private String lienSitePerso;
    private String id, idapplication, idcampagne;
    private String titre;

    private String urlphoto;
    private String photo;
    public Association() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)


    }

    protected Association(Parcel in) {
        nom = in.readString();
        mail = in.readString();
        nbrclik = in.readInt();
        detail = in.readString();
        lienFacebook = in.readString();
        lienSitePerso = in.readString();
        id = in.readString();
        idapplication = in.readString();
        idcampagne = in.readString();
        titre = in.readString();
        urlphoto = in.readString();
        photo = in.readString();
    }

    public static final Creator<Association> CREATOR = new Creator<Association>() {
        @Override
        public Association createFromParcel(Parcel in) {
            return new Association(in);
        }

        @Override
        public Association[] newArray(int size) {
            return new Association[size];
        }
    };

    public String getUrlphoto() {
        return urlphoto;
    }

    public void setUrlphoto(String urlphoto) {
        this.urlphoto = urlphoto;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


    public static Creator<Association> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return nom;
    }

    public Association(String id, String nom, String mail, String detail, String lienFacebook, String lienSitePerso, String idapplication, String idcampagne,String titre,String photourl) {
        this.id = id;
        this.nom = nom;
        this.mail = mail;
        this.detail = detail;
        this.lienFacebook = lienFacebook;
        this.lienSitePerso = lienSitePerso;
        this.idcampagne = idcampagne;
        this.idapplication = idapplication;
        this.titre=titre;
        this.urlphoto=photourl;
        this.photo=photo;


    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setNbrclik(int nbrclik) {
        this.nbrclik = nbrclik;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setLienFacebook(String lienFacebook) {
        this.lienFacebook = lienFacebook;
    }

    public void setLienSitePerso(String lienSitePerso) {
        this.lienSitePerso = lienSitePerso;
    }

    public String getNom() {
        return nom;
    }

    public String getMail() {
        return mail;
    }

    public int getNbrclik() {
        return nbrclik;
    }

    public String getDetail() {
        return detail;
    }

    public String getLienFacebook() {
        return lienFacebook;
    }

    public String getLienSitePerso() {
        return lienSitePerso;
    }

    public void addClick() {
        nbrclik++;
    }

    public String getIdapplication() {
        return idapplication;
    }

    public void setIdapplication(String idapplication) {
        this.idapplication = idapplication;
    }

    public String getIdcampagne() {
        return idcampagne;
    }

    public void setIdcampagne(String idcampagne) {
        this.idcampagne = idcampagne;
    }

    public void updateAssociation(Association association) {
        this.id = association.id;
        this.nom = association.nom;
        this.mail = association.mail;
        this.detail = association.detail;
        this.lienFacebook = association.lienFacebook;
        this.lienSitePerso = association.lienSitePerso;
        this.idapplication = association.getIdapplication();
        this.idcampagne = association.getIdcampagne();
        this.titre=association.getTitre();
        this.photo=association.getPhoto();
        this.urlphoto=association.getUrlphoto();
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nom);
        dest.writeString(mail);
        dest.writeInt(nbrclik);
        dest.writeString(detail);
        dest.writeString(lienFacebook);
        dest.writeString(lienSitePerso);
        dest.writeString(id);
        dest.writeString(idapplication);
        dest.writeString(idcampagne);
        dest.writeString(titre);
        dest.writeString(urlphoto);
        dest.writeString(photo);
    }
}
