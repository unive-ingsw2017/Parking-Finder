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
        testo.setText("HELP \n oioiuiui");




    }
}
