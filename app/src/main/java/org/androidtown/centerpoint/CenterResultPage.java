package org.androidtown.centerpoint;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import static org.androidtown.centerpoint.R.id.line4;
import static org.androidtown.centerpoint.R.id.line5;
import static org.androidtown.centerpoint.R.id.line6;
import static org.androidtown.centerpoint.R.id.line7;
import static org.androidtown.centerpoint.R.id.textView2;
import static org.androidtown.centerpoint.R.id.textView3;
import static org.androidtown.centerpoint.R.id.textView4;
import static org.androidtown.centerpoint.R.id.textView5;
import static org.androidtown.centerpoint.R.id.textView6;
import static org.androidtown.centerpoint.R.id.textView7;

public class CenterResultPage extends FragmentActivity implements OnMapReadyCallback{
    private GoogleMap googleMap = null;
    public static final int LOAD_SUCCESS = 101;
    private String GOOGLE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
    private String send_lat = "";
    private String send_lon = "";
    private String ext = "&language=ko&radius=";
    private String radius = "10000";
    private String ext1 = "&type=subway_station&key=";
    private String API_KEY  = "AIzaSyBBsRw3z-ayQvdL2h3vtVloNZqOWyzwnZA";
    private String REQUEST_URL = GOOGLE_URL+send_lat+send_lon+ext+radius+ext1+API_KEY;
    private Double mid_lat;
    private Double mid_lng;
    private ProgressDialog progressDialog;

    private String Naver_URL = "http://m.map.naver.com/route.nhn?menu=route";
    private String Naver_sname="&sname=";
    private String Naver_sx="&sx=";
    private String Naver_sy="&sy=";
    private String Naver_ename="&ename=";
    private String Naver_ex="&ex=";
    private String Naver_ey="&ey=";
    private String Naver_ext="&pathType=1&showMap=true";

    String[] jsonReceive = new String[3];
    String receiveJSON;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_result_page);

        C app = (C)getApplicationContext();
        setVisibility(app.getNum_people());//리스트뷰 출력
        setTextView();

        //중간값 계산해서 JSON과 통신
        double latsum=0.0;
        double lngsum=0.0;
        Location loc_tmp;
        for(int i=0;i<app.getNum_people();i++){
            loc_tmp = app.getLoc(i);
            latsum += loc_tmp.getLatitude();
            lngsum += loc_tmp.getLongitude();
        }
        latsum = latsum/app.getNum_people();
        lngsum = lngsum/app.getNum_people();

        mid_lat = latsum;
        mid_lng = lngsum;
        //중간값 계산
        String pattern = ".######";
        DecimalFormat def=new DecimalFormat(pattern);
        send_lat = def.format(latsum) + ",";
        send_lon = def.format(lngsum);


        REQUEST_URL = GOOGLE_URL+send_lat+send_lon+ext+radius+ext1+API_KEY;
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

    public void setVisibility(int pos){
        pos-=2;
        LinearLayout linear1 = (LinearLayout)findViewById(line4);
        LinearLayout linear2 = (LinearLayout)findViewById(line5);
        LinearLayout linear3 = (LinearLayout)findViewById(line6);
        LinearLayout linear4 = (LinearLayout)findViewById(line7);

        switch(pos) {
            case 0:
                linear1.setVisibility(View.GONE);
                linear2.setVisibility(View.GONE);
                linear3.setVisibility(View.GONE);
                linear4.setVisibility(View.GONE);
                break;
            case 1:
                linear1.setVisibility(View.VISIBLE);
                linear2.setVisibility(View.GONE);
                linear3.setVisibility(View.GONE);
                linear4.setVisibility(View.GONE);
                break;
            case 2:
                linear1.setVisibility(View.VISIBLE);
                linear2.setVisibility(View.VISIBLE);
                linear3.setVisibility(View.GONE);
                linear4.setVisibility(View.GONE);
                break;
            case 3:
                linear1.setVisibility(View.VISIBLE);
                linear2.setVisibility(View.VISIBLE);
                linear3.setVisibility(View.VISIBLE);
                linear4.setVisibility(View.GONE);
                break;
            case 4:
                linear1.setVisibility(View.VISIBLE);
                linear2.setVisibility(View.VISIBLE);
                linear3.setVisibility(View.VISIBLE);
                linear4.setVisibility(View.VISIBLE);
                break;
        }
    }
    public void setTextView(){

        TextView textViewfir = (TextView) findViewById(textView2);
        TextView textViewsec = (TextView) findViewById(textView3);
        TextView textView = (TextView) findViewById(textView4);
        TextView textView2 = (TextView) findViewById(textView5);
        TextView textView3 = (TextView) findViewById(textView6);
        TextView textView4 = (TextView) findViewById(textView7);
        C app = (C)getApplicationContext();

        for(int i=0; i<app.getNum_people();i++){
            switch (i){
                case 0:textViewfir.setText(app.getPlc(0).getName());
                    break;
                case 1:textViewsec.setText(app.getPlc(1).getName());
                    break;
                case 2:textView.setText(app.getPlc(2).getName());
                    break;
                case 3:textView2.setText(app.getPlc(3).getName());
                    break;
                case 4:textView3.setText(app.getPlc(4).getName());
                    break;
                case 5:textView4.setText(app.getPlc(5).getName());
                    break;
            }
        }
    }

    public void onButtonClicked1(View v){
        C app = (C)getApplicationContext();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        //set text message
        String text="[너와 나의 연결 거리]\n" +app.getPlc(0).getName()+" -> "+jsonReceive[0] + "의 길찾기 결과입니다.\n";
        Naver_sname += app.getPlc(0).getName();
        Naver_sy+=app.getLoc(0).getLatitude();
        Naver_sx+=app.getLoc(0).getLongitude();
        text+=Naver_URL+Naver_sname+Naver_sx+Naver_sy+Naver_ename+Naver_ex+Naver_ey+Naver_ext;
        intent.putExtra(Intent.EXTRA_TEXT,text);
        Intent chooser=Intent.createChooser(intent,"친구에게 공유하기");
        startActivity(chooser);

    }
    public void onButtonClicked2(View v){
        C app = (C)getApplicationContext();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        //set text message
        String text="[너와 나의 연결 거리]\n" +app.getPlc(1).getName()+" -> "+jsonReceive[0] + "의 길찾기 결과입니다.\n";
        Naver_sname += app.getPlc(1).getName();
        Naver_sy+=app.getLoc(1).getLatitude();
        Naver_sx+=app.getLoc(1).getLongitude();
        text+=Naver_URL+Naver_sname+Naver_sx+Naver_sy+Naver_ename+Naver_ex+Naver_ey+Naver_ext;
        intent.putExtra(Intent.EXTRA_TEXT,text);
        Intent chooser=Intent.createChooser(intent,"친구에게 공유하기");
        startActivity(chooser);

    }
    public void onButtonClicked3(View v){
        C app = (C)getApplicationContext();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        //set text message
        String text="[너와 나의 연결 거리]\n" +app.getPlc(2).getName()+" -> "+jsonReceive[0] + "의 길찾기 결과입니다.\n";
        Naver_sname += app.getPlc(2).getName();
        Naver_sy+=app.getLoc(2).getLatitude();
        Naver_sx+=app.getLoc(2).getLongitude();
        text+=Naver_URL+Naver_sname+Naver_sx+Naver_sy+Naver_ename+Naver_ex+Naver_ey+Naver_ext;
        intent.putExtra(Intent.EXTRA_TEXT,text);
        Intent chooser=Intent.createChooser(intent,"친구에게 공유하기");
        startActivity(chooser);
    }
    public void onButtonClicked4(View v){
        C app = (C)getApplicationContext();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        //set text message
        String text="[너와 나의 연결 거리]\n" +app.getPlc(3).getName()+" -> "+jsonReceive[0] + "의 길찾기 결과입니다.\n";
        Naver_sname += app.getPlc(3).getName();
        Naver_sy+=app.getLoc(3).getLatitude();
        Naver_sx+=app.getLoc(3).getLongitude();
        text+=Naver_URL+Naver_sname+Naver_sx+Naver_sy+Naver_ename+Naver_ex+Naver_ey+Naver_ext;
        intent.putExtra(Intent.EXTRA_TEXT,text);
        Intent chooser=Intent.createChooser(intent,"친구에게 공유하기");
        startActivity(chooser);
    }
    public void onButtonClicked5(View v){
        C app = (C)getApplicationContext();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        //set text message
        String text="[너와 나의 연결 거리]\n" +app.getPlc(4).getName()+" -> "+jsonReceive[0] + "의 길찾기 결과입니다.\n";
        Naver_sname += app.getPlc(4).getName();
        Naver_sy+=app.getLoc(4).getLatitude();
        Naver_sx+=app.getLoc(4).getLongitude();
        text+=Naver_URL+Naver_sname+Naver_sx+Naver_sy+Naver_ename+Naver_ex+Naver_ey+Naver_ext;
        intent.putExtra(Intent.EXTRA_TEXT,text);
        Intent chooser=Intent.createChooser(intent,"친구에게 공유하기");
        startActivity(chooser);
    }
    public void onButtonClicked6(View v){
        C app = (C)getApplicationContext();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        //set text message
        String text="[너와 나의 연결 거리]\n" +app.getPlc(5).getName()+" -> "+jsonReceive[0] + "의 길찾기 결과입니다.\n";
        Naver_sname += app.getPlc(5).getName();
        Naver_sy+=app.getLoc(5).getLatitude();
        Naver_sx+=app.getLoc(5).getLongitude();
        text+=Naver_URL+Naver_sname+Naver_sx+Naver_sy+Naver_ename+Naver_ex+Naver_ey+Naver_ext;
        intent.putExtra(Intent.EXTRA_TEXT,text);
        Intent chooser=Intent.createChooser(intent,"친구에게 공유하기");
        startActivity(chooser);
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

            //여러개 값이 올경우 최단 거리의 지하철 선정
            JSONArray jsonArray = jsonData.getJSONArray("results");
            int list_cnt=jsonArray.length();

            String[] getname=new String[list_cnt];
            Double[] getlat=new Double[list_cnt];
            Double[] getlng=new Double[list_cnt];
            Double[] distance=new Double[list_cnt];
            double min=0;
            int cnt=0;
            for(int i=0;i<list_cnt;i++){//여러개의 값이 오는것을 배열로 저장
                JSONObject jsonSave = jsonArray.getJSONObject(i);
                getname[i]=jsonSave.getString("name");
                getlat[i]=jsonSave.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                getlng[i]=jsonSave.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                Log.i("JSON Save : ",jsonSave+"");
                Log.i("JSON DATA : ",getname[i]+","+getlat[i]);
            }
            Log.i("Mid LATLNG : ",mid_lat+""+mid_lng);
            for (int i=0;i<list_cnt;i++){//각 거리 계산
                distance[i] = Math.sqrt((mid_lat-getlat[i])*(mid_lat-getlat[i])
                        + (mid_lng-getlng[i])*(mid_lng-getlng[i]));

                Log.i("Distance : ",getname[i]+" : "+distance[i]);
            }
            min = distance[0];//가장 최단거리에 위치한 지하철역을 찾는다
            for(int i=0;i<list_cnt;i++){
                if(distance[i]<min)
                {
                    min = distance[i];
                    cnt = i;
                }
            }

            arraysum[0]=getname[cnt];
            arraysum[1]=String.valueOf(getlat[cnt]);
            arraysum[2]=String.valueOf(getlng[cnt]);

        }catch (JSONException e){
            e.printStackTrace();
        }
        jsonReceive[0]=arraysum[0];
        jsonReceive[1]=arraysum[1];
        jsonReceive[2]=arraysum[2];

        Naver_ename += jsonReceive[0];
        Naver_ey += jsonReceive[1];
        Naver_ex += jsonReceive[2];
    }


}
