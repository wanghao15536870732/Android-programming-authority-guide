package com.example.lab.android.nuc.photogallery.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.lab.android.nuc.photogallery.Base.FlickrFetchr;
import com.example.lab.android.nuc.photogallery.Base.GalleryItem;
import com.example.lab.android.nuc.photogallery.Base.QueryPreferences;
import com.example.lab.android.nuc.photogallery.R;
import com.example.lab.android.nuc.photogallery.Thread.ThumbnailDownloader;
import com.example.lab.android.nuc.photogallery.service.PollService;

import java.util.ArrayList;
import java.util.List;

public class
PhotoGalleryFragment extends Fragment {

    private PhotoAdapter mPhotoAdapter;

    private static final String TAG = "PhotoGalleryFragment";

    private RecyclerView mPhotoRecyclerView;
    private List<GalleryItem> mItems = new ArrayList<>();
    private ThumbnailDownloader<PhotoHolder> mThumbnailDownloader;

    private int mNextPage = 1, mLastPosition;

    private FetchItemsTask mFetchItemsTask;

    private final int MAX_PAGES = 3;

    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        //在Activity重新创建时可以不完全销毁Fragment，以便Fragment可以恢复
        setRetainInstance( true );

        //让fragment接收菜单回调方法
        setHasOptionsMenu( true );

        //execute()会启动AsyncTask
//        new FetchItemsTask().execute();
        updateItems();


        //添加服务代码
        Intent intentservice = PollService.newIntent( getActivity() );
//        getActivity().startService( intentservice );

//        Intent intent = PollService.newIntent( getActivity() );
//        getActivity().startService( intent );

        //Handler默认跟当前线程的Looper相关联
        //将关联主线程的Handler传递给ThumbnailDownloader()
        Handler responseHandler = new Handler();

        //创建线程
        mThumbnailDownloader = new ThumbnailDownloader<>( responseHandler );
        mThumbnailDownloader.setThumbnailDownloadListener(
                new ThumbnailDownloader.ThumbnailDownloadListener<PhotoHolder>() {
                    @Override
                    //shi用个返回的Bitmap执行UI更新操作
                    public void onThumbnailDownloaded(PhotoHolder target, Bitmap thumbnail) {
                        Drawable drawable = new BitmapDrawable( getResources(), thumbnail );
                        target.bindDrawable( drawable );
                    }
                }
        );

        //启动线程
        mThumbnailDownloader.start();

        //要在start()方法之后调用getLooper()方法，这是一种保证线程就绪的处理方式
        mThumbnailDownloader.getLooper();
        Log.i( TAG, "Background thread started" );
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate( R.layout.fragment_photo_gallery, container, false );
        //attachToRoot：是否将root附加到布局文件的根视图上
        mPhotoRecyclerView = (RecyclerView) v.findViewById( R.id.fragment_photo_gallery_recycler_view );

        //添加动态调整网格
//        mPhotoRecyclerView.getViewTreeObserver()
//                .addOnGlobalLayoutListener( new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//                        int columns = mPhotoRecyclerView.getWidth() / 240;
//                        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), columns));
//                        mPhotoRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//
////                        mPhotoRecyclerView.setAdapter(new PhotoAdapter( mItems ));
////                        mPhotoRecyclerView.addOnScrollListener(onButtomListener);
////                        mPhotoRecyclerView.getLayoutManager().scrollToPosition(mLastPosition);
//                    }
//                } );
        mPhotoRecyclerView.setLayoutManager( new GridLayoutManager( getActivity(), 3 ) );
        //在该方法中调用seyAdapter()方法，可以在每次设备旋转时重新生成RecyclerView
        // 可从新为其配置Adapter`
        setupAdapter();
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在该方法中退出线程
        mThumbnailDownloader.quit();
        Log.i( TAG, "Background thread destroyed" );
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu( menu, menuInflater );
        menuInflater.inflate( R.menu.fragment_photo_gallery, menu );
        /**
         * 实现SearchView。OnQueryTextListener监听方法
         */

        ///首先从菜单中取出MenuItem并把它保存在searchItem变量当中
        final MenuItem searchItem = menu.findItem( R.id.menu_item_search );

        //使用getActionView()取出searchView
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d( TAG, "QueryTextSubmit: " + query );

                //储存用户提交的查询信息
                QueryPreferences.setStoreQuery( getActivity(), query );
                //优化隐藏键盘
//                if (searchView != null){
//                    //得到输入管理对象
//                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
//                            .getSystemService( Context.INPUT_METHOD_SERVICE );
//                    if (inputMethodManager != null){
//                        /*
//                            这将让键盘在所有的情况下都被隐藏，但是一般我们在点击搜索按钮后，
//                                输入法都会乖乖的自动隐藏的。
//                         */
//
//                        // 输入法如果是显示状态，那么就隐藏输入法
//                        inputMethodManager.hideSoftInputFromWindow( searchView.getWindowToken(),0 );
//                    }
//                    searchView.onActionViewCollapsed();// 不获取焦点
//                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d( TAG, "QueryTextChange: " + newText );
                return false;
            }
        } );


        MenuItem togglemenuItem = menu.findItem( R.id.menu_item_toggle_polling );
        if (PollService.isServiceAlarmOn( getActivity())){
            togglemenuItem.setTitle( R.string.stop_polling );
        }else {
            togglemenuItem.setTitle( R.string.start_polling );
        }

        /**
         * 优化应用
         * 回调方法设置搜索文本框的值
         */
        searchView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = QueryPreferences.getStoreQuery( getActivity() );
                searchView.setQuery( query, false );
            }
        } );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_ite_clear:
                QueryPreferences.setStoreQuery( getActivity(), null );
                updateItems();
                return true;
            case R.id.menu_item_toggle_polling:
                boolean shouldStartAlerm = !PollService.isServiceAlarmOn( getActivity() );
                PollService.setServiceAlerm( getActivity(),shouldStartAlerm );
                //menu刷新UI的更改更新
                getActivity().invalidateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected( item );
        }
    }

    private void updateItems() {
        String query = QueryPreferences.getStoreQuery( getActivity() );
        mFetchItemsTask = new FetchItemsTask( query );
        mFetchItemsTask.execute();
    }

    //在视图清理的时候调用清理的方法
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThumbnailDownloader.clearQueue();

    }

    //Adapter的配置和关联
    private void setupAdapter() {
        if (isAdded()) {
            mPhotoAdapter = new PhotoAdapter( mItems );
            mPhotoRecyclerView.setAdapter( mPhotoAdapter );
        }
    }

    //来时准备视图层的部分
    private class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

//        private TextView mTitleTextView;

        private ImageView mItemImageView;
        private GalleryItem mGalleryItem;

        public PhotoHolder(View itemView) {
            super( itemView );
            mItemImageView = (ImageView) itemView.findViewById( R.id.fragment_photo_gallery_image_view );
//          mTitleTextView = (TextView) itemView;
            itemView.setOnClickListener( this );
        }

//        public void bindGalleryItem(GalleryItem item){
//            Picasso.with( getActivity() )
//                    .load( item.getUrl() )
//                    .placeholder( R.drawable.bill_up_close )
//                    .into( mItemImageView );
//        }


        public void bindDrawable(Drawable drawable) {
            mItemImageView.setImageDrawable( drawable );
        }

        public void bindGalleryItem(GalleryItem galleryItem){
            mGalleryItem = galleryItem;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent( Intent.ACTION_VIEW,mGalleryItem.getPhotoPageUri() );
            startActivity( intent );
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

        private List<GalleryItem> mGalleryItems;

        //构建构造器用与外部数据提取数据
        public PhotoAdapter(List<GalleryItem> galleryItems) {
            mGalleryItems = galleryItems;
        }

        @NonNull
        @Override
        public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            TextView textView = new TextView(getActivity());
//            return new PhotoHolder(textView);
            //改为图片的bind
            LayoutInflater inflater = LayoutInflater.from( getActivity() );
            ///实例化gallery_item布局
            View view = inflater.inflate( R.layout.gallery_item, parent, false );
            return new PhotoHolder( view );
        }

        @Override
        public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {

            GalleryItem galleryItem = mGalleryItems.get( position );
//            holder.bindGalleryItem(galleryItem);
            //改为绑定图片
            //先去的照片的信息
            Drawable placeholder = getResources().getDrawable( R.drawable.bill_up_close );
            //再绑定照片
            holder.bindDrawable( placeholder );
            holder.bindGalleryItem(galleryItem);
            //传入设置图片的PhotoHolder和GalleryItem的URL
            mThumbnailDownloader.queueThumbnail( holder, galleryItem.getUrl() );
        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class FetchItemsTask extends AsyncTask<Void, Void, List<GalleryItem>> {

        private String mQuery;

        public FetchItemsTask(String qurey) {
            mQuery = qurey;
        }

        @Override
        protected List<GalleryItem> doInBackground(Void... voids) {
//            try {
//                String result = new FlickrFetchr().getUrlString("https://www.bignerdranch.com");
//                Log.i(TAG,"Fetched contents of URL:" + result);
//            }catch (IOException e){
//                Log.e(TAG,"Failed to fetch URL: ",e);
//            }
//            return new FlickrFetchr().fetchItems();
//            return null;
            //将GalleryItem对象List传递给onPostExecute()方法使用
//            String query = "robot";

            if (mQuery == null) {
                //如果搜索框内容为空,就会默认加载最新的公共图片
                return new FlickrFetchr().fetchRecentPhotos();
            } else {
                ///如果搜索框为非空，执行搜索任务
                return new FlickrFetchr().searchPhotos( mQuery );
            }
        }

        //该方法在主线程内，而非后台线程上运行，所以在该方法按上面更新UI比较安全
        @Override
        protected void onPostExecute(List<GalleryItem> galleryItems) {
            //放入mItems变量
            mItems = galleryItems;
            //调用setupAdapter()方法更新RecyclerView视图的adapter
            setupAdapter();
        }
    }

    /*
    分页加载
     */
//    private RecyclerView.OnScrollListener onButtomListener = new RecyclerView.OnScrollListener() {
//        @Override
//        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//            super.onScrollStateChanged( recyclerView, newState );
//            GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
//            mLastPosition = layoutManager.findLastVisibleItemPosition();
//            if (mPhotoAdapter == null) {
//                return;
//            }
//            if (newState == RecyclerView.SCROLL_STATE_IDLE
//                    && mLastPosition >= mPhotoAdapter.getItemCount() - 1) {
//                if (mFetchItemsTask.getStatus() == AsyncTask.Status.FINISHED) {
//                    mNextPage++;
//                    if (mNextPage < MAX_PAGES) {
//                        Toast.makeText( getActivity(), "waiting to load...", Toast.LENGTH_SHORT ).show();
//                        updateItems( mNextPage );
//                    } else {
//                        Toast.makeText( getActivity(), "This is the end.", Toast.LENGTH_SHORT ).show();
//                    }
//                }
//            }
//
//        }
//
//        @Override
//        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//            super.onScrolled( recyclerView, dx, dy );
//        }
//    };
}
