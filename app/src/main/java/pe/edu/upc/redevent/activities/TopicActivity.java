package pe.edu.upc.redevent.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

import pe.edu.upc.redevent.R;
import pe.edu.upc.redevent.adapters.ImageAdapter;
import pe.edu.upc.redevent.models.APIError;
import pe.edu.upc.redevent.models.APIMessage;
import pe.edu.upc.redevent.models.Topic;
import pe.edu.upc.redevent.models.User;
import pe.edu.upc.redevent.services.RedEventService;
import pe.edu.upc.redevent.services.RedEventServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Basado en https://github.com/paramvir-b/AndroidGridViewCompatLib
 */
public class TopicActivity extends AppCompatActivity {

    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);

        initTopicsGridVIew();
    }

    private void initTopicsGridVIew(){
        try {

            RedEventService service = RedEventServiceGenerator.createService();

            Call<List<Topic>> call = service.getTopics();

            call.enqueue(new Callback<List<Topic>>() {
                @Override
                public void onResponse(Call<List<Topic>> call, Response<List<Topic>> response) {

                    int statusCode = response.code();
                    Log.d(MainActivity.class.getSimpleName(), "HTTP status code: " + statusCode);

                    if(response.isSuccessful()) {

                        List<Topic> topics = response.body();

                        gridView = (GridView) findViewById(R.id.topicsGridview);
                        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
                        gridView.setAdapter(new ImageAdapter(getApplication(), topics));

                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> view, View arg1, int pos, long id) {
                                // We need to invalidate all views on 4.x versions
                                GridView gridView = (GridView) view;
                                gridView.invalidateViews();
                            }
                        });

                    }else{
                        // Best Practice APIError: https://futurestud.io/blog/retrofit-2-simple-error-handling
                        APIError error = APIError.parseError(response);
                        Log.e(this.getClass().getSimpleName(), "ApiError " + error.getStatus() + ":" + error.getMessage());
                        Toast.makeText(getApplication(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<List<Topic>> call, Throwable t) {
                    // Log error here since request failed
                    Log.e(this.getClass().getSimpleName(), "onFailure:" + t.getMessage());
                    Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_LONG).show();
                    t.printStackTrace();
                }

            });

        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), "Exception!" + e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void savePreferences(View view){

        SparseBooleanArray checkArray = gridView.getCheckedItemPositions();

        if(checkArray.size() == 0){
            Toast.makeText(getApplication(), R.string.topic_empty_topics_selected_message, Toast.LENGTH_LONG).show();
            return;
        }

        List<Long> mytopics = new ArrayList<>();
        for (int i=0; i<gridView.getAdapter().getCount();i++){
            if (checkArray.get(i)) {
                ImageAdapter imageAdapter = (ImageAdapter)gridView.getAdapter();
                Topic topic = imageAdapter.getTopic(i);
                mytopics.add(topic.getId());
            }
        }

        Log.d(this.getClass().getSimpleName(), "Topics: " + mytopics);

        // Get last user loggind
        User user = SugarRecord.last(User.class);

        try {

            RedEventService service = RedEventServiceGenerator.createService();

            Call<APIMessage> call = service.savePreferences(user.getId(), mytopics);

            call.enqueue(new Callback<APIMessage>() {
                @Override
                public void onResponse(Call<APIMessage> call, Response<APIMessage> response) {

                    int statusCode = response.code();
                    Log.d(MainActivity.class.getSimpleName(), "HTTP status code: " + statusCode);

                    if(response.isSuccessful()) {

                        APIMessage apiMessage = response.body();
                        Log.d(TopicActivity.class.getSimpleName(), "APIMessage:" + apiMessage.getMessage());

                        startActivity(new Intent(TopicActivity.this, MainActivity.class));
                        finish();

                    }else{
                        // Best Practice APIError: https://futurestud.io/blog/retrofit-2-simple-error-handling
                        APIError error = APIError.parseError(response);
                        Log.e(this.getClass().getSimpleName(), "ApiError " + error.getStatus() + ":" + error.getMessage());
                        Toast.makeText(getApplication(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<APIMessage> call, Throwable t) {
                    // Log error here since request failed
                    Log.e(this.getClass().getSimpleName(), "onFailure:" + t.getMessage());
                    Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_LONG).show();
                    t.printStackTrace();
                }

            });

        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), "Exception!" + e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

}
