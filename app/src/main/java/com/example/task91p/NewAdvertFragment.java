package com.example.task91p;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task91p.data.DatabaseHelper;
import com.example.task91p.model.Advert;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class NewAdvertFragment extends Fragment {
    DatabaseHelper db;
    double latitude;
    double longitude;

    LocationManager locationManager;
    LocationListener locationListener;

    public NewAdvertFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Places.initialize(getContext(), getString(R.string.API_KEY));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_advert, container, false);
    }

    //actions to take after the view has been inflated
    @Override public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        db = new DatabaseHelper(getContext());

        EditText nameEditText = view.findViewById(R.id.nameEditText);
        EditText phoneEditText = view.findViewById(R.id.phoneEditText);
        EditText descEditText = view.findViewById(R.id.descEditText);
        EditText dateEditText = view.findViewById(R.id.editTextDate);
        TextView locationTextView = view.findViewById(R.id.locationTextView);

        Button saveButton = view.findViewById(R.id.saveButton);
        Button getLocateButton = view.findViewById(R.id.getLocationButton);

        RadioButton lostButton = view.findViewById(R.id.lostRadioButton);
        RadioButton foundButton = view.findViewById(R.id.foundRadioButton);

        // Initialize the AutocompleteSupportFragment
        FragmentManager fragmentManager = getChildFragmentManager();
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)fragmentManager.findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)); //make sure to add place.field.lat_lng for coordinates

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                LatLng locationLatLng = place.getLatLng();
                latitude = locationLatLng.latitude;
                longitude = locationLatLng.longitude;
                locationTextView.setText(place.getName());
            }

            @Override
            public void onError(@NonNull Status status) {
                String message = "An error occurred: " + status;
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        //actions when using get location button
        getLocateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //cast system service as locationManager
                locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        locationTextView.setText("Current Location");
                    }
                };

                //make sure we have valid permissions before attempting to get current location
                if (ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }

                //get the location from user just once
                else { locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null); }
            }
        });

        //save button returns to previous fragment
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameString = nameEditText.getText().toString();
                String phoneString = phoneEditText.getText().toString();
                String descString = descEditText.getText().toString();
                String dateString = dateEditText.getText().toString();
                String locateString = locationTextView.getText().toString();

                //check which category the advert is in, but return if there's no category
                String type;
                if (lostButton.isChecked()) { type = getString(R.string.AdvertTypeLost); }
                else if (foundButton.isChecked()) { type = getString(R.string.AdvertTypeFound); }
                else {
                    Toast.makeText(getContext(), "Please select advert category.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //check for empty entries
                if (nameString.trim().equals("")) {
                    Toast.makeText(getContext(), "Please enter a name.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (descString.trim().equals("")) {
                    Toast.makeText(getContext(), "Please enter a description.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (dateString.trim().equals("")) {
                    Toast.makeText(getContext(), "Please enter a date.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (locateString.trim().equals("")) {
                    Toast.makeText(getContext(), "Please enter a location.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //all data valid, so create the advert
                long result = db.insertAdvert(new Advert(type, nameString, phoneString, descString, dateString, locateString, latitude, longitude));
                if (result > 0) {
                    Toast.makeText(getContext(), "Registered advert successfully!", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                else {
                    Toast.makeText(getContext(), "Register error!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }
}