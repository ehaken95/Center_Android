package org.androidtown.centerpoint;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;

import java.lang.reflect.Field;

/**
 * Created by Seo Sung Joon on 2018-02-11 011.
 */


public class MainActivity extends AppCompatActivity {
    private String str_sel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] items = {"선택!","2","3","4","5","6"};
        //타이틀바 중앙 정렬
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_titlebar);


        //************************************
        //OOM에러로 인한 Glide라이브러리 추가
        setContentView(R.layout.activity_main);
        ImageView iv=(ImageView)findViewById(R.id.firstImageView);
        //iv.setImageResource(R.drawable.img_firstpage);
        Glide.with(this).load(R.drawable.img_firstpage).into(iv);
        //**********************************


        //스피너를 final 로 바꿈->선택값 저장하기 위해서
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
        //스피너로 값을 선택하기 위함
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
    //선택된 값을 다음 화면으로 넘기기 위한 구문
    public void onButtonClicked(View v) {
        if (!str_sel.equals("선택!")) {
            Intent it3 = new Intent(getApplicationContext(),Screen3Activity.class);
            it3.putExtra("it2_sel",str_sel);
            startActivity(it3);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("안내");
            builder.setMessage("인원을 선택해 주십시오.");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        //    Intent intent = new Intent(getApplicationContext(),Screen3Activity.class);
    //    startActivity(intent);
    }
}

