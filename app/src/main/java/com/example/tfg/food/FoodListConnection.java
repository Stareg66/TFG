package com.example.tfg.food;

import android.os.AsyncTask;
import android.widget.ListView;

import com.example.tfg.MainActivity;
import com.example.tfg.R;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class FoodListConnection extends AsyncTask<String, Void, Food[]>{

        private final WeakReference<MainActivity> mainActivityReference;

        public FoodListConnection(MainActivity activity) {
            mainActivityReference = new WeakReference<>(activity);
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

        /*protected void onPostExecute(Food[] result){
            super.onPostExecute(result);

            if (result != null) {
                MainActivity mainActivity = mainActivityReference.get();
                ListView listView = mainActivity.findViewById(R.id.list);

                MainActivity.articulos=new ArrayList<>();
                MainActivity.categoriaArticulos=new ArrayList<>();
                for (Article article : result) {
                    MainActivity.articulos.add(article);
                    MainActivity.categoriaArticulos.add(article);
                }

                ListaArticulosAdapter lAdapter = new ListaArticulosAdapter(mainActivity,MainActivity.categoriaArticulos);
                listView.setAdapter(lAdapter);
                mainActivity.setOnClickListener();

            }
        }*/
}
