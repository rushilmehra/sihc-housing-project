package com.scu.housing.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.scu.housing.R;
import com.scu.housing.other.House;

import java.util.ArrayList;

public class HouseDetailsActivity extends AppCompatActivity implements OnStreetViewPanoramaReadyCallback {
    House house;
    ArrayList<String> details;
    ListView houseDetailsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_details);

        // Add up button to action bar.
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Get house location data from intent.
        Intent intent = getIntent();
        house = new House(0, intent.getStringExtra("address"), intent.getBooleanExtra("available", false), intent.getDoubleExtra("bath", 0), intent.getIntExtra("bed", 0), intent.getStringExtra("description"), intent.getDoubleExtra("price", 0), intent.getDoubleExtra("latitude", 0), intent.getDoubleExtra("longitude", 0));

        // Initialize street view fragment.
        StreetViewPanoramaFragment streetViewPanoramaFragment = (StreetViewPanoramaFragment) getFragmentManager().findFragmentById(R.id.house_street_view_fragment);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);

        // Set contents of address text view.
        TextView houseAddressTextView = (TextView) findViewById(R.id.house_address_text_view);
        houseAddressTextView.setText(house.getAddress());

        // Initialize array list of house details for adapter.
        details = new ArrayList<>();
        details.add("Available: " + house.isAvailable());
        details.add("Bath: " + house.getBath());
        details.add("Bed: " + house.getBed());
        details.add("Description: " + house.getDescription());
        details.add("Price: " + house.getPrice());

        // Initialize list view.
        houseDetailsListView = (ListView) findViewById(R.id.house_details_list_view);
        houseDetailsListView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, details));
    }

    @Override
    public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
        streetViewPanorama.setPosition(new LatLng(house.getLatitude(), house.getLongitude()));
    }
}
