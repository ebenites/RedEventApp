package pe.edu.upc.redevent.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.redevent.R;

public class LoginActivity extends AppCompatActivity implements OnConnectionFailedListener {

    private static final String TAG = LoginActivity.class.getSimpleName();

    // UI references.
    private View mLoginFormView;
    private View mProgressView;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Button mEmailSignInButton;

    /* The login button for Google */
    private SignInButton mGoogleLoginButton;

    /* Request code used to invoke sign in user interactions for Google+ */
    private static final int RC_GOOGLE_SIGN_IN = 9001;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Set up the login form.
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {   // On Enter
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptEmailSignIn();
                    return true;
                }
                return false;
            }
        });

        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptEmailSignIn();
            }
        });

        /* *************************************
         *               GOOGLE                *
         ***************************************/
        /* Load the Google login button */
        mGoogleLoginButton = (SignInButton) findViewById(R.id.google_sign_in_button);
        mGoogleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptGoogleSignIn(view);
            }
        });

        // Configure sign-in to request the user's ID, email address, and basic profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        // Request Permissions
        validatePermissions();
    }

    private void attemptEmailSignIn() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            mEmailView.requestFocus();
            return;
        }
        if (!email.contains("@")) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            mEmailView.requestFocus();
            return;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            mPasswordView.requestFocus();
            return;
        }
        if (password.length() <= 4) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            mPasswordView.requestFocus();
            return;
        }

        // Show a progress spinner, and kick off a background task to  perform the user login attempt.
        showProgress(true);

        /* TEMP */  Toast.makeText(this, "No implementado aún", Toast.LENGTH_LONG).show();

//        UserLoginTask mAuthTask = new UserLoginTask(email, password);
//        mAuthTask.execute((Void) null);

    }

    /* *************************************
     *               GOOGLE                *
     ***************************************/
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not be available.
        Log.e(TAG, "onConnectionFailed:" + connectionResult);
    }

    public void attemptGoogleSignIn(View view){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    public void attemptGoogleSignOut(View view) {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {

                /*TEMP*/    showProgress(false);
                /*TEMP*/    findViewById(R.id.login_success_view).setVisibility(View.GONE);
            }
        });
    }

    public void attemptGoogleRevokeAccess(View view) {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {

                /*TEMP*/    showProgress(false);
                /*TEMP*/    findViewById(R.id.login_success_view).setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful
                GoogleSignInAccount account = result.getSignInAccount();
                Log.d(TAG, account.getDisplayName());
                Log.d(TAG, account.getEmail());
                Log.d(TAG, account.getPhotoUrl().toString());
                Log.d(TAG, account.getIdToken());

                Toast.makeText(this, "Bienvenido " + account.getDisplayName(), Toast.LENGTH_LONG).show();

                showProgress(true);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                /**
                 * How to SharedPreferences
                 */

                SharedPreferences sharedPreferences = getSharedPreferences("redevent-data",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email", account.getEmail());
                editor.commit();

                Log.d(TAG, "Read from SharedPreferences[email]: " + sharedPreferences.getString("email",""));

//                editor.clear();
//                editor.commit();

                /**
                 * End SharedPreferences
                 */

                /*TEMP*/    mProgressView.setVisibility(View.GONE);
                /*TEMP*/    mProgressView.animate().setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime)).alpha(0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mProgressView.setVisibility(View.GONE);
                    }
                });
                /*TEMP*/    findViewById(R.id.login_success_view).setVisibility(View.VISIBLE);

//                http://square.github.io/picasso/
                /*TEMP*/    Picasso.with(this).load(account.getPhotoUrl()).resize(200, 200).into(((ImageView) findViewById(R.id.profileImageView)));
                /*TEMP*/    ((TextView)findViewById(R.id.displayNameTextView)).setText(account.getDisplayName());
                /*TEMP*/    ((TextView)findViewById(R.id.emailTextView)).setText(account.getEmail());


            } else {
                // Google Sign In failed, update UI appropriately
                Log.e(TAG, "Google Sign In failed!");

                Toast.makeText(this, "Google Sign In failed!", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Permissions
     */

    private static final int REDEVENT_PERMISSIONS_REQUEST = 100;

    private static List<String> REDEVENT_PERMISSIONS_LIST;

    static {
        REDEVENT_PERMISSIONS_LIST = new ArrayList<>();
        REDEVENT_PERMISSIONS_LIST.add(Manifest.permission.INTERNET);
        REDEVENT_PERMISSIONS_LIST.add(Manifest.permission.GET_ACCOUNTS);
        REDEVENT_PERMISSIONS_LIST.add(Manifest.permission.WRITE_CALENDAR);
        REDEVENT_PERMISSIONS_LIST.add(Manifest.permission.ACCESS_FINE_LOCATION);
        REDEVENT_PERMISSIONS_LIST.add(Manifest.permission.CAMERA);
        REDEVENT_PERMISSIONS_LIST.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }

    private boolean permissionsGranted(){
        for (String permission : REDEVENT_PERMISSIONS_LIST){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    private void validatePermissions(){
        if(!permissionsGranted()){
            ActivityCompat.requestPermissions(this, REDEVENT_PERMISSIONS_LIST.toArray(new String[REDEVENT_PERMISSIONS_LIST.size()]), REDEVENT_PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch (requestCode){
            case REDEVENT_PERMISSIONS_REQUEST: {
                for (int i=0; i<grantResults.length; i++){
                    Log.d("REDEVENT", ""+grantResults[i]);
                    if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Permissions Required!", Toast.LENGTH_LONG).show();
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                finishAffinity();
                            }
                        }, Toast.LENGTH_LONG);
                    }
                }
            }
        }
    }
}
