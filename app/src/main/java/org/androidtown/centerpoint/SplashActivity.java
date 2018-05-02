package org.androidtown.centerpoint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Seo Sung Joon on 2017-09-11 011.
 */
//어플 실행시 화면
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        try{
            Thread.sleep(500);//1초간 노출
        }catch(InterruptedException e){
            e.printStackTrace();
        }



        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

}
