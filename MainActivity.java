package com.nocomp.mausam;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView2;

    public void onClick(View view) throws ExecutionException, InterruptedException {
        String result = "";

        DownloadTask task = new DownloadTask();

        String city = editText.getText().toString();

        String target = "http://api.openweathermap.org/data/2.5/weather?q=(cityname)&APPID=b768cb35af976188cf2cda7d457e1d3d";

        String nateeja = target.replace("(cityname)",city);

        try {

            result = task.execute(nateeja).get();


        } catch (Exception e) {
            Toast.makeText(this, "Enter a correct name!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        } /*catch (ExecutionException e) {
            e.printStackTrace();
            Toast.makeText(this, "Enter a correct name!", Toast.LENGTH_SHORT).show();
        }*/
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);

        textView2 = (TextView) findViewById(R.id.textView2);

    }



    public class DownloadTask extends AsyncTask<String, Void, String>
    {


        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while(data!=-1)
                {
                    char current = (char) data;
                    result+=current;
                    data = reader.read();
                }

                return result;

            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "URL Malfunctioned! Try after some time.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } /*catch (IOException e) {
                Toast.makeText(MainActivity.this, "Enter a correct name!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();


            }*/

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            String main = "";
            String desc = "";

            try {
                JSONObject jsonObject = new JSONObject(result);

                String weatherInfo = jsonObject.getString("weather");

                Log.i("Weather content :", weatherInfo);

                JSONArray arr = new JSONArray(weatherInfo);

                for(int i = 0;i<arr.length();i++)
                {
                    JSONObject jsonPart = arr.getJSONObject(i);
                    main = jsonPart.getString("main");
                    desc = jsonPart.getString("description");
                    Log.i("Main ", main);
                    Log.i("Description ", desc);

                    if(main!="" && desc!="")
                    {
                        textView2.setText("Main : "+main+"\nDescription : "+desc);
                        //textView2.setText("Description : "+desc);
                    }
                }

            } catch (JSONException e) {
                Toast.makeText(MainActivity.this, "Enter a city name!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }
    }
}
