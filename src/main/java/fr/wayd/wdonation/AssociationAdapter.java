package fr.wayd.wdonation;

import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;

import fr.wayd.bean.Association;

public class AssociationAdapter extends BaseAdapter {

    // Une liste de personnes
    private final List<Association> listAssociations;

    // Le contexte dans lequel est présent notre adapter
    private final Context mContext;

    // Un mécanisme pour gérer l'affichage graphique depuis un layout XML
    private final LayoutInflater mInflater;

    private final ArrayList<ActiviteAdapterListener> mListListener = new ArrayList<>();

    public AssociationAdapter(Context context, List<Association> aListP) {
        mContext = context;
        listAssociations = aListP;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return listAssociations.size(); // DOCUMENTEZ_MOI Raccord de méthode auto-généré
    }

    @Override
    public Object getItem(int position) {
        return listAssociations.get(position); // DOCUMENTEZ_MOI Raccord de méthode auto-généré
    }

    @Override
    public long getItemId(int position) {
        return position;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout layoutItem;
        // (1) : Réutilisation des layouts

        if (convertView == null) {
            // Initialisation de notre item à partir du layout XML "personne_layout.xml"
            layoutItem = (RelativeLayout) mInflater.inflate(R.layout.item_association, parent, false);
        } else {
            layoutItem = (RelativeLayout) convertView;
        }

        // (2) : Récupération des TextView de notre layout
        //  RatingBar tv_note = (RatingBar) layoutItem.findViewById(R.id.RT_note);

        TextView nom = (TextView) layoutItem.findViewById(R.id.nom);
        TextView nbrclicks = (TextView) layoutItem.findViewById(R.id.nbrclicks);

        Association association = listAssociations.get(position);
        layoutItem.setTag(association);

        nom.setText(association.getNom());
        nbrclicks.setText(Integer.toString(association.getNbrclik()));

        return layoutItem;

    }

    public interface ActiviteAdapterListener {

    }



    public void addListener(ActiviteAdapterListener aListener) {
        mListListener.add(aListener);
    }


}
