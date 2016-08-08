package pe.edu.upc.redevent.ui.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import pe.edu.upc.redevent.R;
import pe.edu.upc.redevent.models.APIError;
import pe.edu.upc.redevent.models.APISuccess;
import pe.edu.upc.redevent.models.Event;
import pe.edu.upc.redevent.services.RedEventService;
import pe.edu.upc.redevent.services.RedEventServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EventDetailFragment extends  Fragment {

    private ImageView mImageEvent;
    private TextView mDescriptionEvent;
    private TextView mDateEvent;
    private TextView mPriceEvent;
    private TextView mAddressEvent;
    private Button mvalue_button;
    private Button mCheckInButton;

    private String userId;
    private String eventId;
    private String status;

    float userrating;

    private Event mEvent;

    public static EventDetailFragment getInstance(Event event, long userId) {
        EventDetailFragment f = new EventDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("event", event);
        args.putLong("users_id", userId);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = String.valueOf(getArguments().getLong("users_id"));
            mEvent =  (Event) getArguments().getSerializable("event");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_detail, container, false);

        mDescriptionEvent = (TextView) view.findViewById(R.id.descriptionEvent);
        mDateEvent = (TextView) view.findViewById(R.id.datevalueEvent);
        mAddressEvent = (TextView) view.findViewById(R.id.addressvalueEvent);
        mPriceEvent = (TextView) view.findViewById(R.id.pricevalueEvent);

        if (getArguments() != null) {

            eventId = String.valueOf(mEvent.getId());
            status =  mEvent.getStatus();

            String imageURL= mEvent.getImage();
            String descriptionEvent=mEvent.getDescription();
            String dateValueEvent = mEvent.getStartdate();
            String addressEvent= mEvent.getAddress();
            String priceValueEvent = mEvent.getPrice();

            imageURL = RedEventService.API_BASE_URL + imageURL;

            mImageEvent = (ImageView) view.findViewById(R.id.imageEvent);

            Picasso.with(this.getActivity())
                    .load(imageURL)
                    .error(R.drawable.image_default)
                    .into(mImageEvent);


            mDescriptionEvent.setText(descriptionEvent);
            mDateEvent.setText(dateValueEvent);
            mPriceEvent.setText(priceValueEvent);
            mAddressEvent.setText(addressEvent);

        }

        mvalue_button = (Button) view.findViewById(R.id.value_button);
        mvalue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayRating();
            }
        });

        mCheckInButton = (Button)  view.findViewById(R.id.checking_button);

        mCheckInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RedEventService service = RedEventServiceGenerator.createService();
                Call<APISuccess> call = service.checking(userId, eventId );

                call.enqueue(new Callback<APISuccess>() {
                    @Override
                    public void onResponse(Call<APISuccess> call, Response<APISuccess> response) {
                        int statusCode = response.code();
                        Log.d(EventDetailFragment.class.getSimpleName(), "HTTP status code: " + statusCode);

                        if(response.isSuccessful()) {

                            APISuccess APISuccess = response.body();
                            Log.e(this.getClass().getSimpleName(), "APISuccess " +  APISuccess.getMessage());

                        }else{
                            // Best Practice APIError: https://futurestud.io/blog/retrofit-2-simple-error-handling
                            APIError error = APIError.parseError(response);
                            Log.e(this.getClass().getSimpleName(), "ApiError " + error.getStatus() + ":" + error.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<APISuccess> call, Throwable t) {
                        Log.e(this.getClass().getSimpleName(), "onFailure:" + t.getMessage());
                        t.printStackTrace();
                    }

                });

                displayQuestionRating();
                mCheckInButton.setVisibility(View.INVISIBLE);

            }
        });

        mCheckInButton.setVisibility(status.equals("2") ? View.INVISIBLE : View.VISIBLE);
        mvalue_button.setVisibility(status.equals("2") ? View.VISIBLE : View.INVISIBLE);

        return view;
    }


    private void updateCurrentValue(Integer value) {

        Log.d(EventDetailFragment.class.getSimpleName(), "Rating: " + value.toString());

        RedEventService service = RedEventServiceGenerator.createService();

        Call<APISuccess> call = service.rating(userId, eventId, value);

        call.enqueue(new Callback<APISuccess>() {
            @Override
            public void onResponse(Call<APISuccess> call, Response<APISuccess> response) {
                int statusCode = response.code();
                Log.d(EventDetailFragment.class.getSimpleName(), "HTTP status code: " + statusCode);

                if(response.isSuccessful()) {

                    APISuccess APISuccess = response.body();
                    Log.e(EventDetailFragment.this.getClass().getSimpleName(), "APISuccess " +  APISuccess.getMessage());

                }else{
                    // Best Practice APIError: https://futurestud.io/blog/retrofit-2-simple-error-handling
                    APIError error = APIError.parseError(response);
                    Log.e(EventDetailFragment.this.getClass().getSimpleName(), "ApiError " + error.getStatus() + ":" + error.getMessage());
                }
            }

            @Override
            public void onFailure(Call<APISuccess> call, Throwable t) {
                Log.e(EventDetailFragment.class.getSimpleName(), "onFailure:" + t.getMessage());
                t.printStackTrace();
            }

        });



    }

    private void displayQuestionRating() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
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

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Rating Event");

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        final RatingBar inputRatingEvent = new RatingBar(getContext());
        final TextView statusRatingEvent = new TextView(getContext());

        LinearLayout.LayoutParams ratingParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ratingParams.gravity = Gravity.CENTER;

        inputRatingEvent.setLayoutParams(ratingParams);
        statusRatingEvent.setLayoutParams(ratingParams);

        inputRatingEvent.setNumStars(5);
        inputRatingEvent.setStepSize((float)1);
        inputRatingEvent.setRating(3);
        statusRatingEvent.setText(R.string.event_rating_3);

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
                Integer ratingValue =  (int) rating;
                updateCurrentValue(ratingValue);
                mvalue_button.setVisibility(View.INVISIBLE);
//                startActivity(new Intent(EventDetailActivity.this, TopicActivity.class));
//                finish();

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
