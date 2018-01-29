package it.unive.dais.cevid.datadroid.lib.experimental.comm;

import it.unive.dais.cevid.datadroid.lib.experimental.IActivity;
import it.unive.dais.cevid.datadroid.lib.experimental.TyIntent;

import java.io.Serializable;


public class HalfDuplexActivityChannel<Sender extends IActivity<?>,
									   Receiver extends IActivity<?>,
									   Cmd extends Serializable>
extends AbstractActivityChannel<Sender, Receiver> {
			
	@SuppressWarnings("unused")
	private final Receiver __recv = null;
	
	@Deprecated
	public HalfDuplexActivityChannel() {
		super("__recv");
	}
	
	protected HalfDuplexActivityChannel(String fieldName) {
		super(fieldName);
	}
		
	public HalfDuplexActivityChannel(Class<? extends Receiver> recvcl) {
		super(recvcl);
	}

	public void sendCommand(Sender sender, Cmd cmd) {
		TyIntent<Cmd> i = new TyIntent<>(sender.asActivity(), getReceiverClass(), cmd);
		sender.startActivity(i);
	}
	
}
