package css3334.css.nerdfitness;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.content.Intent;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by akadijevic on 5/4/2017.
 */
public class NewPostActivity extends AppCompatActivity {
    Button myUploadButton;
    ImageView myImageView;
    Intent cameraPickerIntent;

    Camera camera;

    int CAMERA_REQUEST_CODE = 1;

    DatabaseReference mstorage;
    ProgressDialog mprogress;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        mstorage = FirebaseDatabase.getInstance().getReference();
        mprogress = new ProgressDialog(this);

        myUploadButton = (Button) findViewById(R.id.upload);
        myImageView = (ImageView) findViewById(R.id.imageView);
/* sets a method for upload button */

        myUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cameraintent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(cameraintent, CAMERA_REQUEST_CODE);


            }
        });

    }

    /**
     * The following code snippet retrieves the photo that the user has captured:
     * From -- https://developers.google.com/places/android-api/placepicker
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
          mprogress.setMessage("Uploading Image");
            mprogress.show();

            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ImageView image = (ImageView)

            Uri uri = data.getData();

            //DatabaseReference filepath = mstorage.child("Photos").child(uri.getLastPathSegment())
        }
    }
}