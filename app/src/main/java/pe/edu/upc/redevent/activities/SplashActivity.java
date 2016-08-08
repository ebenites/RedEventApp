package pe.edu.upc.redevent.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import pe.edu.upc.redevent.R;
import pe.edu.upc.redevent.utils.PreferencesManager;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                verifyLogged();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    private void verifyLogged(){
        PreferencesManager preferencesManager = PreferencesManager.getInstance(this);
        if(preferencesManager.loadPreference(PreferencesManager.PREF_ISLOGGED) != null){
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }else{
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }
        finish();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

}
