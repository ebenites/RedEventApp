package pe.edu.upc.redevent.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.orm.SugarRecord;

import org.w3c.dom.Text;

import java.util.List;

import pe.edu.upc.redevent.R;
import pe.edu.upc.redevent.activities.MainActivity;
import pe.edu.upc.redevent.models.APIError;
import pe.edu.upc.redevent.models.Event;
import pe.edu.upc.redevent.models.Topic;
import pe.edu.upc.redevent.models.User;
import pe.edu.upc.redevent.services.RedEventService;
import pe.edu.upc.redevent.services.RedEventServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private MainActivity mActivity;

    private TextView txtEmail, txtPreferences;

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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        txtEmail = (TextView) view.findViewById(R.id.txtEmail);
        txtPreferences = (TextView) view.findViewById(R.id.txtPreferences);
        callService();

        return view;
    }

    private void callService(){

        try {

            User user = SugarRecord.first(User.class);
            RedEventService service = RedEventServiceGenerator.createService();
            Call<User> call = service.getUser(user.getId());

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    int statusCode = response.code();
                    Log.d(this.getClass().getSimpleName(), "HTTP status code: " + statusCode);

                    if(response.isSuccessful()) {
                        User user = response.body();
                        txtEmail.setText(user.getEmail());
                        List <Topic> topics = user.getTopics();
                        if (topics != null) {
                            String topicList = "";
                            for (Topic topic: topics){
                                topicList += topic.getName() + " ";
                            }
                            txtPreferences.setText(topicList);
                        } else {
                            txtPreferences.setText("No hay preferencias");
                        }

                    } else {
                        APIError error = APIError.parseError(response);
                        Log.e(this.getClass().getSimpleName(), "ApiError " + error.getStatus() + ":" + error.getMessage());
                        Toast.makeText(mActivity, error.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
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
