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



    public ImageListAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Photo> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        listImage = objects;
    }

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
