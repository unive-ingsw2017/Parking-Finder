package it.unive.dais.cevid.datadroid.lib.parser;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Interfaccia che rappresenta un parser di dati qualsiasi.
 * @param <Data>
 */
public interface Parser<Data> {
    @NonNull List<Data> parse() throws IOException;
    @NonNull String getName();
}
