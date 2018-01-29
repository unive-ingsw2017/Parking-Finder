package it.unive.dais.cevid.datadroid.lib.experimental;

import it.unive.dais.cevid.datadroid.lib.experimental.comm.FullDuplexActivityChannel;
import it.unive.dais.cevid.datadroid.lib.experimental.comm.HalfDuplexActivityChannel;
import it.unive.dais.cevid.datadroid.lib.experimental.comm.Reply;
import it.unive.dais.cevid.datadroid.lib.experimental.comm.Request;
import it.unive.dais.cevid.datadroid.lib.experimental.comm.Command;
import it.unive.dais.cevid.datadroid.lib.experimental.comm.RPCFullDuplexActivityChannel;
import it.unive.dais.cevid.datadroid.lib.experimental.comm.RPCHalfDuplexActivityChannel;
import it.unive.dais.cevid.datadroid.lib.experimental.comm.SimpleFullDuplexActivityChannel;
import it.unive.dais.cevid.datadroid.lib.experimental.comm.SimpleHalfDuplexActivityChannel;

import java.io.Serializable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public abstract class SimpleActivity<SavedInstanceState extends Serializable>
extends Activity implements IActivity<SavedInstanceState> {
	
	public SimpleActivity() {
		super();
	}
	
	@Override
	public final Activity asActivity() {
		return this;
	}
	
	public static <Req extends Serializable,
			Rep extends Serializable>
	SimpleFullDuplexActivityChannel<Req, Rep> createSimpleFullDuplexActivityChannel(IActivity<?> sender, Class<? extends IActivity<?>> recvcl) {
		return Aux.createSimpleFullDuplexActivityChannel(sender, recvcl);
	}
	
	public static <Sender extends IActivity<?>,
			Receiver extends IActivity<?>,
			Req extends Serializable,
			Rep extends Serializable>
	FullDuplexActivityChannel<Sender, Receiver, Req, Rep> createFullDuplexActivityChannel(Class<Receiver> recvcl) {
		return Aux.createFullDuplexActivityChannel(recvcl);
	}
	
	public static <Cmd extends Serializable>
	SimpleHalfDuplexActivityChannel<Cmd> createSimpleHalfDuplexActivityChannel(IActivity<?> sender, Class<? extends IActivity<?>> recvcl) {
		return Aux.createSimpleHalfDuplexActivityChannel(sender, recvcl);
	}
	
	public static <Sender extends IActivity<?>,
			Receiver extends IActivity<?>,
			Cmd extends Serializable>
	HalfDuplexActivityChannel<Sender, Receiver, Cmd> createHalfDuplexActivityChannel(Class<Receiver> recvcl) {
		return Aux.createHalfDuplexActivityChannel(recvcl);
	}

	// typed API
	//
	
	@Override
	public <T extends Serializable> void setResult(int resultCode, TyIntent<T> i) {
		aux.setResult(resultCode, i);
	}

	@Override
	public <T extends Serializable> void startActivityForResult(TyIntent<T> i, int requestCode) {
		aux.startActivityForResult(i, requestCode);
	}

	@Override
	public <T extends Serializable> void startActivity(TyIntent<T> i) {
		aux.startActivity(i);
	}
	
	protected void onCreate(TyBundle<SavedInstanceState> tb) {
		aux.onCreate(tb);
	}
					
	// framework overrides
	//
	
	@Override
	@Deprecated
	protected final void onCreate(Bundle b) {
		super.onCreate(b);
		aux.onCreate(b);
	}

	@Override
	protected abstract void onActivityResult(int requestCode, int resultCode, Intent i);
		
	// framework protected overrides workaround
	//
	
	@Override
	public void __protected_onCreate(TyBundle<SavedInstanceState> tb) {
		this.onCreate(tb);
	}
	
	@Override
	public void __protected_onCreate(Bundle b) {
		super.onCreate(b);
	}
	
	@Override
	public void __protected_onActivityResult(int requestCode, int resultCode, Intent i) {
		super.onActivityResult(requestCode, resultCode, i);
	}

	// delegate object containing shared implementation
	//
	
	protected final Aux<SavedInstanceState> aux = new Aux<>(this);

	static class Aux<SavedInstanceState extends Serializable> {

		protected final IActivity<SavedInstanceState> _super;
		
		Aux(IActivity<SavedInstanceState> _super) {
			this._super = _super;
		}
		
		public static <Req extends Serializable,
				Rep extends Serializable>
		SimpleFullDuplexActivityChannel<Req, Rep> createSimpleFullDuplexActivityChannel(IActivity<?> sender, Class<? extends IActivity<?>> recvcl) {
			return new SimpleFullDuplexActivityChannel<>(sender, recvcl);
		}
		
		public static <Sender extends IActivity<?>,
				Receiver extends IActivity<?>,
				Req extends Serializable,
				Rep extends Serializable>
		FullDuplexActivityChannel<Sender, Receiver, Req, Rep> createFullDuplexActivityChannel(Class<Receiver> recvcl) {
			return new FullDuplexActivityChannel<>(recvcl);
		}
		
		public static <Sender extends IActivity<?>,
				Receiver extends IActivity<?>,
				Req extends Request<Sender, Receiver, Rep>,
			    Rep extends Reply<Sender>>
		RPCFullDuplexActivityChannel<Sender, Receiver, Req, Rep> createRPCFullDuplexActivityChannel(Class<Receiver> recvcl) {
			return new RPCFullDuplexActivityChannel<>(recvcl);
		}
						
		public static <Cmd extends Serializable>
		SimpleHalfDuplexActivityChannel<Cmd> createSimpleHalfDuplexActivityChannel(IActivity<?> sender, Class<? extends IActivity<?>> recvcl) {
			return new SimpleHalfDuplexActivityChannel<>(sender, recvcl);
		}
		
		public static <Sender extends IActivity<?>,
				Receiver extends IActivity<?>,
				Cmd extends Serializable>
		HalfDuplexActivityChannel<Sender, Receiver, Cmd> createHalfDuplexActivityChannel(Class<Receiver> recvcl) {
			return new HalfDuplexActivityChannel<>(recvcl);
		}
		
		public static <Sender extends IActivity<?>,
				Receiver extends IActivity<?>,
				Cmd extends Command<Receiver>>
		RPCHalfDuplexActivityChannel<Sender, Receiver, Cmd> createRPCHalfDuplexActivityChannel(Class<Receiver> recvcl) {
			return new RPCHalfDuplexActivityChannel<>(recvcl);
		}

		<T extends Serializable> void startActivityForResult(TyIntent<T> i, int requestCode) {		
			_super.startActivityForResult(i.getIntent(), requestCode);
		}
		
		<T extends Serializable> void startActivity(TyIntent<T> i) {		
			_super.startActivity(i.getIntent());
		}
		
		void onCreate(TyBundle<SavedInstanceState> tb) {
			_super.__protected_onCreate(tb == null ? null : tb.getBundle());
		}
		
		void onCreate(Bundle b) {
			_super.__protected_onCreate(TyBundle.<SavedInstanceState>fromBundle(b));
		}

		<T extends Serializable> void setResult(int resultCode, TyIntent<T> i) {
			_super.setResult(resultCode, i.getIntent());
		}
							
		void onActivityResult(int requestCode, int resultCode, Intent i) {
			_super.__protected_onActivityResult(requestCode, resultCode, i);
			switch (resultCode) {
			case RESULT_OK:
				TyIntent<Reply<IActivity<SavedInstanceState>>> ti = TyIntent.create(i);
				ti.get().get().eval(_super);
				break;
	    	
			default:
				throw new IllegalArgumentException(String.format("resultCode(%d) != RESULT_OK", resultCode));
			}
		}
			
	}

}
