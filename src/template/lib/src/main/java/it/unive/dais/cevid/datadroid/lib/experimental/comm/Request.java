package it.unive.dais.cevid.datadroid.lib.experimental.comm;

import it.unive.dais.cevid.datadroid.lib.experimental.IActivity;
import it.unive.dais.cevid.datadroid.lib.util.Function;

import java.io.Serializable;

public abstract class Request<Sender extends IActivity<?>,
							  Receiver extends IActivity<?>,
							  Rep extends Reply<Sender>>
implements Function<Receiver, Rep>, Serializable {
	private static final long serialVersionUID = 1L;
	
	@Override
	public Rep eval(Receiver recv) { throw new UnsupportedOperationException(); }
	
	public void eval(Receiver recv, Function<Rep, Void> ret) {
		ret.eval(eval(recv));
	}
		
}
