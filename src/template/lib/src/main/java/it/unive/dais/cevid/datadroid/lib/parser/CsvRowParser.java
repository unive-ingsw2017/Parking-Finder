package it.unive.dais.cevid.datadroid.lib.parser;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.text.ParseException;

import it.unive.dais.cevid.datadroid.lib.util.UnexpectedException;

/**
 * Sottoclasse di {@code AbstractAsyncCsvParser} che implementa un parser riga-per-riga di CSV.
 * Questa classe è usabile direttamente e non necessita di essere ereditata.
 * Non utilizza il generic FiltrableData e non richiede la definizione di una classe per rappresentare le colonne
 * di una riga di CSV; viene invece utilizzata la classe {@code Row} che rappresenta tale informazione
 * in maniera untyped ma generale tramite un dizionario.
 * Un esempio d'uso con un file CSV con header e virgole come separatore:
 * <blockquote><pre>
 * {@code
 * CsvRowParser parser = new CsvRowParser(new FileReader("nome_file.csv"), true, ",");
 * List<CsvRowParser.Row> rows = parser.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
 * for (CsvRowParser.Row row : rows) {
 *     String id = row.get("ID"), nome = row.get("NAME");
 *     // fai qualcosa con id e nome
 * }
 * }
 * </pre></blockquote>
 *
 * @author Alvise Spanò, Università Ca' Foscari
 */
public class CsvRowParser extends AbstractAsyncCsvParser<CsvRowParser.Row> {

    private static final String TAG = "CsvRowParser";

    /**
     * Costruttore tramite Reader.
     *
     * @param rd        parametro di tipo Reader da cui leggere il CSV.
     * @param hasHeader flag booleano che indica se il CSV ha un header alla prima riga.
     * @param sep       separatore tra le colonne del CSV (ad esempio il punto e virgola ";" oppure la virgola ",").
     */
    public CsvRowParser(@NonNull Reader rd, boolean hasHeader, String sep) {
        super(rd, hasHeader, sep);
    }

    /**
     * Costruttore tramite file.
     *
     * @param file      oggetto di tipo File.
     * @param hasHeader flag booleano che indica se il CSV ha un header alla prima riga.
     * @param sep       separatore tra le colonne del CSV (ad esempio il punto e virgola ";" oppure la virgola ",").
     * @throws FileNotFoundException lanciata se il file non esiste.
     */
    public CsvRowParser(@NonNull File file, boolean hasHeader, String sep) throws FileNotFoundException {
        super(file, hasHeader, sep);
    }

    /**
     * Parsa una singola linea dato l'array di stringhe con le colonne separate.
     *
     * @param columns array di stringhe in cui ogni elemento contiene il contenuto (sotto forma di stringa) di ogni colonna.
     * @return ritorna un oggetto Row.
     * @throws ParseException        lanciata dal parser.
     * @throws NumberFormatException lanciata dal parser.
     */
    @NonNull
    @Override
    protected Row parseColumns(@NonNull String[] columns) throws ParseException {
        return new Row(columns);
    }

    /**
     * Classe che rappresenta una singola riga del CSV parsata dal {@code CsvRowParser}.
     * Si tratta di una rappresentazione senza tipi e generale, basata su un dizionario le cui chiavi sono i nomi delle colonne
     * trovati nell'header ed i valori sono ovviamente i valori delle colonne.
     * Un oggetto di tipo Row rappresenta una sola riga del CSV ed è consultabile come se fosse una mappa, ovvero
     * tramite metodi {@code get} via chiave, dove la chiave è il nome della colonna di cui si vuole sapere il valore.
     * Per i CSV senza header, le chiavi del dizionario sono i numeri delle colonne, sotto forma di stringhe, a partire
     * dal numero 0.
     */
    public class Row {

        private String[] values;

        /**
         * Costruttore tramite array di valori delle colonne.
         *
         * @param values array con i valori da impostare per tutte le colonne.
         */
        protected Row(String[] values) {
            put(values);
        }

        /**
         * Imposta il valore di una colonna dato il nome.
         *
         * @param column il nome della colonna.
         * @param value  il valore da impostare.
         */
        public void put(String column, String value) {
            put(indexOfColumn(column), value);
        }

        /**
         * Calcola l'indice dato il nome di una colonna dell'header.
         * Permette sostanzialmente di sintetizzare l'accesso via nome tramite un indice.
         *
         * @param column il nome della colonna.
         * @return ritorna il numero della colonna a cui corrisponde il nome dato (dove 0 è la prima); oppure
         * una eccezione {@code IllegalArgumentException} nel caso in cui il nome non esista.
         */
        protected int indexOfColumn(String column) {
            column = trimString(column);
            String[] h = getHeader();
            for (int i = 0; i < h.length; i++) {
                String s = trimString(h[i]);
                if (s.equalsIgnoreCase(column)) {
                    return i;
                }
            }
            throw new IllegalArgumentException(String.format("cannot find column '%s' in header", column));
        }

        /**
         * Ritorna l'header a cui questa Row è associata, similmente al metodo {@code CsvParser.getHeader}, ma
         * non può ritornare null perché non è possibile avere il riferimento ad un oggetto di tipo Row
         * senza che sia già stato parsato l'header, pertanto il caso in cui l'header è null è inatteso
         * e lancia una eccezione {@code UnexpectedException}.
         *
         * @return ritorna l'header associato a questa Row sotto forma di array di stringhe.
         */
        @NonNull
        private String[] getHeader() {
            String[] h = CsvRowParser.this.getHeader();
            if (h == null) throw new UnexpectedException("no header parsed yet");
            return h;
        }

        /**
         * Imposta il valore di una colonna dato l'indice (da 0 alla numero di colonne meno 1).
         * Funziona sia quando il CSV ha un header, sia quando l'header è generato automaticamente.
         *
         * @param i     indice della colonna.
         * @param value value da impostare.
         */
        public void put(int i, String value) {
            values[i] = trimString(value);
        }

        /**
         * Imposta tutte le colonne da un array di valori.
         *
         * @param values valori da impostare.
         */
        public void put(String[] values) {
            String[] h = getHeader();
            if (values.length != h.length)
                throw new IllegalArgumentException(String.format("CSV has %d columns but header has %d", values.length, h.length));
            this.values = trimStrings(values);
        }

        /**
         * Ritorna il valore contenuto alla colonna data.
         *
         * @param column il nome della colonna.
         * @return ritorna il valore (stringa) contenuto alla colonna data.
         */
        @NonNull
        public String get(String column) {
            return trimString(get(indexOfColumn(column)));
        }

        /**
         * Ritorna il valore contenuto alla colonna all'indice dato.
         *
         * @param i l'indice della colonna.
         * @return ritorna il valore (stringa) contenuto alla colonna all'indice dato.
         */
        @NonNull
        public String get(int i) {
            return values[i];
        }

        /**
         * Dimensione della riga; essa è uguale a quella dell'header.
         *
         * @return la dimensione della riga.
         */
        public int size() {
            return values.length;
        }

        /**
         * Ritorna i valori sotto forma di array.
         *
         * @return ritorna l'array dei valori.
         */
        @NonNull
        public String[] getValues() {
            return values;
        }


    }
}
