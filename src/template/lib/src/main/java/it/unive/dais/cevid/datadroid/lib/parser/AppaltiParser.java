package it.unive.dais.cevid.datadroid.lib.parser;

import android.support.annotation.NonNull;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class AppaltiParser<Progress> extends AbstractAsyncParser<AppaltiParser.Data, Progress> {
    private static final String TAG = "AppaltiParser";
    private static final String DATI_ASSENTI_O_MAL_FORMATTATI = "Dati assenti o mal formattati";

    private List<URL> urls;

    public AppaltiParser(List<URL> url) {
        this.urls = url;
    }

    @NonNull
    @Override
    public List<Data> parse() throws IOException {
        NodeList nodes;
        List<Data> datalist = new ArrayList<>();
        for (URL url : urls) {
            try {
                URLConnection conn = url.openConnection();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(conn.getInputStream());
                nodes = doc.getElementsByTagName("lotto");
                datalist.addAll(parseNodes(nodes));
            } catch (ParserConfigurationException | SAXException e) {
                throw new IOException(e);
            }
        }
        return datalist;
    }

    protected String getTextByTag(Element e, String tagName, String defaultString) {
        Node n = getElementByTag(e, tagName);
        return n == null ? defaultString : n.getTextContent();
    }

    protected String getTextByTag(Element e, String tagName) {
        return getTextByTag(e, tagName, DATI_ASSENTI_O_MAL_FORMATTATI);
    }

    protected Element getElementByTag(Element e, String tagName) {
        return (Element) e.getElementsByTagName(tagName).item(0);
    }

    protected List<Data> parseNodes(NodeList nodes) {
        List<Data> r = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Element parent = (Element) nodes.item(i);
            Data d = new Data();

            //controlli aggiudicatario
            Element e = getElementByTag(parent, "aggiudicatario");
            if (e != null) {
                d.aggiudicatario = getTextByTag(e, "ragioneSociale");
                d.codiceFiscaleAgg = getTextByTag(e, "codiceFiscale", "0");
            } else {
                d.aggiudicatario = DATI_ASSENTI_O_MAL_FORMATTATI;
                d.codiceFiscaleAgg = "0";
            }

            //controllo proponente
            e = getElementByTag(parent, "proponente");
            if (e != null) {
                d.proponente = getTextByTag(e, "ragioneSociale");
                d.codiceFiscaleProp = getTextByTag(e, "codiceFiscale", "0");
            } else {
                d.proponente = DATI_ASSENTI_O_MAL_FORMATTATI;
                d.codiceFiscaleProp = "0";
            }

            //controllo tempi di completamento
            e = getElementByTag(parent, "tempiCompletamento");
            if (e != null) {
                d.dataInizio = getTextByTag(e, "dataInizio", "0");
                d.dataFine= getTextByTag(e, "dataUltimazione", "0");
            } else {
                d.dataInizio = "0";
                d.dataFine = "0";
            }

            //controllo oggetto
            d.oggetto = getTextByTag(parent, "oggetto");

            //controllo scelta contraente
            d.sceltac = getTextByTag(parent, "sceltaContraente");

            //controllo importo
            d.importo = getTextByTag(parent, "importoAggiudicazione", "0");

            //controllo importo somme liquidate
            d.importoSommeLiquidate = getTextByTag(parent,"importoSommeLiquidate","0");

            //controllo cig
            d.cig = getTextByTag(parent, "cig", "0");

            r.add(d);
        }
        return r;
    }


    public static class Data implements Serializable{
        public String cig;
        public String proponente;
        public String codiceFiscaleProp;
        public String oggetto;
        public String sceltac;
        public String aggiudicatario;
        public String codiceFiscaleAgg;
        public String importo;
        public String importoSommeLiquidate;
        public String dataInizio;
        public String dataFine;
    }
}
