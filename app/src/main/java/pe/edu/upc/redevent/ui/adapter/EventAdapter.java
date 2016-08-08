package pe.edu.upc.redevent.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import pe.edu.upc.redevent.R;
import pe.edu.upc.redevent.models.Event;
import pe.edu.upc.redevent.services.RedEventService;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private List<Event> eventList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, category, date;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            category = (TextView) view.findViewById(R.id.category);
            date = (TextView) view.findViewById(R.id.date);
            image = (ImageView) view.findViewById(R.id.event_image);
        }
    }

    public EventAdapter(List<Event> eventList) {
        this.eventList = eventList;
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
                .resize(50, 50)
                .centerCrop()
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}
