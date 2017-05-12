package css3334.css.nerdfitness;

/**
 * Created by akadijevic on 5/6/2017.
 */
import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
public class ImageListAdapter extends ArrayAdapter<Photo> {
    private Activity context;
    private int resource;
    private List<Photo> listImage;

/*
 * @param Activity Context
 * @param resource
 * @param List
 * the code gets the local activity and resource
 * the listImage declared above stores the Photo object that contains constructors url and caption
 */

    public ImageListAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Photo> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        listImage = objects;
    }

    /*
     *this method passed creates a view from an XML file that is then displayed on the main activity
     *the caption text is set to get the the caption through listimage variable that stores the objects from the Photo Class
     * Glide is an embedded image library that gets te current activity, and loads the image through the listImage
     * it then loads it into the imgview that we have in the layout
     * this snippet of code has been taken from: https://www.youtube.com/watch?v=akIrsTB2zIQ
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View v = inflater.inflate(resource,null);
        TextView tvCaption = (TextView) v.findViewById(R.id.tvImageCaption);
        ImageView img = (ImageView) v.findViewById(R.id.imgView);
        tvCaption.setText(listImage.get(position).getCaption());
        Glide.with(context).load(listImage.get(position).getUrl()).into(img);
        return v;
    }
}
