package pe.edu.upc.redevent.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import pe.edu.upc.redevent.R;

public class EventActivity extends AppCompatActivity {
    private static final String PREFS = "prefs";
    private static final String PREF_NAME = "Name";
    private Button mvalue_button;
    private Button mCheckInButton;
    //TextView nameTextView;
    float userrating;

    // Storage Access Class
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

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
    }

    private void updateCurrentValue(String value) {
        // Updates UI element
        //if (value.length() > 0) nameTextView.setText("Stored name is " + name);
        //else nameTextView.setText(R.string.default_text);
    }


   // private void updateKeptName(String value) {

   //     SharedPreferences.Editor e = sharedPreferences.edit();
    //    e.putString(PREF_NAME, value);
   //     e.commit();
   //     updateCurrentValue(value);
    //}

    //private String restoreKeptName() {
     //   return sharedPreferences.getString(PREF_NAME, "");
    //}

  //  private void openPreferences() {
  //      sharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
  //  }

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
        //openPreferences();
        //String name = restoreKeptName();

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
                    statusRatingEvent.setText("Malo");
                }
                if(userrating == 2){
                    statusRatingEvent.setText("Regular");
                }
                if(userrating == 3){
                    statusRatingEvent.setText("Bueno");
                }
                if(userrating == 4){
                    statusRatingEvent.setText("Muy Bueno");
                }
                if(userrating == 5){
                    statusRatingEvent.setText("Excelente");
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

}