package com.example.testurl;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetServer extends AsyncTask<String, Void, String> {
    private final static String TAG = "HTTPURLCONNECTION test";

    @Override
    protected String doInBackground(String... urls) {
        return GET(urls[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d(TAG,"onPostExecute");
    }

    private String GET(String APIUrl) {
        String result = "";
        HttpURLConnection connection;
        try {
            URL url = new URL(APIUrl);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("authentication", MainActivity.Authentication);
            connection.setDoInput(true);

            InputStream inputStream = connection.getInputStream();
            int status = connection.getResponseCode();
            Log.d(TAG, String.valueOf(status));
            if(inputStream != null){
                InputStreamReader reader = new InputStreamReader(inputStream,"UTF-8");
                BufferedReader in = new BufferedReader(reader);

                String line="";
                while ((line = in.readLine()) != null) {
                    result += (line+"\n");
                }
            } else{
                result = "Did not work!";
            }
            return  result;
        } catch (Exception e) {
            Log.d("ATask InputStream", e.getLocalizedMessage());
            e.printStackTrace();
            return result;
        }
    }
}