package org.androidtown.centerpoint;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;

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

public class MapViewPage extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.OnConnectionFailedListener{

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

        if ( currentMarker != null ) currentMarker.remove();
        if ( location != null) {
            //현재위치의 위도 경도 가져옴
            LatLng currentLocation = new LatLng( location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(currentLocation);
            markerOptions.title(markerTitle);
            markerOptions.snippet(markerSnippet);
            markerOptions.draggable(true);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            currentMarker = this.googleMap.addMarker(markerOptions);
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
            return;
        }
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker = this.googleMap.addMarker(markerOptions);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {

    // OnMapReadyCallback implements 해야 mapView.getMapAsync(this); 사용가능. this 가 OnMapReadyCallback



        this.googleMap = googleMap;



        //런타임 퍼미션 요청 대화상자나 GPS 활성 요청 대화상자 보이기전에 지도의 초기위치를 서울로 이동

        setCurrentLocation(null, "위치정보 가져올 수 없음", "위치 퍼미션과 GPS 활성 여부 확인");



        //나침반이 나타나도록 설정

        googleMap.getUiSettings().setCompassEnabled(true);

        // 매끄럽게 이동함

        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));



        //  API 23 이상이면 런타임 퍼미션 처리 필요

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // 사용권한체크

            int hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);



            if ( hasFineLocationPermission == PackageManager.PERMISSION_DENIED) {

                //사용권한이 없을경우

                //권한 재요청

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            } else {

                //사용권한이 있는경우

                if ( googleApiClient == null) {
                    ;

                }



                if ( ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)

                {

                    googleMap.setMyLocationEnabled(true);

                }

            }

        } else {



            if ( googleApiClient == null) {



            }



            googleMap.setMyLocationEnabled(true);

        }






    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
