package org.androidtown.centerpoint;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Field;

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

        //맵에서 텍스트뷰 쓰기 위함
        mContext=this;

        setContentView(R.layout.activity_screen3);
        int pos=0;
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


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                setVisibility(spinner.getSelectedItemPosition());

            }//인원 선택시 ui숨김 설정하는 함수이동
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
        TextView textView = (TextView) findViewById(textView4);
        TextView textView2 = (TextView) findViewById(textView5);
        TextView textView3 = (TextView) findViewById(textView6);
        TextView textView4 = (TextView) findViewById(textView7);
        Button button = (Button)findViewById(R.id.button3);
        Button button2 = (Button)findViewById(R.id.button4);
        Button button3 = (Button)findViewById(R.id.button5);
        Button button4 = (Button)findViewById(R.id.button6);
        switch(pos) {
            case 0:
                textView.setVisibility(View.INVISIBLE);
                textView2.setVisibility(View.INVISIBLE);
                textView3.setVisibility(View.INVISIBLE);
                textView4.setVisibility(View.INVISIBLE);
                button.setVisibility(View.INVISIBLE);
                button2.setVisibility(View.INVISIBLE);
                button3.setVisibility(View.INVISIBLE);
                button4.setVisibility(View.INVISIBLE);
                break;
            case 1:
                textView.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.INVISIBLE);
                textView3.setVisibility(View.INVISIBLE);
                textView4.setVisibility(View.INVISIBLE);
                button.setVisibility(View.VISIBLE);
                button2.setVisibility(View.INVISIBLE);
                button3.setVisibility(View.INVISIBLE);
                button4.setVisibility(View.INVISIBLE);
                break;
            case 2:
                textView.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.VISIBLE);
                textView3.setVisibility(View.INVISIBLE);
                textView4.setVisibility(View.INVISIBLE);
                button.setVisibility(View.VISIBLE);
                button2.setVisibility(View.VISIBLE);
                button3.setVisibility(View.INVISIBLE);
                button4.setVisibility(View.INVISIBLE);
                break;
            case 3:
                textView.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.VISIBLE);
                textView3.setVisibility(View.VISIBLE);
                textView4.setVisibility(View.INVISIBLE);
                button.setVisibility(View.VISIBLE);
                button2.setVisibility(View.VISIBLE);
                button3.setVisibility(View.VISIBLE);
                button4.setVisibility(View.INVISIBLE);
                break;
            case 4:
                textView.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.VISIBLE);
                textView3.setVisibility(View.VISIBLE);
                textView4.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
                button2.setVisibility(View.VISIBLE);
                button3.setVisibility(View.VISIBLE);
                button4.setVisibility(View.VISIBLE);
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
        Intent it_2=new Intent(getApplicationContext(),CenterResultPage.class);
        startActivity(it_2);
    }

}
