package css3334.css.nerdfitness;

/**
 * Created by akadijevic on 5/5/2017.
 */

/*
 * this class is used to serve as an object in which we store the caption and url as constructors.
 */
public class Photo {

    public String caption;
    public String url;

    public String getCaption() {
        return caption;
    }

    public String getUrl() {
        return url;
    }

    public Photo(String caption, String url) {
        this.caption = caption;
        this.url = url;

    }
    public Photo () {

    }
}
