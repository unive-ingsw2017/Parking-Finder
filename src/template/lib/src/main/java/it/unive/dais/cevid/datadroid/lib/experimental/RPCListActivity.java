package it.unive.dais.cevid.datadroid.lib.experimental;


import it.unive.dais.cevid.datadroid.lib.experimental.comm.Reply;
import it.unive.dais.cevid.datadroid.lib.experimental.comm.Command;
import it.unive.dais.cevid.datadroid.lib.experimental.comm.RPCFullDuplexActivityChannel;
import it.unive.dais.cevid.datadroid.lib.experimental.comm.RPCHalfDuplexActivityChannel;
import it.unive.dais.cevid.datadroid.lib.experimental.comm.Request;
import java.io.Serializable;

import android.content.Intent;

public abstract class RPCListActivity<SavedInstanceState extends Serializable> extends SimpleListActivity<SavedInstanceState> {

	@Override
	protected final void onActivityResult(int requestCode, int resultCode, Intent i) {
		aux.onActivityResult(requestCode, resultCode, i);
	}
	
	/*@Override
	@Deprecated
	public <Req extends Serializable,
			Rep extends Serializable>
	SimpleFullDuplexActivityChannel<Req, Rep> createSimpleFullDuplexActivityChannel(IActivity<?> sender, Class<? extends IActivity<?>> recvcl) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public <Sender extends IActivity<?>,
			Receiver extends IActivity<?>,
			Req extends Serializable,
			Rep extends Serializable>
	FullDuplexActivityChannel<Sender, Receiver, Req, Rep> createFullDuplexActivityChannel(Class<Receiver> recvcl) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public <Cmd extends Serializable>
	SimpleHalfDuplexActivityChannel<Cmd> createSimpleHalfDuplexActivityChannel(IActivity<?> sender, Class<? extends IActivity<?>> recvcl) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Deprecated
	public <Sender extends IActivity<?>,
			Receiver extends IActivity<?>,
			Cmd extends Serializable>
	HalfDuplexActivityChannel<Sender, Receiver, Cmd> createHalfDuplexActivityChannel(Class<Receiver> recvcl) {
		throw new UnsupportedOperationException();
	}*/
	
	public static <Sender extends IActivity<?>,
			Receiver extends IActivity<?>,
			Req extends Request<Sender, Receiver, Rep>,
		    Rep extends Reply<Sender>>
	RPCFullDuplexActivityChannel<Sender, Receiver, Req, Rep> createRPCFullDuplexActivityChannel(Class<Receiver> recvcl) {
		return SimpleActivity.Aux.createRPCFullDuplexActivityChannel(recvcl);
	}
	
	public static <Sender extends IActivity<?>,
			Receiver extends IActivity<?>,
			Cmd extends Command<Receiver>>
	RPCHalfDuplexActivityChannel<Sender, Receiver, Cmd> createRPCHalfDuplexActivityChannel(Class<Receiver> recvcl) {
		return SimpleActivity.Aux.createRPCHalfDuplexActivityChannel(recvcl);
	}

}
