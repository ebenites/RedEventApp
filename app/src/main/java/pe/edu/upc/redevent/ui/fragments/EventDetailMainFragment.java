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

import com.orm.SugarRecord;
import com.squareup.picasso.Picasso;

import java.util.List;

import pe.edu.upc.redevent.R;
import pe.edu.upc.redevent.activities.MainActivity;
import pe.edu.upc.redevent.models.APIError;
import pe.edu.upc.redevent.models.APISuccess;
import pe.edu.upc.redevent.models.Event;
import pe.edu.upc.redevent.models.User;
import pe.edu.upc.redevent.services.RedEventService;
import pe.edu.upc.redevent.services.RedEventServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EventDetailMainFragment extends  Fragment {

    private ImageView mImageEvent;
    private TextView descriptionDetailEvent;
    private TextView dateDetailEvent;
    private TextView hourDetailEvent;
    private TextView addressDetailEvent;

    private String userId;
    private String eventId;
    private String status;

    private ImageView imageDetailEvent;

    float userrating;

    private Event mEvent;

    public static EventDetailMainFragment getInstance(Event event, long userId) {
        EventDetailMainFragment f = new EventDetailMainFragment();
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
        View view= inflater.inflate(R.layout.fragment_event_detail_main, container, false);
        imageDetailEvent = (ImageView) view.findViewById(R.id.imageDetailEvent);
        descriptionDetailEvent = (TextView) view.findViewById(R.id.descriptionDetailEvent);
        dateDetailEvent = (TextView) view.findViewById(R.id.dateDetailEvent);
        hourDetailEvent = (TextView) view.findViewById(R.id.hourDetailEvent);
        addressDetailEvent = (TextView) view.findViewById(R.id.addressDetailEvent);
        Button buttonJoin = (Button) view.findViewById(R.id.buttonJoin);
        descriptionDetailEvent.setText(mEvent.getDescription());
        addressDetailEvent.setText(mEvent.getAddress());
        Picasso.with(view.getContext()).
                load(RedEventService.API_BASE_URL.concat(mEvent.getImage()))
                .placeholder(R.drawable.image_default)
                .into(imageDetailEvent);

        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("RedEvent",  " click join evnt " + mEvent.getName());
                joinToEvent(mEvent);
            }
        });
        return view;
    }

    public void joinToEvent(Event event){
        List<User> listUser = SugarRecord.listAll(User.class);
        for (User user : listUser) {
            Log.d("RedEvent", " User " +user.getEmail());
            join(event, user);
        }
    }

    public void join(Event event, User user){
        try {

            RedEventService service = RedEventServiceGenerator.createService();

            Call<String> call = service.joinEvent(""+user.getId(), ""+event.getId());

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    int statusCode = response.code();
                    Log.d(MainActivity.class.getSimpleName(), "HTTP status code: " + statusCode);

                    if(response.isSuccessful()) {
                        Intent i = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                        startActivity(i);

                    }else{
                        // Best Practice APIError: https://futurestud.io/blog/retrofit-2-simple-error-handling
                        APIError error = APIError.parseError(response);
                        Log.e(this.getClass().getSimpleName(), "ApiError " + error.getStatus() + ":" + error.getMessage());
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    // Log error here since request failed
                    Log.e(this.getClass().getSimpleName(), "onFailure:" + t.getMessage());
                    t.printStackTrace();
                }

            });

        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), "Exception!" + e.getMessage());
            e.printStackTrace();
        }

        Intent i = new Intent(getActivity().getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}
