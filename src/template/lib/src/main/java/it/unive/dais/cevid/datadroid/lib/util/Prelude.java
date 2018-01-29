package it.unive.dais.cevid.datadroid.lib.util;

import android.support.annotation.NonNull;

import java.util.Comparator;

/**
 * Piccola libreria di utilità di vario genere.
 * @author Alvise Spanò, Università Ca' Foscari
 */
public final class Prelude {

    /**
     * Elimina tutte le occorrenze dei caratteri passati dall'inizio e dalla fine della stringa data.
     * Non vengono eliminate le occorrenze in mezzo alla stringa.
     * Questa funzione produce una nuova stringa, non modifica quella passata come argomento.
     * @param s la stringa da manipolare.
     * @param cs array di caratteri da eliminare.
     * @return ritorna la nuova stringa.
     */
    public static String trim(String s, char[] cs) {
        for (char c : cs) {
            s = s.replaceAll(c + "$", "").replaceAll("^" + c, "");
        }
        return s;
    }

    /**
     * Limita il parametro x all'interno dell'intervallo tra a e b dato un oggetto comparatore non-nullo.
     * Questa funzione è generica sul tipo numerico e richiede un Comparator per eseguire i confronti tra i valori.
     * @param a estremo sinistro dell'intervallo.
     * @param b estremo destro dell'intervallo.
     * @param x valore da limitare.
     * @param c oggetto di tipo {@code Comparator<T>} che permette il confronto tra i valori di tipo T.
     * @param <T> il tipo dei valori numerici da manipolare.
     * @return risultato.
     */
    public static <T> T crop(T a, T b, T x, @NonNull Comparator<T> c) {
        return c.compare(x,  a) <= 0 ? a : c.compare(x, b) >= 0 ? b : x;
    }

    /**
     * Limita il parametro x all'interno dell'intervallo tra a e b.
     * Questa funzione è generica sul tipo numerico, richiedento che implementi l'interfaccia {@code Comparable<T>}.
     * Ad esempio è possibile chiamare questa funzione con gli int, i double ed altri tipi builtin.
     * @param a estremo sinistro dell'intervallo.
     * @param b estremo destro dell'intervallo.
     * @param x valore da limitare.
     * @param <T> il tipo dei valori numerici da manipolare, che deve implementare {@code Comparable<T>}.
     * @return risultato.
     */
    public static <T extends Comparable<T>> T crop(T a, T b, T x) {
        return x.compareTo(a) <= 0 ? a : x.compareTo(b) >= 0 ? b : x;
    }

    /**
     * Dato un parametro x definito nell'intervallo tra a0 e b0, proietta x entro l'intervallo tra a1 e b1 mantenendo le proporzioni.
     * Chiamato y il risultato della proiezione, garantisce che {@code (x - a0) / (b0 - a0) = (y - a1) / (b1 - a1)}.
     * In altre parole, garantisce che la distanza
     * relativa di x dall'estremo sinistro dell'intervallo di partenza sta alla lunghezza di quest'ultimo come la distanza di
     * y dall'estremo sinistro dell'intervallo di destinazione sta alla lunghezza di quest'ultimo.
     * Per esempio, {@code proj(0, 10, 100, 200, 6) = 160} poiché la proporzione tra 6 e l'intervallo {@code [0, 10]} è uguale
     * alla proporzione tra 160 e {@code [100, 200]}.
     * @param a0 estremo sinistro dell'intervallo di partenza.
     * @param b0 estremo destro dell'intervallo di partenza.
     * @param a1 estremo sinistro dell'intervallo di destinazione.
     * @param b1 estremo destro dell'intervallo di destinazione.
     * @param x valore di tipo double da proiettare.
     * @return risultato della proiezione.
     */
    public static double proj(double a0, double b0, double a1, double b1, double x) {
        x = crop(a0, b0, x);
        return (x - a0) * (b0 - a0) / (b1 - a1) + a1;
    }

}
