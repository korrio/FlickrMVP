package to.marcus.FlickrMVP.network;

import retrofit.Callback;
import retrofit.http.GET;
import to.marcus.FlickrMVP.model.GirlPhotosResponse;

public interface ApiServiceNew {

    @GET("/api/data/%E7%A6%8F%E5%88%A9/100/1/")
    void getGirlPhoto(Callback<GirlPhotosResponse> callback);

}
