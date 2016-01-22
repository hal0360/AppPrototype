package cpit.prototype;

/**
 * Created by user on 18/01/2016.
 */
public class Image_stamp {
    public int image_id;
    public int member_id;
    public String stamp;
    public double duration;

    public Image_stamp(int iid, int mid, String st, double dur){
        image_id = iid;
        member_id = mid;
        stamp = st;
        duration = dur;
    }
}
