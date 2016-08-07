package pe.edu.upc.redevent.activities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.MediaType;
import pe.edu.upc.redevent.R;

import pe.edu.upc.redevent.models.APIError;
import pe.edu.upc.redevent.models.EventDetail;
import pe.edu.upc.redevent.services.RedEventService;
import pe.edu.upc.redevent.services.RedEventServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EventDetailActivity extends AppCompatActivity {
    private ImageView mImageEvent;
    private TextView mDescriptionEvent;
    private TextView mDateEvent;
    private TextView mHourEvent;
    private TextView mAddressEvent;
    private Button mvalue_button;
    private Button mCheckInButton;

    private String userId;
    private String eventId;
    private String status;

    //TextView nameTextView;
    float userrating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Intent intent = getIntent();

        Bundle extras =intent.getExtras();

        mDescriptionEvent = (TextView) findViewById(R.id.descriptionEvent);
        mDateEvent = (TextView) findViewById(R.id.datevalueEvent);
        mHourEvent = (TextView) findViewById(R.id.hourvalueEvent);
        mAddressEvent = (TextView) findViewById(R.id.addressvalueEvent);

        if (extras != null) {//ver si contiene datos

            userId = (String)extras.get("users_id");
            eventId = (String)extras.get("events_id");
            status =  (String)extras.get("status");

            String imageURL= (String) extras.get("imageURL");
            String descriptionEvent=(String)extras.get("descriptionEvent");
            String datevalueEvent= (String) extras.get("datevalueEvent");
            String hourvalueEvent= (String) extras.get("hourvalueEvent");
            String addressEvent= (String) extras.get("addressEvent");

            mImageEvent = (ImageView) findViewById(R.id.imageView);
            mImageEvent.setImageBitmap(getBitmapFromURL(imageURL));

            mDescriptionEvent.setText(descriptionEvent);
            mDateEvent.setText(datevalueEvent);
            mHourEvent.setText(hourvalueEvent);
            mAddressEvent.setText(addressEvent);

        }

        mvalue_button = (Button) findViewById(R.id.value_button);
        mvalue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayRating();
            }
        });

        mCheckInButton = (Button) findViewById(R.id.checking_button);
        mCheckInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayQuestionRating();
            }
        });

        mCheckInButton.setVisibility(status == "3" ? View.INVISIBLE : View.VISIBLE);
        mvalue_button.setVisibility(status == "3" ? View.VISIBLE : View.INVISIBLE);

    }

    private void updateCurrentValue(String value) {

        RedEventService service = RedEventServiceGenerator.createService();

        startActivity(new Intent(EventDetailActivity.this, TopicActivity.class));
        finish();

    }


    private void displayQuestionRating() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Desea Evaluar el Evento?");
        alert.setPositiveButton("Evaluar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //String inputName = input.getText().toString();
                displayRating();
            }
        });
        alert.setNegativeButton("En Otro Momento", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mvalue_button.setVisibility(View.VISIBLE);
            }
        });
        alert.show();
    }

    private void displayRating() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Rating Event");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final RatingBar inputRatingEvent = new RatingBar(this);
        final TextView statusRatingEvent = new TextView(this);


        LinearLayout.LayoutParams ratingParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ratingParams.gravity = Gravity.CENTER;

        inputRatingEvent.setLayoutParams(ratingParams);
        statusRatingEvent.setLayoutParams(ratingParams);

        inputRatingEvent.setNumStars(5);
        inputRatingEvent.setStepSize((float)1);
        inputRatingEvent.setRating(3);

        inputRatingEvent.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                userrating= inputRatingEvent.getRating();

                if(userrating == 0){

                    inputRatingEvent.setRating(1);

                }
                if(userrating == 1){
                    statusRatingEvent.setText(R.string.event_rating_1);
                }
                if(userrating == 2){
                    statusRatingEvent.setText(R.string.event_rating_2);
                }
                if(userrating == 3){
                    statusRatingEvent.setText(R.string.event_rating_3);
                }
                if(userrating == 4){
                    statusRatingEvent.setText(R.string.event_rating_4);
                }
                if(userrating == 5){
                    statusRatingEvent.setText(R.string.event_rating_5);
                }

            }
        });

        layout.addView(inputRatingEvent);
        layout.addView(statusRatingEvent);

        alert.setView(layout);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                float rating = inputRatingEvent.getRating();
                String ratingValue =  Float.toString(rating);
                updateCurrentValue(ratingValue);
                mvalue_button.setVisibility(View.INVISIBLE);
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mvalue_button.setVisibility(View.VISIBLE);
            }
        });
        alert.show();

    }

    private Bitmap getBitmapFromURL(String src) {
        try {

            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            Log.e("src", src);
            URL url = new URL(src);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 );
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            InputStream input = conn.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap", "returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception", e.getMessage());
            return null;
        }
    }
}