package pe.edu.upc.redevent.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.redevent.R;
import pe.edu.upc.redevent.activities.MainActivity;
import pe.edu.upc.redevent.models.APIError;
import pe.edu.upc.redevent.models.Event;
import pe.edu.upc.redevent.models.User;
import pe.edu.upc.redevent.services.RedEventService;
import pe.edu.upc.redevent.services.RedEventServiceGenerator;
import pe.edu.upc.redevent.ui.adapter.EventAdapter;
import pe.edu.upc.redevent.ui.adapter.MyEventAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyEventsFragment extends Fragment {

    private MainActivity mActivity;
    private RecyclerView recyclerView;
    private MyEventAdapter mAdapter;

    private List<Event> eventList = new ArrayList<>();
    private User mUser;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            mActivity = (MainActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_myevents, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.my_event_recycler_view);
        mUser = SugarRecord.first(User.class);

        mAdapter = new MyEventAdapter(eventList, new MyEventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Event item) {
                showDetail(item);
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

//        prepareMovieData();

        callRequest();

        return view;
    }

    private void showDetail(Event event) {
        FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
        MyEventDetailFragment f = MyEventDetailFragment.getInstance(event, mUser.getId());
        ft.replace(R.id.flContent, f);
        ft.commit();
    }

    private void callRequest(){
        try {

            RedEventService service = RedEventServiceGenerator.createService();
            Call<List<Event>> call = service.getMyEvents(mUser.getId());

            call.enqueue(new Callback<List<Event>>() {
                @Override
                public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                    int statusCode = response.code();
                    Log.d(this.getClass().getSimpleName(), "HTTP status code: " + statusCode);

                    if(response.isSuccessful()) {
                        List<Event> events = response.body();
                        Log.d(this.getClass().getSimpleName(), "Events: " + events.size());
                        if (events.size() > 0){
                            for (Event event: events) {
                                eventList.add(event);
                                mAdapter.notifyDataSetChanged();
                            }
                        }

                    } else {
                        APIError error = APIError.parseError(response);
                        Log.e(this.getClass().getSimpleName(), "ApiError " + error.getStatus() + ":" + error.getMessage());
                        Toast.makeText(mActivity, error.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<List<Event>> call, Throwable t) {
                    // Log error here since request failed
//                    showProgress(false);

                    Log.e(this.getClass().getSimpleName(), "onFailure:" + t.getMessage());
                    Toast.makeText(mActivity, t.getMessage(), Toast.LENGTH_LONG).show();
                    t.printStackTrace();
                }

            });

        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), "Exception!" + e.getMessage());
            Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

}
