package pe.edu.upc.redevent.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import pe.edu.upc.redevent.R;

public class EventActivity extends AppCompatActivity {
    private static final String PREFS = "prefs";
    private static final String PREF_NAME = "Name";

    TextView nameTextView;
    float userrating;
    Button ValueEvent_button = (Button) findViewById(R.id.value_button);
    // Storage Access Class
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        ToggleButton mCheckInButton = (ToggleButton) findViewById(R.id.checking_button);
        mCheckInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayQuestionRating();
            }
        });
    }

    private void updateCurrentName(String name) {
        // Updates UI element
        if (name.length() > 0) nameTextView.setText("Stored name is " + name);
        else nameTextView.setText(R.string.default_text);
    }


    private void updateKeptName(String name) {

        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putString(PREF_NAME, name);
        e.commit();
        updateCurrentName(name);
    }

    private String restoreKeptName() {
        return sharedPreferences.getString(PREF_NAME, "");
    }

    private void openPreferences() {
        sharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
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
                // Nothing
            }
        });
        alert.show();
    }

    private void displayRating() {
        //openPreferences();
        //String name = restoreKeptName();

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Rating Event");

        //final EditText input = new EditText(this);
        //alert.setView(input);

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
        inputRatingEvent.setRating(5);

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
                //String inputName = input.getText().toString();
                float rating = inputRatingEvent.getRating();
                String inputName =  Float.toString(rating);
                updateKeptName(inputName);
                ValueEvent_button.setVisibility(View.INVISIBLE);
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ValueEvent_button.setVisibility(View.VISIBLE);
            }
        });
        alert.show();

    }

}