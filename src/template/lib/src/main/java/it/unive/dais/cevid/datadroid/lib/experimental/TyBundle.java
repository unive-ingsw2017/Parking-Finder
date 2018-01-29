package it.unive.dais.cevid.datadroid.lib.experimental;

import java.io.Serializable;

import android.os.*;

public class TyBundle<T extends Serializable> {
	
	static protected final String DEFAULT_KEY = "default_key"; 
	
	protected final Bundle bundle;
	
	public TyBundle() {
		bundle = new Bundle();
	}
	
	public TyBundle(int capacity) {
		bundle = new Bundle(capacity);
	}
	
	protected TyBundle(Bundle b) {
		bundle = b;
	}
	
	public TyBundle(TyBundle<T> b) {
		bundle = b.getBundle();
	}

	public TyBundle(T o) {
		this();
		put(o);
	}

	public void put(T o) {
		bundle.putSerializable(DEFAULT_KEY, o);
	}
	
	@SuppressWarnings("unchecked")
	public T get() {
		return (T) bundle.getSerializable(DEFAULT_KEY);
	}

	public final Bundle getBundle() {
		return bundle;
	}

    public static <T extends Serializable> TyBundle<T> fromBundle(Bundle b) {
		return b == null ? null : new TyBundle<T>(b);
	}
}
