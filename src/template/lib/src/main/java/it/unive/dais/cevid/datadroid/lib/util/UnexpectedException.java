package it.unive.dais.cevid.datadroid.lib.util;

/**
 * Eccezione di tipo inatteso, sottoclasse di RuntimeException.
 * Da usare per segnalare un errore che non è contemplato dall'applicazione, come ad esempio un caso che non dovrebbe accadere oppure scenari
 * non previsti ma che non si possono escludere dal codice del programma.
 * @author Alvise Spanò, Università Ca' Foscari
 */
public class UnexpectedException extends RuntimeException {
    /**
     * Costruttore per stringa.
     * @param msg la stringa col messaggio contenuto nell'eccezione.
     */
    public UnexpectedException(String msg) {
        super(msg);
    }
}
