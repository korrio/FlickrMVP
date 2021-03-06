package to.marcus.FlickrMVP.ui.presenter;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import to.marcus.FlickrMVP.data.PhotoCache;
import to.marcus.FlickrMVP.data.PhotoFactory;
import to.marcus.FlickrMVP.data.event.GirlImagesReceivedEvent;
import to.marcus.FlickrMVP.data.event.ImagesGirlRequestedEvent;
import to.marcus.FlickrMVP.data.event.ImagesReceivedEvent;
import to.marcus.FlickrMVP.data.interactor.PhotoInteractor;
import to.marcus.FlickrMVP.model.GirlPhotosResponse;
import to.marcus.FlickrMVP.model.Photo;
import to.marcus.FlickrMVP.model.Photos;
import to.marcus.FlickrMVP.network.PhotoHandler;
import to.marcus.FlickrMVP.ui.adapter.PhotoRecyclerAdapter;
import to.marcus.FlickrMVP.ui.views.PhotosView;

/**
* Created by marcus on 15/04/15
*/

public class RecentPresenterImpl implements RecentPresenter {
    private final String TAG = RecentPresenterImpl.class.getSimpleName();
    private PhotoCache photoCache;
    private final Bus bus;
    private PhotosView view;
    private Photos defaultPhotosArray;
    PhotoHandler mResponseHandler;
    private PhotoInteractor photoInteractor;

    public RecentPresenterImpl(PhotosView view, Bus bus, PhotoCache photoCache, PhotoInteractor photoInteractor){
        this.photoCache = photoCache;
        this.bus = bus;
        this.view = view;
        this.photoInteractor = photoInteractor;
        initResponseHandler();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        if(savedInstanceState == null){
            initInstanceState();
            requestNetworkPhotos();
            view.showProgressBar();
        }else{
            restoreInstanceState(savedInstanceState);
        }
        view.initSwipeRefreshWidget();
    }

    @Override
    public void onSaveInstanceState(Bundle out){
        Photos.putParcelableArray(out, defaultPhotosArray);
    }

    private void restoreInstanceState(Bundle savedInstanceState){
        defaultPhotosArray = Photos.getParcelableArray(savedInstanceState);
        initGridViewAdapter();
    }

    @Override
    public void onResume(){
        bus.register(this);
    }

    @Override
    public void onPause(){
        photoInteractor.savePhotos();
        bus.unregister(this);
    }

    @Override
    public void onRefresh(){requestNetworkPhotos();}

    @Override
    public void onDestroy(){
        mResponseHandler.quit();
    }

    @Override
    public void onDestroyView(){
        mResponseHandler.clearQueue();
    }

    @Override
    public void requestNetworkPhotos(){
        //notify our ApiRequestHandler
        //bus.post(new ImagesRequestedEvent());
        bus.post(new ImagesGirlRequestedEvent());
    }

    @Subscribe
    public void onGirlImagesArrayReceived(GirlImagesReceivedEvent event){

        ArrayList<Photo> photoList = new ArrayList<>();
        //private Photo(String id, String owner, String secret, String server, int farm, String title, int ispublic, int isfriend, int isfamily, String url_s){


        for (GirlPhotosResponse.ResultsEntity result : event.getResult()) {
            Photo photo = new Photo(result.getObjectId(),result.getWho(),"","",1,result.getWho(),1,1,1,result.getUrl());
            photoList.add(photo);
        }

        this.defaultPhotosArray.setPhotos(photoList);
        initGridViewAdapter();
        if(view.isSwipeRefreshing()){
            view.hideSwipeRefreshWidget();
        }
        view.hideProgressBar();
    }

    @Subscribe
    public void onImagesArrayReceived(ImagesReceivedEvent event){
        this.defaultPhotosArray.setPhotos(event.getResult());
        initGridViewAdapter();
        if(view.isSwipeRefreshing()){
            view.hideSwipeRefreshWidget();
        }
        view.hideProgressBar();
    }

    @Override
    public void onNetworkPhotoSelected(String url){
        view.showWebViewPhotoFragment(url);
    }

    private void initInstanceState(){
        defaultPhotosArray = PhotoFactory.Photos.initDefaultPhotosArray();
        defaultPhotosArray.setPhotos(new ArrayList<Photo>());
    }

    private void initResponseHandler(){
        mResponseHandler = new PhotoHandler<android.widget.ImageView>(new Handler(), photoCache);
        mResponseHandler.start();
        mResponseHandler.getLooper();
        //listen for incoming images sent back to main handler thread
        mResponseHandler.setListener(new PhotoHandler.Listener<android.widget.ImageView>(){
            public void onPhotoDownloaded(android.widget.ImageView imageView, Bitmap thumbnail){
                imageView.setImageBitmap(thumbnail);
            }
        });
    }

    private void initGridViewAdapter(){
        view.setGridViewAdapter(
            new PhotoRecyclerAdapter(view.getContext()
            ,defaultPhotosArray.getPhotos()
            ,mResponseHandler
            ,new PhotoRecyclerAdapter.RecyclerViewItemClickListener(){
                @Override
                public void onItemClick(View v, int position) {
                    Photo photo = view.getAdapter().getItem(position);
                    //Record to history
                    photoInteractor.addPhoto(photo);
                    onNetworkPhotoSelected(photo.getBigUrl());
                }
            })
        );
    }

}