package pe.edu.upc.redevent.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import pe.edu.upc.redevent.R;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            public void run() {

                //startActivity(new Intent (SplashActivity.this,LoginActivity.class));

                Intent explicit_intent;
                explicit_intent = new Intent(SplashActivity.this, EventDetailActivity.class);

                explicit_intent.putExtra("users_id","1");
                explicit_intent.putExtra("events_id","2");
                explicit_intent.putExtra("status","1");
                explicit_intent.putExtra("imageURL","/uploads/images/events/google-io.jpg");
                explicit_intent.putExtra("descriptionEvent","Road to Ultra, el exitoso festival electrónico que en 2015 logró congregar a casi 20 mil personas en su primera edición, regresará a Lima el 7 de octubre de 2016 como parte del circuito Ultra de Latinoamérica.");
                explicit_intent.putExtra("dateValueEvent","2016-10-07 15:00:00");
                explicit_intent.putExtra("addressEvent","Circuito de playas de la Costa Verde");

                startActivity(explicit_intent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

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
