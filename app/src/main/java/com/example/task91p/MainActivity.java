package com.example.task91p;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Window;

import com.example.task91p.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //will hide the title
        getSupportActionBar().hide();                   // hide the title bar
        setContentView(R.layout.activity_main);

        //select our fragment
        Fragment fragment;
        fragment = new HomePageFragment();

        //set up and head to the next fragment
        FragmentManager fragmentManager = getSupportFragmentManager();

        //only initialize home page fragment the first time we come here
        if (savedInstanceState == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.mainActivityFragment, fragment).commit();
        }
    }
}