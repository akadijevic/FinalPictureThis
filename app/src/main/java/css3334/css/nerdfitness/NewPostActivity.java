package css3334.css.nerdfitness;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.Date;

import static android.R.attr.bitmap;
import static android.R.attr.data;


/**
 * Created by akadijevic on 5/4/2017.
 */
public class NewPostActivity extends AppCompatActivity {
    Button myUploadButton, PostButton;
    ImageView myImageView;
    EditText txtImageCaption;

    int CAMERA_REQUEST_CODE = 1;
    StorageReference mstorage;
    DatabaseReference mdatabase;
    ProgressDialog mprogress;
public  static final String FB_STORAGE_PATH = "image/";
    public  static final String FB_DATABASE_PATH = "image";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        mstorage = FirebaseStorage.getInstance().getReference();
        mdatabase =FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);
        mprogress = new ProgressDialog(this);
        myUploadButton = (Button) findViewById(R.id.upload);
        myImageView = (ImageView) findViewById(R.id.imageView);
        PostButton = (Button) findViewById(R.id.saveButton);
        txtImageCaption = (EditText) findViewById(R.id.txtPhotoCaption);

        setupCamera();
        setupSaveButton();
    }
    /* sets a method for upload button */
    private void setupCamera() {
        myUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);

            }
        });

    }

    private void setupSaveButton(){

        PostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivityIntent = new Intent(v.getContext(), MainActivity.class);
                mainActivityIntent.putExtra("bitmap", bitmap);
                finish();
                startActivity(mainActivityIntent);

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

            Bundle extras = data.getExtras();
            final Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] databaos = baos.toByteArray();

            myImageView.setImageBitmap(bitmap);


          // Uri uri = data.getData();
            StorageReference filepath = mstorage.child("Photo" +new Date().getTime());

             UploadTask uploadTask = filepath.putBytes(databaos);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    mprogress.dismiss();

                    Photo photoUpload = new Photo(txtImageCaption.getText().toString(), taskSnapshot.getDownloadUrl().toString());

                  // Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    String uploadId = mdatabase.push().getKey();
                    mdatabase.child(uploadId).setValue(photoUpload);
                   // Picasso.with(NewPostActivity.this).load(downloadUrl).fit().centerCrop().into(myImageView);
                    //get the camera image


                    /* Uri downloadUri= taskSnapshot.getDownloadUrl();
                    Picasso.with(NewPostActivity.this).load(downloadUri).fit().centerCrop().into(myImageView);
                    Toast.makeText(NewPostActivity.this, "Picture Taken", Toast.LENGTH_LONG).show(); */



                }
            });

                    /* .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            }) */


        }

    }
}