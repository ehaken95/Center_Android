package org.androidtown.centerpoint;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.Intent;

import com.ramotion.foldingcell.FoldingCell;

import java.lang.reflect.Field;

public class Screen3Activity extends AppCompatActivity {
    TextView textView;
    String[] items = {"선택!","2","3","4","5","6"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_screen3);

        //*************
        Intent it3 = getIntent();
        String s2_receive = it3.getStringExtra("it2_sel");
       int pos=0;

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

        // get our folding cell
        final FoldingCell fc = (FoldingCell) findViewById(R.id.folding_cell);
        // attach click listener to folding cell
        fc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc.toggle(false);
            }
        });
        //*****************
        Spinner spinner = (Spinner)findViewById(R.id.spinner);

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
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView){
            }

        });

    }
}
