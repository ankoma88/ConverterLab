package com.ankoma88.converterlab.activities;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.ankoma88.converterlab.R;
import com.ankoma88.converterlab.adapters.MarkerWindowAdapter;
import com.ankoma88.converterlab.db.TraderDAO;
import com.ankoma88.converterlab.models.Trader;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**Activity for showing organization's location using Google Places API*/
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = MapActivity.class.getSimpleName();
    private static final String COUNTRY = "Ukraine";
    private static final LatLngBounds BOUNDS = new LatLngBounds(
            new LatLng(43.00, 23.00), new LatLng(52.00, 41.00));

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private Toolbar mToolbar;
    private Trader mTrader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setupToolbar();
        rebuildGoogleApiClient();
        initMap();
    }

    private void initMap() {
        if (mMap == null) {
            ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map))
                    .getMapAsync(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        setupMap(googleMap);
        searchForPlace();
    }

    private void setupMap(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setInfoWindowAdapter(new MarkerWindowAdapter(MapActivity.this));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return true;
            }
        });
    }

    /**Gets input data and starts search for specific place*/
    private void searchForPlace() {
        int traderId = getIntent().getIntExtra(TradersActivity.EXTRA_TRADER_ID, -1);
        if (traderId == -1) {
            informError();
            return;
        }

        getPlaceData(traderId);
    }

    /**Builds search query and starts search*/
    private void getPlaceData(int traderId) {
        final TraderDAO traderDAO = new TraderDAO(MapActivity.this);
        mTrader = traderDAO.get(traderId);
        updateToolbar();

        String placeQuery = COUNTRY + ", "
                + mTrader.getRegion() + ", "
                + mTrader.getCity() + ", "
                + mTrader.getAddress();

        loadPredictions(placeQuery);
    }

    private void updateToolbar() {
        mToolbar.setTitle(mTrader.getName());
        mToolbar.setSubtitle(mTrader.getCity());
    }

    /**Uses GeoDataApi for predictions search*/
    private void loadPredictions(final String placeQuery) {
        Log.d(TAG, "Searching for: " + placeQuery);
        PendingResult<AutocompletePredictionBuffer> predictionResults =
                Places.GeoDataApi.getAutocompletePredictions(
                        mGoogleApiClient, placeQuery, BOUNDS, null);
        predictionResults.setResultCallback(mPredictionsFoundCallback);
    }

    /**Uses GeoDataApi for place search*/
    private void loadPlace(final String placeId) {
        Log.d(TAG, "Searching for: " + placeId);
        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                .getPlaceById(mGoogleApiClient, placeId);
        placeResult.setResultCallback(mPlaceFoundCallback);
    }

    private void showPlaceMarker(Place place) {
        LatLng latLng = place.getLatLng();
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(mTrader.getName())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_bank))
                .snippet(place.getAddress().toString()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18), 500, null);
        marker.showInfoWindow();
    }

    private void rebuildGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Places.GEO_DATA_API)
                    .build();
        }
    }

    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_AM);
        setSupportActionBar(mToolbar);
        final ActionBar actionBar = MapActivity.this.getSupportActionBar();
        if (actionBar != null) {
            if (NavUtils.getParentActivityName(MapActivity.this) != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    private void informError() {
        Toast.makeText(MapActivity.this, R.string.error_search_place, Toast.LENGTH_LONG)
                .show();
    }

    /**Excludes bad address from search query and searches city*/
    private void doAlternativeSearch() {
        String alternativePlaceQuery = COUNTRY + ", "
                + mTrader.getRegion() + ", "
                + mTrader.getCity();
        mToolbar.setSubtitle(alternativePlaceQuery + mTrader.getAddress());

        loadPredictions(alternativePlaceQuery);
    }

    /**Result of places search. Shows found place on map*/
    private ResultCallback<PlaceBuffer> mPlaceFoundCallback
            = new ResultCallback<PlaceBuffer>() {

        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                informError();
                places.release();
                return;
            }

            showPlaceMarker(places.get(0));
            places.release();
        }
    };

    /**Result after predictions search. Starts place search on success*/
    private ResultCallback<AutocompletePredictionBuffer> mPredictionsFoundCallback
            = new ResultCallback<AutocompletePredictionBuffer>() {

        @Override
        public void onResult(AutocompletePredictionBuffer predictions) {
            if (!predictions.getStatus().isSuccess()) {
                informError();
                predictions.release();

            } else if (predictions.getCount() == 0) {
                predictions.release();
                doAlternativeSearch();

            } else {
                AutocompletePrediction prediction = predictions.get(0);
                loadPlace(prediction.getPlaceId());
                predictions.release();
            }
        }
    };

}
