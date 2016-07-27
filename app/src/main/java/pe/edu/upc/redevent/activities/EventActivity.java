package pe.edu.upc.redevent.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import pe.edu.upc.redevent.R;

public class EventActivity extends AppCompatActivity {
    private static final String PREFS = "prefs";
    private static final String PREF_NAME = "Name";

    TextView nameTextView;

    // Storage Access Class
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Button mCheckInButton = (Button) findViewById(R.id.checking_button);
        mCheckInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayRating();
            }
        });
    }

   private void updateCurrentName(String name) {
        // Updates UI element
        if(name.length() > 0) nameTextView.setText("Stored name is " + name);
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

    private void displayRating() {
        openPreferences();
        String name = restoreKeptName();
        if (name.length() > 0) {
            Toast.makeText(this, "Welcome back " + name + "!", Toast.LENGTH_LONG).show();
            updateCurrentName(name);
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Name Keeper");
            alert.setMessage("Whats is your name?");
            //final EditText input = new EditText(this);
            //alert.setView(input);

            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            final EditText inputFirstName = new EditText(this);
            final EditText inputLastName = new EditText(this);
            inputFirstName.setHint("First Name");
            inputLastName.setHint("Last Name");
            layout.addView(inputFirstName);
            layout.addView(inputLastName);
            alert.setView(layout);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //String inputName = input.getText().toString();
                    String inputName = inputFirstName.getText().toString() +
                            " " + inputLastName.getText().toString();
                    updateKeptName(inputName);
                    Toast.makeText(getApplicationContext(),
                            "Welcome "+inputName+"!",Toast.LENGTH_LONG).show();
                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Nothing
                }
            });
            alert.show();
        }

    }

}
