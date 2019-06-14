package com.example.dell.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;

//抽象类必须调用父类方法， MySQLiteHelper这个类继承了SQLiteOpenHelper
public class MySQLiteHelper extends SQLiteOpenHelper {

       /* public static final String cost_time = "time";
        public static final String cost_fee = "fee";
        public static final String accout_cost = "finance";*/

        public MySQLiteHelper(Context context, String name, CursorFactory factory, int version) {
                //调用父类构造函数,context是当前的上下文环境；name是自定义的数据库的名字，
                // factory一般是表示查询时传入的Cursor，一般传入null，需要时查询；
                // version数据库的版本，用于升级。
                super(context, getMyDatabaseName(name), factory, version);
        }

        private static String getMyDatabaseName(String name){
                String databasename = name;
                boolean isSdcardEnable = false;
                String state = Environment.getExternalStorageState();
                if(Environment.MEDIA_MOUNTED.equals(state)){//SDCard是否插入
                        isSdcardEnable = true;
                }
                String dbPath = null;
                if(isSdcardEnable){
                        dbPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Finance/database/";
                        Log.i("text",dbPath+"dbpath");
                }else{//未插入SDCard，建在内存中

                }
                File dbp = new File(dbPath);
                if(!dbp.exists()){
                        dbp.mkdirs();
                }
                databasename = dbPath + databasename;
                return databasename;
        }

         //当数据库首次创建时执行该方法，一般将创建表等初始化操作放在该方法中执行.
         //重写onCreate方法，调用execSQL方法创建表
        @Override
        public void onCreate(SQLiteDatabase db) {
                Log.i("SWORD", "create a Database");
                //创建数据库sql语句,integer PRIMARY KEY AUTOINCREMENT定义自增长字段作为主键
                String sql = "create table finance(ID integer PRIMARY KEY AUTOINCREMENT," +
                        "Type varchar(10)," +
                        "Time varchar(20)," +
                        "Fee double," +
                        "Remarks varchar(20)," +
                        "Budget varchar(10))";
                //执行创建数据库操作
                db.execSQL(sql);
                //创建表
                 sql = "create table plan(ID integer PRIMARY KEY AUTOINCREMENT," +
                         "Morningplan varchar(100)," +
                         "Afternoonplan varchar(100)," +
                         "Nightplan varchar(100)," +
                         "Rank varchar(5), " +
                         "Conclusion varchar(100))";

                db.execSQL(sql);//执行sql
        }

        @Override
        //当打开数据库时传入的版本号与当前的版本号不同时会调用该方法.i1为更高的版本
        // 重写完这个方法后，在调用构造函数时，仅仅需要把version参数改成比之前的高一个版本即可
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }

        /*public void insertCost(CostBean costBean){

                SQLiteDatabase database = getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(cost_time,costBean.time);
                cv.put(cost_fee,costBean.fee);
                database.insert(accout_cost,null,cv);
        }*/
}
