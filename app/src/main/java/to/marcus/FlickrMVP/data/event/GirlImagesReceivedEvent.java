package to.marcus.FlickrMVP.data.event;

import android.util.Log;

import java.util.List;

import to.marcus.FlickrMVP.model.GirlPhotosResponse;

/**
 * Created by marcus on 22/04/15
 */

public class GirlImagesReceivedEvent {

    private static final String TAG = GirlImagesReceivedEvent.class.getSimpleName();
    private List<GirlPhotosResponse.ResultsEntity> result;

    public GirlImagesReceivedEvent(List<GirlPhotosResponse.ResultsEntity> result){
        Log.i(TAG, "arraylist for presenter");
        this.result = result;
    }

    public List<GirlPhotosResponse.ResultsEntity> getResult(){return result;}
}