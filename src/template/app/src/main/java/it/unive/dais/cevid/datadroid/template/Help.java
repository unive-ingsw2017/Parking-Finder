package it.unive.dais.cevid.datadroid.template;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by utente on 24/01/2018.
 */

public class Help extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.help);

        TextView testo = (TextView) findViewById(R.id.helpText);
        testo.setText("Aiuto all’utilizzo di Parking Finder \n" +
                "Su Parking Finder puoi ottenere informazioni stradali sui parcheggi in Italia, wow! \n \n" +
                "1.\tpremere il tasto di ricerca per mostrare i parcheggi nel raggio di 2km dalla tua posizione \n" +
                "2.\tSeleziona il parcheggio che preferisci per poter avviare le indicazioni stradali \n" +
                "3.\t Premi il pulsante “Avvia percorso” per iniziare la navigazione con Google Maps \n" +
                "4.\tPer scegliere un altro parcheggio selezionalo sulla mappa. \n" +
                "5.\tAttraverso il menù si puo salvare il parcheggio selezionato oppure caricare l'ultimo parcheggio salvato \n");





    }
}
