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
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by akadijevic on 5/4/2017.
 */
public class NewPostActivity extends AppCompatActivity {

    @Bind(R.id.saveButton) Button PostButton;
    @Bind(R.id.upload) Button myUploadButton;
    @Bind(R.id.imageView)ImageView myImageView;
    @Bind(R.id.txtPhotoCaption) EditText txtImageCaption;

    int CAMERA_REQUEST_CODE = 1;
    StorageReference mstorage;
    DatabaseReference mdatabase;
    ProgressDialog mprogress;

   // public  static final String FB_STORAGE_PATH = "image/";
    public  static final String FB_DATABASE_PATH = "image";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        ButterKnife.bind(this);

        mstorage = FirebaseStorage.getInstance().getReference();
        mdatabase =FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);
        mprogress = new ProgressDialog(this);


        setupCamera();
        setupSaveButton();
    }

    /* sets a method for upload button
    * the startActivityforResult method is called passing two parameters
    * @param intent
    * @param CAMERA_REQUEST CODE that has been declared above as "1"
    * */
    private void setupCamera() {
        myUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);

            }
        });

    }

    /*
     * method that is called when the button Post is clicked
     * the user is redirect to MainActivity
     * the bitmap object we have declared in the OnActivityresults method is passed
     */
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


    /*
     * The following code snippet retrieves the photo that the user has captured
     * @param requestCode
     * @param resultCode
     * @param data
     * if the request code is equal to the code declared above and resultcode is valid
     * the progress Dialog is shown with the messages
     * then the Bundle is declared that fetches the data
     * the following code snippet then reduces the size of the photo so that it can be passed back to mainactivity
     * the imageView is then set to displayed the photo captured in the lower quality
     * the filepath is declared to take the current date and Time, it is then stored into a database storage
     * the uploudTask is then declared to take that filepath and reduces the size of the image
     * if the uploadTask is successful the progress dialog is dismissed
     * the new object Photo is then being created passing the two parameters
     * @param txtImageCaption takes the user input and converts it into a string
     * @tasSnapshot.getDownloadUri basically gets the download uri passed into a photo object
     * then we declared a string that will store a unique key of the file and push it into a Firebase database
     * this key will be assigned to the object
     *  if the uploadTask is not successful the toast is displayed
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


            StorageReference filepath = mstorage.child("Photo" +new Date().getTime());

             UploadTask uploadTask = filepath.putBytes(databaos);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    mprogress.dismiss();

                    Photo photoUpload = new Photo(txtImageCaption.getText().toString(), taskSnapshot.getDownloadUrl().toString());

                    String uploadId = mdatabase.push().getKey();
                    mdatabase.child(uploadId).setValue(photoUpload);



                }
            });

                  uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(NewPostActivity.this, "Uploading photo failed.",Toast.LENGTH_LONG).show();
                }
            });


        }

    }
}