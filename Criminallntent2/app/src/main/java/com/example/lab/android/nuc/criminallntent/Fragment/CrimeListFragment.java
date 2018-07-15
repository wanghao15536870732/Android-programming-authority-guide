package com.example.lab.android.nuc.criminallntent.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lab.android.nuc.criminallntent.crime.Crime;
import com.example.lab.android.nuc.criminallntent.crime.CrimeLab;
import com.example.lab.android.nuc.criminallntent.Activity.CrimePagerActivity;
import com.example.lab.android.nuc.criminallntent.R;
import com.example.lab.android.nuc.criminallntent.utils.PictureUtils;

import java.util.List;

import javax.security.auth.callback.Callback;


public class CrimeListFragment extends Fragment {

    private RecyclerView mCrimeRecyclerView;

    private int mIndex;

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private static final int REQUEST_CRIME_ITEM = 2;


    private final int REQUEST_CRIME = 1;

    private CrimeAdapter mAdapter;

    private boolean mSubtitleVisible;


    //首先要定义一个成员变量，存放实现Callbacks的对象
    // ，然后托管activity强制类型转换为Callback对象并赋值给Callbacks类型变量
    private Callbacks mCallback;

    /**
     * 添加接口
     */
    public interface Callbacks{
        void onCrimeSelected(Crime crime);
    }

    private TextView mNoCrimetextView;
    private Button mNoCrimeButton;

    //activity赋值是在Fragment的生命周期方法中处理的
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallback = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //让fragment知道CrimeListFragment需接受选线个菜单回调
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //创建RecyclerView视图
        View view = inflater.inflate(R.layout.fragment_crime_list,container,false);

        mNoCrimetextView = (TextView) view.findViewById( R.id.no_crime_textview );
        mNoCrimeButton = (Button) view.findViewById( R.id.no_crime_add_button );
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.addItemDecoration( new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));

        //如果没有LayoutManager的支持，不仅Recycler无法工作，还会导致应用崩溃，
        //所以，RecyclerView视图创建完成之后，就立即传给了LayoutManager对象
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //如果saveInstanceState数据存存在的话，证明前一个活动被摧毁，只是将数据取出来
        if (savedInstanceState != null){
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        //更新UI界面
        updateUI();
        return view;
    }



    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Crime mCrime;

        private TextView mDateTextView;
        private TextView mTitleTextView;
        private CheckBox mSolvedCheckBox;
        private ImageView mImageView;

        public CrimeHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.
                    findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView = (TextView) itemView.
                    findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox = (CheckBox) itemView.
                    findViewById(R.id.list_item_crime_solved_check_box);
            mImageView = (ImageView) itemView.findViewById( R.id.crime_photo );
        }


        @SuppressLint("SetTextI18n")
        public void bindCrime(Crime crime){
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            String date = (String) DateFormat.format("EEEE, MMM dd, yyyy", mCrime.getDate());
            int hour =  mCrime.getHour();
            int minute = mCrime.getMinute();
            mDateTextView.setText("日期："  + date + "  时间：" + hour + " : " + minute);

            mSolvedCheckBox.setChecked(mCrime.isSolved());

        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(getActivity(),mCrime.getTitle() + "clicked!",
//                    Toast.LENGTH_SHORT).show();

            /***
             * 挑战练习
             */
            CrimeLab crimeLab = CrimeLab.get( getActivity() );
            mIndex = crimeLab.getCrimeIndex( mCrime );
            //
            Intent intent = CrimePagerActivity.newIntent(getActivity(),mCrime.getId());
            startActivityForResult(intent,REQUEST_CRIME_ITEM);
            /*
             平板需要更改的地方
            */
//            mCallback.onCrimeSelected(mCrime );

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       switch (requestCode ){
           case REQUEST_CRIME:

               break;
           case REQUEST_CRIME_ITEM:

               break;


       }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes){
            mCrimes = crimes;
        }

        @Override
        //RecyclerView需要新的View视图来显示列表时,会调用onCreateViewHolder()方法
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //创建View视图。然后封装到ViewHolder当中，此时，RecyclerView并不要求封装视图加载数据
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_crime,parent,false);
            return new CrimeHolder(view);
        }

        @Override
        /*该方法会把VIewHolder的view视图和模型层数据绑定起来，
         * 收到VIewHolder和列表项在数据集中的索引位置后,通过发送crime标题
         * 给viewHolder的TextView视图，完成Crime数据和View视图的绑定
        */
        public void onBindViewHolder(CrimeHolder holder, int position) {

            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        public void setCrimes(List<Crime> crimes){
            mCrimes = crimes;
        }
    }


    //在onResume()方法中触发updateUI()方法刷新显示列表项

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }


    //当将屏横转时,活动会被摧毁并重建,而onSaveInstanceState()方法实在活动
    // 被摧毁的时候将mSubtitleVisible数值储存下来
    //当横屏的时候在onCreateView方法中调用该方法就可以大考再次显示的效果
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE,mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //将布局文件中定义的菜单填充到Menu实例中
        inflater.inflate(R.menu.fragment_crime_list,menu);
        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }



    public void updateUI(){

        CrimeLab crimeLab = CrimeLab.get(getActivity());

        List<Crime> crimes = crimeLab.getCrimes();
        //如果没有配置好CrimeAdapter,就创建一个CrimeAdapter
        if (mAdapter == null){
            //创建RecyclerAdapter
            mAdapter = new CrimeAdapter(crimes);
            // 然后设置给RecyclerView
            mCrimeRecyclerView.setAdapter(mAdapter);
        }else{
            //如果已经配置好CrimeAdapter，就调用notifyDataSetChanged()
            // 方法修改updateUI()方法
            mAdapter.setCrimes(crimes);
//            mAdapter.notifyDataSetChanged();
            //实现高效的RecyclerView刷新方法

            //如果想要刷新删除crime之后的CrimeListFragment，
//            mAdapter.notifyItemChanged( mIndex );  //单例更新  -- 会报错
            mAdapter.notifyDataSetChanged();  //全量更新
        }

        /*
        挑战练习 用于RecyclerView的空列表
         */
        if (crimes.size() == 0){
            mNoCrimetextView.setVisibility( View.VISIBLE );
            mNoCrimeButton.setVisibility( View.VISIBLE );
        }else {
            mNoCrimeButton.setVisibility( View.GONE );
            mNoCrimetextView.setVisibility( View.GONE );
        }
        updateSubtitle();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_new_crime:
                //新建新的crime实例
                Crime crime = new Crime();
                //添加crime到CrimeLab中
                CrimeLab.get(getActivity()).addCrime(crime);
                //然后重启CrimePagerActivity实例，让用户可以编辑新创建的crime实例
                Intent intent = CrimePagerActivity.newIntent(getActivity(),crime.getId());
                startActivity(intent);
                /*
                平板需要更改的地方
                 */
//                updateUI();
//                mCallback.onCrimeSelected( crime );
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    //用于子标题显示crime记录条数
    private void updateSubtitle(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
//        @SuppressLint("StringFormatMatches")
//        String subtitle = getString(R.string.subtitle_format,crimeCount);

        // 处理单复数的问题

        //失败
//        String subtitle = String.format( getResources().getQuantityString( R.plurals.subtitle_plural, crimeCount), crimeCount );

        String subtitle = null;
        if (crimeCount == 0){
           mSubtitleVisible = false;
        }else if (crimeCount == 1){
            subtitle =  crimeCount + " crime";
        }else {
            subtitle = crimeCount + " crimes";
        }
        if (!mSubtitleVisible){
            subtitle = null;
        }
        //托管CrimeListFragment的activity被强制转换成AppCompatActivity的子类
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);

    }

}
