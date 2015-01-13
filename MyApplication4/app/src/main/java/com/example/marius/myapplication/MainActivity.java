package com.example.marius.myapplication;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;

import static com.google.android.gms.common.GooglePlayServicesUtil.isGooglePlayServicesAvailable;


public class MainActivity extends RWServices implements SigninFragment.OnFragmentInteractionListener,
        RegistrationFragment.OnFragmentInteractionListener, WelcomeFragment.OnFragmentInteractionListener,
        SaveLocationFragment.OnFragmentInteractionListener,MapFragment.OnFragmentInteractionListener,
        LocationsListFragment.OnFragmentInteractionListener, LocationListener
        {

    private android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
    private String currentUser;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private Location location;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        location = locationManager.getLastKnownLocation(provider);

        SigninFragment signIn = new SigninFragment();
        ft.replace(R.id.details, signIn, "SignInFragment") ;
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);


    }


    @Override
    public void OnLogInButtonPressedInRegistrationFragment(String username, String password, String retypedPassword) throws IOException
    {
     super.CreateFileWriter("Authentification.txt");
     super.CreateFileReader("Authentification.txt");
     if(!super.CheckUsernameExistance(username)) {
         if(password.equals(retypedPassword) )
             if(password.length()>5 ) {
                 try {
                     super.WriteLine(username, password, "Authentification.txt");
                     Toast.makeText(getApplicationContext(), "Username and password registered. You can Sing In now.", Toast.LENGTH_LONG).show();
                     android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                     SigninFragment signIn = new SigninFragment();
                     ft.replace(R.id.details, signIn, "Sign In Fragment");
                     ft.commit();
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             }
             else Toast.makeText(getApplication().getBaseContext(), "The password is too short", Toast.LENGTH_LONG).show();
         else Toast.makeText(getApplication().getBaseContext(), "The two passwords are different", Toast.LENGTH_LONG).show();
     }
     else Toast.makeText(getApplication().getBaseContext(), "Username already existent", Toast.LENGTH_LONG).show();

    }

    @Override
    public void OnSignInButtonPressedInSignInFragment(String username, String password) throws IOException {

        if(super.VerifyFileExistance("Authentification.txt")) {
            try {
                if (super.CheckCredentialsValidity(username, password)) {
                    android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                    WelcomeFragment welcome = new WelcomeFragment();
                    ft.replace(R.id.details, welcome, "Welcome fragment");
                    ft.commit();
                    currentUser = username;
                    Toast.makeText(getApplicationContext(), "Welcome "+username+" !", Toast.LENGTH_LONG).show();
                }
                else Toast.makeText(getApplicationContext(), "Combination username-password not existent", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
            Toast.makeText(getApplicationContext(), "Combination username-password not existent - no authentification file", Toast.LENGTH_LONG).show();

    }

    @Override
    public void OnLogInButtonPressedInSignInFrgment(Uri uri) {
        android.support.v4.app.FragmentTransaction ftRegister = fm.beginTransaction();
        RegistrationFragment register = new RegistrationFragment();
        ftRegister.replace(R.id.details, register, "RegisterFragment");
        ftRegister.commit();
    }

    @Override
    public void OnCancelButtonPressedInRegistrationFragment(Uri uri)
    {
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        SigninFragment signIn = new SigninFragment();
        ft.replace(R.id.details, signIn, "SignInFragment") ;
        ft.commit();
    }


    @Override
    public void onSaveLocationCoordinatesButtonPressedInWelcomeFragment() {
        int googlePlayAvailable = isGooglePlayServicesAvailable(this.getApplicationContext());
        /*if(googlePlayAvailable != ConnectionResult.SUCCESS)
        {
            Toast.makeText(this.getApplication().getBaseContext(), "Google play error : " + GooglePlayServicesUtil.getErrorString(googlePlayAvailable), Toast.LENGTH_LONG).show();
            return;
           //GooglePlayServicesUtil.getErrorDialog(googlePlayAvailable,null,0);
        }*/
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        com.example.marius.myapplication.MapFragment mapFragment = new com.example.marius.myapplication.MapFragment();
        ft.replace(R.id.details, mapFragment, "MapFragment") ;
        ft.commit();
        setUpMapIfNeeded();
    }

    @Override
    public void onSignOutButtonPressedInWelcomeFragment() {
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        SigninFragment signIn = new SigninFragment();
        ft.replace(R.id.details, signIn, "SignInFragment") ;
        ft.commit();
    }

    @Override
    public void onShowMyLocationButtonPressedInWelcomeFragment() throws IOException {
       if(super.VerifyFileExistance(currentUser+"'s_locations.txt")) {
           ArrayList<String> locations = new ArrayList<String>();
           try {
               locations = super.GetLocationsList(currentUser+"'s_locations.txt");//currentUser + "'s_locations.txt");
           } catch (IOException e) {
               e.printStackTrace();
           }
           //verify if any data saved already
           if (locations == null){
              Toast.makeText(getApplicationContext(), "You didn't saved any locations yet", Toast.LENGTH_LONG).show();
              return;
           }

           Bundle bundle= new Bundle();
           bundle.putStringArrayList("key",locations);

           android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
           LocationsListFragment locationsListFragment = new LocationsListFragment();
           locationsListFragment.setArguments(bundle);
           ft.replace(R.id.details, locationsListFragment, "SignInFragment") ;
           ft.commit();

       }
        else Toast.makeText(getApplicationContext(), "You didn't saved any locations yet", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onSaveLocationCoordinatesInSaveLocationFragment(String departureCity, String destinationCity, String description){

            String locationAndDescription = location.getLatitude()+"#" + location.getLongitude()+"#"+description;
            String departureAndDestination = departureCity+"#"+destinationCity+"#";

            try {
                super.WriteLine(departureAndDestination,locationAndDescription,currentUser+"'s_locations.txt");
            } catch (Exception e) {
                e.printStackTrace();
            }

            Toast.makeText(getApplicationContext(), "Location saved succesfully !", Toast.LENGTH_LONG).show();
            android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
            WelcomeFragment welcome = new WelcomeFragment();
            ft.replace(R.id.details, welcome, "Welcome fragment");
            ft.commit();

    }

    @Override
    public void onCancelButtonPressedInSaveLocationFragment() {

            android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
            WelcomeFragment welcome = new WelcomeFragment();
            ft.replace(R.id.details, welcome, "Welcome fragment");
            ft.commit();
    }


            @Override
    public void onMapFragmentInflate() {
        setUpMapIfNeeded();
    }


    @Override
    public void onSaveLocationButtonPressedInMapFragment() throws IOException {
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        SaveLocationFragment saveLocationFragment = new SaveLocationFragment();
        ft.replace(R.id.details, saveLocationFragment, "SaveLocationFragment");
        ft.commit();
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            //Toast.makeText(getApplicationContext(), "mMap e null" , Toast.LENGTH_LONG).show();
            SupportMapFragment map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            if(map != null)
                Toast.makeText(getApplicationContext(), "map nu e null" , Toast.LENGTH_LONG).show();
            if(map != null)
            mMap = (map).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        Location location = mMap.getMyLocation();
        mMap.addMarker(new MarkerOptions().position(new LatLng(0.0, 0.0)).title("Marker"));
    }


            //////////////////////////////////////////////////////////////////////////////////////////
            @Override
            public void onFragmentInteraction(Uri uri) {
                Toast.makeText(getApplicationContext(), "You didn't saved any locations yet", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onItemSelectedInList(String string) {
                Toast.makeText(getApplicationContext(), "You selected : "+string , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onGoBackButton() {
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                WelcomeFragment welcomeFragment = new WelcomeFragment();
                ft.replace(R.id.details, welcomeFragment, "welcomeFragment");
                ft.commit();
            }


            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
            ////////////////////////////////////////////////////////////////////////////////////////
        }
