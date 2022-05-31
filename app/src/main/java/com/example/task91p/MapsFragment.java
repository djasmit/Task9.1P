package com.example.task91p;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.task91p.data.DatabaseHelper;
import com.example.task91p.model.Advert;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment {

    private List<Advert> advertList;
    private ArrayList<Advert> advertArrayList;
    private DatabaseHelper db;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            //add marker for every advert
            for (Advert advert : advertList) {

                //get marker name and location from advert
                MarkerOptions markerOptions = new MarkerOptions();
                LatLng position = new LatLng(advert.get_latitude(), advert.get_longitude());
                markerOptions.position(position);
                markerOptions.title(advert.toString());

                //get marker color based on type of advert
                float color;
                Log.i("Maps", advert.get_type());
                if (advert.get_type().equals(getString(R.string.AdvertTypeLost))) { color = BitmapDescriptorFactory.HUE_RED; }
                else { color = BitmapDescriptorFactory.HUE_BLUE; }
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(color));

                //add marker to map
                googleMap.addMarker(markerOptions);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        //get item data from database and fill array
        advertArrayList = new ArrayList<>();
        db = new DatabaseHelper(getContext());

        //fill advert array list with all adverts pulled from database
        advertList = db.fetchAllAdverts();
        advertArrayList.addAll(advertList);
    }
}