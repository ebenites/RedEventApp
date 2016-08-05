package pe.edu.upc.redevent.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pe.edu.upc.redevent.R;
import pe.edu.upc.redevent.domain.Event;
import pe.edu.upc.redevent.ui.adapter.EventAdapter;

public class EventsFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventAdapter mAdapter;

    private List<Event> eventList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.event_recycler_view);

        mAdapter = new EventAdapter(eventList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareMovieData();

        return view;
    }


    private void prepareMovieData() {
        Event event = new Event("Danzas contemporaneas", "Baile", new Date());
        eventList.add(event);

        event = new Event("Comiccon", "Categoria 1", new Date());
        eventList.add(event);

        event = new Event("Maraton harry potter", "Categoria 2", new Date());
        eventList.add(event);

        event = new Event("Food truck", "Categoria 3", new Date());
        eventList.add(event);

        mAdapter.notifyDataSetChanged();
    }
}
