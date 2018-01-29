package it.unive.dais.cevid.datadroid.lib.experimental;

import it.unive.dais.cevid.datadroid.lib.experimental.comm.Reply;
import it.unive.dais.cevid.datadroid.lib.experimental.comm.Request;

import java.io.Serializable;

import android.content.*;
import android.os.Bundle;

public class TyIntent<T extends Serializable> extends Intent {
	
	protected TyIntent(Intent i) {
		super(i);
	}
	
	public TyIntent(TyIntent<T> ti) {
		super(ti);
	}
	
	public TyIntent(T o) {
		super();
		put(o);
	}

	TyIntent(Context ctx, Class<?> cl) {
		super(ctx, cl);
	}
	
	public TyIntent(Context ctx, Class<?> cl, T o) {
		this(ctx, cl);
		put(o);
	}

	public static <T extends Serializable> TyIntent<T> create(Intent i) {
		return new TyIntent<>(i);
	}
		
	public static <Req extends Request<Sender, Receiver, Rep>,
				   Rep extends Reply<Sender>,
				   Sender extends IActivity<?>,
				   Receiver extends IActivity<?>>
	TyIntent<Req> create(IActivity<?> ita, Class<Receiver> cl, Req a) {
		return new TyIntent<>(ita.asActivity(), cl, a);
	}
	
	public void put(TyBundle<T> tb) {
		super.putExtras(tb.getBundle());
	}
	
	public void put(T o) {
		put(new TyBundle<>(o));
	}
	
	public TyBundle<T> get() {
		Bundle b = super.getExtras();
		return new TyBundle<>(b);
	}

	public final Intent getIntent() {
		return this;
	}
}

