package to.marcus.FlickrMVP.modules;

import android.content.Context;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import to.marcus.FlickrMVP.data.PhotoCache;
import to.marcus.FlickrMVP.network.ApiEndpoint;
import to.marcus.FlickrMVP.network.ApiEndpointNew;
import to.marcus.FlickrMVP.network.ApiService;
import to.marcus.FlickrMVP.network.ApiServiceNew;
import to.marcus.FlickrMVP.ui.views.base.BaseApplication;

/**
 * A module for Android-specific dependencies which require a {@link Context} or
 * {@link android.app.Application} to create.
 */

@Module(
        injects = BaseApplication.class
       ,includes = InteractorModule.class
       )

public class ApplicationModule {
    private BaseApplication application;

    public ApplicationModule(BaseApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public PhotoCache provideCache(){return new PhotoCache();}

    @Provides
    @Singleton
    public Bus provideBus() {
        return new Bus();
    }

    @Provides
    @Singleton
    public ApiEndpoint provideEndPoint() {
        return new ApiEndpoint();
    }

    @Provides
    @Singleton
    public ApiEndpointNew provideEndPointNew() {
        return new ApiEndpointNew();
    }

    @Provides
    @Singleton
    public BaseApplication provideApplication(){
        return application;
    }

    @Provides
    public Context provideApplicationContext(){return application.getApplicationContext();}

    @Provides
    @Singleton
    public ApiService provideApiService(ApiEndpoint endpoint){
        return new RestAdapter.Builder()
                .setEndpoint(endpoint)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build()
                .create(ApiService.class);
    }

    @Provides
    @Singleton
    public ApiServiceNew provideApiServiceNew(ApiEndpointNew endpoint){
        return new RestAdapter.Builder()
                .setEndpoint(endpoint)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build()
                .create(ApiServiceNew.class);
    }
}
