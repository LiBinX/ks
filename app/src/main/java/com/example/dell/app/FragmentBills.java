package com.example.dell.app;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bank.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;


public class FragmentBills extends Fragment {

        //列举数据的ListView
        private ListView mlistbills;
        // 适配器
        private SimpleAdapter mlistbillsAdapter;
        //MySQLiteHelper类，操作数据库
        private MySQLiteHelper mMysql;
        private SQLiteDatabase mDataBase;

        // 存储数据的数组列表
        ArrayList<HashMap<String, Object>> listData = new ArrayList<HashMap<String, Object>>();  ;

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                //inflater将layout的xml布局文件实例化为View类对象
                View view = inflater.inflate(R.layout.bills, container, false);

                return view;
        }

        //从数据库获得适配器数据
        public void  GetData()
        {
                mMysql = new MySQLiteHelper(getActivity(), "finance.db", null, 1);
                mDataBase = mMysql.getReadableDatabase();

                Cursor cursor = mDataBase.rawQuery("select Fee,Budget from finance",null);   //query方法查询到的是一个指向cursor对象第一行的前面一行数据
                int columnsSize = cursor.getColumnCount();//返回所有列的总数

                //获取键值对
                HashMap<String,Object> map = new HashMap<String,Object>();
                while (cursor.moveToNext()) {
                        for (int i = 0; i < columnsSize; i++) {
                                if(cursor.getString(1).equals("衣")){
                                        map.put("Type",R.drawable.cloth);
                                } else if(cursor.getString(1).equals("食")) {
                                        map.put("Type",R.drawable.chart);
                                } else if(cursor.getString(1).equals("住")) {
                                        map.put("Type",R.drawable.zhu);
                                } else if(cursor.getString(1).equals("行")) {
                                        map.put("Type",R.drawable.getmoney);
                                }else if(cursor.getString(1).equals("其他")) {
                                        map.put("Type", R.drawable.getmoney);
                                }
                                map.put("Time", cursor.getString(2));
                                map.put("Fee", cursor.getString(3));
                                map.put("Remarks", cursor.getString(4));
                        }
                      listData.add(map);
                }

                //保留小数点后两位
                DecimalFormat df = new DecimalFormat("###.##");

                cursor.close();
                mDataBase.close();
                mMysql.close();
        }
}

