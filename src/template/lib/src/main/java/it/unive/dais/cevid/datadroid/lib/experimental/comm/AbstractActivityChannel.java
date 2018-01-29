package it.unive.dais.cevid.datadroid.lib.experimental.comm;

import it.unive.dais.cevid.datadroid.lib.experimental.IActivity;
import it.unive.dais.cevid.datadroid.lib.experimental.TyIntent;

import java.io.Serializable;
import java.lang.reflect.Field;

import android.app.Activity;

public abstract class AbstractActivityChannel<Sender extends IActivity<?>, Receiver extends IActivity<?>> {
	
	private Class<? extends Receiver> recvcl;
	
	@SuppressWarnings("unchecked")
	protected AbstractActivityChannel(String fieldname) {
		try {
			Field f = RPCFullDuplexActivityChannel.class.getDeclaredField(fieldname);
			this.recvcl = (Class<? extends Receiver>) f.getType();
		}
		catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected AbstractActivityChannel(Class<? extends Receiver> recvcl) {
		this.recvcl = recvcl;
	}
			
	protected static <T extends Serializable> T extractIntent(IActivity<?> a) {
		TyIntent<T> i = TyIntent.create(a.getIntent());
		return i.get().get();
	}
	
	protected static <T extends Serializable> void returnIntent(IActivity<?> a, T o) {
		a.setResult(Activity.RESULT_OK, new TyIntent<>(o));
		a.finish();
	}
	
	protected Class<? extends Receiver> getReceiverClass() {
		return recvcl;
	}

}
