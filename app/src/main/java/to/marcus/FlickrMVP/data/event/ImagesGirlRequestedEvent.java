package to.marcus.FlickrMVP.data.event;

import android.util.Log;

/**
 * Created by marcus on 4/20/2015
 */

public class ImagesGirlRequestedEvent {
    private final static String TAG = ImagesGirlRequestedEvent.class.getSimpleName();
    private String result;

    //GetRecent
    public ImagesGirlRequestedEvent(){
        Log.i(TAG, "GetGirlPhotos requested Event");
    };

    public String getResult(){
        return result;
    }
}
