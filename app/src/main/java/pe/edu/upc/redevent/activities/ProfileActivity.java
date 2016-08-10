package pe.edu.upc.redevent.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orm.SugarRecord;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pe.edu.upc.redevent.R;
import pe.edu.upc.redevent.models.APIError;
import pe.edu.upc.redevent.models.APIMessage;
import pe.edu.upc.redevent.models.User;
import pe.edu.upc.redevent.services.RedEventService;
import pe.edu.upc.redevent.services.RedEventServiceGenerator;
import pe.edu.upc.redevent.utils.PermissionUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    public static final int CAMERA_PERMISSIONS_REQUEST = 32;
    public static final int CAMERA_IMAGE_REQUEST = 33;
    private static final int GALLERY_IMAGE_REQUEST = 31;

    public static final String FILE_NAME = "photo.jpg";

    private ImageView photoImageView;
    private Button photoButton;
    private TextView fullnameTextView;
    private Button fullnameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Profile");

        photoImageView = (ImageView)findViewById(R.id.photoImageView);
        photoButton = (Button)findViewById(R.id.photoButton);
        fullnameTextView = (TextView) findViewById(R.id.fullnameTextView);
        fullnameButton = (Button)findViewById(R.id.fullnameButton);

        User user = SugarRecord.last(User.class);
        if(user.getPhoto() != null) {
            Picasso.with(this).load(RedEventService.API_BASE_URL + user.getPhoto()).into(photoImageView);
        }

        fullnameTextView.setText(user.getFullname());


    }

    public void selectPhoto(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder
                .setMessage(R.string.profile_select_a_source)
                .setPositiveButton(R.string.profile_select_gallery, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startGalleryChooser();
                    }
                })
                .setNegativeButton(R.string.profile_select_camera, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startCamera();
                    }
                });
        builder.create().show();

    }

    public void startGalleryChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a photo"),
                GALLERY_IMAGE_REQUEST);
    }

    public void startCamera() {
        if (PermissionUtils.requestPermission(this, CAMERA_PERMISSIONS_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getCameraFile()));
            startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
        }
    }

    public File getCameraFile() {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.e("XXXXXXXXXXXXXXX", "requestcode:"+requestCode+"-"+resultCode);
        if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            uploadImage(data.getData());
        }
        else if(requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
            uploadImage(Uri.fromFile(getCameraFile()));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionUtils.permissionGranted(requestCode, CAMERA_PERMISSIONS_REQUEST, grantResults)) {
            startCamera();
        }
    }

    public void uploadImage(Uri uri) {
        if (uri != null) {
            try {
                // scale the image to 800px to save on bandwidth
                Bitmap bitmap = scaleBitmapDown(MediaStore.Images.Media.getBitmap(getContentResolver(), uri), 240);

                // Send photo

                photoImageView.setImageBitmap(bitmap);



                User user = SugarRecord.last(User.class);

                RedEventService service = RedEventServiceGenerator.createService();

//                File file = new File(Environment.getExternalStorageDirectory().getPath()  + "/photo.jpg");
                File file = getCameraFile();

                // create RequestBody instance from file
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                // MultipartBody.Part is used to send also the actual file name
                MultipartBody.Part body = MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

                Call<APIMessage> call = service.upload_photo(user.getId(), body);

                call.enqueue(new Callback<APIMessage>() {
                    @Override
                    public void onResponse(Call<APIMessage> call, Response<APIMessage> response) {
                        int statusCode = response.code();
                        Log.d(MainActivity.class.getSimpleName(), "HTTP status code: " + statusCode);

                        if(response.isSuccessful()) {



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



            } catch (IOException e) {
                Log.d(this.getClass().getSimpleName(), "Image picking failed because " + e.getMessage());
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d(this.getClass().getSimpleName(), "Image picker gave us a null image.");
            Toast.makeText(this, "Image picker gave us a null image.", Toast.LENGTH_LONG).show();
        }
    }

    public Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

}
