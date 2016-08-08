package pe.edu.upc.redevent.ui.adapter;

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

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private List<Event> eventList;

    public interface OnItemClickListener {
        void onItemClick(Event event);
    }

    private final OnItemClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, category, date;
        public ImageView image;
        private Button btnSeeDetail;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            category = (TextView) itemView.findViewById(R.id.category);
            date = (TextView) itemView.findViewById(R.id.date);
            image = (ImageView) itemView.findViewById(R.id.event_image);
            btnSeeDetail = (Button) itemView.findViewById(R.id.btnSee);
        }
    }

    public EventAdapter(List<Event> eventList, OnItemClickListener listener) {
        this.eventList = eventList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Event event = eventList.get(position);
//        holder.bind(event, listener);
        holder.title.setText(event.getName());
        holder.category.setText(String.valueOf(event.getTopic().getName()));
        holder.date.setText(getStartdateFormatted(event.getStartdate()));
        holder.btnSeeDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(event);
            }
        });

        Picasso.with(holder.itemView.getContext()).
                load(RedEventService.API_BASE_URL.concat(event.getImage()))
                .placeholder(R.drawable.image_default)
                .resizeDimen(R.dimen.list_detail_image_size, R.dimen.list_detail_image_size)
                .centerInside()
                .into(holder.image);
    }

    public String getStartdateFormatted(String startdate){
        DateFormat ff = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat fi = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date date = fi.parse(startdate);
            return ff.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}
