package org.androidtown.centerpoint;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Field;

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


public class Screen3Activity extends AppCompatActivity {

    //맵뷰에서 텍스트뷰쓰기위함
    public static Context mContext;
    //첫번째 화면에서 선택된 값을 이쪽으로 가져온 후
    //인원수를 조정하기 위함
    String[] items = {"2","3","4","5","6"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //타이틀바 중앙 정렬
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_titlebar);
        //pos 저장위한 전역변수
        final C app = (C)getApplicationContext();
        for(int i=0;i<5;i++) {
            app.setFalseIs_peopleSearchComplete(i);
        }
        //맵에서 텍스트뷰 쓰기 위함
        mContext=this;

        setContentView(R.layout.activity_screen3);
        int pos=0;
        int save_pos=0;
        //*************
        Intent it3 = getIntent();
        String s2_receive = it3.getStringExtra("it2_sel");

        if(s2_receive.equals("2"))
        {
            pos=0;
        }
        if(s2_receive.equals("3"))
        {
            pos=1;
        }
        if(s2_receive.equals("4"))
        {
            pos=2;
        }
        if(s2_receive.equals("5"))
        {
            pos=3;
        }
        else if(s2_receive.equals("6"))
        {
            pos = 4;
        }
        //pos는 인자값인데, 2~6까지가 배열의 인자임
        //인덱스와 인자값을 일치시키기 위해 +2
        save_pos=pos+2;
        app.setNum_people(save_pos);
        //*****************
        final Spinner spinner = (Spinner)findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,items);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);



        //아래는 dropdown list scroll 구현
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner);

            // Set popupWindow height to 500px
            popupWindow.setHeight(500);
        }
        catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
        //여기까지
        spinner.setAdapter(adapter);

        spinner.setSelection(pos);
        save_pos=pos+2;
        app.setNum_people(save_pos);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                setVisibility(spinner.getSelectedItemPosition());
                int tmp_save_pos=spinner.getSelectedItemPosition()+2;
                app.setNum_people(tmp_save_pos);
            }//인원 선택시 ui숨김 설정하는 함수이동 및 전역으로 인원수 설정
            @Override
            public void onNothingSelected(AdapterView<?> adapterView){
            }

        });

        //set visibility of person ui
        setVisibility(pos);
        //--------------------------------------------------------------------
    }
    //인원 선택값에 따른 ui숨김/노출 설정
    public void setVisibility(int pos){

        C app = (C)getApplicationContext();
        LinearLayout linear1 = (LinearLayout)findViewById(line4);
        LinearLayout linear2 = (LinearLayout)findViewById(line5);
        LinearLayout linear3 = (LinearLayout)findViewById(line6);
        LinearLayout linear4 = (LinearLayout)findViewById(line7);
        switch(pos) {
            case 0:
                linear1.setVisibility(View.INVISIBLE);
                linear2.setVisibility(View.INVISIBLE);
                linear3.setVisibility(View.INVISIBLE);
                linear4.setVisibility(View.INVISIBLE);
                for(int i=2;i<=5;i++) {
                    if (app.getLoc(i) != null) {
                        app.setLoc(null, i);
                        app.setPlc(null,i);
                        app.setFalseIs_peopleSearchComplete(i);
                    }
                }
                break;
            case 1:
                linear1.setVisibility(View.VISIBLE);
                linear2.setVisibility(View.INVISIBLE);
                linear3.setVisibility(View.INVISIBLE);
                linear4.setVisibility(View.INVISIBLE);
                for(int i=3;i<=5;i++) {
                    if (app.getLoc(i) != null) {
                        app.setLoc(null, i);
                        app.setPlc(null,i);
                        app.setFalseIs_peopleSearchComplete(i);
                    }
                }
                break;
            case 2:
                linear1.setVisibility(View.VISIBLE);
                linear2.setVisibility(View.VISIBLE);
                linear3.setVisibility(View.INVISIBLE);
                linear4.setVisibility(View.INVISIBLE);
                for(int i=4;i<=5;i++) {
                    if (app.getLoc(i) != null) {
                        app.setLoc(null, i);
                        app.setPlc(null,i);
                        app.setFalseIs_peopleSearchComplete(i);
                    }
                }
                break;
            case 3:
                linear1.setVisibility(View.VISIBLE);
                linear2.setVisibility(View.VISIBLE);
                linear3.setVisibility(View.VISIBLE);
                linear4.setVisibility(View.INVISIBLE);
                for(int i=5;i<=5;i++) {
                    if (app.getLoc(i) != null) {
                        app.setLoc(null, i);
                        app.setPlc(null,i);
                        app.setFalseIs_peopleSearchComplete(i);
                    }
                }
                break;
            case 4:
                linear1.setVisibility(View.VISIBLE);
                linear2.setVisibility(View.VISIBLE);
                linear3.setVisibility(View.VISIBLE);
                linear4.setVisibility(View.VISIBLE);
                break;
        }
        changed_pos_then_setTextView(pos);
    }

    public void changed_pos_then_setTextView(int pos){//스피너 값변경에 따른 텍스트뷰 재설정

        TextView textView1 = (TextView) findViewById(textView4);
        TextView textView2 = (TextView) findViewById(textView5);
        TextView textView3 = (TextView) findViewById(textView6);
        TextView textView4 = (TextView) findViewById(textView7);

        switch (pos){
            case 0:
                textView1.setText(R.string.sc3_person_3);
                textView2.setText(R.string.sc3_person_4);
                textView3.setText(R.string.sc3_person_5);
                textView4.setText(R.string.sc3_person_6);

                break;
            case 1:
                textView2.setText(R.string.sc3_person_4);
                textView3.setText(R.string.sc3_person_5);
                textView4.setText(R.string.sc3_person_6);
                break;
            case 2:
                textView3.setText(R.string.sc3_person_5);
                textView4.setText(R.string.sc3_person_6);
                break;
            case 3:
                textView4.setText(R.string.sc3_person_6);
                break;
            case 4:
                break;
        }


    }
    public void setTextView(){//맵뷰와 연동되는 텍스트뷰 설정

        TextView textViewfir = (TextView) findViewById(textView2);
        TextView textViewsec = (TextView) findViewById(textView3);
        TextView textView = (TextView) findViewById(textView4);
        TextView textView2 = (TextView) findViewById(textView5);
        TextView textView3 = (TextView) findViewById(textView6);
        TextView textView4 = (TextView) findViewById(textView7);
        C app = (C)getApplicationContext();

        if(app.getIs_search()){
            switch (app.getNum_textview()){
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
    public void onButtonClicked(View v) {
        C app = (C)getApplicationContext();
        switch(v.getId()){//몇 번째 버튼이 눌렸는지에 대한 값 저장
            case R.id.button1: app.setNum_buttonclicked(0); break;
            case R.id.button2: app.setNum_buttonclicked(1); break;
            case R.id.button3: app.setNum_buttonclicked(2); break;
            case R.id.button4: app.setNum_buttonclicked(3); break;
            case R.id.button5: app.setNum_buttonclicked(4); break;
            case R.id.button6: app.setNum_buttonclicked(5); break;
        }
        Intent it_1 = new Intent(getApplicationContext(),MapViewPage.class);
        startActivity(it_1);
    }
    public void onButtonClickedResult(View v){
        C app = (C)getApplicationContext();
        int isSafeToGOResult = 1;//0이면 안됨.1이면 됨
        for(int i=0;i<app.getNum_people();i++){
            if(!app.getIs_peopleSearchComplete(i)){//가면 안될경우
                 isSafeToGOResult=0;
            }
        }
        if(isSafeToGOResult==0)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("안내");
            builder.setMessage("인원별 목적지를 전부 설정해 주세요.");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{//여기서 결과화면으로 넘어가야함
            Intent it_2=new Intent(getApplicationContext(),CenterResultPage.class);
            startActivity(it_2);
        }

    }

}
