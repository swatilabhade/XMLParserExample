package com.testing.xmlparserexample.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.testing.xmlparserexample.R;

public class UrlInputFragment extends Fragment implements View.OnClickListener {
    View view;
    EditText url_et;
    TextView submit_tv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_urlinput, container, false);
        initialization();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (((AppCompatActivity) getActivity()) != null)
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("HOME");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initialization() {

        url_et = view.findViewById(R.id.url_et);
        submit_tv = view.findViewById(R.id.submit_tv);
        submit_tv.setOnClickListener(this);
        url_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() > 3) {
                    if (s.toString().startsWith("http") || s.toString().startsWith("https")) {
                        url_et.setError(null);
                    } else {
                        url_et.setError("Enter correct URL");

                    }
                }
            }
        });
        isStoragePermissionGranted();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit_tv) {
            checkValidations();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("input", url_et.getText().toString());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        String savedInput;
        if (savedInstanceState != null) {
            savedInput = savedInstanceState.getString("input");
            url_et.setText(savedInput);
        }

    }

    private void checkValidations() {
        String url = url_et.getText().toString();
        if (url.isEmpty())
            Toast.makeText(getActivity(), "Please enter URL ", Toast.LENGTH_SHORT).show();
        else if (!URLUtil.isValidUrl(url) || !url.contains(".com")) {
            Toast.makeText(getActivity(), "Please enter valid URL ", Toast.LENGTH_SHORT).show();
        } else {
            if (isStoragePermissionGranted()) {
                Bundle bundle = new Bundle();
                Fragment fragment = new ItemListFragment();
                bundle.putString("url", url);
                fragment.setArguments(bundle);
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
            }
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            return true;
        }
    }
}
