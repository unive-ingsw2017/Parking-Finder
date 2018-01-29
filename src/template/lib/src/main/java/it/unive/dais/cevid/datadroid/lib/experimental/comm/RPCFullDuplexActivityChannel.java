package it.unive.dais.cevid.datadroid.lib.experimental.comm;

import it.unive.dais.cevid.datadroid.lib.experimental.IActivity;
import it.unive.dais.cevid.datadroid.lib.util.Function;

public class RPCFullDuplexActivityChannel<Sender extends IActivity<?>,
										  Receiver extends IActivity<?>,
										  Req extends Request<Sender, Receiver, Rep>,
										  Rep extends Reply<Sender>>
extends FullDuplexActivityChannel<Sender, Receiver, Req, Rep> {
		
	@SuppressWarnings("unused")
	private final Receiver __recv = null;
	
	@Deprecated
	public RPCFullDuplexActivityChannel() {
		super("__recv");
	}
		
	public RPCFullDuplexActivityChannel(Class<Receiver> recvcl) {
		super(recvcl);
	}

	public void evalRequest(final Receiver recv) {
		AbstractActivityChannel.<Request<Sender, IActivity<?>, Rep>>extractIntent(recv).eval(recv,
				new Function<Rep, Void>() {
					@Override
					public Void eval(Rep rep) {
						returnIntent(recv, rep);
						return null;
					}
				});
	}

}
