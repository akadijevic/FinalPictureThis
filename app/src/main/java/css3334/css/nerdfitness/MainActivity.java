package css3334.css.nerdfitness;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private List<Photo> imgList;
    private ImageListAdapter adapter;
    private ProgressDialog progressDialog;
    @Bind(R.id.listViewImage) ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);

        ButterKnife.bind(this);
        imgList = new ArrayList<>();

          /*show progress dialog List image loading */
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait while loading");
        progressDialog.show();

        /*
         * The next following three lines set up an icon to be visible in the app bar
         */
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_action_logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        setupFirebaseDataChange();
        checkUserAuthenticated();
        setupFloatingButton();


    }

    /*
    * the following method is called by on create method
    * when floating button clicked, new activity will start
    * @param getContext returns the context of the view that is currently running
    * @param NewPostActivity reffers to the new activity started by the onClickListener
    */

    private void setupFloatingButton() {

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newPost = new Intent(view.getContext(), NewPostActivity.class);
                finish();
                startActivity(newPost);
            }
        });
    }
    /*
    * This method initilizes the DatabaseReference
    * */
    private void setupFirebaseDataChange() {

        mDatabase = FirebaseDatabase.getInstance().getReference(NewPostActivity.FB_DATABASE_PATH);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                progressDialog.dismiss();

                /*Loop that is getting image data from the database */
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Photo img = snapshot.getValue(Photo.class);
                    imgList.add(img);
                }

                //initialize adapter
                adapter = new ImageListAdapter(MainActivity.this, R.layout.image_item, imgList);
                //setting adapater for the list view
                lv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }

    /*
     * this method checks whether there is a user signed in already
     * first, the object for Firebase is declared
     * second, the mAuthListener is initialized
     * if there is not a user signed in, the Login activity is prompted
     *  @param getBaseContext returns the context of the view that is currently running
     * @param LoginActivity
     */
    private void checkUserAuthenticated() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {

                    Log.d("CSS3334","onAuthStateChanged - User NOT is signed in");
                    Intent signInIntent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(signInIntent);
                }
            }
        };
    }

    /*
     * this method gets a user via mAuth and signs out the current user
     */
   public void Signout() { mAuth.signOut(); }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*
     * method that handles when the action bar items are clicked
     * the method gets the item action bar items by their id declared in a layout
     * When signout button clicked, the method signout() is called
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

         if (id ==R.id.action_signout){
            Signout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* This method initializes the authentication listener */
    @Override
    public void onStart() {

        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    /*discontinues the authentication
    * removing a listener
    * */
    @Override
    public void onStop() {

        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
