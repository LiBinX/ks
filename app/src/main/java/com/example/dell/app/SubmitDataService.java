package com.example.dell.app;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;
import android.os.Vibrator;

import com.bank.R;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class SubmitDataService extends Service {

    private Vibrator vibrator;
    boolean finishflag =false;
    //数据库
    private MySQLiteHelper mMysql;
    private SQLiteDatabase mDataBase;
    IBinder  myBinder = new MyBinder();

    class MyBinder extends Binder {
        public Service getService(){
            return SubmitDataService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    public boolean getfinishflag()
    {
        return finishflag;
    }

    //第一次开始的时候执行，或者关闭服务后执行
    @Override
    public void onCreate() {
        super.onCreate();
//                Log.d(TAG, "onCreate() executed");
    }

    // 每次开启服务都会执行，重新开启服务都会执行
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 开始执行后台任务
                SubmitData();
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }

    //销毁服务
    public void onDestroy() {
        super.onDestroy();
    }

    public void SubmitData() {
        mMysql = new MySQLiteHelper(this, "finance.db", null, 1);
        mDataBase = mMysql.getReadableDatabase();

        Cursor cursor = mDataBase.rawQuery("select * from finance", null);
        cursor.moveToFirst();
        int columnsSize = cursor.getColumnCount();
        int number = 0;
        while (number < cursor.getCount()) {
            String budget = cursor.getString(cursor.getColumnIndex("Budget"));
            int  ID          = cursor.getInt(cursor.getColumnIndex("ID"));
            Double Fee      =cursor.getDouble(cursor.getColumnIndex("Fee"));
            String Time      =cursor.getString(cursor.getColumnIndex("Time"));
            String Remarks = cursor.getString(cursor.getColumnIndex("Remarks"));
            String Type     =cursor.getString(cursor.getColumnIndex("Type"));
            String xml = CreateXml(ID,Type,Fee,Time,Remarks,budget);
            SubmitRequest(xml);
            cursor.moveToNext();
            number++;
        }
        cursor.close();
        mDataBase.close();
        mMysql.close();
        //消息通知栏
        //定义NotificationManager
        String ns = Context.NOTIFICATION_SERVICE;
        //NotificationManager mNotificationManager = (NotificationManager)getSystemService(ns);
        //定义通知栏展现的内容信息
        int icon = R.drawable.back;
        CharSequence tickerText = "通知栏";
        long when = System.currentTimeMillis();
        //Notification notification = new Notification(icon, tickerText, when);

        //定义下拉通知栏时要展现的内容信息
        Context context = getApplicationContext();
        CharSequence contentTitle = "已经完成同步";
        CharSequence contentText = "进入程序查看详情";

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        //notification.setLatestEventInfo(context, contentTitle, contentText,
          //      contentIntent);
        //用mNotificationManager的notify方法通知用户生成标题栏消息通知
       // mNotificationManager.notify(1, notification);

        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        long [] pattern = {100,400,100,400};   // 停止 开启 停止 开启
        vibrator.vibrate(pattern,-1);
    }


    public String CreateXml(int ID, String Type, double Fee, String Time, String Remarks, String Budget)
    {

        String xml = "<?xml version='1.0' encoding='UTF-8'?>"
                + "<Data>"
                + "<ID>" + ID + "</ID>"
                + "<Type>" + Type + "</Type>"
                + "<Fee>" + Fee + "</Fee>"
                + "<Time>" + Time + "</Time>"
                + "<Remarks>" + Remarks + "</Remarks>"
                + "<Budget>" + Budget + "</Budget>"
                + "</Data>";
        return xml;
    }

    //执行功能的函数
    public void SubmitRequest(String xml) {
        try {
            // 创建url资源
            URL url = new URL("http://119.29.85.118//finance.php");
            // 建立http连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置允许输出
            conn.setDoOutput(true);

            conn.setDoInput(true);

            // 设置不用缓存
            conn.setUseCaches(false);
            // 设置传递方式
            conn.setRequestMethod("POST");
            // 设置维持长连接
            conn.setRequestProperty("Connection", "Keep-Alive");
            // 设置文件字符集:
            conn.setRequestProperty("Charset", "UTF-8");
            //转换为字节数组
            byte[] data = xml.getBytes();
            // 设置文件长度
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
            // 设置文件类型:
            conn.setRequestProperty("contentType", "text/xml");
            // 开始连接请求
            conn.connect();
            OutputStream out = conn.getOutputStream();
            // 写入请求的字符串
            out.write(data);
            out.flush();
            out.close();

            System.out.println(conn.getResponseCode());

            // 请求返回的状态
            if (conn.getResponseCode() == 200) {
                System.out.println("连接成功");
                // 请求返回的数据
                InputStream in = conn.getInputStream();
                String a = null;
                try {
                    byte[] data1 = new byte[in.available()];
                    in.read(data1);
                    // 转成字符串
                    a = new String(data1);
                    System.out.println(a);
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            } else {
                System.out.println("no++");
            }
            finishflag =true;

        } catch (Exception e) {
            finishflag =false;
        }
    }
}
