package org.androidtown.centerpoint;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapViewPage extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "MapViewPage";
    private static final LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2002;
    private GoogleMap googleMap = null;
    private MapView mapView = null;
    private GoogleApiClient googleApiClient = null;
    private Marker currentMarker = null;

    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {
        //장소를 검색했을때, 해당 장소에 마커를 찍음
        if (currentMarker != null) currentMarker.remove();
        Log.i(TAG, "Location :" + location);
        if (location != null) {
            Log.d(TAG, "setCurrentLocation: ");
            //현재위치의 위도 경도 가져옴
            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(currentLocation);
            markerOptions.title(markerTitle);
            markerOptions.snippet(markerSnippet);
            markerOptions.draggable(false);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            currentMarker = this.googleMap.addMarker(markerOptions);
            currentMarker.showInfoWindow();//누르지 않아도 정보창 띄움
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
            return;
        }
        //registerLocationUpdates();
        /*
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker = this.googleMap.addMarker(markerOptions);
        */
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(DEFAULT_LOCATION));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //맵뷰
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view_page);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        //장소검색을 위한 구문
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());
                Location location = new Location("");
                location.setLatitude(place.getLatLng().latitude);
                location.setLongitude(place.getLatLng().longitude);

                //test---------------------------------------------------

                C app = (C)getApplicationContext();
                app.setLoc(location,app.getNum_buttonclicked());
                app.setPlc(place,app.getNum_buttonclicked());


                //end---------------------------------------------------

                setCurrentLocation(location, place.getName().toString(), place.getAddress().toString());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
        //지역 범주 제한 설정, 일단은 서울권으로 설정
        autocompleteFragment.setBoundsBias(new LatLngBounds(
                new LatLng(37.432596, 126.810879),
                new LatLng(37.693709, 127.171560)));

    }

    public void onMapClick(LatLng point){

        if (currentMarker != null) currentMarker.remove();
        Log.d(TAG, "setClickCurrentLocation: ");

        //현재 위도와 경도에서 화면 포인트를 알려준다
        Double latitude = point.latitude;
        Double longitude = point.longitude;


        //현재 화면에 찍힌 포인트로부터 위도와 경도를 알려준다.
        final LatLng latLng=new LatLng(latitude,longitude);

        Geocoder mGeoCoder = new Geocoder(this);
        List<Address> mResultList=null;
        try{
            mResultList=mGeoCoder.getFromLocation(
                    latLng.latitude,latLng.longitude,1);
        }catch (IOException e){
            e.printStackTrace();
            Log.d(TAG,"onComplete:주소변환실패");
        }
        String[] splitstr=mResultList.get(0).toString().split(",");
        final String address=splitstr[0].substring(splitstr[0].indexOf("\"")+1,splitstr[0].length()-2);
        Place plc = new Place() {
            @Override
            public String getId() {
                return null;
            }

            @Override
            public List<Integer> getPlaceTypes() {
                return null;
            }

            @Nullable
            @Override
            public CharSequence getAddress() {
                return null;
            }

            @Override
            public Locale getLocale() {
                return null;
            }

            @Override
            public CharSequence getName() {
                return address;
            }

            @Override
            public LatLng getLatLng() {
                return latLng;
            }

            @Nullable
            @Override
            public LatLngBounds getViewport() {
                return null;
            }

            @Nullable
            @Override
            public Uri getWebsiteUri() {
                return null;
            }

            @Nullable
            @Override
            public CharSequence getPhoneNumber() {
                return null;
            }

            @Override
            public float getRating() {
                return 0;
            }

            @Override
            public int getPriceLevel() {
                return 0;
            }

            @Nullable
            @Override
            public CharSequence getAttributions() {
                return null;
            }

            @Override
            public Place freeze() {
                return null;
            }

            @Override
            public boolean isDataValid() {
                return false;
            }
        };
        Location location = new Location("");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        C app = (C)getApplicationContext();
        app.setLoc(location,app.getNum_buttonclicked());
        app.setPlc(plc,app.getNum_buttonclicked());

        MarkerOptions mOptions= new MarkerOptions();
        mOptions.position(latLng);
        mOptions.draggable(false);
        mOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        currentMarker = this.googleMap.addMarker(mOptions);
        currentMarker.showInfoWindow();//누르지 않아도 정보창 띄움
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        // OnMapReadyCallback implements 해야 mapView.getMapAsync(this); 사용가능. this 가 OnMapReadyCallback
        this.googleMap = googleMap;
        //런타임 퍼미션 요청 대화상자나 GPS 활성 요청 대화상자 보이기전에 지도의 초기위치를 서울로 이동
        Log.i(TAG, "start");
        setCurrentLocation(null, "위치정보 가져올 수 없음", "위치 퍼미션과 GPS 활성 여부 확인");
        //나침반이 나타나도록 설정
        //googleMap.getUiSettings().setCompassEnabled(true);
        // 매끄럽게 이동함
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        //  API 23 이상이면 런타임 퍼미션 처리 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 사용권한체크
            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            if (hasFineLocationPermission == PackageManager.PERMISSION_DENIED) {
                //사용권한이 없을경우
                //권한 재요청
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            } else {
                //사용권한이 있는경우
                if (googleApiClient == null) {
                    ;
                }

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    googleMap.setMyLocationEnabled(true);
                }
            }
        } else {
            if (googleApiClient == null) {
            }
            googleMap.setMyLocationEnabled(true);
        }
        //맵뷰클릭마커
        googleMap.setOnMapClickListener(this);
    }
    public void onButtonClicked(View v) {
        //맵뷰 화면에서 하단 버튼 클릭 시
        //위치를 설정 하지 않았을 경우(마커가 없을 경우)
        //에러와 함께 버튼이 눌리지 않게 해야함
        //좌표값을 3번화면으로 동시에 넘긴다.->전역으로 처리완료
        C app = (C)getApplicationContext();
        Log.i(TAG,"test B");

        if(currentMarker==null)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("안내");
            builder.setMessage("정확한 위치를 검색해 주세요.");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            app.setIs_search(true);
            app.setNum_textview(app.getNum_buttonclicked());//버튼위치를 불러온 후 해당 위치 텍스트뷰 설정
            ((Screen3Activity)Screen3Activity.mContext).setTextView();//텍스트뷰 수정
            app.setIs_peopleSearchComplete(app.getNum_buttonclicked());//버튼위치에 해당하는 n번째 인원의 검색완료표시
            this.finish();//맵뷰 화면을 종료하고 이전 화면으로 넘어간다.
        }


    }


//    LocationManager mLM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//    private void registerLocationUpdates() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        mLM.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, defaultLocationListener);
//        mLM.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1,defaultLocationListener);
//    }
//    private final LocationListener defaultLocationListener = new LocationListener() {
//        @Override
//        public void onLocationChanged(Location location) {
//            double longitude = location.getLongitude();//경
//            double latitude =location.getLatitude();//위
//            DEFAULT_LOCATION = new LatLng(longitude,latitude);
//            String provider = location.getProvider();//위치제공자
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//
//        }
//    };
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
