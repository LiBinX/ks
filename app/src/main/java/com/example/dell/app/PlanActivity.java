package com.example.dell.app;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bank.R;

import java.util.ArrayList;
import java.util.HashMap;

public class PlanActivity extends Activity implements View.OnClickListener {

        private Button PlanSure, PlanCancel;
        private EditText MorningPlan, AfternoonPlan, NightPlan;
        private ImageView PlanBack;

        //数据库
        private MySQLiteHelper mMysql;
        private SQLiteDatabase mDataBase;

        private ArrayList<String> Data = new ArrayList<String>();
        ArrayList<HashMap<String, Object>> listData = new ArrayList<HashMap<String, Object>>();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.plan);
                initView();
        }

        public void initView() {

                PlanSure = (Button) findViewById(R.id.plan_sure);
                PlanCancel = (Button) findViewById(R.id.plan_cancel);
                MorningPlan = (EditText) findViewById(R.id.MorningPlan);
                AfternoonPlan = (EditText) findViewById(R.id.AfternoonPlan);
                NightPlan = (EditText) findViewById(R.id.NightPlan);
                PlanBack = (ImageView)findViewById(R.id.imagePlanBack);

                PlanSure.setOnClickListener(this);
                PlanCancel.setOnClickListener(this);
                PlanBack.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
                switch (view.getId()) {
                        case R.id.plan_sure:
                                GetData();
                                WriteData();
                                break;
                        case R.id.plan_cancel:
                                this.finish();
                                break;
                        case R.id.imagePlanBack:
                                this.finish();
                                break;
                }
        }

        public void GetData()
        {
                Data.clear();
                Data.add(MorningPlan.getText().toString());
                Data.add(AfternoonPlan.getText().toString());
                Data.add(NightPlan.getText().toString());
        }

        public void WriteData()
        {
                mMysql = new MySQLiteHelper(this, "finance.db", null, 1);
                mDataBase = mMysql.getReadableDatabase();

                ContentValues cv=new ContentValues();
                cv.put("MorningPlan",Data.get(0));
                cv.put("AfternoonPlan",Data.get(1));
                cv.put("NightPlan", Data.get(2));

                mDataBase.insert("plan", "NightPlan", cv);
                mDataBase.close();
                mMysql.close();
                //结束当前activity
                this.finish();

        }

}


