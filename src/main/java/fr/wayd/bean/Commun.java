package fr.wayd.bean;

import com.google.firebase.database.ServerValue;

import java.text.SimpleDateFormat;
import java.util.Date;

import fr.wayd.wdonation.AcceuilActivity;
import fr.wayd.wdonation.R;

public class Commun {

    static public String getDateNowStr() {
        SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yy");
        Date maintenant = new Date();
        return formater.format(maintenant);


    }

   public static String getMessagePartage(Association association,int typemessage) {

       if (typemessage == AcceuilActivity.MESSAGE_PARTAGE1)
           return "J'utilise l'application wDonation qui permet de faire des dons à des associations locales sans dépenser d'argent. Toi aussi, rejoins la communauté !";

       if (typemessage == AcceuilActivity.MESSAGE_PARTAGE2)
           return "Je viens de soutenir " + association.getNom() + " en utilisant wDonation sans dépenser d'argent. Toi aussi soutien gratuitement et simplement des associations ! ";

       return null;
   }
}
