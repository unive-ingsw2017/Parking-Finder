package it.unive.dais.cevid.datadroid.lib.experimental.comm;

import it.unive.dais.cevid.datadroid.lib.experimental.IActivity;

import java.io.Serializable;

public class SimpleFullDuplexActivityChannel<Req extends Serializable,
						                     Rep extends Serializable>
extends FullDuplexActivityChannel<IActivity<?>, IActivity<?>, Req, Rep> {
		
	protected IActivity<?> sender;
	
	public SimpleFullDuplexActivityChannel(IActivity<?> sender, Class<? extends IActivity<?>> recvcl) {
		super(recvcl);
		this.sender = sender;
	}

	protected SimpleFullDuplexActivityChannel(String fieldName) {
		super(fieldName);
	}
		
	public void sendRequest(Req req) {
		sendRequest(this.sender, req);
	}
	
	public Rep receiveReply() {
		return receiveReply(this.sender);
	}

}
