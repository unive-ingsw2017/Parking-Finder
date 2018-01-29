package it.unive.dais.cevid.datadroid.lib.parser;

import android.support.annotation.NonNull;
import android.util.JsonReader;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe astratta che rappresenta un parser JSON generico.
 * E' necessario ereditare questa classe e implementare almeno il metodo astratto {@code parseItem} che deve parsare un
 * singolo oggetto JSON da mettere nella lista che il metodo {@code parse} ritorna.
 * Qualora il formato del JSON non sia una lista, è possibile fare override del metodo {@code parse} e
 * definire un parser specifico per un formato particolare, ritornando una lista unitaria.
 * @param <Item> il tipo di un elemento del JSON, ad esempio un tipo definito dall'utente che rappresenta una singola
 *              riga di dati.
 * @param <Progress> tipo Progress che viene inoltrato alla superclasse AsyncTask.
 * @author Alvise Spanò, Università Ca' Foscari
 */
public abstract class AbstractAsyncJsonParser<Item, Progress> extends AbstractAsyncParser<Item, Progress> {

    @NonNull protected final JsonReader reader;

    /**
     * Costruttore protected via Reader.
     * @param rd oggetto Reader usato come input.
     */
    protected AbstractAsyncJsonParser(@NonNull Reader rd) {
        this.reader = new JsonReader(rd);
    }

    /**
     * Costruttore protected via URL.
     * @param url oggetto URL usato come input.
     */
    protected AbstractAsyncJsonParser(@NonNull URL url) throws IOException {
        this(urlToReader(url));
    }

    /**
     * Implementazione che parsa il JSON come una lista (o array) di oggetti di tipo Item.
     * @return ritorna una lista di Item.
     * @throws IOException lancia questa eccezione quando ci sono problemi di I/O.
     */
    @Override
    @NonNull
    public List<Item> parse() throws IOException {
        List<Item> r = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            r.add(parseItem(reader));
        }
        reader.endArray();
        return r;
    }

    /**
     * Metodo astratto che deve implementare il parser di un singolo oggetto JSON.
     * @param reader input di tipo JsonReader.
     * @return ritorna un singolo oggetto di tipo Item.
     * @throws IOException lancia questa eccezione quando ci sono problemi di I/O.
     */
    @NonNull
    protected abstract Item parseItem(JsonReader reader) throws IOException;

}