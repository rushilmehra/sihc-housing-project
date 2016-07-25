package com.scu.housing.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.scu.housing.R;
import com.scu.housing.adapters.HouseListAdapter;
import com.scu.housing.other.House;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HousesActivity extends AppCompatActivity implements OnMapReadyCallback {
    Context context;
    private ArrayList<House> houses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_houses);

        context = this;

        // Download houses from server.
        //final String url = "http://172.21.98.196:3001/api/Houses";
        //new DownloadAsyncTask().execute(url);

        // Initialize list of houses.
        houses = new ArrayList<>();
        houses.addAll(House.getDummyData());

        // Initialize the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);

        // Initialize list of houses.
        RecyclerView houseList = (RecyclerView) findViewById(R.id.house_list);
        houseList.setLayoutManager(new LinearLayoutManager(this));
        HouseListAdapter houseListAdapter = new HouseListAdapter(this, houses);
        houseList.setAdapter(houseListAdapter);

        final Button button = (Button) findViewById(R.id.btnGotoApp);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent startApplicationActivity = new Intent(context, ApplicationActivity.class);
                startActivity(startApplicationActivity);
            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        for (int i = 0; i < houses.size(); i++) {
            House house = houses.get(i);
            LatLng latLng = getLocationFromAddress(this, house.getAddress());
            if (latLng != null) {
                // Huzzah! Geocoding succeeded! Display pin!
                house.setLatitude(latLng.latitude);
                house.setLongitude(latLng.longitude);
                googleMap.addMarker(new MarkerOptions().title(house.getAddress()).snippet(house.getLatitude() + " , " + house.getLongitude()).position(latLng));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }
            else if (house.getLatitude() != 0 && house.getLongitude() != 0){
                // Boo! Geocoding failed! See if House has coordinates, and display those instead!
                googleMap.addMarker(new MarkerOptions().title(house.getAddress()).snippet(house.getLatitude() + " , " + house.getLongitude()).position(new LatLng(house.getLatitude(), house.getLongitude())));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(house.getLatitude(), house.getLongitude()), 15));
            }
            else {
                // Geocoding failed and House does not have coordinates. (Edge Case: House is in the Gulf of Guinea, by the coast of South Africa. Poor house.)
                Log.e("Housing", "This house has an unknown address and no coordinates: " + house.toString());
            }
        }
    }

    // Method adapted from Nayanesh Gupte's post @ http://bit.ly/29SaomP.
    public LatLng getLocationFromAddress(Context context, String addressString) {

        Geocoder coder = new Geocoder(context);
        List<Address> addresses;
        LatLng latLng = null;

        try {
            addresses = coder.getFromLocationName(addressString, 5);
            if (addresses == null) {
                return null;
            }
            Address address = addresses.get(0);
            latLng = new LatLng(address.getLatitude(), address.getLongitude());

        } catch (Exception e) {
            // The method Geocoder.getFromLocationName can throw IllegalArgumentException if location is null or IOException if network is unavailable.
            // e.printStackTrace();
        }

        return latLng;
    }

    public class DownloadAsyncTask extends AsyncTask<String, Void, String> {

        InputStream inputStream;

        @Override
        protected String doInBackground(String... strings) {
            return downloadHouses(strings[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (!result.equals("failed")) {
                parseResult(result);
            } else {
                Log.e("Housing", "Failed to fetch data.");
            }
        }

        private String downloadHouses(String string) {
            try {
                URL url = new URL(string);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                // Optional request headers.
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");

                urlConnection.setRequestMethod("GET");

                // Start query.
                urlConnection.connect();

                // Convert input stream to string.
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                return convertInputStreamToString(inputStream);

            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            }
        }

        private String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = "";
            String result = "";

            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }

            // Close input stream if at end of stream.
            if (null != inputStream) {
                inputStream.close();
            }

            return result;
        }

        private void parseResult(String result) {
            try {
                ArrayList<JSONObject> houseJSONObjects = new ArrayList<>();
                JSONArray response = new JSONArray(result);

                // Separate each house JSON object.
                for (int i = 0; i < response.length(); i++) {
                    houseJSONObjects.add(response.optJSONObject(i));
                }

                // Extract properties of each house JSON object.
                for (JSONObject houseJSONObject : houseJSONObjects) {
                    JSONObject locationJSONObject = houseJSONObject.optJSONObject("location");

                    House house = new House(houseJSONObject.optInt("id"),
                            houseJSONObject.optString("address"),
                            houseJSONObject.optBoolean("available"),
                            houseJSONObject.optInt("bath"),
                            houseJSONObject.optInt("bed"),
                            houseJSONObject.optString("description"),
                            houseJSONObject.optDouble("price"),
                            locationJSONObject.optDouble("lat"),
                            locationJSONObject.optDouble("lng"));

                    houses.add(house);
                }


            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("Housing", "Failed to parse result: " + e.toString());
            }

        }
    }

}