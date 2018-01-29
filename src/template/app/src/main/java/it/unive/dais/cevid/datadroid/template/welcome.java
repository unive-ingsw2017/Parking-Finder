package it.unive.dais.cevid.datadroid.template;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by utente on 07/12/2017.
 */

public  class welcome extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.welcome);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent myIntent = new Intent(welcome.this, MapsActivity.class);
                startActivity(myIntent);
            }
        }, 2000);   //2 aspetta secondi



    }

}
