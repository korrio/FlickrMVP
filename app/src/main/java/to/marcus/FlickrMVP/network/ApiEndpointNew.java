package to.marcus.FlickrMVP.network;

import retrofit.Endpoint;

public class ApiEndpointNew implements Endpoint {
    private static final String ENDPOINT = "http://gank.avosapps.com/";

    @Override
    public String getUrl() {
        return ENDPOINT;
    }

    @Override
    public String getName(){
        return "Gank IO";
    }
}
