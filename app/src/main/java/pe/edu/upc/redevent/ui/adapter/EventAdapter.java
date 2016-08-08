package pe.edu.upc.redevent.ui.adapter;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pe.edu.upc.redevent.R;
import pe.edu.upc.redevent.models.Event;
import pe.edu.upc.redevent.services.RedEventService;
import pe.edu.upc.redevent.ui.fragments.EventDetailMainFragment;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private AppCompatActivity mActivity;
    private List<Event> eventList;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, category, date;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            category = (TextView) view.findViewById(R.id.category);
            date = (TextView) view.findViewById(R.id.date);
            image = (ImageView) view.findViewById(R.id.event_image);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d("RedEvent", " position " + eventList.get(this.getAdapterPosition()).getName());

            FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
            EventDetailMainFragment fragment = new EventDetailMainFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("EVENT", eventList.get(this.getAdapterPosition()));
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();

        }
    }

    public EventAdapter(List<Event> eventList, AppCompatActivity mActivity) {
        this.eventList = eventList;
        this.mActivity = mActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.title.setText(event.getName());
        holder.category.setText(String.valueOf(event.getMaxattendees()));
        holder.date.setText(event.getStartdate());
        Picasso.with(holder.itemView.getContext()).
                load(RedEventService.API_BASE_URL.concat(event.getImage()))
                .placeholder(R.drawable.image_default)
                .resizeDimen(R.dimen.list_detail_image_size, R.dimen.list_detail_image_size)
                .centerInside()
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}
