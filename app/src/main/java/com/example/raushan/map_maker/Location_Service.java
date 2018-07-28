package com.example.raushan.map_maker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Location_Service extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button logout, addplace, sbmt;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    LatLng latLng;
    EditText plcnm;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        logout = (Button) findViewById(R.id.logout);
        mAuth = FirebaseAuth.getInstance();
        addplace = (Button) findViewById(R.id.addplace);
        sbmt = (Button) findViewById(R.id.sumbt);
        plcnm = (EditText) findViewById(R.id.placename);
        sbmt.setVisibility(View.GONE);
        plcnm.setVisibility(View.GONE);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(Location_Service.this, MainActivity.class));
            }
        });
        addplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sbmt.setVisibility(View.VISIBLE);
                plcnm.setVisibility(View.VISIBLE);
            }
        });
        sbmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String placename = plcnm.getText().toString();
                DatabaseReference places = FirebaseDatabase.getInstance().getReference(userId);
                GeoFire geoplac = new GeoFire(places);
                geoplac.setLocation(placename, new GeoLocation(latLng.latitude, latLng.longitude));
            }
        });
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(android.location.Location location) {
                    double longi = location.getLongitude();
                    double lati = location.getLatitude();
                    latLng = new LatLng(lati, longi);
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(lati, longi, 1);
                        String str = addressList.get(0).getLocality() + ",";
                        mMap.addMarker(new MarkerOptions().position(latLng));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.2f));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(android.location.Location location) {
                    double longi = location.getLongitude();
                    double lati = location.getLatitude();
                    latLng = new LatLng(lati, longi);
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(lati, longi, 1);
                        String str = addressList.get(0).getLocality() + ",";
                        mMap.addMarker(new MarkerOptions().position(latLng));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.2f));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Location ltt;
        mMap = googleMap;
//        getDriversAround();
    }
    List<Marker> markers = new ArrayList<Marker>();
    private void getDriversAround(){
        Log.v("location_list","entered1");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.v("location_list","entered2");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(userId).child("gktb");
        Log.v("location_list","entered3");
        GeoFire geoFire = new GeoFire(ref);
        Log.v("location_list","entered4");
        Log.v("location_list","entered5");
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(latLng.longitude, latLng.latitude), 99999);
        Log.v("location_list","entered6");
        Log.v("location_list","entered7");
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                LatLng driverLocation = new LatLng(location.latitude, location.longitude);
                //Marker Icon
                Marker mDriverMarker = mMap.addMarker(new MarkerOptions().position(driverLocation).title(key));
                mDriverMarker.setTag(key);
                markers.add(mDriverMarker);
            }
            @Override
            public void onKeyExited(String key) {

            }
            @Override
            public void onKeyMoved(String key, GeoLocation location) {
            }
            @Override
            public void onGeoQueryReady() {
            }
            @Override
            public void onGeoQueryError(DatabaseError error) {
            }
        });
    }
}
