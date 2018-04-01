package org.androidtown.centerpoint;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class CenterResultPage extends FragmentActivity implements OnMapReadyCallback{
    private GoogleMap googleMap = null;
    public static final int LOAD_SUCCESS = 101;
    private String GOOGLE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
    private String send_lat = "37.550102,";
    private String send_lon = "127.074430";
    private String ext = "&language=ko&radius=";
    private String radius = "500";
    private String ext1 = "&type=subway_station&key=";
    private String API_KEY  = "AIzaSyBBsRw3z-ayQvdL2h3vtVloNZqOWyzwnZA";
    private String REQUEST_URL = GOOGLE_URL+send_lat+send_lon+ext+radius+ext1+API_KEY;

    private ProgressDialog progressDialog;
    String[] jsonReceive = new String[3];
    String receiveJSON;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_result_page);
        getJSONThread getJSON = new getJSONThread();
        getJSON.start();
        try{
            getJSON.join();
        }catch (InterruptedException e){

        }
        jsonParser(receiveJSON);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.resultmap);
        mapFragment.getMapAsync(this);

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
                       // centerResultPage.progressDialog.dismiss();

                        String jsonString = (String)msg.obj;
                        Log.i("receive_message",jsonString);
                        break;
                }
            }
        }
    }

    public void onMapReady(GoogleMap googleMap) {
        // OnMapReadyCallback implements 해야 mapView.getMapAsync(this); 사용가능. this 가 OnMapReadyCallback
        this.googleMap = googleMap;
        // 매끄럽게 이동함
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        Double lat = Double.parseDouble(jsonReceive[1]);
        Double lng = Double.parseDouble(jsonReceive[2]);
        LatLng loc = new LatLng(lat,lng);
        this.googleMap.addMarker(new MarkerOptions()
                .position(loc)
                .title(jsonReceive[0]));
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,15));
    }

    public class getJSONThread extends Thread{
        public void run(){
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
            receiveJSON = result;
        }
    }


    public void jsonParser(String jsonString){
        String name=null;
        Double lat=0.0;
        Double lng=0.0;
        String [] arraysum = new String[3];
        try{
            JSONObject jsonData = new JSONObject(jsonString);
            JSONObject jsonObject = jsonData.getJSONArray("results").getJSONObject(0);
            name = jsonObject.getString("name");
            lat = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
            lng = jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng");

            arraysum[0]=name;
            arraysum[1]=String.valueOf(lat);
            arraysum[2]=String.valueOf(lng);

        }catch (JSONException e){
            e.printStackTrace();
        }
        jsonReceive[0]=arraysum[0];
        jsonReceive[1]=arraysum[1];
        jsonReceive[2]=arraysum[2];

    }


}
