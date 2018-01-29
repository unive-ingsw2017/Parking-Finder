package it.unive.dais.cevid.datadroid.lib.experimental.comm;

import java.io.Serializable;

import it.unive.dais.cevid.datadroid.lib.util.Function;


public abstract class Reply<T> implements Function<T, Void>, Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public abstract Void eval(T o);
}
