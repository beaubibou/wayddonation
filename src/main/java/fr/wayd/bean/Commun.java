package fr.wayd.bean;

import com.google.firebase.database.ServerValue;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Commun {

    static public String getDateNowStr() {
        SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yy");
        Date maintenant = new Date();
        return formater.format(maintenant);


    }

   public static String getMessagePartage(Association association){
        return    "Faites comme moi, j'ai fait un don gratuitement à l'association "+association.getNom()+" grace à l'application wDonation."+"\n\n "+
                "market://details?id=fr.wayd.wdonation";

    }
}
