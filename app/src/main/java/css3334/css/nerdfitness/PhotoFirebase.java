package css3334.css.nerdfitness;

/**
 * Created by akadijevic on 5/5/2017.
 */
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PhotoFirebase {
    StorageReference myPhotoDbRef;
    public static final String PhotoDataTag = "Photo Data";

    public StorageReference open() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        myPhotoDbRef = storage.getReference(PhotoDataTag);
        return myPhotoDbRef;
    }
    public void close() {

    }
}
