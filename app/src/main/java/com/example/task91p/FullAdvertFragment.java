package com.example.task91p;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task91p.R;
import com.example.task91p.data.DatabaseHelper;
import com.example.task91p.model.Advert;

import java.util.ArrayList;
import java.util.List;

public class FullAdvertFragment extends Fragment {

    private List<Advert> _advertList = new ArrayList<>();
    private int _advertIndex;
    private Advert _currentAd;
    private DatabaseHelper db;

    public FullAdvertFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _advertList = getArguments().getParcelableArrayList("AdvertList");
        _advertIndex = getArguments().getInt("AdvertIndex");
        _currentAd = _advertList.get(_advertIndex);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_full_advert, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = new DatabaseHelper(getContext());

        TextView descTextView = view.findViewById(R.id.fullAdTypeDescTextView);
        TextView dateTextView = view.findViewById(R.id.fullAdDateTextView);
        TextView locTextView = view.findViewById(R.id.fullAdLocTextView);

        String descString = _currentAd.toString();
        String dateString = _currentAd.get_date();
        String locString = "At " + _currentAd.get_location();

        descTextView.setText(descString);
        dateTextView.setText(dateString);
        locTextView.setText(locString);


        //save button returns to previous fragment
        Button removeButton = view.findViewById(R.id.fullAdRemoveButton);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long result = db.deleteAdvert(_currentAd.get_advertID());
                if (result > 0) {
                    Toast.makeText(getContext(), "Deleted advert successfully!", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                else {
                    Toast.makeText(getContext(), "Deletion error!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}