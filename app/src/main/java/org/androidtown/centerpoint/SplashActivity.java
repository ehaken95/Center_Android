package org.androidtown.centerpoint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Seo Sung Joon on 2017-09-11 011.
 */

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        try{
            Thread.sleep(4000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

}
