package it.unive.dais.cevid.datadroid.lib.parser;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Clase astratta parametrica che rappresenta un parser di dati in senso generale, sottoclasse di AsyncTask.
 * L'utente deve ereditare questa classe ed implementarne i metodi mancanti oppure utilizzare direttamente alcune sottoclassi non astratte
 * già contenute in questa libreria.
 *
 * @param <Data> il tipo di una riga di dati (non dell'intera collezione dei dati).
 * @param <Progress> tipo Progress inoltrato alla classe parametrica AsyncTask.
 *                  Da usare per rappresentare il progresso del parsing, come una progress bar.
 *                  Per ignorarlo passare il tipo Void come parametro Progress a questa classe.
 * @author Alvise Spanò, Università Ca' Foscari
 */
public abstract class AbstractAsyncParser<Data, Progress> implements AsyncParser<Data, Progress> {

    private static final String TAG = "AbstractAsyncParser";
    protected final MyAsyncTask asyncTask = new MyAsyncTask();

    /**
     * Converte una URL in un {@code InputStreamReader}.
     * Questo metodo statico è utile per implementare, nelle sottoclassi di questa classe, un costruttore aggiuntivo un parametro di
     * tipo URL come, che può essere convertito in un {@code InputStreamReader} tramite questo metodo statico e passato rapidamente
     * al costruttore principale, come per esempio:
     * <blockquote><pre>
     * {@code
     * public static class MyDataParser extends AbstractAsyncParser<MapItem, Void, InputStreamReader> {
     *      protected MyDataParser(InputStreamReader rd) {
     *          super(rd);
     *      }
     *
     *      protected MyDataParser(URL url) throws IOException {
     *          super(urlToReader(url));
     *      }
     *
     *      protected List<MapItem> parse(InputStreamReader rd) throws IOException {
     *          // fai qualcosa usando rd
     *      }
     * }
     * }
     * </pre></blockquote>
     * @param url parametro di tipo URL.
     * @return risultato di tipo InputStreamReader.
     * @throws IOException lancia questa eccezione quando sorgono problemi di I/O.
     */
    @NonNull
    protected static InputStreamReader urlToReader(@NonNull URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream stream = connection.getInputStream();
        return new InputStreamReader(stream);
    }

    /**
     * Metodo di cui è necessario fare override nelle sottoclassi.
     * Deve occuparsi del parsing vero e proprio.
//     * @param input parametro di tipo Input.
     * @return ritorna una lista di oggetti di tipo FiltrableData.
     * @throws IOException lanciata se il parser incontra problemi.
     */
    @Override
    @NonNull
    public abstract List<Data> parse() throws IOException;

    @Override
    @NonNull
    public String getName() { return AbstractAsyncParser.class.getName(); }


    /**
     * Restituisce l'oggetto interno di tipo AsyncTask.
     * @return oggetto di tipo AsyncTask.
     */
    @Override
    public AsyncTask<Void, Progress, List<Data>> getAsyncTask() {
        return asyncTask;
    }

    protected class MyAsyncTask extends AsyncTask<Void, Progress, List<Data>> {
        /**
         * Metodo interno che invoca {@code parse} all'interno di un blocco try..catch.
         * Non è necessario fare override a meno che non si desideri specificare un comportamento diverso.
         * Il metodo da definire nelle sottoclassi è {@code parse}.
         * @param params nessun parametro.
         * @return la lista di dati prodotti da {@code parse}.
         */
        @Override
        @Nullable
        protected List<Data> doInBackground(Void... params) {
            final String name = AbstractAsyncParser.this.getName();
            try {
                Log.v(TAG, String.format("started parser %s", name));
                List<Data> r = parse();
                Log.v(TAG, String.format("parser %s finished (%d elements)", name, r.size()));
                return r;
            } catch (IOException e) {
                Log.e(TAG, String.format("exception caught during parser %s: %s", name, e));
                e.printStackTrace();
                return null;
            }
        }

        /**
         * Questo metodo è solamente uno stub di {@code publishProgress}.
         * E' necessario perché {@code publishProgress} ha visibilità {@code protected} e quindi non può essere chiamato
         * dalle sottoclassi di {@code AbstractAsyncParser}.
         * @param p
         */
        void publish(Progress... p) { this.publishProgress(p); }
    }
}
