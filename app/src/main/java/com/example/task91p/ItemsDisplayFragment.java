package com.example.task91p;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.task91p.R;
import com.example.task91p.data.DatabaseHelper;
import com.example.task91p.model.Advert;

import java.util.ArrayList;
import java.util.List;

public class ItemsDisplayFragment extends Fragment {
    private ListView advertListView;
    private List<Advert> advertList;
    private ArrayList<Advert> advertArrayList;
    private ArrayAdapter<Advert> advertArrayAdapter;
    private DatabaseHelper db;

    public ItemsDisplayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_items_display, container, false);
    }

    //actions to take after the view has been inflated
    @Override public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        advertListView = view.findViewById(R.id.advertListView);
        advertArrayList = new ArrayList<>();
        db = new DatabaseHelper(getContext());

        //fill advert array list with all adverts pulled from database
        advertList = db.fetchAllAdverts();
        advertArrayList.addAll(advertList);

        advertArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, advertArrayList);
        advertListView.setAdapter(advertArrayAdapter);
        advertListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                selectFragment(position);
            }
        });
    }

    //jumps to the full story fragment, with the selected story
    protected void selectFragment(int advertIndex) {
        Fragment fragment;
        fragment = new FullAdvertFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("AdvertIndex", advertIndex);
        bundle.putParcelableArrayList("AdvertList", (ArrayList<? extends Parcelable>) advertList);
        fragment.setArguments(bundle);

        //we want to use the fragment from the home page, so get the activity and its support fragment manager
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainActivityFragment, fragment).addToBackStack(null).commit();
    }
}