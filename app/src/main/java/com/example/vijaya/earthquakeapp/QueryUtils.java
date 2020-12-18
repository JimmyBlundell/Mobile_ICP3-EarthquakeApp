package com.example.vijaya.earthquakeapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {
    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the USGS dataset and return a list of {@link Earthquake} objects.
     */
    public static List<Earthquake> fetchEarthquakeData2(String requestUrl) {
        // An empty ArrayList that we can start adding earthquakes to
        List<Earthquake> earthquakes = new ArrayList<>();
        //  URL object to store the url for a given string
        URL url = null;
        // A string to store the response obtained from rest call in the form of string
        String jason = "";
        StringBuilder result = new StringBuilder();

        // two try blocks: one for json parsing errors, and the other that catches exceptions when adding the data to the earthquakes list
        try {
            url = new URL(requestUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String str;
            while ((str = reader.readLine()) != null) {
                result.append(str);
            }

            jason = result.toString();

            try {
                JSONObject json1 = new JSONObject(jason);
                JSONArray data = json1.getJSONArray("features");
                JSONObject json2 = new JSONObject(data.getString(0));
                JSONObject json3;


                for (int i = 0; i < data.length(); i++) {
                    json2 = new JSONObject(data.getString(i));
                    json3 = new JSONObject(json2.getString("properties"));
                    Earthquake earth = new Earthquake(Double.parseDouble(json3.getString("mag")), json3.getString("place"), Long.parseLong(json3.getString("time")), json3.getString("url"));
                    earthquakes.add(earth);
                    Log.d("Earthquakes!", json3.getString("mag"));
                }
            } catch (Throwable t) {
                Log.e("Earthquakes!", "Uh oh! Could not parse JSON");
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Exception:  ", e);
        }
        // Return the list of earthquakes
        return earthquakes;
    }
}