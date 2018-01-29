package it.unive.dais.cevid.datadroid.template;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.coreutils.BuildConfig;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


/**
 * Activity per la schermata di crediti e about.
 *
 * @author Alvise Spanò, Università Ca' Foscari
 */
public class InfoActivity extends AppCompatActivity {

    /**
     * Produce la stringa completa coi crediti.
     * @param ctx oggetto Context, tipicamente {@code this} se chiamato da un'altra Activity.
     * @return ritorna la stringa completa.
     */
    public static String credits(Context ctx) {
        ApplicationInfo ai = ctx.getApplicationInfo();
        StringBuffer buf = new StringBuffer();
        buf.append("\tVERSION.RELEASE {").append(Build.VERSION.RELEASE).append("}");
        buf.append("\n\tVERSION.INCREMENTAL {").append(Build.VERSION.INCREMENTAL).append("}");
        buf.append("\n\tVERSION.SDK {").append(Build.VERSION.SDK_INT).append("}");
        buf.append("\n\tBOARD {").append(Build.BOARD).append("}");
        buf.append("\n\tBRAND {").append(Build.BRAND).append("}");
        buf.append("\n\tDEVICE {").append(Build.DEVICE).append("}");
        buf.append("\n\tFINGERPRINT {").append(Build.FINGERPRINT).append("}");
        buf.append("\n\tHOST {").append(Build.HOST).append("}");
        buf.append("\n\tID {").append(Build.ID).append("}");
        return String.format(
                "--- APP ---\n Sviluppata da Abdelmoughit faris Tusar Dhali Gianfilippo Bellin Badr Wahmane \n" +
                        "%s v%s [%s]\n" +
                        "(c) %s %s @ %s - %s \n\n" +
                        "--- ANDROID ---\n%s ",
                ctx.getString(ai.labelRes),
                BuildConfig.VERSION_NAME,
                BuildConfig.BUILD_TYPE,
                R.string.credits_year, R.string.credits_project, R.string.credits_company, R.string.credits_authors,
                buf);
    }

    /**
     * Metodo di creazione dell'activity che imposta il layout e la text view con la stringa con i crediti.
     * @param saveInstanceState
     */
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_info);
        TextView tv_1 = (TextView) findViewById(R.id.textView_1);
        tv_1.setText(credits(this));
    }

}
