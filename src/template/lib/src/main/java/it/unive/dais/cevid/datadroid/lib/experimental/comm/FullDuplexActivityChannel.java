package it.unive.dais.cevid.datadroid.lib.experimental.comm;

import it.unive.dais.cevid.datadroid.lib.experimental.IActivity;
import it.unive.dais.cevid.datadroid.lib.experimental.TyIntent;

import java.io.Serializable;

public class FullDuplexActivityChannel<Sender extends IActivity<?>,
        Receiver extends IActivity<?>,
        Req extends Serializable,
        Rep extends Serializable>
        extends AbstractActivityChannel<Sender, Receiver> {

    @SuppressWarnings("unused")
    private final Receiver __recv = null;

    @Deprecated
    public FullDuplexActivityChannel() {
        super("__recv");
    }

    protected FullDuplexActivityChannel(String fieldName) {
        super(fieldName);
    }

    public FullDuplexActivityChannel(Class<? extends Receiver> recvcl) {
        super(recvcl);
    }

    public void sendRequest(Sender sender, Req req) {
        TyIntent<Req> i = new TyIntent<>(sender.asActivity(), getReceiverClass(), req);
        sender.startActivityForResult(i, 0);
    }

    public void sendReply(Receiver recv, Rep r) {
        returnIntent(recv, r);
    }

    public Rep receiveReply(Sender sender) {
        return extractIntent(sender);
    }

    public Req receiveRequest(Receiver recv) {
        return extractIntent(recv);
    }


    ////
    ////

    public class SenderApi {
        public void startActivityForResult(Req req) {
//            startActivityForResult(req);
            throw new UnsupportedOperationException();
        }

        public Rep getReply(IActivity<?> a) {
//            return getReply(a);
            throw new UnsupportedOperationException();
        }
    }

    public class ReceiverApi {
        public Req getRequest(IActivity<?> a) {
//            return getRequest(a);
            throw new UnsupportedOperationException();
        }

        public void setReply(IActivity<?> a, Rep r) {
//            setReply(a, r);
            throw new UnsupportedOperationException();
        }
    }

    public SenderApi asSender = new SenderApi();
    public ReceiverApi asReceiver = new ReceiverApi();

}
