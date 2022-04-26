package com.testing.xmlparserexample.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.testing.xmlparserexample.R;
import com.testing.xmlparserexample.fragment.UrlInputFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UrlInputFragment()).commit();
    }

}