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
 * Created by Seo Sung Joon on 2018-03-02 011.
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
        //***************************************

        //**베타 테스트 어플을 위한 사용 제한 알림 문구** //
        /*
        AlertDialog.Builder tst = new AlertDialog.Builder(this);
        tst.setTitle("베타 테스트 어플리케이션");
        tst.setMessage("본 어플은 베타 테스트 어플이며\n" +
                "2018/05/23 23:59 이후부터는 사용할 수 없습니다.\n" +
                "충분히 사용해 보시고\n" +
                "메시지로 안내드린 설문조사에 응해주시면" +
                "\n추첨을 통해 기프티콘을 드리도록 하겠습니다." +
                "\n감사합니다.");
        tst.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog tt = tst.create();
        tt.show();

        Date testEndDate;
        Date currentDate;
        String oTime="";
        String endDate="2018년05월23일 23:59";
        SimpleDateFormat mSimpleDateFormat=new SimpleDateFormat("yyyy년MM월dd일 HH:mm", Locale.KOREA);
        Date currentTime=new Date();
        oTime = mSimpleDateFormat.format(currentTime);//현재시간 (string)
        try {
            testEndDate = mSimpleDateFormat.parse(endDate);
            currentDate = mSimpleDateFormat.parse(oTime);

        }catch (ParseException e){
            return;
        }

        int compare = currentDate.compareTo(testEndDate);
        if(compare>0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("감사합니다.");
            builder.setMessage("본 테스트 어플의 사용 기한이 만료되었습니다.\n" +
                    "지금까지 사용해 주셔서 감사드리며\n" +
                    "제출해 주신 피드백을 토대로\n" +
                    "개선된 어플로 정식 출시때 찾아뵙겠습니다." +
                    "본 어플은 삭제 부탁드립니다."+
                    "\n감사합니다.");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        */
        //***********************베타 테스트 안내 문구 종료***************//

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
    }
}

