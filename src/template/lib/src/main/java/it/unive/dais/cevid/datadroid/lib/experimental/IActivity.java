package it.unive.dais.cevid.datadroid.lib.experimental;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;

public interface IActivity<SavedInstanceState extends Serializable> {
		
	// typed API	
	Activity asActivity();
	<T extends Serializable> void setResult(int resultCode, TyIntent<T> i);
	<T extends Serializable> void startActivityForResult(TyIntent<T> i, int requestCode);
	<T extends Serializable> void startActivity(TyIntent<T> i);
	
	// framework overrides
	void setResult(int resultCode, Intent i);
	void startActivityForResult(Intent i, int requestCode);
	void startActivity(Intent i);
	Intent getIntent();
	void finish();

	// framework protected override workaround proxies
	void __protected_onCreate(Bundle tb);
	void __protected_onCreate(TyBundle<SavedInstanceState> tb);
	void __protected_onActivityResult(int requestCode, int resultCode, Intent i);

}
