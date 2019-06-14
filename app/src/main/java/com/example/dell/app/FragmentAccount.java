package com.example.dell.app;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bank.R;

import java.text.DecimalFormat;

public class FragmentAccount extends Fragment {

        private MySQLiteHelper mMysql;
        private SQLiteDatabase mDataBase;
        private TextView textRemainder,textPay,textIncome;

        Button bt;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.finance_check, container, false);

                double resultIncome = 0,resultRemainder = 0,resultPay = 0;
                textIncome = (TextView)view.findViewById(R.id.textincome);
                textRemainder = (TextView)view.findViewById(R.id.textremainder);
                textPay = (TextView)view.findViewById(R.id.textpay);
                bt = view.findViewById(R.id.button);

                bt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), RecorderActivity.class);
                        startActivity(intent);
                        }
                });

                mMysql = new MySQLiteHelper(getActivity(), "finance.db", null, 1);
                mDataBase = mMysql.getReadableDatabase();

                Cursor cursor = mDataBase.rawQuery("select Fee,Budget from finance",null);

                cursor.moveToFirst();

                if (cursor.getCount() > 0) {
                        for (int i = 0; i < cursor.getCount(); i++) {
                                //移动到指定记录
                                double Fee = cursor.getDouble(cursor.getColumnIndex("Fee"));
                                String budget =""+
                                        cursor.getString(cursor.getColumnIndex("Budget"));
                                if(budget.equals("支出")) {
                                        resultPay += Fee;
                                } else if (budget.equals("收入")){
                                        resultIncome += Fee;
                                }else{

                                }
                                cursor.moveToNext();
                        }
                }
                DecimalFormat df = new DecimalFormat("###.##");
                textPay.setText(String.valueOf(df.format(resultPay)));
                textIncome.setText(String.valueOf(df.format(resultIncome)));
                textRemainder.setText(String.valueOf(df.format(resultIncome - resultPay)));

                cursor.close();
                mDataBase.close();
                mMysql.close();

                return view;
        }
}


