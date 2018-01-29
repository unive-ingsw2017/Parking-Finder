package it.unive.dais.cevid.datadroid.lib.util;

import com.google.android.gms.maps.model.LatLng;

/**
 * Classe astratta che rappresenta una entità visualizzabile su una mappa.
 * I metodi presenti sono il minimo indispensabile per creare un Marker in una GoogleMap, dotato di posizione, titolo e descrizione.
 * @author Alvise Spanò, Università Ca' Foscari
 */
public abstract class MapItem {

    /**
     * Ritorna la posizione.
     * @return la posizione in un oggetto di tipo LatLng
     */
    public abstract LatLng getPosition();

    /**
     * Ritorna il titolo.
     * @return la stringa col titolo.
     */
    public String getTitle() {
        return toString();
    }

    /**
     * Ritorna la descrizione.
     * @return la stringa con la descrizione.
     */
    public String getDescription() {
        return getTitle();
    }
}
