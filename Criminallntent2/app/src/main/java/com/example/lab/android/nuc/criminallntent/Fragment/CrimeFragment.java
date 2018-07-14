package com.example.lab.android.nuc.criminallntent.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.lab.android.nuc.criminallntent.Dialog.DatePickerFragment;
import com.example.lab.android.nuc.criminallntent.PictureUtils;
import com.example.lab.android.nuc.criminallntent.crime.Crime;
import com.example.lab.android.nuc.criminallntent.crime.CrimeLab;
import com.example.lab.android.nuc.criminallntent.R;

import java.io.File;
import java.sql.BatchUpdateException;
import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";

    private static final String DIALOG_DATE = "DialogDate";


    private static final int REQUEST_DATE = 0;

    private static final int REQUEST_CNTACT = 1;

    private static final int REQUEST_PHOTO = 2;

    private File mPhotoField;

    private Crime mCrime;

    private EditText mEditText;

    //添加组件到逻辑中
    private Button mDataButton;

    private Button mSuspectButton;
    private CheckBox mSolvedCheckBox;

    private Button mReportButton;

    private ImageButton mPhotoButton;

    private ImageView mPhotoView;

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
//          调用Intent的getSerializableExtra()方法获取UUID并存入变量中


        //改为从fragment的argument中获取UUID:
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        //获取Crime的ID后，再利用他从CrimeLab单例中调取对象
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        //获取文件夹所在位置
        mPhotoField = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
    }


    //zai onPause方法中更新SQLite数据

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity())
                .updateCrime(mCrime);
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
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,getCrimeReport());
                intent.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.crime_report_suspect));
                startActivity(intent);
            }
        });

        mPhotoButton = (ImageButton) v.findViewById(R.id.crime_camera);

        //用于拍照的隐士intent
        final Intent pickContact = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        pickContact.addCategory(Intent.CATEGORY_HOME);
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

        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact,PackageManager.MATCH_DEFAULT_ONLY) == null){
            //按钮变灰，无法点击
            mSuspectButton.setEnabled(false);
        }

        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoField != null && captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);
        if (canTakePhoto){
            Uri uri = Uri.fromFile(mPhotoField);
//            captureImage.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        }

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(captureImage,REQUEST_PHOTO);
            }
        });
        mPhotoView = (ImageView) v.findViewById(R.id.crime_photo);
        updatePhotoView();
        return v;
    }

    @Override

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Activity.RESULT_OK){
            return;
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
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            Cursor c = getActivity().getContentResolver().
                    query(contactUri,queryFields,null,null,null);
            try{
                if (c.getCount() == 0){
                    c.moveToFirst();
                    //获取联系人的姓名
                    String suspect = c.getString(0);
                    mCrime.setSuspect(suspect);
                    mSuspectButton.setText(suspect);
                }
            }finally {
                //关闭查询
                c.close();
            }

        }else if (requestCode == REQUEST_PHOTO){
            updatePhotoView();
        }
    }

    private void updateDate() {
        mDataButton.setText(mCrime.getDate().toString());
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
    private void updatePhotoView(){
        if (mPhotoField == null || !mPhotoField.exists()){
            mPhotoView.setImageBitmap(null);
        }else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoField.getPath(),getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }
}
