package com.example.tfg.food;

import android.os.AsyncTask;

import com.example.tfg.fragments.ListFragment;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class FoodListConnection extends AsyncTask<String, Void, Food[]>{

        private final WeakReference<ListFragment> listFragmentWeakReference;
        private final WeakReference<FoodListAdapter> adapterWeakReference;

        public FoodListConnection(ListFragment activity, FoodListAdapter adapter) {
            listFragmentWeakReference = new WeakReference<>(activity);
            adapterWeakReference = new WeakReference<>(adapter);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Food[] doInBackground(String... urls) {
            URL url;

            try {
                url = new URL(urls[0]);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(false);
                int code = conn.getResponseCode();
                if(code == HttpURLConnection.HTTP_OK){
                    InputStream is = conn.getInputStream();
                    StringBuilder result = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    reader.close();
                    Gson gson = new Gson();
                    return gson.fromJson(result.toString(), Food[].class);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Food[] result){
            super.onPostExecute(result);

            FoodListAdapter adapter = adapterWeakReference.get();
            if(result != null){
                adapter.updateData(Arrays.asList(result));
            }
            adapter.notifyDataSetChanged();
        }
}
