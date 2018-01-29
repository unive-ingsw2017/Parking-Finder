package it.unive.dais.cevid.datadroid.lib.experimental;

import it.unive.dais.cevid.datadroid.lib.experimental.comm.FullDuplexActivityChannel;
import it.unive.dais.cevid.datadroid.lib.experimental.comm.HalfDuplexActivityChannel;
import it.unive.dais.cevid.datadroid.lib.experimental.comm.SimpleFullDuplexActivityChannel;
import it.unive.dais.cevid.datadroid.lib.experimental.comm.SimpleHalfDuplexActivityChannel;

import java.io.Serializable;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

public abstract class SimpleListActivity<SavedInstanceState extends Serializable>
        extends ListActivity
        implements IActivity<SavedInstanceState> {

    public SimpleListActivity() {
        super();
    }

    @Override
    public final Activity asActivity() {
        return this;
    }

    public static <Req extends Serializable,
            Rep extends Serializable>
    SimpleFullDuplexActivityChannel<Req, Rep> createSimpleFullDuplexActivityChannel(IActivity<?> sender, Class<? extends IActivity<?>> recvcl) {
        return SimpleActivity.Aux.createSimpleFullDuplexActivityChannel(sender, recvcl);
    }

    public static <Sender extends IActivity<?>,
            Receiver extends IActivity<?>,
            Req extends Serializable,
            Rep extends Serializable>
    FullDuplexActivityChannel<Sender, Receiver, Req, Rep> createFullDuplexActivityChannel(Class<Receiver> recvcl) {
        return SimpleActivity.Aux.createFullDuplexActivityChannel(recvcl);
    }

    public static <Cmd extends Serializable>
    SimpleHalfDuplexActivityChannel<Cmd> createSimpleHalfDuplexActivityChannel(IActivity<?> sender, Class<? extends IActivity<?>> recvcl) {
        return SimpleActivity.Aux.createSimpleHalfDuplexActivityChannel(sender, recvcl);
    }

    public static <Sender extends IActivity<?>,
            Receiver extends IActivity<?>,
            Cmd extends Serializable>
    HalfDuplexActivityChannel<Sender, Receiver, Cmd> createHalfDuplexActivityChannel(Class<Receiver> recvcl) {
        return SimpleActivity.Aux.createHalfDuplexActivityChannel(recvcl);
    }

    // typed API
    //

    @Override
    public <T extends Serializable> void setResult(int resultCode, TyIntent<T> i) {
        aux.setResult(resultCode, i);
    }

    @Override
    public <T extends Serializable> void startActivityForResult(TyIntent<T> i, int requestCode) {
        aux.startActivityForResult(i, requestCode);
    }

    @Override
    public <T extends Serializable> void startActivity(TyIntent<T> i) {
        aux.startActivity(i);
    }

    protected void onCreate(TyBundle<SavedInstanceState> tb) {
        aux.onCreate(tb);
    }

    // framework overrides
    //

    @Override
    @Deprecated
    protected final void onCreate(Bundle b) {
        super.onCreate(b);
        aux.onCreate(b);
    }

	/*@Override
    protected <T extends Serializable> void onActivityResult(int requestCode, int resultCode, TyIntent<T> i) {
		super.onActivityResult(requestCode, resultCode, i);
	}*/

    // framework protected overrides workaround
    //

    @Override
    public void __protected_onCreate(TyBundle<SavedInstanceState> tb) {
        this.onCreate(tb);
    }

    @Override
    public void __protected_onCreate(Bundle b) {
        super.onCreate(b);
    }

    @Override
    public void __protected_onActivityResult(int requestCode, int resultCode, Intent i) {
        super.onActivityResult(requestCode, resultCode, i);
    }

    // delegate object containing shared implementation
    //

    protected final SimpleActivity.Aux<SavedInstanceState> aux = new SimpleActivity.Aux<>(this);
}	
	
