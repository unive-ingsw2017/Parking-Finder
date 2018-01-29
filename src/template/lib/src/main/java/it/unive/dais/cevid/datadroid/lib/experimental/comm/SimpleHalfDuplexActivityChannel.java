package it.unive.dais.cevid.datadroid.lib.experimental.comm;

import it.unive.dais.cevid.datadroid.lib.experimental.IActivity;

import java.io.Serializable;

public class SimpleHalfDuplexActivityChannel<Cmd extends Serializable>
extends HalfDuplexActivityChannel<IActivity<?>, IActivity<?>, Cmd> {

	protected IActivity<?> sender;
		
	public SimpleHalfDuplexActivityChannel(IActivity<?> sender, Class<? extends IActivity<?>> recvcl) {
		super(recvcl);
		this.sender = sender;
	}

	protected SimpleHalfDuplexActivityChannel(String fieldName) {
		super(fieldName);
	}
	
	public static <Cmd extends Serializable>
	SimpleHalfDuplexActivityChannel<Cmd> create(IActivity<?> sender, Class<? extends IActivity<?>> recv) {
		return new SimpleHalfDuplexActivityChannel<>(sender, recv);
	}
	
	public void sendCommand(Cmd cmd) {
		sendCommand(this.sender, cmd);
	}

}
