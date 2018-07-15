package com.example.lab.android.nuc.criminallntent.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.lab.android.nuc.criminallntent.Activity.CrimePagerActivity;
import com.example.lab.android.nuc.criminallntent.utils.PictureUtils;
import com.example.lab.android.nuc.criminallntent.crime.Crime;
import com.example.lab.android.nuc.criminallntent.crime.CrimeLab;
import com.example.lab.android.nuc.criminallntent.R;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";

    private static final String DIALOG_DATE = "DialogDate";

    private static final String DIALOG_TIME = "DialogTime";

    private static final int REQUEST_DATE = 0;

    private static final int REQUEST_CNTACT = 1;

    private static final int REQUEST_PHOTO = 2;

    private static final int REQUEST_DIAL = 3;
    private static final int REQUEST_TIME = 6;

    private static final int REQUEST_DIALOG_PHOTO = 4;
    private static final String DAILOG_PHOTO = "DialogPhoto";

    private File mPhotoFile;

    private Crime mCrime;

    private EditText mEditText;

    //添加组件到逻辑中
    private Button mDataButton;

    private Button mSuspectButton;
    private CheckBox mSolvedCheckBox;

    private Button mReportButton;

    private ImageButton mPhotoButton;

    private Button mTimeButton;

    private Button dialButton;
    private ImageView mPhotoView;
    private Button callSusoectButton;

    private Uri imageUri;
    private String SuspectContactId;

    public static CrimeFragment newInstance(UUID crimeId){
        //每个fragment实例都可以携带一个Bundle对象，该Bundle包含有键值对，
        // 我们可以像附加extra到Activity的intent当中一样使用它们，
        // 一个键值即对应一个argument
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID,crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mCrime = new Crime();
        //getIntent方法返回用来启动MainActivity的Intent
//          调用Intent的getSerializableExtra()方法获取UUID并存入变量中a
        //改为从fragment的argument中获取UUID:
        setHasOptionsMenu( true );
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        //获取Crime的ID后，再利用他从CrimeLab单例中调取对象
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        //获取文件夹所在位置
        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
    }


    //zai onPause方法中更新SQLite数据

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity())
                .updateCrime(mCrime);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu( menu, inflater );
        inflater.inflate( R.menu.fragment_crime,menu );
    }



    @Nullable
    @Override
    //该方法实例化fragment视图的布局，然后将实例化的View返回给托管activity
    //LayoutInflater和ViewGroup是两个必要的参数，Bundle用来恢复数据,
    //可供该方法从保存状态下的重建视图
    //fragment的视图是通过调用LayoutInflate.inflate方法并传入布局的资源ID形成的。
    // 第二个参数是父布局
    //第三个参数是指告诉布局生成器是否将生成的视图添加给俯视图
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_crime,container,false);

        mEditText = (EditText) v.findViewById(R.id.crime_title);

        //更新记事本的内容
        mEditText.setText(mCrime.getTitle());
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //在这里只覆写onTextChanged()方法来显示TextView
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /*
        挑战练习  call
         */
        callSusoectButton = (Button) v.findViewById( R.id.crime_call_suspect );
        if (mCrime.getSuspectcontact() == null){
            callSusoectButton.setEnabled( false );
        }else {
            callSusoectButton.setEnabled( true );
            callSusoectButton.setText( mCrime.getSuspectcontact() );
        }
        callSusoectButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( Intent.ACTION_DIAL );
                intent.setData( Uri.parse("tel:" + mCrime.getSuspectcontact()) );
                startActivity(intent);
            }
        } );

        /**
         * 挑战练习 通过查找联系人对应的号码
         */

//        dialButton = (Button) v.findViewById( R.id.crime_dial );
//        dialButton.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Uri number = Uri.parse( mCrime.getPhotoNumber());
//                final Intent pickNumber = new Intent( Intent.ACTION_DIAL,number);
//                startActivityForResult(pickNumber,REQUEST_DIAL);
//            }
//        } );

        mDataButton = (Button) v.findViewById(R.id.crime_date);
        updateDate();
        //禁用Button的点击事件，在用户的界面的上显示的是灰色的Button
//        mDataButton.setEnabled(false);
        mDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this,REQUEST_DATE);
                dialog.show(manager,DIALOG_DATE);
            }
        });

        mTimeButton = (Button) v.findViewById( R.id.crime_time );
        mTimeButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance( mCrime.getHour(),mCrime.getMinute());
                dialog.setTargetFragment( CrimeFragment.this,REQUEST_TIME);
                dialog.show(fragmentManager,DIALOG_DATE );
            }
        } );



        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        //更新记事本的CheckBox的勾选事件
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener
                (new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });


        mReportButton = (Button) v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                 * 挑战练习
                 * 利用ShareCompat.Intent.Builder来创建Intent
                 */

//                ShareCompat.IntentBuilder sc = ShareCompat.IntentBuilder.from( getActivity() );
//                sc.setType( "text/plain" );
//                sc.setText( getCrimeReport());
//                sc.setSubject( getString( R.string.crime_report_suspect));
//                sc.createChooserIntent();
//                sc.startChooser();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                //return report
                intent.putExtra(Intent.EXTRA_TEXT,getCrimeReport());
                //return the suspect is %s
                intent.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.crime_report_suspect));
                intent = Intent.createChooser( intent,getString( R.string.send_report ));
                startActivity(intent);
            }
        });



        //用于拍照的隐士intent
        final Intent pickContact = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
//       pickContact.addCategory(Intent.CATEGORY_HOME);
        ///照片按钮的点击响应事件
        mSuspectButton = (Button) v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact,REQUEST_CNTACT);
            }
        });

        if (mCrime.getSuspect() != null){
            //在按钮上显示Suspect的信息
            mSuspectButton.setText(mCrime.getSuspect());
        }

        /**
         * 如果模拟器上没上联系人应用，需要通过 操作系统当中的PackageManager进行自检，
         * 在onCreateVIew()方法当中实现检查
         * 如果搜索到目标 他会返回ResultInfo告诉你找到了那个Activity,
         * 如果找不到，必须禁用联系人按钮，否则系统回崩溃
         */

        PackageManager packageManager = getActivity().getPackageManager();
//        if (packageManager.resolveActivity(pickContact,PackageManager.MATCH_DEFAULT_ONLY) == null){
//            //按钮变灰，无法点击
//            mSuspectButton.setEnabled(false);
//        }

        mPhotoButton = (ImageButton) v.findViewById(R.id.crime_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
//        mPhotoButton.setEnabled(canTakePhoto);
        try{
            if (mPhotoFile.exists()){
                mPhotoFile.delete();
            }
            mPhotoFile.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24){
            imageUri = FileProvider.getUriForFile(getContext(),"com.example.lab.android.nuc.criminallntent.fileprovider",mPhotoFile );

        }else {
            imageUri = Uri.fromFile( mPhotoFile );
        }
        mCrime.setPhotoFile( mPhotoFile );
        captureImage.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage,REQUEST_PHOTO);
            }
        });
        /**
         * 挑战联系 优化照片显示
         */

        mPhotoView = (ImageView) v.findViewById(R.id.crime_photo);
        mPhotoView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPhotoFile == null || !mPhotoFile.exists()){
                    mPhotoView.setImageDrawable( null );
                }else {
                    FragmentManager manager = getFragmentManager();
                    PhotoFragment dialog = PhotoFragment.newInstance( mPhotoFile );
                    dialog.setTargetFragment( CrimeFragment.this,REQUEST_DIALOG_PHOTO );
                    dialog.show( manager,DIALOG_DATE );
                }
            }
        } );
        //更新图片视图
//        updatePhotoView();
        /**
         * 优化略缩图
         */
        mCrime.setImageWidth( mPhotoView.getWidth() );
        mCrime.setImageHeight( mPhotoButton.getHeight() );
        updatePhotoView( mPhotoView.getWidth(),mPhotoView.getHeight() );

        return v;
    }


    /*
    挑战练习 13 实现删除 crime
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_delete_crime:
                CrimeLab.get( getActivity()).removeCrime( mCrime );
                CrimeLab.get( getActivity() ).deleteCrime( mCrime );
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected( item );

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_TIME){
            int hour = (int) data.getSerializableExtra( TimePickerFragment.EXTRA_HOUR );
            int minute = (int) data.getSerializableExtra( TimePickerFragment.EXTRA_MINUTE );
            mCrime.setHour( hour );
            mCrime.setMinute( minute );
            updateTime();
        }
        if (requestCode == REQUEST_DATE){
            //覆盖onActivityResult()方法从extra中获取日期数据，
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);

            //设置对应的Crimed的记录日期
            mCrime.setDate(date);
            //更新Button所显示的信息
            updateDate();
        }else if (requestCode == REQUEST_CNTACT && data != null){
            //获取日期的Uri
            Uri contactUri = data.getData();
            //创建了一条查询语句，要球返回全部联系人的姓名
            //然后查询数据库
            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME,

                    //记住这个是最重要的，别忘了
                    ContactsContract.Contacts._ID
            };
            Cursor c = getActivity().getContentResolver().
                    query(contactUri,queryFields,null,null,null);
            try{
                if (c.getCount() == 0) {
                    return;
                }
                c.moveToFirst();
                //获取联系人的姓名
                String suspect = c.getString(0);
//                String Photonumber = c.getString( 1 );
                mCrime.setSuspect(suspect);
//                mCrime.setPhotoNumber( Photonumber );
                mSuspectButton.setText(suspect);
                String _id = c.getString( 1 );
                SuspectContactId = _id;
            }finally {
                //关闭查询
                c.close();
            }

            /*
            挑战练习  添加拨号
             */
            Cursor cursor_1 = getActivity().getContentResolver()
                    .query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{SuspectContactId},
                            null);
            try{
                if (cursor_1.getCount() == 0){
                    return;
                }
                cursor_1.moveToFirst();
                int index = cursor_1.getColumnIndex( ContactsContract.CommonDataKinds.Phone.NUMBER );
                String number = cursor_1.getString( index );
                mCrime.setSuspectcontact( number );
                callSusoectButton.setEnabled( true );
                callSusoectButton.setText( number );
            }finally {
                cursor_1.close();
            }
        }else if (requestCode == REQUEST_PHOTO){
//            updatePhotoView();
            /**
             * 优化略缩图加载
             */
            updatePhotoView( mPhotoView.getWidth(),mPhotoView.getHeight() );
        }
    }

    private void updateDate() {

//        mDataButton.setText(mCrime.getDate().toString());

        /**
         * 第八章挑战练习
         */
        String date = (String) DateFormat.format("EEEE, MMM dd, yyyy", mCrime.getDate());
        mDataButton.setText( date );

    }
    @SuppressLint("SetTextI18n")
    private void updateTime() {
        if (mCrime.getMinute() < 10){
            mTimeButton.setText( mCrime.getHour() + " : 0" + mCrime.getMinute() );
        }else {
            mTimeButton.setText( mCrime.getHour() + " : " + mCrime.getMinute() );
        }
    }


    //添加getCrimeReport()方法用于创建四段字符串信息，并返回拼接完整的信息
    private String getCrimeReport(){
        //设置联系人的report
        String solvedString = null;
        if (mCrime.isSolved()){
            solvedString = getString(R.string.crime_report_solved);
        }else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        //设置日期的格式
        String dateFormat = "EEE,MMM dd";
        String dateString = android.text.format.DateFormat.format(dateFormat,mCrime.getDate()).toString();
        String suspect = mCrime.getSuspect();
        if (suspect == null){
            suspect = getString(R.string.crime_report_no_suspect);
        }else {
            suspect = getString(R.string.crime_report_suspect);
        }
        //将这些数据都设置到report里面
        String report = getString(R.string.crime_report,
                mCrime.getTitle(),dateString,solvedString,suspect);
        return report;
    }


    //添加刷新mPhotoView的方法
    private void updatePhotoView(int width,int height){
        if (mPhotoFile == null || ! mPhotoFile.exists()){
            mPhotoView.setImageBitmap(null);
        }else {
//            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(),getActivity());
            //优化略缩图加载
            Bitmap bitmap = PictureUtils.getScaledBitmap( mPhotoFile.getPath(), width,height);
            mPhotoView.setImageBitmap(bitmap);

        }
    }
}
