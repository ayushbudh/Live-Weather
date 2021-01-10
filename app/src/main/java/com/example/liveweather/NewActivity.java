package com.example.liveweather;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class NewActivity extends AppCompatActivity {
    TextView showMsg;
    ImageView icon;

    public void getClicked(View view){
        TextView query = (TextView) findViewById(R.id.textField);
        DownloadTask task = new DownloadTask();

        if(query.getText().toString().equals("")){
            showMsg.setText("Input Field is Empty!");
        }
        else {
            task.execute("https://api.openweathermap.org/data/2.5/weather?q=" + query.getText().toString() + "&appid=33165d4519e4c5acfe68f873b162004f");
        }
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(query.getWindowToken(),0);

    }

    public void setText(String text){
        showMsg.setText(text);
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result ="";
            URL url;
            URL imageUrl;
            HttpURLConnection urlConnection = null;
            HttpURLConnection imageUrlConnection =null;

            try{
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while(data!=-1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;

            }catch (Exception e){
                e.printStackTrace();
                showMsg.setText("Please enter valid city name!");
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);

                String weatherInfo = jsonObject.getString("weather");

                JSONArray arr = new JSONArray(weatherInfo);

                String message ="";
                for(int i=0; i<arr.length(); i++){
                    JSONObject jsonPart = arr.getJSONObject(i);

                    String main = jsonPart.getString("main");
                    String description = jsonPart.getString("description");
                    message+=main + " : " + description + "\r\n";

                }
                setText(message);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        showMsg = (TextView) findViewById(R.id.wmsg);
        icon = (ImageView) findViewById(R.id.applogo);
        icon.setImageResource(R.drawable.applogoimg);

    }
}