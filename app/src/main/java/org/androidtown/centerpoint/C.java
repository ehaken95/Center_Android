
package org.androidtown.centerpoint;


import android.app.Application;
import android.content.res.Configuration;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.places.Place;
//입력된 위치값을 전역으로 저장하고
//위치의 한글명과 좌표값을 어떤 클래스 파일에서도 접근할 수 있게
//하기 위한 소스코드
/**
 * Created by seojj on 2018-03-15 015.
 */
public class C extends Application{
    private Place[] Plc = new Place[6];
    private Location[] Loc = new Location[6];

    public Place getPlc(int person){
        return Plc[person];
    }
    public void setPlc(Place Plc,int person){
        this.Plc[person]=Plc;
        Log.i("Test","Place is Complete Save : "+ this.Plc[person]);
    }
    public Location getLoc(int person){
        return Loc[person];
    }
    public void setLoc(Location Loc,int person){
        this.Loc[person]=Loc;
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }
    @Override
    public void onLowMemory(){
        super.onLowMemory();
    }
}
