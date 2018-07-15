package com.example.lab.android.nuc.criminallntent.datebase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lab.android.nuc.criminallntent.crime.Crime;
import com.example.lab.android.nuc.criminallntent.datebase.CrimeDbSchema.CrimeTable;

/**
 * Created by 王浩 on 2018/3/23.
 */

//自定义一个类继承SQLiteOpenHelper类.重写俩个方法
public class CrimeBaseHelper extends SQLiteOpenHelper{

    private static final int VERSION  = 1;

    private static final String DATEBASE_NAME = "crimebase.db";


    public CrimeBaseHelper(Context context){
        //第一个参数表示当前的context
        //第二个参数是数据库名，这里进行了全局的定义
        //第三个参数表示允许我们在查询数据时返回一个而自定义的Cursor,一般都是传入null
        //第四个参数表示当前数据库的版本号
        super(context,DATEBASE_NAME,null,VERSION);

    }

    //该方法用于创建初始数据库
    @Override
    public void onCreate(SQLiteDatabase db) {
        //打包之后就可以轻松的以CrimeTable.Cols.UUID的形式
        db.execSQL("create table " + CrimeTable.NAME + "(" +
            "_id integer primary key autoincrement," +
            CrimeTable.Cols.UUUID + "," +
            CrimeTable.Cols.TITLE + "," +
            CrimeTable.Cols.DATE + "," +
            CrimeTable.Cols.SOLVED + "," +
            CrimeTable.Cols.SUSPECT + "," +
            CrimeTable.Cols.SuspectContact +    //add this
                ")"
        );
    }

    //该方法负责与升级相关的工作
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
