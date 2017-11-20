package org.androidtown.centerpoint;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Spinner;
import android.view.View;
import java.lang.reflect.Field;
/**
 * Created by Seo Sung Joon on 2017-09-11 011.
 */


public class MainActivity extends AppCompatActivity {
    private static String str_sel;
    TextView textView;
    String[] items = {"선택!","2","3","4","5","6"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //스피너를 final로 바꿈->선택값 저장하기 위해서
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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                //여기로 선택값으로 넣어줌
                str_sel = spinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView){
            }
        });

    }
    public void onButtonClicked(View v) {
        Intent it3 = new Intent(getApplicationContext(),Screen3Activity.class);
        it3.putExtra("it2_sel",str_sel);
        startActivity(it3);
    //    Intent intent = new Intent(getApplicationContext(),Screen3Activity.class);
    //    startActivity(intent);
    }
}
