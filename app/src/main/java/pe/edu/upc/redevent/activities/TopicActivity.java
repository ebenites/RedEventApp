package pe.edu.upc.redevent.activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

import pe.edu.upc.redevent.R;

/**
 * Basado en https://github.com/paramvir-b/AndroidGridViewCompatLib
 */
public class TopicActivity extends AppCompatActivity {

    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);

        gridView = (GridView) findViewById(R.id.topicsGridview);
        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
        gridView.setAdapter(new ImageAdapter(this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> view, View arg1, int pos, long id) {
                // We need to invalidate all views on 4.x versions
                GridView gridView = (GridView) view;
                gridView.invalidateViews();
            }
        });

    }


    public class ImageAdapter extends BaseAdapter {

        private Context context;

        // references to our images
        private Integer[] thumbIds = {
                R.drawable.topic_1, R.drawable.topic_2, R.drawable.topic_3, R.drawable.topic_4, R.drawable.topic_5,
                R.drawable.topic_6, R.drawable.topic_7, R.drawable.topic_8, R.drawable.topic_9, R.drawable.topic_10,
                R.drawable.topic_11, R.drawable.topic_12, R.drawable.topic_13, R.drawable.topic_14, R.drawable.topic_15,
                R.drawable.topic_16, R.drawable.topic_17, R.drawable.topic_18, R.drawable.topic_19, R.drawable.topic_20
        };

        private String[] thumbLabels = {
                "Música", "Negocios", "Gastronomía", "Comunidad", "Artes",
                "Cine y medios de comunicación", "Deportes y salud", "Salud", "Ciencia y tecnología", "Viajes y actividades al aire libre",
                "Organizaciones y causas benéficas", "Espiritualidad", "Familia y educación", "Vacaciones", "Gobierno",
                "Moda", "Hogar y estilo de vida", "Coches, barcos y aviones", "Aficiones", "Otros"
        };

        public ImageAdapter(Context context) {
            this.context = context;
        }

        public int getCount() {
            return thumbIds.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {

            RelativeLayout itemRelativeLayout;
            ImageView imageView;
            CheckBox checkBox;
            TextView textView;

            if (convertView == null) {

                itemRelativeLayout = new RelativeLayout(context);
//                itemRelativeLayout.setBackgroundColor(Color.RED);

                // ImageView

                imageView = new ImageView(context);
                imageView.setId(new Random().nextInt(50000) + 1);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                RelativeLayout.LayoutParams imageViewLayoutParams = new RelativeLayout.LayoutParams(250, 250);
                imageViewLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                imageView.setLayoutParams(imageViewLayoutParams);

                itemRelativeLayout.addView(imageView);

                // CheckBox

                checkBox = new CheckBox(context);
                checkBox.setClickable(false);
                checkBox.setFocusable(false);

                RelativeLayout.LayoutParams checkBoxLayoutParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                checkBoxLayoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, imageView.getId());
                checkBoxLayoutParams.addRule(RelativeLayout.ALIGN_END, imageView.getId());
                checkBox.setLayoutParams(checkBoxLayoutParams);

                itemRelativeLayout.addView(checkBox);

                // TextView

                textView = new TextView(context);
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(Color.WHITE);
                textView.setTypeface(null, Typeface.BOLD);
                textView.setShadowLayer(2.5f, -1.5f, 1.5f, Color.BLACK);
                textView.setText(thumbLabels[position]);

                RelativeLayout.LayoutParams textViewLayoutParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                checkBoxLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, imageView.getId());
                textView.setLayoutParams(textViewLayoutParams);

                itemRelativeLayout.addView(textView);

            }else{

                itemRelativeLayout = (RelativeLayout)convertView;

                imageView = (ImageView) itemRelativeLayout.getChildAt(0);
                checkBox = (CheckBox) itemRelativeLayout.getChildAt(1);

            }

            GridView gvc = (GridView) parent;
            if (gvc.getChoiceMode() == ListView.CHOICE_MODE_MULTIPLE) {
                SparseBooleanArray checkArray;
                checkArray = gvc.getCheckedItemPositions();

                if (checkArray != null) {

                    int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

                    if (checkArray.get(position)) {
                        checkBox.setChecked(true);
//                        imageView.setPadding(10,10,10,10);
//                        imageView.setBackgroundColor(getColor(R.color.colorPrimaryDark));
                        imageView.animate().setDuration(shortAnimTime).alpha(0.5f);
//                        imageView.setAlpha(0.5f);
                    }else{
                        checkBox.setChecked(false);
//                        imageView.setPadding(10,10,10,10);
//                        imageView.setPadding(0,0,0,0);
//                        imageView.setBackgroundColor(Color.WHITE);
                        imageView.animate().setDuration(shortAnimTime).alpha(1f);
//                        imageView.setAlpha(1f);
                    }
                }

            }

            imageView.setImageResource(thumbIds[position]);
            return itemRelativeLayout;
        }

    }
}
