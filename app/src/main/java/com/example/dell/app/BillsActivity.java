package com.example.dell.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.bank.R;

import java.util.ArrayList;
import java.util.HashMap;

public class BillsActivity extends Activity implements View.OnClickListener {

        private ListView mlistbills;
        private ImageView imageviewback;
        private SimpleAdapter mlistbillsAdapter;
        //数据库
        private MySQLiteHelper mMysql;
        private SQLiteDatabase mDataBase;

        // 存储数据的数组列表
        ArrayList<HashMap<String, Object>> listData = new ArrayList<HashMap<String, Object>>();

        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                setContentView(R.layout.bills);   //setContentView将这个bills布局填充到 Activity 上

                mlistbills = (ListView) this.findViewById(R.id.list_bills);
                imageviewback = (ImageView) this.findViewById(R.id.imageviewBack);

                imageviewback.setOnClickListener(this);

                GetData();

                //SimpleAdapter适配器把数据变成符合界面风格的形式，并且通过ListView显示出来。
                mlistbillsAdapter = new SimpleAdapter(this, listData, R.layout.billsitem,
                        new String[]{"Time", "Type", "Fee", "Remarks"},
                        new int[]{R.id.texttimeshow, R.id.imagetypeshow, R.id.textfeeshow, R.id.textremarksshow}
                );
                //赋予数据，setAdapter重新设置适配器
                mlistbills.setAdapter(mlistbillsAdapter);
                //设置监听
                mlistbills.setOnCreateContextMenuListener(listviewLongPress);
                mlistbills.setOnTouchListener(onTouchListener);
        }

        //触摸事件监听
        View.OnTouchListener onTouchListener = new View.OnTouchListener() {
                float x, y, ux, uy;
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                        // TODO Auto-generated method stub
                        switch (event.getAction()) {
                                //MotionEvent触摸事件，ACTION_DOWN手指初次接触到屏幕时触发。
                                case MotionEvent.ACTION_DOWN:
                                        x = event.getX();     //获取按压点的位置
                                        y = event.getY();
                                        break;
                                case MotionEvent.ACTION_UP:   //ACTION_UP手指离开屏幕时触发。
                                        ux = event.getX();
                                        uy = event.getY();
                                        int p2 = ((ListView)v).pointToPosition((int) ux, (int) uy);

                                        return false;
                        }
                        return false;
                }
        };

        AdapterView.OnItemLongClickListener listviewLongClick = new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                return false;
                }
        };

        AdapterView.OnItemClickListener listviewClick = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 Toast.makeText(getApplicationContext(),
                        "weffwe", Toast.LENGTH_SHORT).show();

                }
        };

        //从数据库获得适配器数据
        public void GetData() {
                mMysql = new MySQLiteHelper(this, "finance.db", null, 1);
                mDataBase = mMysql.getReadableDatabase();
                // 获取要查询的数据对象，在这里获取整个表的内容，rawQuery是直接使用SQL语句进行查询，在字符串内的“？”会被后面的String[]数组逐一对换掉
                Cursor cursor = mDataBase.rawQuery("select * from finance order by ID DESC ", null);
                cursor.moveToFirst();//遍历所有的元素
                int columnsSize = cursor.getColumnCount();  //返回列的总数
                int number = 0;
                while (number < cursor.getCount()) {

                        HashMap<String, Object> map = new HashMap<String, Object>();
                        String budget =""+ cursor.getString(cursor.getColumnIndex("Budget"));
                        map.put("ID", cursor.getString(cursor.getColumnIndex("ID")));
                        map.put("Fee", cursor.getDouble(cursor.getColumnIndex("Fee")));
                        map.put("Time", cursor.getString(cursor.getColumnIndex("Time")));
                        if (budget.equals("收入"))
                                map.put("Fee", "+" + cursor.getString(cursor.getColumnIndex("Fee")));
                        else
                                map.put("Fee", "-" + cursor.getString(cursor.getColumnIndex("Fee")));
                                map.put("Remarks", cursor.getString(cursor.getColumnIndex("Remarks")));

                        if ((cursor.getString(cursor.getColumnIndex("Type"))).equals("衣")) {
                                map.put("Type", R.drawable.cloth);
                        } else if ((cursor.getString(cursor.getColumnIndex("Type"))).equals("食")) {
                                map.put("Type", R.drawable.shi);
                        } else if ((cursor.getString(cursor.getColumnIndex("Type"))).equals("住")) {
                                map.put("Type", R.drawable.zhu);
                        } else if ((cursor.getString(cursor.getColumnIndex("Type"))).equals("行")) {
                                map.put("Type", R.drawable.xing);
                        } else if ((cursor.getString(cursor.getColumnIndex("Type"))).equals("其他")) {
                                map.put("Type", R.drawable.getmoney);
                        }

                        cursor.moveToNext();
                        listData.add(map);
                        number++;
                        System.out.println(listData);
                        }
                        //在这里释放掉所有的元素
                        cursor.close();
                        mDataBase.close();
                        mMysql.close();
                }

                @Override
                public void onClick(View view) {
                        switch (view.getId()) {
                                case R.id.imageviewBack:
                                        this.finish();
                                        break;
                                default:
                                        break;
                        }
                }

                // 长按事件响应
                 View.OnCreateContextMenuListener listviewLongPress = new  View.OnCreateContextMenuListener() {
                        @Override
                        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                                // TODO Auto-generated method stub
                                final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

                                new AlertDialog.Builder(BillsActivity.this)
                    /* 弹出窗口的最上头文字 */
                    .setTitle("删除当前数据")
                    /* 设置弹出窗口的图式 */
                    .setIcon(android.R.drawable.ic_dialog_info)
                    /* 设置弹出窗口的信息 */
                    .setMessage("确定删除当前记录")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialoginterface, int i) {
                            // 获取位置索引
                            int mListPos = info.position;
                            // 获取对应HashMap数据内容
                            HashMap<String, Object> map = listData.get(mListPos);
                            // 获取id
                            int id = Integer.valueOf((map.get("ID").toString()));
                            // 获取数组具体值后,可以对数据进行相关的操作,例如更新数据
                            String[] whereArgs = new String[]{String.valueOf(id)};
                            //获取当前数据库
                            mMysql = new MySQLiteHelper(BillsActivity.this, "finance.db", null, 1);
                            mDataBase = mMysql.getReadableDatabase();
                            try {
                                    mDataBase.delete("Finance", "ID=?", whereArgs);
                                    listData.remove(mListPos);
                                    mlistbillsAdapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                    Log.e("删除出错了", "error");
                            } finally {
                                    mDataBase.close();
                                    mMysql.close();
                            }
                            }
                    }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialoginterface, int i) {
                             }
                    }).show();
                        }
                 };
}