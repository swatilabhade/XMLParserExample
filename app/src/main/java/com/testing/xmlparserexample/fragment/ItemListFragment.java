package com.testing.xmlparserexample.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.testing.xmlparserexample.R;
import com.testing.xmlparserexample.adapter.ItemListAdapterClass;
import com.testing.xmlparserexample.model.ItemDetails;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ItemListFragment extends Fragment {

    View view;
    private ListView itemList;
    private ItemListAdapterClass mAdapter;
    private ProgressBar bar;
    private TextView empty_response_tv, progressbar_tv;
    List<ItemDetails> itemDetailsList = new ArrayList<>();
    ItemDetails currentDetails = null;
    private String url;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_itemlist, container, false);
        initialization();
        return view;
    }

    private void initialization() {

        if (getArguments() != null)
            url = getArguments().getString("url");

        itemList = (ListView) view.findViewById(R.id.itemList);
        bar = (ProgressBar) view.findViewById(R.id.progressBar);
        empty_response_tv = view.findViewById(R.id.empty_response_tv);
        progressbar_tv = view.findViewById(R.id.progressbar_tv);
        if (isStoragePermissionGranted())
            new getXMLResponseAsyncTask().execute(url);
        else
            requestPermission();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (((AppCompatActivity) getActivity()) != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
            setHasOptionsMenu(true);
        }

        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("BACK");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("url", url);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        String savedUrl;
        if (savedInstanceState != null) {
            savedUrl = savedInstanceState.getString("url");
            new getXMLResponseAsyncTask().execute(savedUrl);
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private class getXMLResponseAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            bar.setVisibility(View.VISIBLE);
            progressbar_tv.setVisibility(View.VISIBLE);
            empty_response_tv.setVisibility(View.INVISIBLE);
        }


        @Override
        public Boolean doInBackground(String... urls) {
            URL url;
            boolean flag = false;
            try {
                url = new URL(urls[0]);
                URLConnection connection = url.openConnection();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(connection.getInputStream());
                NodeList nodes = doc.getElementsByTagName("Item");
                for (int i = 0; i < nodes.getLength(); i++) {
                    Element element = (Element) nodes.item(i);
                    NodeList ItemType = element.getElementsByTagName("ItemType");
                    if (ItemType.item(0).getTextContent().equals("Station")) {
                        currentDetails = new ItemDetails();
                        NodeList StationId = element.getElementsByTagName("StationId");
                        NodeList StationName = element.getElementsByTagName("StationName");
                        NodeList Logo = element.getElementsByTagName("Logo");
                        currentDetails.setStationId(StationId.item(0).getTextContent());
                        currentDetails.setStationName(StationName.item(0).getTextContent());
                        currentDetails.setLogo(Logo.item(0).getTextContent());
                        itemDetailsList.add(currentDetails);
                    }
                }
                flag = true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
                flag = false;
            }
            return flag;
        }

        public void onPostExecute(Boolean result) {
            bar.setVisibility(View.INVISIBLE);
            progressbar_tv.setVisibility(View.INVISIBLE);
            if (result) {
                empty_response_tv.setVisibility(View.INVISIBLE);
                itemList.setVisibility(View.VISIBLE);
            } else {
                empty_response_tv.setVisibility(View.VISIBLE);
                itemList.setVisibility(View.INVISIBLE);
            }
            mAdapter = new ItemListAdapterClass(getActivity(), R.layout.list_item, itemDetailsList);
            itemList.setAdapter(mAdapter);

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

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getActivity(), "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }
}




