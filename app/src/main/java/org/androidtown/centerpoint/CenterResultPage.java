package org.androidtown.centerpoint;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class CenterResultPage extends AppCompatActivity {
    public static final int LOAD_SUCCESS = 101;
    private String REQUEST_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=37.550102, 127.074430&language=ko&radius=500&type=subway_station&key=AIzaSyBBsRw3z-ayQvdL2h3vtVloNZqOWyzwnZA";
    private TextView textviewJSONText;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_result_page);
        Button buttonRequestJSON = (Button)findViewById(R.id.button_main_requestjson);
        textviewJSONText = (TextView)findViewById(R.id.textview_main_jsontext);
        textviewJSONText.setMovementMethod(new ScrollingMovementMethod());

        buttonRequestJSON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog( CenterResultPage.this );
                progressDialog.setMessage("Please wait.....");
                progressDialog.show();

                getJSON();
            }
        });
    }
    private final MyHandler mHandler = new MyHandler(this);


    private static class MyHandler extends Handler {
        private final WeakReference<CenterResultPage> weakReference;

        public MyHandler(CenterResultPage centerResultPage) {
            weakReference = new WeakReference<CenterResultPage>(centerResultPage);
        }

        @Override
        public void handleMessage(Message msg) {

            CenterResultPage centerResultPage = weakReference.get();

            if (centerResultPage != null) {
                switch (msg.what) {

                    case LOAD_SUCCESS:
                        centerResultPage.progressDialog.dismiss();

                        String jsonString = (String)msg.obj;
                        Log.i("receive_message",jsonString);
                        centerResultPage.textviewJSONText.setText(jsonString);
                        break;
                }
            }
        }
    }




    public void  getJSON() {

        Thread thread = new Thread(new Runnable() {

            public void run() {

                String result;

                try {

                    Log.d(" ", REQUEST_URL);
                    URL url = new URL(REQUEST_URL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                    httpURLConnection.setReadTimeout(3000);
                    httpURLConnection.setConnectTimeout(3000);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setUseCaches(false);
                    httpURLConnection.connect();


                    int responseStatusCode = httpURLConnection.getResponseCode();

                    InputStream inputStream;
                    if (responseStatusCode == HttpURLConnection.HTTP_OK) {

                        inputStream = httpURLConnection.getInputStream();
                    } else {
                        inputStream = httpURLConnection.getErrorStream();

                    }


                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    StringBuilder sb = new StringBuilder();
                    String line;


                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }

                    bufferedReader.close();
                    httpURLConnection.disconnect();

                    result = sb.toString().trim();


                } catch (Exception e) {
                    result = e.toString();
                }


                Message message = mHandler.obtainMessage(LOAD_SUCCESS, result);
                mHandler.sendMessage(message);
            }

        });
        thread.start();
    }
}
