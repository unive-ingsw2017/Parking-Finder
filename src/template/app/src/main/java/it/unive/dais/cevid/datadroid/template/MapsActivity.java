/**
 * Questo package contiene le componenti Android riusabili.
 * Si tratta di classi che contengono già funzionalità base e possono essere riusate apportandovi modifiche
 */
package it.unive.dais.cevid.datadroid.template;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import it.unive.dais.cevid.datadroid.lib.parser.AsyncParser;
import it.unive.dais.cevid.datadroid.lib.parser.CsvRowParser;
import it.unive.dais.cevid.datadroid.lib.util.MapItem;

/**
 * Questa classe è la componente principale del toolkit: fornisce servizi primari per un'app basata su Google Maps, tra cui localizzazione, pulsanti
 * di navigazione, preferenze ed altro. Essa rappresenta un template che è una buona pratica riusabile per la scrittura di app, fungendo da base
 * solida, robusta e mantenibile.
 * Vengono rispettate le convenzioni e gli standard di qualità di Google per la scrittura di app Android; ogni pulsante, componente,
 * menu ecc. è definito in modo che sia facile riprodurne degli altri con caratteristiche diverse.
 * All'interno del codice ci sono dei commenti che aiutano il programmatore ad estendere questa app in maniera corretta, rispettando le convenzioni
 * e gli standard qualitativi.
 * Per scrivere una propria app è necessario modificare questa classe, aggiungendo campi, metodi e codice che svolge le funzionalità richieste.
 *
 * @author Alvise Spanò, Università Ca' Foscari
 */
public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnMarkerClickListener {

    protected static final int REQUEST_CHECK_SETTINGS = 500;
    protected static final int PERMISSIONS_REQUEST_ACCESS_BOTH_LOCATION = 501;
    // alcune costanti
    private static final String TAG = "MapsActivity";
    /**
     * Questo oggetto è la mappa di Google Maps. Viene inizializzato asincronamente dal metodo {@code onMapsReady}.
     */
    protected GoogleMap gMap;
    /**
     * Pulsanti in sovraimpressione gestiti da questa app. Da non confondere con i pulsanti che GoogleMaps mette in sovraimpressione e che non
     * fanno parte degli oggetti gestiti manualmente dal codice.
     */
    protected ImageButton button_search, button_car,button_Parcheggio_piu_vicino,button_save;
    /**
     * API per i servizi di localizzazione.
     */
    protected FusedLocationProviderClient fusedLocationClient;
    /**
     * Posizione corrente. Potrebbe essere null prima di essere calcolata la prima volta.
     */
    @Nullable
    protected LatLng currentPosition = null;
    /**
     * Il marker che viene creato premendo il pulsante button_search (cioè quello dell'app, non quello di Google Maps).
     * E' utile avere un campo d'istanza che tiene il puntatore a questo marker perché così è possibile rimuoverlo se necessario.
     * E' null quando non è stato creato il marker, cioè prima che venga premuto il pulsante HERE la prima volta.
     */
    @Nullable
    protected Marker hereMarker = null;

    /**
     * Questo metodo viene invocato quando viene inizializzata questa activity.
     * Si tratta di una sorta di "main" dell'intera activity.
     * Inizializza i campi d'istanza, imposta alcuni listener e svolge gran parte delle operazioni "globali" dell'activity.
     *
     * @param savedInstanceState bundle con lo stato dell'activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // inizializza le preferenze
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // trova gli oggetti che rappresentano i bottoni e li salva come campi d'istanza
        button_search = (ImageButton) findViewById(R.id.button_search);
        button_car = (ImageButton) findViewById(R.id.button_car);
        button_Parcheggio_piu_vicino = (ImageButton) findViewById(R.id.button_parcheggio_vicino);
        button_save  = (ImageButton) findViewById(R.id.button_save);


        // API per i servizi di localizzazione
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // inizializza la mappa asincronamente
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // quando viene premito il pulsante HERE viene eseguito questo codice
        button_search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.d(TAG, "here button clicked");
                gpsCheck();
                updateCurrentPosition();
                if (hereMarker != null) hereMarker.remove();
                if (currentPosition != null) {

                    MarkerOptions opts = new MarkerOptions();
                    opts.position(currentPosition);
                    opts.title(getString(R.string.marker_title));
                    opts.snippet(String.format("lat: %g\nlng: %g", currentPosition.latitude, currentPosition.longitude));
                    hereMarker = gMap.addMarker(opts);
                    piazzaMarker(); //aggiungo i marker


                    if (gMap != null)
                        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, getResources().getInteger(R.integer.zoomFactor_button_here)));
                    Toast.makeText(MapsActivity.this,"i parcheggi nel raggio di 2 km sono stati caricati",Toast.LENGTH_SHORT).show();
                } else
                    Log.d(TAG, "no current position available");
            }


        });

        button_Parcheggio_piu_vicino.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "here button clicked");
                gpsCheck();
                updateCurrentPosition();
                if (hereMarker != null) hereMarker.remove();
                if (currentPosition != null) {
                    MarkerOptions opts = new MarkerOptions();
                    opts.position(currentPosition);
                    opts.title(getString(R.string.marker_title));
                    opts.snippet(String.format("lat: %g\nlng: %g", currentPosition.latitude, currentPosition.longitude));
                    hereMarker = gMap.addMarker(opts);



                    MapItem parcheggio = piazzaParcheggioPiuVicino();
                    if (gMap != null)
                        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(parcheggio.getPosition(), getResources().getInteger(R.integer.zoomFactor_button_here)));
                    Toast.makeText(MapsActivity.this,"il parcheggio piu vicino è stato caricato",Toast.LENGTH_SHORT).show();
                } else
                    Log.d(TAG, "no current position available");
            }


        });




    }




    // ciclo di vita della app
    //

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * Applica le impostazioni (preferenze) della mappa ad ogni chiamata.
     */
    @Override
    protected void onResume() {
        super.onResume();
        applyMapSettings();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Pulisce la mappa quando l'app viene distrutta.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        gMap.clear();
    }

    /**
     * Quando arriva un Intent viene eseguito questo metodo.
     * Può essere esteso e modificato secondo le necessità.
     *
     * @see Activity#onActivityResult(int, int, Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(intent);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // inserire codice qui
                        break;
                    case Activity.RESULT_CANCELED:
                        // o qui
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    /**
     * Questo metodo viene chiamato quando viene richiesto un permesso.
     * Si tratta di un pattern asincrono che va gestito come qui impostato: per gestire nuovi permessi, qualora dovesse essere necessario,
     * è possibile riprodurre un comportamento simile a quello già implementato.
     *
     * @param requestCode  codice di richiesta.
     * @param permissions  array con i permessi richiesti.
     * @param grantResults array con l'azione da intraprende per ciascun dei permessi richiesti.
     * @see Activity#onRequestPermissionsResult(int, String[], int[])
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_BOTH_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "permissions granted: ACCESS_FINE_LOCATION + ACCESS_COARSE_LOCATION");
                } else {
                    Log.e(TAG, "permissions not granted: ACCESS_FINE_LOCATION + ACCESS_COARSE_LOCATION");
                    Snackbar.make(this.findViewById(R.id.map), R.string.msg_permissions_not_granted, Snackbar.LENGTH_LONG);
                }
            }
        }
    }

    /**
     * Invocato quando viene creato il menu delle impostazioni.
     *
     * @param menu l'oggetto menu.
     * @return ritornare true per visualizzare il menu.
     * @see Activity#onCreateOptionsMenu(Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.maps_with_options, menu);
        return true;
    }

    /**
     * Invocato quando viene cliccata una voce nel menu delle impostazioni.
     *
     * @param item la voce di menu cliccata.
     * @return ritorna true per continuare a chiamare altre callback; false altrimenti.
     * @see Activity#onOptionsItemSelected(MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.MENU_SETTINGS:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.MENU_INFO:
                startActivity(new Intent(this, InfoActivity.class));
                break;

            case R.id.MENU_HELP:
                startActivity(new Intent(this, Help.class));
                break;
            case R.id.PARCHEGGI_SALVATI:
               // startActivity(new Intent(this, InfoActivity.class));
                if(currentPosition!=null)
                  caricaParcheggio();
                break;


        }
        return false;
    }

    // onConnection callbacks
    //
    //

    /**
     * Viene chiamata quando i servizi di localizzazione sono attivi.
     * Aggiungere qui eventuale codice da eseguire in tal caso.
     *
     * @param bundle il bundle passato da Android.
     * @see com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks#onConnected(Bundle)
     */
    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "location service connected");
    }

    /**
     * Viene chiamata quando i servizi di localizzazione sono sospesi.
     * Aggiungere qui eventuale codice da eseguire in tal caso.
     *
     * @param i un intero che rappresenta il codice della causa della sospenzione.
     * @see com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks#onConnectionSuspended(int)
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "location service connection suspended");
        Toast.makeText(this, R.string.conn_suspended, Toast.LENGTH_LONG).show();
    }

    /**
     * Viene chiamata quando la connessione ai servizi di localizzazione viene persa.
     * Aggiungere qui eventuale codice da eseguire in tal caso.
     *
     * @param connectionResult oggetto che rappresenta il risultato della connessione, con le cause della disconnessione ed altre informazioni.
     * @see com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener#onConnectionFailed(ConnectionResult)
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "location service connection lost");
        Toast.makeText(this, R.string.conn_failed, Toast.LENGTH_LONG).show();
    }

    // maps callbacks
    //
    //

    /**
     * Chiamare questo metodo per aggiornare la posizione corrente del GPS.
     * Si tratta di un metodo proprietario, che non ridefinisce alcun metodo della superclasse né implementa alcuna interfaccia: un metodo
     * di utilità per aggiornare asincronamente in modo robusto e sicuro la posizione corrente del dispositivo mobile.
     */
    public void updateCurrentPosition() {
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "requiring permission");
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_BOTH_LOCATION);
        } else {
            Log.d(TAG, "permission granted");
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(MapsActivity.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location loc) {
                            if (loc != null) {
                                currentPosition = new LatLng(loc.getLatitude(), loc.getLongitude());
                                Log.i(TAG, "current position updated");
                            }
                        }
                    });
        }
    }

    /**
     * Viene chiamato quando si clicca sulla mappa.
     * Aggiungere qui codice che si vuole eseguire quando l'utente clicca sulla mappa.
     *
     * @param latLng la posizione del click.
     */
    @Override
    public void onMapClick(LatLng latLng) {
        // nascondi il pulsante della navigazione (non quello di google maps, ma il nostro pulsante custom)
        button_car.setVisibility(View.INVISIBLE);
        button_save.setVisibility(View.INVISIBLE);

    }

    /**
     * Viene chiamato quando si clicca a lungo sulla mappa (long click).
     * Aggiungere qui codice che si vuole eseguire quando l'utente clicca a lungo sulla mappa.
     *
     * @param latLng la posizione del click.
     */
    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    /**
     * Viene chiamato quando si muove la camera.
     * Attenzione: solamente al momento in cui la camera comincia a muoversi, non durante tutta la durata del movimento.
     *
     * @param reason
     */
    @Override
    public void onCameraMoveStarted(int reason) {
        setHereButtonVisibility();
    }

    /**
     * Metodo proprietario che imposta la visibilità del pulsante HERE.
     * Si occupa di nascondere o mostrare il pulsante HERE in base allo zoom attuale, confrontandolo con la soglia di zoom
     * impostanta nelle preferenze.
     * Questo comportamento è dimostrativo e non è necessario tenerlo quando si sviluppa un'applicazione modificando questo template.
     */
    public void setHereButtonVisibility() {
        if (gMap != null) {
            if (gMap.getCameraPosition().zoom < SettingsActivity.getZoomThreshold(this)) {
                button_search.setVisibility(View.INVISIBLE);
            } else {
                button_search.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Questo metodo è molto importante: esso viene invocato dal sistema quando la mappa è pronta.
     * Il parametro è l'oggetto di tipo GoogleMap pronto all'uso, che viene immediatamente assegnato ad un campo interno della
     * classe.
     * La natura asincrona di questo metodo, e quindi dell'inizializzazione del campo gMap, implica che tutte le
     * operazioni che coinvolgono la mappa e che vanno eseguite appena essa diventa disponibile, vanno messe in questo metodo.
     * Ciò non significa che tutte le operazioni che coinvolgono la mappa vanno eseguite qui: è naturale aver bisogno di accedere al campo
     * gMap in altri metodi, per eseguire operazioni sulla mappa in vari momenti, ma è necessario tenere a mente che tale campo potrebbe
     * essere ancora non inizializzato e va pertanto verificata la nullness.
     *
     * @param googleMap oggetto di tipo GoogleMap.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_BOTH_LOCATION);
        } else {
            gMap.setMyLocationEnabled(true);
        }

        gMap.setOnMapClickListener(this);
        gMap.setOnMapLongClickListener(this);
        gMap.setOnCameraMoveStartedListener(this);
        gMap.setOnMarkerClickListener(this);

        UiSettings uis = gMap.getUiSettings();
        uis.setZoomGesturesEnabled(true);
        uis.setMyLocationButtonEnabled(true);
        gMap.setOnMyLocationButtonClickListener(
                new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        gpsCheck();
                        return false;
                    }
                });
        uis.setCompassEnabled(true);
        uis.setZoomControlsEnabled(true);
        uis.setMapToolbarEnabled(true);

        applyMapSettings();



        gpsCheck();
        updateCurrentPosition();

    }

    /**
     * Metodo proprietario che forza l'applicazione le impostazioni (o preferenze) che riguardano la mappa.
     */
    protected void applyMapSettings() {
        if (gMap != null) {
            Log.d(TAG, "applying map settings");
            gMap.setMapType(SettingsActivity.getMapStyle(this));
        }
        setHereButtonVisibility();
    }

    /**
     * Naviga dalla posizione from alla posizione to chiamando il navigatore di Google.
     *
     * @param from posizione iniziale.
     * @param to   posizione finale.
     */
    protected void navigate(@NonNull LatLng from, @NonNull LatLng to) {
        Intent navigation = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=" + from.latitude + "," + from.longitude + "&daddr=" + to.latitude + "," + to.longitude + ""));
        navigation.setPackage("com.google.android.apps.maps");
        Log.i(TAG, String.format("starting navigation from %s to %s", from, to));
        startActivity(navigation);
    }

    // marker stuff
    //
    //

    /**
     * Callback che viene invocata quando viene cliccato un marker.
     * Questo metodo viene invocato al click di QUALUNQUE marker nella mappa; pertanto, se è necessario
     * eseguire comportamenti specifici per un certo marker o gruppo di marker, va modificato questo metodo
     * con codice che si occupa di discernere tra un marker e l'altro in qualche modo.
     *
     * @param marker il marker che è stato cliccato.
     * @return ritorna true per continuare a chiamare altre callback nella catena di callback per i marker; false altrimenti.
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        lastMarker=marker.getPosition();
        marker.showInfoWindow();
        button_car.setVisibility(View.VISIBLE);
        button_save.setVisibility(View.VISIBLE);

        button_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Snackbar.make(v, R.string.msg_button_car, Snackbar.LENGTH_SHORT);
                if (currentPosition != null) {
                    navigate(currentPosition, marker.getPosition());

                }

            }
        });


        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Snackbar.make(v, R.string.msg_button_car, Snackbar.LENGTH_SHORT);
                if(currentPosition!=null)
                    salvaParcheggio();
                Toast.makeText(MapsActivity.this,"il parcheggio selezionato è stato salvato",Toast.LENGTH_SHORT).show();


            }
        });

        return false;
    }





    /**
     * Metodo di utilità che permette di posizionare rapidamente sulla mappa una lista di MapItem.
     * Attenzione: l'oggetto gMap deve essere inizializzato, questo metodo va pertanto chiamato preferibilmente dalla
     * callback onMapReady().
     * @param l la lista di oggetti di tipo I tale che I sia sottotipo di MapItem.
     * @param <I> sottotipo di MapItem.
     * @return ritorna la collection di oggetti Marker aggiunti alla mappa.
     */
    @NonNull
    protected <I extends MapItem> Collection<Marker> putMarkersFromMapItems(List<I> l) {
        Collection<Marker> r = new ArrayList<>();
        for (MapItem i : l) {
            MarkerOptions opts = new MarkerOptions().title(i.getTitle()).position(i.getPosition()).snippet(i.getDescription()).icon(BitmapDescriptorFactory.fromResource(R.mipmap.park));
            //MarkerOptions opts = new MarkerOptions().title(i.getTitle()).position(i.getPosition()).snippet(i.getDescription());
            r.add(gMap.addMarker(opts));
        }
        return r;
    }

    /**
     * Metodo proprietario di utilità per popolare la mappa con i dati provenienti da un parser.
     * Si tratta di un metodo che può essere usato direttamente oppure può fungere da esempio per come
     * utilizzare i parser con informazioni geolocalizzate.
     *
     * @param parser un parser che produca sottotipi di MapItem, con qualunque generic Progress o Input
     * @param <I>    parametro di tipo che estende MapItem.
     * @return ritorna una collection di marker se tutto va bene; null altrimenti.
     */
    @Nullable
    protected <I extends MapItem> Collection<Marker> putMarkersFromData(@NonNull AsyncParser<I, ?> parser) {
        try {
            List<I> l = parser.getAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            Log.i(TAG, String.format("parsed %d lines", l.size()));
            return putMarkersFromMapItems(l);
        } catch (InterruptedException | ExecutionException e) {
            Log.e(TAG, String.format("exception caught while parsing: %s", e));
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Controlla lo stato del GPS e dei servizi di localizzazione, comportandosi di conseguenza.
     * Viene usata durante l'inizializzazione ed in altri casi speciali.
     */
    protected void gpsCheck() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(MapsActivity.this).addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(MapsActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }


    // demo code

    @Nullable
    private Collection<Marker> markers;

    private void piazzaMarker() {

        gMap.clear();
        updateCurrentPosition();
        try {

            InputStream is = getResources().openRawResource(R.raw.parcheggi_venezia);
            CsvRowParser p = new CsvRowParser(new InputStreamReader(is), true, ";");

            List<CsvRowParser.Row> rows = p.getAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            List<MapItem> l = new ArrayList<>();
            for (final CsvRowParser.Row r : rows) {

                final float [] distanza = new float[2];
                final String lat = r.get("Latitudine"), lng = r.get("Longitudine");
                double lati = /*(currentPosition==null)?  45.4844663 :*/ currentPosition.latitude;
                double longi = /*(currentPosition==null)?  12.2392899 : */currentPosition.longitude;
                Location.distanceBetween(Double.parseDouble(lat),Double.parseDouble(lng),lati,longi,distanza);

               // if(r.get("Comune").equals("Venezia"))
               if(distanza[0]<2000)/*carica solo i parcheggi al raggio di 2km*/
                    l.add(new MapItem() {
                        @Override
                        public LatLng getPosition() {



                            return new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                        }

                        @Override
                        public String getTitle() {
                            if(r.get("Nome").equals(""))
                                return "Parcheggio";
                            else
                                return r.get("Nome");
                        }

                        @Override
                        public String getDescription() {
                            return "distanza :"+distanza[0]+" metri";
                        }
                    });
            }
           markers = putMarkersFromMapItems(l);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


    }

    private MapItem piazzaParcheggioPiuVicino() {



        gMap.clear();
        updateCurrentPosition();
        try {
            double min=100000;
            MapItem piuVicino=null;
            InputStream is = getResources().openRawResource(R.raw.parcheggi_venezia);
            CsvRowParser p = new CsvRowParser(new InputStreamReader(is), true, ";");

            List<CsvRowParser.Row> rows = p.getAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get();
            List<MapItem> l = new ArrayList<>();
            for (final CsvRowParser.Row r : rows) {

                final float [] distanza = new float[2];
                final String lat = r.get("Latitudine"), lng = r.get("Longitudine");
                //Location.distanceBetween(Double.parseDouble(lat),Double.parseDouble(lng),45.4844663,12.2392899,distanza);
                double lati = /*(currentPosition==null)?  45.4844663 :*/ currentPosition.latitude;
                double longi = /*(currentPosition==null)?  12.2392899 : */currentPosition.longitude;
                Location.distanceBetween(Double.parseDouble(lat),Double.parseDouble(lng),lati,longi,distanza);


                if(distanza[0]<=min) {/**/
                    piuVicino = new MapItem() {
                        @Override
                        public LatLng getPosition() {


                            return new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                        }

                        @Override
                        public String getTitle() {
                            if (r.get("Nome").equals(""))
                                return "Parcheggio";
                            else
                                return r.get("Nome");
                        }

                        @Override
                        public String getDescription() {
                            return "distanza :" + distanza[0] + " metri";
                        }
                    };
                    min=distanza[0];
                }

            }
            l.add(piuVicino);
            markers = putMarkersFromMapItems(l);

           return (piuVicino);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

          return null;
    }

    String nomefile = "parcheggio";
    LatLng lastMarker;

    private void salvaParcheggio(){
        updateCurrentPosition();
        //String stringa = Double.toString(currentPosition.latitude)+"$"+Double.toString(currentPosition.longitude);
        String stringa = Double.toString(lastMarker.latitude)+"$"+Double.toString(lastMarker.longitude)+"$";


        try {
            FileOutputStream fos = openFileOutput(nomefile, Context.MODE_PRIVATE);
            fos.write(stringa.getBytes());
            fos.close();
        }catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void caricaParcheggio(){


        try {

            FileInputStream fos = openFileInput(nomefile);
            String input="";
            String lat="";
            String lng="";

            while(fos.available()>0&&!input.equals("$")){
                input=""+(char)fos.read();
                if(!input.equals("$"))
                   lat+=input;
            }
            input="";
            while(fos.available()>0&&!input.equals("$")){
                input=""+(char)fos.read();
                if(!input.equals("$"))
                   lng+=input;
            }

               navigate(currentPosition,new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)) );
            fos.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


}
