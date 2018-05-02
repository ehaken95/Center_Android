
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
    private boolean is_search = false;
    private int num_textview = 0;//텍스트뷰의 위치
    private int num_buttonclicked;//어떤 버튼이 눌렸는지 확인
    private int num_people;//몇명의 사람인지 확인
    private boolean[] is_peopleSearchComplete = {false,false,false,false,false,false};
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

    public void setIs_search(boolean is_search){
        this.is_search = is_search;
    }
    public boolean getIs_search(){
        return is_search;
    }

    public void setNum_textview(int num_textview){
        this.num_textview = num_textview;
    }
    public int getNum_textview(){
        return num_textview;
    }

    public void setNum_buttonclicked(int num_buttonclicked){
        this.num_buttonclicked=num_buttonclicked;
    }
    public int getNum_buttonclicked(){
        return num_buttonclicked;
    }

    public void setNum_people(int num_people){
        this.num_people = num_people;
    }
    public int getNum_people(){
        return num_people;
    }

    public void setIs_peopleSearchComplete(int position) {
        this.is_peopleSearchComplete[position] = true;
    }
    public boolean getIs_peopleSearchComplete(int position){
        return is_peopleSearchComplete[position];
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
