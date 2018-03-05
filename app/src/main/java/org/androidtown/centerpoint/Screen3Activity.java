package org.androidtown.centerpoint;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Field;

import static org.androidtown.centerpoint.R.id.textView4;
import static org.androidtown.centerpoint.R.id.textView5;
import static org.androidtown.centerpoint.R.id.textView6;
import static org.androidtown.centerpoint.R.id.textView7;

public class Screen3Activity extends AppCompatActivity {
    TextView textView;

    String[] items = {"선택!","2","3","4","5","6"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_screen3);
        int pos=0;
        //*************
        Intent it3 = getIntent();
        String s2_receive = it3.getStringExtra("it2_sel");

        if(s2_receive.equals("2"))
        {
            pos=1;
        }
        if(s2_receive.equals("3"))
        {
            pos=2;
        }
        if(s2_receive.equals("4"))
        {
            pos=3;
        }
        if(s2_receive.equals("5"))
        {
            pos=4;
        }
        else if(s2_receive.equals("6"))
        {
            pos = 5;
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
        //******************************
        spinner.setSelection(pos);
        //******************************

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
            setVisibility(spinner.getSelectedItemPosition());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView){
            }

        });

        //set visibility of person ui
        setVisibility(pos);
        //--------------------------------------------------------------------

    }
    public void setVisibility(int pos){
        TextView textView = (TextView) findViewById(textView4);
        TextView textView2 = (TextView) findViewById(textView5);
        TextView textView3 = (TextView) findViewById(textView6);
        TextView textView4 = (TextView) findViewById(textView7);
        switch(pos) {
            case 1:
                textView.setVisibility(View.INVISIBLE);
                textView2.setVisibility(View.INVISIBLE);
                textView3.setVisibility(View.INVISIBLE);
                textView4.setVisibility(View.INVISIBLE);
                break;
            case 2:
                textView.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.INVISIBLE);
                textView3.setVisibility(View.INVISIBLE);
                textView4.setVisibility(View.INVISIBLE);
                break;
            case 3:
                textView.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.VISIBLE);
                textView3.setVisibility(View.INVISIBLE);
                textView4.setVisibility(View.INVISIBLE);
                break;
            case 4:
                textView.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.VISIBLE);
                textView3.setVisibility(View.VISIBLE);
                textView4.setVisibility(View.INVISIBLE);
                break;
            case 5:
                textView.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.VISIBLE);
                textView3.setVisibility(View.VISIBLE);
                textView4.setVisibility(View.VISIBLE);
                break;
        }
    }
    public void onButtonClicked(View v) {
        Intent it_1 = new Intent(getApplicationContext(),MapViewPage.class);
        startActivity(it_1);
    }

}
