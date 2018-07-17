package com.example.lab.android.nuc.locatr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.SupportActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class LocatrFragment extends SupportMapFragment {
    private static final String TAG = "LocatrFragment";

    private ProgressBar mProgressBar;
//    private ImageView mImageView;
    private GoogleApiClient mClient;

    private GoogleMap mMap;

    private Bitmap mMapImage;
    private GalleryItem mMapItem;
    private Location mCurrentLocation;

    public static LocatrFragment newInstance(){
        return new LocatrFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setHasOptionsMenu( true );

        mClient = new GoogleApiClient.Builder( getActivity() )
                .addApi( LocationServices.API)
                .addConnectionCallbacks( new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        //更新
                        getActivity().invalidateOptionsMenu();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                } )
                .build();
        getMapAsync( new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                updateUI();
            }
        } );
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_locate:
                findImage();
                return true;
            default:
                return super.onOptionsItemSelected( item );
        }
    }

//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate( R.layout.fragment_locatr,container,false );
//        mImageView = (ImageView) view.findViewById( R.id.image );
//        mProgressBar = (ProgressBar) view.findViewById( R.id.locatr_progressBar );
//        mProgressBar.setMax( 100 );
//        return view;
//    }


    @Override
    public void onStart() {
        super.onStart();
        mClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mClient.disconnect();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu( menu, inflater );
        inflater.inflate( R.menu.fragment_locatr,menu );

        MenuItem searchItem = menu.findItem( R.id.action_locate );
        searchItem.setEnabled( mClient.isConnected() );

    }

    private void findImage(){
        LocationRequest request = LocationRequest.create();
        request.setPriority( LocationRequest.PRIORITY_HIGH_ACCURACY );
        request.setNumUpdates( 1 );
        request.setInterval( 0 );
        LocationServices.FusedLocationApi
                .requestLocationUpdates( mClient, request, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Log.e( TAG,"Got a fix " + location );
                        new SearchTask().execute( location );
                    }
                } );
    }

    private class SearchTask extends AsyncTask<Location,Void,Void>{
        private GalleryItem mGalleryItem;
        private Bitmap mBitmap;

        private Location mLocation;

        @Override
        protected Void doInBackground(Location... locations) {
            mLocation = locations[0];
            FlickrFetchr flickr = new FlickrFetchr();
            List<GalleryItem> items = flickr.searchPhotos(locations[0] );

            if (items.size() == 0){
                return null;
            }
            mGalleryItem = items.get( 0 );

            try{
                byte[] bytes = flickr.getUrlBytes( mGalleryItem.getUrl() );
                mBitmap = BitmapFactory.decodeByteArray( bytes,0,bytes.length );
            }catch (IOException e){
              Log.e( TAG,"Unable to download bitmap",e );
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            mImageView.setImageBitmap( mBitmap);
            //返回数据后，保存到变量当中
            mMapImage = mBitmap;
            mMapItem = mGalleryItem;
            mCurrentLocation = mLocation;

            updateUI();
        }
    }

    private void updateUI(){
        //如果有空的指数的话
        if (mMap == null || mMapImage == null){
            return;
        }
        LatLng itemPoint = new LatLng( mMapItem.getLat(),mMapItem.getLon());
        LatLng myPoint = new LatLng(
                mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());


        BitmapDescriptor itemBitmap = BitmapDescriptorFactory.fromBitmap( mMapImage );
        MarkerOptions itemMarker = new MarkerOptions()
                .position( itemPoint )
                .icon( itemBitmap );
        MarkerOptions myMarker = new MarkerOptions()
                .position( myPoint );

        mMap.clear();
        mMap.addMarker( itemMarker );
        mMap.addMarker( myMarker );

        LatLngBounds latLngBounds = LatLngBounds.builder()
                .include( itemPoint )
                .include( myPoint )
                .build();
        int margin = getResources().getDimensionPixelSize( R.dimen.map_inset_margin );
        CameraUpdate updata = CameraUpdateFactory.newLatLngBounds( latLngBounds,margin );
        mMap.animateCamera( updata );
    }

}
