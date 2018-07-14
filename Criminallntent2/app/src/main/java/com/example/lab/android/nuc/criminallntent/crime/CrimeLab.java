package com.example.lab.android.nuc.criminallntent.crime;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.example.lab.android.nuc.criminallntent.datebase.CrimeBaseHelper;
import com.example.lab.android.nuc.criminallntent.datebase.CrimeCursorWrapper;
import com.example.lab.android.nuc.criminallntent.datebase.CrimeDbSchema;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by 王浩 on 2018/3/16.
 */

public class CrimeLab {
    private static CrimeLab sCrimeLab;

//    private List<Crime> mCrimes;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context){
        if (sCrimeLab == null){

            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context){

        //将context赋值给实例变量
        //引用上下文
        mContext = context.getApplicationContext();

        //getWritableDatabase()方法用于创建或者打开一个现有数据库
        mDatabase = new CrimeBaseHelper(mContext)
                .getWritableDatabase();


//        mCrimes = new ArrayList<>();
        //既然都添加了添加crime的功能了，就没有必要水机生成100个了
//        for (int i = 0; i < 100; i++) {
//            Crime crime = new Crime();
//            crime.setTitle("Crime #" + i);
//            //断点型的CheckBox
//            crime.setSolved(i % 2 == 0);
//            mCrimes.add(crime);
//        }
    }

    public void addCrime(Crime c){
//        mCrimes.add(c);

        ContentValues values = getContentValuse(c);

        //传入的数据的第一个参数时数据库表名，最后一个是要写入的数据
        mDatabase.insert(CrimeDbSchema.CrimeTable.NAME,null,values);

    }

    public List<Crime> getCrimes() {
//        return mCrimes;
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrimes(null,null);

        try{
            //湘江cursor查询的位置放到每一行的最前头
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                crimes.add(cursor.getCrime());
                //跳转到下一行
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return crimes;
    }




    //完善遍历出所有crime的方法，返回crime数组对象
    public Crime getCrime(UUID id){
//        for (Crime crime : mCrimes) {
//            if (crime.getId().equals(id)){
//                return crime;
//            }
//        }
        CrimeCursorWrapper cursor = queryCrimes(
                CrimeDbSchema.CrimeTable.Cols.UUUID + " = ?",
                new String[]{ id.toString() }
        );
        try{
            if (cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        }finally {
            cursor.close();
        }
    }

    public File getPhotoFile(Crime crime){
        //Environment.DIRECTORY_PICTURES用于储存图像文件
        File externalFileDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (externalFileDir == null){
            return null;
        }
        return new File(externalFileDir,crime.getPhotoFilename());
    }

    public void updateCrime(Crime crime){
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValuse(crime);
        //要通过 new String确定想要更新的是那些数据，这里表示的是uuidString
        mDatabase.update(CrimeDbSchema.CrimeTable.NAME,values,
                CrimeDbSchema.CrimeTable.Cols.UUUID + " = ? ",
                new String[]{uuidString});
    }


    private static ContentValues getContentValuse(Crime crime){
        ContentValues values = new ContentValues();
        values.put(CrimeDbSchema.CrimeTable.Cols.UUUID,crime.getId().toString());
        values.put(CrimeDbSchema.CrimeTable.Cols.TITLE,crime.getTitle());
        values.put(CrimeDbSchema.CrimeTable.Cols.DATE,crime.getDate().getTime());
        values.put(CrimeDbSchema.CrimeTable.Cols.SOLVED,crime.isSolved() ? 1 : 0);
        values.put(CrimeDbSchema.CrimeTable.Cols.SUSPECT,crime.getSuspect());
        return values;
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[]whereArgs){
        @SuppressLint("Recycle") Cursor cursor = mDatabase.query(
                CrimeDbSchema.CrimeTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new CrimeCursorWrapper(cursor);
    }
}
